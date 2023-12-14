package scheduling;

import events.EventHandler;
import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;

import scheduling.policy.SchedulingPolicy;

import java.util.List;

/**
 * Class that represents a scheduler.
 * It interacts with the scheduling policy and the event handler.
 */
public class Scheduler
{
    /**
     * Creates a new scheduler.
     * @param serverList The list of servers on which to do scheduling
     * @param evtHandler The event handler.
     * @param policy A scheduling policy.
     */
    public Scheduler(List<Server> serverList, EventHandler evtHandler, SchedulingPolicy policy)
    {
        arrivedJobs  = 0;
        finishedJobs = 0;

        this.serverList = serverList;
        this.evtHandler = evtHandler;
        this.policy = policy;
    }

    /**
     * Returns the list of servers that the scheduler is handling.
     * @return The list of servers that the scheduler is handling.
     */
    public List<Server> getServers()
    {
        return serverList;
    }

    /**
     * Adds a new entry for scheduling.
     * @param entry The entry to schedule.
     */
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
                evtHandler.generateFinishEvent(entry.getCategory(), entry.getKey(), target.getId(), (ArrivalEntry)entry)
                        .getLinkedArrival().setStartExecution(entry.getKey());
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
                evtHandler.generateFinishEvent(currentJob.getCategory(), entry.getKey(), target.getId(), (ArrivalEntry)currentJob)
                        .getLinkedArrival().setStartExecution(entry.getKey());
            }

            finishedJobs++;
        }
    }

    /**
     * Returns the number of finished jobs.
     * @return The number of finished jobs.
     */
    public int getFinishedJobs()
    {
        return finishedJobs;
    }

    /**
     * Returns the number of arrived jobs.
     * @return The number of arrived jobs.
     */
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
