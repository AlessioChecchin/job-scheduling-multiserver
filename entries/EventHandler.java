package entries;

import config.CategoryConfig;
import config.ProjectConfig;

import java.util.PriorityQueue;

public class EventHandler
{
    private final PriorityQueue<Entry> history;
    private final PriorityQueue<Entry> entries;
    private final ProjectConfig config;
    private int arrivalCount;
    private int finishCount;
    public EventHandler(ProjectConfig config)
    {
        this.config = config;
        entries = new PriorityQueue<>();
        history = new PriorityQueue<>();
    }

    public void generateArrivalEvent(CategoryConfig catConfig, double currentTime)
    {
        Entry entry = new ArrivalEntry(currentTime + catConfig.arrivalGenerator.exponentialDistribution(catConfig.lambdaArrival));
        entry.setCategory(catConfig);

        entries.add(entry);

        arrivalCount++;

        pushToHistory(entry);
    }

    public void generateFinishEvent(CategoryConfig catConfig, double currentTime, int serverId)
    {
        double serviceTime = catConfig.serviceGenerator.exponentialDistribution(catConfig.lambdaService);
        FinishEntry entry = new FinishEntry(currentTime + serviceTime);
        entry.setCategory(catConfig);
        entry.serverId = serverId;
        entry.serviceTime = serviceTime;

        entries.add(entry);

        finishCount++;

        pushToHistory(entry);
    }

    protected void pushToHistory(Entry entry)
    {
        if(config.showShortOutput())
        {
            history.add(entry);
        }
    }

    public boolean hasEvent()
    {
        return !entries.isEmpty();
    }
    public Entry remove()
    {
        return entries.remove();
    }

    public int getArrivalCount()
    {
        return arrivalCount;
    }

    public int getFinishCount()
    {
        return finishCount;
    }

    public PriorityQueue<Entry> getHistory()
    {
        return history;
    }
}
