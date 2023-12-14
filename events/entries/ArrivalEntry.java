package events.entries;

/**
 * Represents an arrival entry.
 */
public class ArrivalEntry extends Entry
{
    /**
     * Creates a new arrival entry.
     * @param key The key of the entry.
     */
    public ArrivalEntry(double key)
    {
        super(key);
    }

    /**
     * Setter for start execution time.
     * @param time Start execution time.
     */
    public void setStartExecution(double time)
    {
        executionStart = time;
    }

    /**
     * Gets the instant it started executing.
     * @return The instant it started executing.
     */
    public double getStartExecution()
    {
        return executionStart;
    }

    private double executionStart;
}
