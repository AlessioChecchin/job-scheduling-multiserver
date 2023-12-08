package scheduling;

import config.CategoryConfig;

import events.EventHandler;
import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;
import scheduling.policy.SchedulingPolicy;

import java.util.List;

public class Scheduler
{
    public Scheduler(List<Server> serverList, EventHandler evtHandler, SchedulingPolicy policy)
    {
        arrivedJobs  = 0;
        finishedJobs = 0;

        this.serverList = serverList;
        this.evtHandler = evtHandler;
        this.policy = policy;
    }

    public List<Server> getServers()
    {
        return serverList;
    }

    public void schedule(Entry entry)
    {
        // Obtaining server target based on current target and scheduler state.
        Server target = policy.selectServer(entry, this);

        if(entry instanceof ArrivalEntry)
        {
            boolean immediateExecution = !target.isBusy();

            // If the server isn't busy, the job starts executing immediately
            if(immediateExecution)
            {
                CategoryConfig catConfig = entry.getCategory();

                evtHandler.generateFinishEvent(entry.getCategory(), entry.getKey(), target.getId());
            }

            target.enqueue(entry);
            arrivedJobs++;
        }
        else if(entry instanceof FinishEntry)
        {
            // Entry that finished executing
            Entry finished = target.remove();

            // Current executing job
            if(target.isBusy())
            {
                Entry currentJob = target.getCurrentJob();

                // Generates end event for current job
                evtHandler.generateFinishEvent(currentJob.getCategory(), entry.getKey(), target.getId());
            }

            finishedJobs++;
        }
    }

    public int getFinishedJobs()
    {
        return finishedJobs;
    }

    public int getArrivedJobs()
    {
        return arrivedJobs;
    }


    private final SchedulingPolicy policy;
    private final EventHandler evtHandler;
    private final List<Server> serverList;
    private int arrivedJobs;
    private int finishedJobs;

}
