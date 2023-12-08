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

    public void generateArrivalEvent(CategoryConfig catConfig, double currentTime)
    {
        Entry entry = new ArrivalEntry(currentTime + catConfig.arrivalGenerator.exponentialDistribution(catConfig.lambdaArrival));
        entry.setCategory(catConfig);

        entries.add(entry);
    }

    public void generateFinishEvent(CategoryConfig catConfig, double currentTime, int serverId)
    {
        double serviceTime = catConfig.serviceGenerator.exponentialDistribution(catConfig.lambdaService);
        FinishEntry entry = new FinishEntry(currentTime + serviceTime, serverId, serviceTime);

        entry.setCategory(catConfig);
        entries.add(entry);
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
