package scheduling;

import events.entries.Entry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class that represents a simulation server.
 */
public class Server
{
    /**
     * Creates a server
     * @param id Identifier of the server. It must be unique.
     */
    public Server(int id)
    {
        this.jobs = new LinkedList<>();
        this.id = id;
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
     * Jobs to execute.
     */
    private final Queue<Entry> jobs;

    /**
     * Id of the server.
     */
    private final int id;
}
