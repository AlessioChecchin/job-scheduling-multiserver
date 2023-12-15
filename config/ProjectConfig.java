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
        return this.pSchedulingPolicyType == 0 && this.rSimulationRepetitions == 1 && this.nTotalJobs <= 10;
    }

    /**
     * Adds an end of time for a specific run.
     * @param eta The end of time for the current run.
     */
    public void addEndTime(double eta)
    {
        this.eta += eta;
        this.nRun++;
    }

    /**
     * Adds the queuing time of a generic job.
     * @param qt Queuing time.
     */
    public void addJobQueuingTime(double qt)
    {
        this.jobAqt += qt;
        this.processedJobs++;
    }

    /**
     * Adds the stats of a category after a generic run.
     * @param categoryId The id of the category.
     * @param aqt Average queuing time of the category during the last run.
     * @param ast Average service time of the category during the run.
     * @param processedEntities Number of categories processed during the last run.
     * @throws ArrayIndexOutOfBoundsException If the category id is not valid.
     */
    public void addCategoryStats(int categoryId, double aqt, double ast, int processedEntities) throws ArrayIndexOutOfBoundsException
    {
        CategoryStats stat = this.categoryStats.get(categoryId);
        stat.avgQueuingTimeSum += aqt;
        stat.avgServiceTimeSum += ast;
        stat.processedEntities += processedEntities;
        stat.n++;
    }

    /**
     * Clears the stats.
     */
    public void clearStats()
    {
        this.eta = 0;
        this.jobAqt = 0;
        this.processedJobs = 0;
        this.nRun = 0;

        for(int i = 0; i < this.hCategoriesNumber; i++)
        {
            this.categoryStats.set(i, new CategoryStats());
        }
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
        if(this.processedJobs == 0) throw new IllegalStateException();

        return this.jobAqt / this.processedJobs;
    }

    /**
     * Average over R runs of the average queuing time.
     * @param categoryId The category to query.
     * @return The average of the average queuing time.
     */
    public double getAvgCategoryQueuingTime(int categoryId)
    {
        CategoryStats stats = this.categoryStats.get(categoryId);
        if(stats.n == 0) throw new IllegalStateException();
        return stats.avgQueuingTimeSum / stats.n;
    }

    /**
     * Average over R runs of the average service time.
     * @param categoryId The category to query.
     * @return The average of the average service time.
     */
    public double getAvgCategoryServiceTime(int categoryId)
    {
        CategoryStats stats = this.categoryStats.get(categoryId);
        if(stats.n == 0) throw new IllegalStateException();
        return stats.avgServiceTimeSum / stats.n;
    }

    /**
     * Average over R runs of the number of categories processed.
     * @param categoryId The category to query.
     * @return The average processed categories.
     */
    public double getAvgCategoryNumber(int categoryId)
    {
        CategoryStats stats = this.categoryStats.get(categoryId);
        if(stats.n == 0) throw new IllegalStateException();
        return ((double)stats.processedEntities) / stats.n;
    }

    /**
     * To string method.
     * @return A string representation of the object.
     */
    public String toString()
    {
        return String.format("%s[kServerNumber=%d, hCategoriesNumber=%d, nTotalJobs=%d, rSimulationRepetitions=%d, pSchedulingPolicyType=%d]",
                getClass().getName(), this.kServerNumber, this.hCategoriesNumber, this.nTotalJobs, this.rSimulationRepetitions, this.pSchedulingPolicyType);
    }

    /**
     * Getter for the number of repetitions of the simulation.
     * @return The number of repetitions.
     */
    public int getSimulationRepetitions()
    {
        return this.rSimulationRepetitions;
    }

    /**
     * Getter for the number of categories.
     * @return The number of categories.
     */
    public int getCategoriesNumber()
    {
        return this.hCategoriesNumber;
    }

    /**
     * Getter for the number of servers.
     * @return The number of servers.
     */
    public int getServerNumber()
    {
        return this.kServerNumber;
    }

    /**
     * Getter for the number of jobs processed over the entire simulation (R runs).
     * @return The number of jobs.
     */
    public int getTotalJobs()
    {
        return this.nTotalJobs;
    }

    /**
     * Getter for the scheduling policy type.
     * @return The scheduling policy type.
     */
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

    /**
     * Sum of end of times over R runs.
     */
    private double eta;

    /**
     * Number of processed jobs.
     */
    private int processedJobs;

    /**
     * Current run.
     */
    private int nRun;

    /**
     * Average queuing time of jobs over R runs.
     */
    private double jobAqt;

    /**
     * Array of category statistics.
     */
    private final ArrayList<CategoryStats> categoryStats;

    /**
     * Class that represents statistic of a specific category, over R runs.
     */
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