package events.entries;

public class FinishEntry extends Entry
{
    public FinishEntry(double key, int serverId, double serviceTime, ArrivalEntry arrivalEntry)
    {
        super(key);
        this.serverId = serverId;
        this.serviceTime = serviceTime;
        this.arrivalEntry = arrivalEntry;
    }

    public int getServerId()
    {
        return serverId;
    }

    public double getServiceTime()
    {
        return serviceTime;
    }

    public ArrivalEntry getLinkedArrival()
    {
        return arrivalEntry;
    }
    private final ArrivalEntry arrivalEntry;
    private final int serverId;
    private final double serviceTime;

}
