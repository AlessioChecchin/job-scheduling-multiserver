package events.entries;

public class FinishEntry extends Entry
{
    public FinishEntry(double key, int serverId, double serviceTime)
    {
        super(key);
        this.serverId = serverId;
        this.serviceTime = serviceTime;
    }

    public int getServerId()
    {
        return serverId;
    }

    public double getServiceTime()
    {
        return serviceTime;
    }

    private final int serverId;
    private final double serviceTime;

}
