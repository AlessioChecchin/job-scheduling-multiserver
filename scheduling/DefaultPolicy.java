package scheduling;

import entries.ArrivalEntry;
import entries.Entry;
import entries.FinishEntry;

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
            return serverList.get(((FinishEntry) entry).serverId);
        }

        return null;
    }

}
