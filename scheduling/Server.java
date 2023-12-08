package scheduling;

import events.entries.Entry;

import java.util.LinkedList;
import java.util.Queue;

public class Server
{
    public Server(int id)
    {
        jobs = new LinkedList<>();
        this.id = id;
    }

    public boolean isBusy()
    {
        return !jobs.isEmpty();
    }

    public void enqueue(Entry entry)
    {
        jobs.add(entry);
    }

    public Entry remove()
    {
        return jobs.remove();
    }

    public Entry getCurrentJob()
    {
        return jobs.element();
    }

    public int getId()
    {
        return id;
    }

    private final Queue<Entry> jobs;
    private final int id;
}
