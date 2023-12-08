package scheduling.policy;

import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;

import scheduling.Scheduler;
import scheduling.Server;

import java.util.List;

public class DefaultPolicy implements SchedulingPolicy
{
    public Server selectServer(Entry entry, Scheduler scheduler)
    {
        List<Server> serverList = scheduler.getServers();

        if(entry instanceof ArrivalEntry)
        {
            return serverList.get(scheduler.getArrivedJobs() % serverList.size());
        }
        else if(entry instanceof FinishEntry)
        {
            return serverList.get(((FinishEntry) entry).getServerId());
        }

        return null;
    }
}
