package scheduling.policy;

import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;

import scheduling.Scheduler;
import scheduling.Server;

import java.util.List;

/**
 * Class that represents the default scheduling policy.
 */
public class DefaultPolicy implements SchedulingPolicy
{
    public Server pollServer(Entry entry, Scheduler scheduler)
    {
        List<Server> serverList = (List<Server>) scheduler.getServers();

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

    @Override
    public void putServer(Server server, Scheduler scheduler)
    {

    }
}
