package events.entries;

public class ArrivalEntry extends Entry
{
    public ArrivalEntry(double key)
    {
        super(key);
    }
    public void setStartExecution(double time)
    {
        executionStart = time;
    }

    public double getStartExecution()
    {
        return executionStart;
    }

    private double executionStart;
}
