package events.entries;

/**
 * Represents a finish entry.
 */
public class FinishEntry extends Entry
{
    /**
     * Creates a finish entry.
     * @param key The key of the entry.
     * @param serverId The server that processed the entry.
     * @param serviceTime The time that took to execute.
     * @param arrivalEntry The linked arrival entry.
     */
    public FinishEntry(double key, int serverId, double serviceTime, ArrivalEntry arrivalEntry)
    {
        super(key, arrivalEntry.getCategory());
        this.serverId = serverId;
        this.serviceTime = serviceTime;
        this.arrivalEntry = arrivalEntry;
    }

    /**
     * Returns the id of the server that processed the entry.
     * @return Identifier of the server.
     */
    public int getServerId()
    {
        return this.serverId;
    }

    /**
     * Returns the time that took the job to complete the execution.
     * @return Service time.
     */
    public double getServiceTime()
    {
        return this.serviceTime;
    }

    /**
     * Returns the linked arrival entry.
     * @return The linked arrival entry.
     */
    public ArrivalEntry getLinkedArrival()
    {
        return this.arrivalEntry;
    }

    /**
     * Linked arrival entry.
     */
    private final ArrivalEntry arrivalEntry;

    /**
     * The server that processed the job associated with the entry.
     */
    private final int serverId;

    /**
     * The service time of the job associated with the entry.
     */
    private final double serviceTime;

}
