package config;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents the configuration of the project.
 */
public class ProjectConfig
{
    /**
     * Configuration constructor.
     * @param kServerNumber The number of servers of the simulation.
     * @param hCategoriesNumber The number of categories.
     * @param nTotalJobs The number of jobs to simulate.
     * @param rSimulationRepetitions The number of repetitions of the simulation.
     * @param pSchedulingPolicyType The scheduling policy to use.
     */
    public ProjectConfig(int kServerNumber, int hCategoriesNumber, int nTotalJobs, int rSimulationRepetitions, int pSchedulingPolicyType)
    {
        this.kServerNumber = kServerNumber;
        this.hCategoriesNumber = hCategoriesNumber;
        this.nTotalJobs = nTotalJobs;
        this.rSimulationRepetitions = rSimulationRepetitions;
        this.pSchedulingPolicyType = pSchedulingPolicyType;
        this.categoryStats = new ArrayList<>(Arrays.asList(new CategoryStats[this.hCategoriesNumber]));

        this.clearStats();
    }

    /**
     * This method which form of output the simulation uses.
     * @return The form of output the simulation uses.
     */
    public boolean hasShortOutput()
    {
        return pSchedulingPolicyType == 0 && rSimulationRepetitions == 1 && nTotalJobs <= 10;
    }

    public void addEndTime(double eta)
    {
        this.eta += eta;
        this.nRun++;
    }

    public void addJobQueuingTime(double qt)
    {
        this.jobAqt += qt;
        this.processedJobs++;
    }

    public void addCategoryStats(int categoryId, double aqt, double ast, int processedEntities) throws ArrayIndexOutOfBoundsException
    {
        CategoryStats stat = this.categoryStats.get(categoryId);
        stat.avgQueuingTimeSum += aqt;
        stat.avgServiceTimeSum += ast;
        stat.processedEntities += processedEntities;
        stat.n++;
    }

    public void clearStats()
    {
        this.eta = 0;
        this.jobAqt = 0;
        this.processedJobs = 0;
        this.nRun = 0;

        for(int i = 0; i < hCategoriesNumber; i++)
            categoryStats.set(i, new CategoryStats());
    }

    /**
     * Get the average end time of the simulations.
     * @return AVG end time.
     */
    public double getAvgEta()
    {
        if(this.nRun == 0) throw new IllegalStateException();

        return this.eta / this.nRun;
    }

    /**
     * Returns the average queuing time of a job.
     * @return AVG queuing time.
     */
    public double getAvgQueuingTime()
    {
        if(processedJobs == 0) throw new IllegalStateException();

        return jobAqt / processedJobs;
    }

    public double getAvgCategoryQueuingTime(int categoryId)
    {
        CategoryStats stats = categoryStats.get(categoryId);
        if(stats.n == 0) throw new IllegalStateException();
        return stats.avgQueuingTimeSum / stats.n;
    }

    public double getAvgCategoryServiceTime(int categoryId)
    {
        CategoryStats stats = categoryStats.get(categoryId);
        if(stats.n == 0) throw new IllegalStateException();
        return stats.avgServiceTimeSum / stats.n;
    }

    public double getAvgCategoryNumber(int categoryId)
    {
        CategoryStats stats = categoryStats.get(categoryId);
        if(stats.n == 0) throw new IllegalStateException();
        return ((double)stats.processedEntities) / stats.n;
    }

    public String toString()
    {
        return String.format("%s[kServerNumber=%d, hCategoriesNumber=%d, nTotalJobs=%d, rSimulationRepetitions=%d, pSchedulingPolicyType=%d]",
                getClass().getName(), kServerNumber, hCategoriesNumber, nTotalJobs, rSimulationRepetitions, pSchedulingPolicyType);
    }

    public int getSimulationRepetitions()
    {
        return this.rSimulationRepetitions;
    }

    public int getCategoriesNumber()
    {
        return this.hCategoriesNumber;
    }

    public int getServerNumber()
    {
        return this.kServerNumber;
    }

    public int getTotalJobs()
    {
        return this.nTotalJobs;
    }

    public int getSchedulingPolicy()
    {
        return this.pSchedulingPolicyType;
    }

    /**
     * The number of servers of the simulation.
     */
    private final int kServerNumber;

    /**
     * The number of categories.
     */
    private final int hCategoriesNumber;

    /**
     * The number of jobs to simulate.
     */
    private final int nTotalJobs;

    /**
     * The number of repetitions of the simulation.
     */
    private final int rSimulationRepetitions;

    /**
     * The scheduling policy to use.
     */
    private final int pSchedulingPolicyType;

    private double eta;
    private int processedJobs;
    private int nRun;
    private double jobAqt;

    private final ArrayList<CategoryStats> categoryStats;

    private static class CategoryStats
    {
        public double avgQueuingTimeSum;
        public double avgServiceTimeSum;
        public int processedEntities;
        public int n;

        public CategoryStats()
        {
            this.avgQueuingTimeSum = 0;
            this.avgServiceTimeSum = 0;
            this.processedEntities = 0;
            this.n = 0;
        }
    }
}