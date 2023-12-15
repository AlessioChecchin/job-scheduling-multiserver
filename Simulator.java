import config.Category;
import config.ProjectConfig;
import events.*;
import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;
import scheduling.policy.DefaultPolicy;
import scheduling.Scheduler;
import scheduling.Server;
import utils.RandomGenerator;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Scanner;

/**
 * Class that implements a simulator.
 */
public class Simulator
{
    /**
     * Simulator constructor.
     * @param path The path of the entrypoint.
     * @throws IOException If there are errors while reading input.
     */
    public Simulator(String path) throws IOException
    {
        // Reading input from file
        readInput(path);

        this.history = new PriorityQueue<>();
        this.currentRun = 0;
    }

    /**
     * Executes the next iteration of the R simulations.
     * @throws IllegalStateException If the simulation has already run R times.
     */
    public void nextRun() throws IllegalStateException
    {
        if(!this.hasNext()) throw new IllegalStateException();

        this.run();

        for(Category category: categories)
        {
            config.addCategoryStats(category.getId(), category.getAvgQueuingTime(), category.getAvgServiceTime(), category.getProcessedCategories());
            category.clearStats();
        }

        this.currentRun++;
    }

    /**
     * Checks if he can run the next time.
     * @return If he can run the next time.
     */
    public boolean hasNext()
    {
        return this.currentRun < this.config.getSimulationRepetitions();
    }

    /**
     * Runs the simulation 1 time.
     */
    protected void run()
    {
        // Creating an event handler
        this.evtHandler = new EventHandler();

        this.initializeJobs();

        // Creating a new scheduler.
        Scheduler scheduler = new Scheduler(this.createServers(), this.evtHandler, new DefaultPolicy());

        double currentEta = 0;

        // Running until all jobs are finished
        while(this.evtHandler.hasEvent())
        {
            Entry entry = this.evtHandler.remove();
            if(entry instanceof ArrivalEntry)
            {
                if(scheduler.getArrivedJobs() < this.config.getTotalJobs())
                {
                    this.evtHandler.generateArrivalEvent(entry.getCategory(), entry.getKey());
                    scheduler.schedule(entry);

                    this.pushToHistory(entry);
                }
            }
            else if(entry instanceof FinishEntry)
            {
                scheduler.schedule(entry);

                FinishEntry finishEntry = (FinishEntry) entry;

                currentEta = finishEntry.getKey();

                ArrivalEntry origin = finishEntry.getLinkedArrival();
                double delta = origin.getStartExecution() - origin.getKey();

                // Adding queuing time for category-related stats
                entry.getCategory().addStats(delta, finishEntry.getServiceTime());

                // Adding queuing time for job-related stats
                config.addJobQueuingTime(delta);

                pushToHistory(entry);
            }
        }

        config.addEndTime(currentEta);
    }

    /**
     * Initializes the initial jobs.
     */
    protected void initializeJobs()
    {
        // Initializing jobs
        for(int i = 0; i < config.getCategoriesNumber(); i++)
        {
            Category catConfig = categories.get(i);
            evtHandler.generateArrivalEvent(catConfig, 0);
        }
    }

    /**
     * Creates the servers
     * @return The list of created servers.
     */
    protected List<Server> createServers()
    {
        List<Server> servers = new ArrayList<>();

        // Creating K servers.
        for(int i = 0; i < config.getServerNumber(); i++)
        {
            servers.add(new Server(i));
        }

        return servers;
    }

