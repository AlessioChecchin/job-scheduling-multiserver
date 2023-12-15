package events.entries;

import config.Category;

public class Entry implements Comparable<Entry>
{
    public Entry(double key)
    {
        this.key = key;
    }

    public double getKey()
    {
        return key;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public Category getCategory()
    {
        return category;
    }

    @Override
    public int compareTo(Entry o)
    {
        return Double.compare(key, o.key);
    }

    public String toString()
    {
        return String.format("%s[key=%f][category=%s]", getClass().getName(), key, category.toString());
    }

    private final double key;
    private Category category;
}
