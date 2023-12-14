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
        jobs = new LinkedList<>();
        this.id = id;
    }

    /**
     * Returns if the server is busy.
     * @return True if the server is busy, false otherwise.
     */
    public boolean isBusy()
    {
        return !jobs.isEmpty();
    }

    /**
     * Adds a new entry to execute.
     * @param entry The entry to execute.
     */
    public void enqueue(Entry entry)
    {
        jobs.add(entry);
    }

    /**
     * Removes an entry from the queue.
     * @return The removed entry.
     */
    public Entry remove()
    {
        return jobs.remove();
    }

    /**
     * Returns the current executing entry.
     * @return The current executing entry.
     */
    public Entry getCurrentJob()
    {
        return jobs.element();
    }

    /**
     * Returns the identifier of the server.
     * @return The identifier of the server.
     */
    public int getId()
    {
        return id;
    }

    private final Queue<Entry> jobs;
    private final int id;
}