    /**
     * Reads input from file.
     * @param path The path of the input file.
     * @throws IOException If there are errors while reading the file.
     */
    protected void readInput(String path) throws IOException
    {
        Scanner fileScanner = new Scanner(new FileReader(path));

        if(fileScanner.hasNextLine()) {
            Scanner metaScanner = new Scanner(fileScanner.nextLine());
            metaScanner.useDelimiter(",");

            int kServerNumber          = Integer.parseInt(metaScanner.next());
            int hCategoriesNumber      = Integer.parseInt(metaScanner.next());
            int nTotalJobs             = Integer.parseInt(metaScanner.next());
            int rSimulationRepetitions = Integer.parseInt(metaScanner.next());
            int pSchedulingPolicyType  = Integer.parseInt(metaScanner.next());

            config = new ProjectConfig(
                    kServerNumber,
                    hCategoriesNumber,
                    nTotalJobs,
                    rSimulationRepetitions,
                    pSchedulingPolicyType
            );

            metaScanner.close();
        }

        categories = new ArrayList<>();

        int count = 0;
        while (fileScanner.hasNextLine() && count < config.getCategoriesNumber())
        {
            Scanner paramScanner = new Scanner((fileScanner.nextLine()));
            paramScanner.useDelimiter(",");

            double lambdaArrival             = Double.parseDouble(paramScanner.next());
            double lambdaService             = Double.parseDouble(paramScanner.next());
            int seedArrival                  = Integer.parseInt(paramScanner.next());
            int seedService                  = Integer.parseInt(paramScanner.next());
            RandomGenerator arrivalGenerator = new RandomGenerator(seedArrival);
            RandomGenerator serviceGenerator = new RandomGenerator(seedService);

            Category catConfig = new Category(
                    count,
                    lambdaArrival,
                    lambdaService,
                    seedArrival,
                    seedService,
                    arrivalGenerator,
                    serviceGenerator
            );

            categories.add(catConfig);

            paramScanner.close();

            count++;
        }

        fileScanner.close();
    }

    public String toString()
    {
        // Using builder for performance.
        StringBuilder builder = new StringBuilder();

        builder
                .append(config.getServerNumber()).append(',')
                .append(config.getCategoriesNumber()).append(',')
                .append(config.getTotalJobs()).append(',')
                .append(config.getSimulationRepetitions()).append(',')
                .append(config.getSchedulingPolicy()).append(System.lineSeparator());

        // Check the type of output to use.
        if(config.hasShortOutput())
        {
            PriorityQueue<Entry> tHistory = new PriorityQueue<>(history);

            while(!tHistory.isEmpty())
            {
                Entry entry = tHistory.remove();

                double serviceTime = 0;

                if(entry instanceof FinishEntry)
                {
                    serviceTime = ((FinishEntry)entry).getServiceTime();
                }

                builder
                        .append(entry.getKey()).append(',')
                        .append(serviceTime).append(',')
                        .append(entry.getCategory().getId()).append(System.lineSeparator());
            }
        }

        if(currentRun != 0)
        {
            builder
                    .append(config.getAvgEta()).append(System.lineSeparator())
                    .append(config.getAvgQueuingTime()).append(System.lineSeparator());

            for(Category catConfig: categories)
            {
                int id = catConfig.getId();

                builder
                        .append(config.getAvgCategoryNumber(id)).append(',')
                        .append(config.getAvgCategoryQueuingTime(id)).append(',')
                        .append(config.getAvgCategoryServiceTime(id)).append(System.lineSeparator());
            }

        }
        return builder.toString();
    }

    protected void pushToHistory(Entry entry)
    {
        if(config.hasShortOutput())
        {
            history.add(entry);
        }
    }

    private ProjectConfig config;
    private List<Category> categories;
    private EventHandler evtHandler;
    private PriorityQueue<Entry> history;
    private int currentRun;

    public static void main(String[] args)
    {
        // Basic input parameter check
        if(args.length == 0) throw new IllegalArgumentException("Invalid parameter");

        try
        {
            // Instantiating simulation
            Simulator sim = new Simulator(args[0]);

            while(sim.hasNext())
            {
                sim.nextRun();
            }

            System.out.print(sim);

        }
        catch(IOException e)
        {
            System.out.println("IOException " + e.getMessage());
        }
    }
}
