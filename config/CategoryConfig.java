package config;

import utils.RandomGenerator;

/**
 * This class represents a category.
 */
public class CategoryConfig
{
    /**
     * Lambda used for generating inter-arrival times with exponential distribution of lambda.
     */
    public final double lambdaArrival;

    /**
     * Lambda used for generating service times with exponential distribution of lambda.
     */
    public final double lambdaService;

    /**
     * Seed used for generating inter-arrival times.
     */
    public final int seedArrival;

    /**
     * Seed used for generating service times.
     */
    public final int seedService;

    /**
     * Generator for arrival time with exponential distribution.
     */
    public final RandomGenerator arrivalGenerator;

    /**
     * Generator for service time with exponential distribution.
     */
    public final RandomGenerator serviceGenerator;

    /**
     * Identifier of the category
     */
    public final int id;

    /**
     * Creates a new category.
     *
     * @param id Identifier of the category. It must be unique.
     * @param lambdaArrival Lambda used for generating arrival times with exponential distribution of lambda.
     * @param lambdaService Lambda used for generating service times with exponential distribution of lambda.
     * @param seedArrival Seed used for generating inter-arrival times.
     * @param seedService Seed used for generating service times.
     * @param arrivalGenerator Generator for arrival time with exponential distribution.
     * @param serviceGenerator Generator for service time with exponential distribution.
     */
    public CategoryConfig(int id, double lambdaArrival, double lambdaService, int seedArrival, int seedService, RandomGenerator arrivalGenerator, RandomGenerator serviceGenerator)
    {
        this.id               = id;
        this.lambdaArrival    = lambdaArrival;
        this.lambdaService    = lambdaService;
        this.seedArrival      = seedArrival;
        this.seedService      = seedService;
        this.arrivalGenerator = arrivalGenerator;
        this.serviceGenerator = serviceGenerator;

        qtSum = 0;
        svTimeSum = 0;
        processedCategories = 0;
    }

    /**
     * Add data to calculate statistics over the simulation.
     * @param queuingTime Queuing time for a job of the current category. It's the amount of time that the jobs waits
     *                    before executing.
     * @param serviceTime Amount of time that a job takes to complete.
     */
    public void addCategoryStats(double queuingTime, double serviceTime)
    {
        qtSum += queuingTime;
        svTimeSum += serviceTime;
        processedCategories++;
    }

    /**
     * Calculates the average queuing time.
     * @return AQT for current category.
     */
    public double getAvgQueuingTime()
    {
        if(processedCategories == 0)
            throw new IllegalStateException();
        return qtSum / processedCategories;
    }

    /**
     * Returns the number of processed categories.
     * @return The number of processed categories.
     */
    public int getProcessedCategories()
    {
        return processedCategories;
    }

    /**
     * Calculates the average service time.
     * @return The average service time.
     */
    public double getAvgServiceTime()
    {
        if(processedCategories == 0) throw new IllegalStateException();

        return svTimeSum / processedCategories;
    }

    public String toString()
    {
        return String.format("%s[id=%d, lambdaArrival=%f, lambdaService=%f, seedArrival=%d, seedService=%d]",
                getClass().getName(), id, lambdaArrival, lambdaService, seedArrival, seedService);
    }

    private int processedCategories;
    private double qtSum;
    private double svTimeSum;
}