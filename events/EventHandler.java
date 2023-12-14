package events;

import config.CategoryConfig;
import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;

import java.util.PriorityQueue;

public class EventHandler
{
    public EventHandler()
    {
        entries = new PriorityQueue<>();
    }

    public ArrivalEntry generateArrivalEvent(CategoryConfig catConfig, double currentTime)
    {
        ArrivalEntry entry = new ArrivalEntry(currentTime + catConfig.arrivalGenerator.exponentialDistribution(catConfig.lambdaArrival));
        entry.setCategory(catConfig);

        entries.add(entry);

        return entry;
    }

    public FinishEntry generateFinishEvent(CategoryConfig catConfig, double currentTime, int serverId, ArrivalEntry linkedArrival)
    {
        double serviceTime = catConfig.serviceGenerator.exponentialDistribution(catConfig.lambdaService);
        FinishEntry entry = new FinishEntry(currentTime + serviceTime, serverId, serviceTime, linkedArrival);

        entry.setCategory(catConfig);
        entries.add(entry);

        return entry;
    }

    public boolean hasEvent()
    {
        return !entries.isEmpty();
    }

    public Entry remove()
    {
        return entries.remove();
    }


    // Private fields
    private final PriorityQueue<Entry> entries;
}
