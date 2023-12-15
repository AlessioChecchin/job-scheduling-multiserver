package events;

import config.Category;

import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;

import java.util.PriorityQueue;

/**
 * Class that handles event creation.
 */
public class EventHandler
{

    /**
     * Constructor.
     */
    public EventHandler()
    {
        entries = new PriorityQueue<>();
    }

    /**
     * Generates a new arrival event (entry)
     * @param catConfig The category of the job that the event represents.
     * @param currentTime Current time.
     * @return The generated arrival event.
     */
    public ArrivalEntry generateArrivalEvent(Category catConfig, double currentTime)
    {
        ArrivalEntry entry = new ArrivalEntry(currentTime + catConfig.getArrivalGenerator().exponentialDistribution(catConfig.getLambdaArrival()));
        entry.setCategory(catConfig);

        entries.add(entry);

        return entry;
    }

    /**
     * Generates a new finish event (entry)
     * @param catConfig The category of the job that the event represents.
     * @param currentTime Current time.
     * @param serverId The server that executed the job.
     * @param linkedArrival The arrival entry linked to the same job.
     * @return The generated finish event.
     */
    public FinishEntry generateFinishEvent(Category catConfig, double currentTime, int serverId, ArrivalEntry linkedArrival)
    {
        double serviceTime = catConfig.getServiceGenerator().exponentialDistribution(catConfig.getLambdaService());
        FinishEntry entry = new FinishEntry(currentTime + serviceTime, serverId, serviceTime, linkedArrival);

        entry.setCategory(catConfig);
        entries.add(entry);

        return entry;
    }

    /**
     * Checks if there are pending events.
     * @return If there are pending events.
     */
    public boolean hasEvent()
    {
        return !entries.isEmpty();
    }

    /**
     * Removes from the priority queue and returns the most recent event.
     * @return The most recent event.
     */
    public Entry remove()
    {
        return entries.remove();
    }

    private final PriorityQueue<Entry> entries;
}
