import config.CategoryConfig;
import config.ProjectConfig;
import entries.*;
import scheduling.DefaultPolicy;
import scheduling.Scheduler;
import scheduling.Server;
import utils.RandomGenerator;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Simulator
{
    public ProjectConfig config;
    public List<CategoryConfig> categoryConfig;
    private Scheduler scheduler;
    private EventHandler evtHandler;

    public Simulator(String path)
    {
        // Reading input from file
        readInput(path);

        // Creating an event handler
        evtHandler = new EventHandler(config);

        // Initializing jobs
        for(int i = 0; i < config.hCategoriesNumber; i++)
        {
            CategoryConfig catConfig = categoryConfig.get(i);
            evtHandler.generateArrivalEvent(catConfig, 0);
        }

        List<Server> servers = new ArrayList<>();

        // Creating K servers.
        for(int i = 0; i < config.kServerNumber; i++)
        {
            servers.add(new Server(i));
        }

        // Creating a new scheduler.
        scheduler = new Scheduler(servers, evtHandler, new DefaultPolicy());

        // Running until all jobs are finished
        while(evtHandler.hasEvent())
        {
            Entry entry = evtHandler.remove();
            if(entry instanceof ArrivalEntry && evtHandler.getArrivalCount() < config.nTotalJobs)
            {
                evtHandler.generateArrivalEvent(entry.getCategory(), entry.getKey());
            }

            scheduler.schedule(entry);

        }

        outputResult();

    }

    protected void readInput(String path)
    {
        try (Scanner fileScanner = new Scanner(new FileReader(path)))
        {
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

            categoryConfig = new ArrayList<>();

            int count = 0;
            while (fileScanner.hasNextLine() && count < config.hCategoriesNumber)
            {
                Scanner paramScanner = new Scanner((fileScanner.nextLine()));
                paramScanner.useDelimiter(",");

                double lambdaArrival    = Double.parseDouble(paramScanner.next());
                double lambdaService    = Double.parseDouble(paramScanner.next());
                int seedArrival      = Integer.parseInt(paramScanner.next());
                int seedService      = Integer.parseInt(paramScanner.next());
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

                categoryConfig.add(catConfig);

                paramScanner.close();

                count++;
            }

        }
        catch (IOException e)
        {
            System.out.println("IOException caught: " + e.getMessage());
        }
    }

    protected void outputResult()
    {
        System.out.println(config);

        if(config.showShortOutput())
        {
            PriorityQueue<Entry> history = evtHandler.getHistory();
            while(!history.isEmpty())
            {
                Entry entry = history.remove();

                double serviceTime = 0;

                if(entry instanceof FinishEntry)
                {
                    serviceTime = ((FinishEntry)entry).serviceTime;
                }

                System.out.println(entry.getKey() + "," + serviceTime + "," + entry.getCategory().id);
            }
        }

        System.out.println(categoryConfig);
    }


    public static void main(String[] args)
    {
        // Basic input parameter check
        if(args.length == 0) throw new IllegalArgumentException("Invalid parameter");

        String filePath = args[0];

        // Instantiating simulation
        new Simulator(filePath);
    }
}
