package events.entries;

import config.CategoryConfig;

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

    public void setCategory(CategoryConfig category)
    {
        this.category = category;
    }

    public CategoryConfig getCategory()
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
    private CategoryConfig category;
}
