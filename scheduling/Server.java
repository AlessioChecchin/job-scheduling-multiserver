package scheduling;

import events.entries.Entry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class that represents a simulation server.
 */
public class Server implements Comparable<Server>
{
    /**
     * Creates a server
     * @param id Identifier of the server. It must be unique.
     */
    public Server(int id)
    {
        this.jobs = new LinkedList<>();
        this.id = id;
        this.waitingTime = 0;
    }

    /**
     * Returns if the server is busy.
     * @return True if the server is busy, false otherwise.
     */
    public boolean isBusy()
    {
        return !this.jobs.isEmpty();
    }

    /**
     * Adds a new entry to execute.
     * @param entry The entry to execute.
     */
    public void enqueue(Entry entry)
    {
        this.jobs.add(entry);
    }

    /**
     * Removes an entry from the queue.
     * @return The removed entry.
     */
    public Entry remove()
    {
        return this.jobs.remove();
    }

    /**
     * Returns the current executing entry.
     * @return The current executing entry.
     */
    public Entry getCurrentJob()
    {
        return this.jobs.element();
    }

    /**
     * Returns the identifier of the server.
     * @return The identifier of the server.
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Set the estimated waiting time.
     * @param newValue The estimated waiting time.
     */
    public void setWaitingTime(double newValue)
    {
        this.waitingTime = newValue;
    }

    /**
     * Returns the current estimated waiting time for a new job.
     * @return Estimated waiting time.
     */
    public double getWaitingTime()
    {
        return waitingTime;
    }

    /**
     * Compare to override
     * @param o the object to be compared.
     * @return Double.compare(this.waitingTime, o.waitingTime)
     */
    @Override
    public int compareTo(Server o)
    {
        return Double.compare(this.waitingTime, o.waitingTime);
    }

    /**
     * Jobs to execute.
     */
    private final Queue<Entry> jobs;

    /**
     * Identifier of the server.
     */
    private final int id;

    /**
     * Estimated waiting time.
     */
    private double waitingTime;
}
