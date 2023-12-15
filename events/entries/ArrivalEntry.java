package events.entries;

import config.Category;

/**
 * Represents an arrival entry.
 */
public class ArrivalEntry extends Entry
{
    /**
     * Creates a new arrival entry.
     * @param key The key of the entry.
     */
    public ArrivalEntry(double key, Category category)
    {
        super(key, category);
    }

    /**
     * Setter for start execution time.
     * @param executionStart Start execution time.
     */
    public void setStartExecution(double executionStart)
    {
        this.executionStart = executionStart;
    }

    /**
     * Gets the instant it started executing.
     * @return The instant it started executing.
     */
    public double getStartExecution()
    {
        return this.executionStart;
    }

    /**
     * The moment when the job associated with this entry started executing.
     */
    private double executionStart;
}
