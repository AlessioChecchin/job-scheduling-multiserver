import config.CategoryConfig;
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

public class Simulator
{
    public Simulator(String path) throws IOException
    {
        // Reading input from file
        readInput(path);

        history = new PriorityQueue<>();
        currentRun = 0;
    }

    protected void run()
    {
        // Creating an event handler
        evtHandler = new EventHandler();

        initializeJobs();

        // Creating a new scheduler.
        Scheduler scheduler = new Scheduler(createServers(), evtHandler, new DefaultPolicy());

        double currentEta = 0;

        // Running until all jobs are finished
        while(evtHandler.hasEvent())
        {
            Entry entry = evtHandler.remove();
            if(entry instanceof ArrivalEntry)
            {
                if(scheduler.getArrivedJobs() < config.nTotalJobs)
                {
                    evtHandler.generateArrivalEvent(entry.getCategory(), entry.getKey());
                    scheduler.schedule(entry);

                    pushToHistory(entry);
                }
            }
            else if(entry instanceof FinishEntry)
            {
                scheduler.schedule(entry);

                currentEta = entry.getKey();

                ArrivalEntry origin = ((FinishEntry)entry).getLinkedArrival();

                double delta = origin.getStartExecution() - origin.getKey();

                // Adding queuing time for category-related stats
                entry.getCategory().addCategoryStats(delta, ((FinishEntry) entry).getServiceTime());

                // Adding queuing time for job-related stats
                config.addQueuingTime(delta);

                pushToHistory(entry);
            }
        }

        config.addEta(currentEta);
    }

    public void nextRun()
    {
        if(currentRun >= config.rSimulationRepetitions)
        {
            throw new IllegalStateException();
        }

        run();

        currentRun++;
    }

    public boolean hasNext()
    {
        return currentRun < config.rSimulationRepetitions;
    }

    protected void initializeJobs()
    {
        // Initializing jobs
        for(int i = 0; i < config.hCategoriesNumber; i++)
        {
            CategoryConfig catConfig = categories.get(i);
            evtHandler.generateArrivalEvent(catConfig, 0);
        }
    }

    protected List<Server> createServers()
    {
        List<Server> servers = new ArrayList<>();

        // Creating K servers.
        for(int i = 0; i < config.kServerNumber; i++)
        {
            servers.add(new Server(i));
        }

        return servers;
    }

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
        while (fileScanner.hasNextLine() && count < config.hCategoriesNumber)
        {
            Scanner paramScanner = new Scanner((fileScanner.nextLine()));
            paramScanner.useDelimiter(",");

            double lambdaArrival             = Double.parseDouble(paramScanner.next());
            double lambdaService             = Double.parseDouble(paramScanner.next());
            int seedArrival                  = Integer.parseInt(paramScanner.next());
            int seedService                  = Integer.parseInt(paramScanner.next());
            RandomGenerator arrivalGenerator = new RandomGenerator(seedArrival);
            RandomGenerator serviceGenerator = new RandomGenerator(seedService);

            CategoryConfig catConfig = new CategoryConfig(
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
        StringBuilder builder = new StringBuilder();

        builder
                .append(config.kServerNumber).append(',')
                .append(config.hCategoriesNumber).append(',')
                .append(config.nTotalJobs).append(',')
                .append(config.rSimulationRepetitions).append(',')
                .append(config.pSchedulingPolicyType).append(System.lineSeparator());

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
                        .append(entry.getCategory().id).append(System.lineSeparator());
            }
        }
        if(currentRun != 0)
        {
            builder
                    .append(config.getAvgEta()).append(System.lineSeparator())
                    .append(config.getAvgQueuingTime()).append(System.lineSeparator());

            for(CategoryConfig catConfig: categories)
            {
                double avgNr = ((double)catConfig.getProcessedCategories()) / currentRun;

                builder
                        .append(avgNr).append(',')
                        .append(catConfig.getAvgQueuingTime()).append(',')
                        .append(catConfig.getAvgServiceTime()).append(System.lineSeparator());
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


    public ProjectConfig config;
    public List<CategoryConfig> categories;
    private EventHandler evtHandler;
    private PriorityQueue<Entry> history;
    private int currentRun;

    public static void main(String[] args)
    {
        // Basic input parameter check
        if(args.length == 0)
        {
            throw new IllegalArgumentException("Invalid parameter");
        }

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
