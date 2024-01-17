package scheduling.policy;

import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;

import scheduling.Scheduler;
import scheduling.Server;

import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class that represents the default scheduling policy.
 */
public class CustomPolicy implements SchedulingPolicy
{
    public Server pollServer(Entry entry, Scheduler scheduler)
    {
        PriorityQueue<Server> serverList = (PriorityQueue<Server>) scheduler.getServers();

        if(entry instanceof ArrivalEntry)
        {
            return serverList.poll();
        }
        else if(entry instanceof FinishEntry)
        {
            Iterator<Server> iterator = serverList.iterator();
            while(iterator.hasNext())
            {
                Server current = iterator.next();

                if (((FinishEntry) entry).getServerId() == current.getId())
                {
                    iterator.remove();
                    return current;
                }
            }

        }

        return null;
    }

    @Override
    public void putServer(Server server, Scheduler scheduler)
    {
        scheduler.getServers().add(server);
    }
}
