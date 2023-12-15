package events.entries;

import config.Category;

/**
 * Class that represents an entry.
 */
public class Entry implements Comparable<Entry>
{
    /**
     * Entry constructor.
     * @param key The key of the entry.
     * @param category The category associated with the entry.
     */
    public Entry(double key, Category category)
    {
        this.key = key;
        this.category = category;
    }

    /**
     * Key getter.
     * @return Key.
     */
    public double getKey()
    {
        return this.key;
    }

    /**
     * Getter for category.
     * @return The category associated with the entry.
     */
    public Category getCategory()
    {
        return this.category;
    }

    @Override
    public int compareTo(Entry obj)
    {
        return Double.compare(this.key, obj.key);
    }

    /**
     * To string method.
     * @return A string representation of the object.
     */
    public String toString()
    {
        return String.format("%s[key=%f][category=%s]", getClass().getName(), this.key, this.category.toString());
    }

    /**
     * Key of the entry.
     */
    private final double key;

    /**
     * Category associated to the entry.
     */
    private final Category category;
}
