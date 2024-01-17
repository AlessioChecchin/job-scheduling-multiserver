package scheduling;

import events.EventHandler;
import events.entries.ArrivalEntry;
import events.entries.Entry;
import events.entries.FinishEntry;

import scheduling.policy.SchedulingPolicy;

import java.util.Collection;
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
    public Scheduler(Collection<Server> serverList, EventHandler evtHandler, SchedulingPolicy policy)
    {
        this.serverList = serverList;
        this.evtHandler = evtHandler;
        this.policy = policy;

        this.arrivedJobs  = 0;
        this.finishedJobs = 0;
    }

    /**
     * Returns the list of servers that the scheduler is handling.
     * @return The list of servers that the scheduler is handling.
     */
    public Collection<Server> getServers()
    {
        return this.serverList;
    }

    /**
     * Adds a new entry for scheduling.
     * @param entry The entry to schedule.
     */
    public void schedule(Entry entry)
    {
        // Obtaining server target based on current target and scheduler state.
        Server target = this.policy.pollServer(entry, this);

        if(entry instanceof ArrivalEntry)
        {
            boolean immediateExecution = !target.isBusy();

            // If the server isn't busy, the job starts executing immediately
            if(immediateExecution)
            {
                this.evtHandler.generateFinishEvent((ArrivalEntry)entry, entry.getKey(), target.getId())
                        .getLinkedArrival().setStartExecution(entry.getKey());
            }

            target.enqueue(entry);
            // Adjust estimated queuing time.
            target.setWaitingTime(target.getWaitingTime() + 1.0 / entry.getCategory().getLambdaService());

            this.arrivedJobs++;
        }
        else if(entry instanceof FinishEntry)
        {
            // Entry that finished executing
            Entry finished = target.remove();
            target.setWaitingTime(target.getWaitingTime() - 1.0 / entry.getCategory().getLambdaService());

            // Current executing job
            if(target.isBusy())
            {
                Entry currentJob = target.getCurrentJob();

                // Generates end event for current job
                this.evtHandler.generateFinishEvent((ArrivalEntry)currentJob, entry.getKey(), target.getId())
                        .getLinkedArrival().setStartExecution(entry.getKey());
            }

            this.finishedJobs++;
        }

        policy.putServer(target, this);
    }

    /**
     * Returns the number of finished jobs.
     * @return The number of finished jobs.
     */
    public int getFinishedJobs()
    {
        return this.finishedJobs;
    }

    /**
     * Returns the number of arrived jobs.
     * @return The number of arrived jobs.
     */
    public int getArrivedJobs()
    {
        return this.arrivedJobs;
    }

    /**
     * Policy used to select the target server.
     */
    private final SchedulingPolicy policy;

    /**
     * Event handler.
     */
    private final EventHandler evtHandler;

    /**
     * Server list.
     */
    private final Collection<Server> serverList;

    /**
     * Number of jobs arrived.
     */
    private int arrivedJobs;

    /**
     * Number of finished jobs.
     */
    private int finishedJobs;
}
