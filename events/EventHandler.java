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
        this.entries = new PriorityQueue<>();
    }

    /**
     * Generates a new arrival event (entry)
     * @param catConfig The category of the job that the event represents.
     * @param currentTime Current time.
     * @return The generated arrival event.
     */
    public ArrivalEntry generateArrivalEvent(Category catConfig, double currentTime)
    {
        ArrivalEntry entry = new ArrivalEntry(currentTime + catConfig.getArrivalGenerator().exponentialDistribution(catConfig.getLambdaArrival()), catConfig);
        this.entries.add(entry);

        return entry;
    }

    /**
     * Generates a new finish event (entry)
     * @param linkedArrival The arrival entry linked to the same job.
     * @param currentTime Current time.
     * @param serverId The server that executed the job.
     * @return The generated finish event.
     */
    public FinishEntry generateFinishEvent(ArrivalEntry linkedArrival, double currentTime, int serverId)
    {
        Category catConfig = linkedArrival.getCategory();
        double serviceTime = catConfig.getServiceGenerator().exponentialDistribution(catConfig.getLambdaService());
        FinishEntry entry = new FinishEntry(currentTime + serviceTime, serverId, serviceTime, linkedArrival);

        this.entries.add(entry);

        return entry;
    }

    /**
     * Checks if there are pending events.
     * @return If there are pending events.
     */
    public boolean hasEvent()
    {
        return !this.entries.isEmpty();
    }

    /**
     * Removes from the priority queue and returns the most recent event.
     * @return The most recent event.
     */
    public Entry remove()
    {
        return this.entries.remove();
    }

    /**
     * Temporary queue for handling entries.
     */
    private final PriorityQueue<Entry> entries;
}
