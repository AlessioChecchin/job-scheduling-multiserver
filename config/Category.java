package config;

import utils.RandomGenerator;

import java.util.Random;

/**
 * This class represents a category.
 */
public class Category
{
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
    public Category(int id, double lambdaArrival, double lambdaService, int seedArrival, int seedService, RandomGenerator arrivalGenerator, RandomGenerator serviceGenerator)
    {
        this.id               = id;
        this.lambdaArrival    = lambdaArrival;
        this.lambdaService    = lambdaService;
        this.seedArrival      = seedArrival;
        this.seedService      = seedService;
        this.arrivalGenerator = arrivalGenerator;
        this.serviceGenerator = serviceGenerator;

        this.clearStats();
    }

    /**
     * Add data to calculate statistics over the simulation.
     * @param queuingTime Queuing time for a job of the current category. It's the amount of time that the jobs waits
     *                    before executing.
     * @param serviceTime Amount of time that a job takes to complete.
     */
    public void addStats(double queuingTime, double serviceTime)
    {
        this.queuingTimeSum += queuingTime;
        this.serviceTimeSum += serviceTime;
        this.processedCategories++;
    }

    public void clearStats()
    {
        this.queuingTimeSum = 0;
        this.serviceTimeSum = 0;
        this.processedCategories = 0;
    }

    /**
     * Calculates the average queuing time.
     * @return AQT for current category.
     */
    public double getAvgQueuingTime()
    {
        if(this.processedCategories == 0) throw new IllegalStateException();
        return this.queuingTimeSum / this.processedCategories;
    }

    /**
     * Returns the number of processed categories.
     * @return The number of processed categories.
     */
    public int getProcessedCategories()
    {
        return this.processedCategories;
    }

    /**
     * Calculates the average service time.
     * @return The average service time.
     */
    public double getAvgServiceTime()
    {
        if(this.processedCategories == 0) throw new IllegalStateException();
        return this.serviceTimeSum / this.processedCategories;
    }

    public String toString()
    {
        return String.format("%s[id=%d, lambdaArrival=%f, lambdaService=%f, seedArrival=%d, seedService=%d]",
                getClass().getName(), this.id, this.lambdaArrival, this.lambdaService, this.seedArrival, this.seedService);
    }

    public double getLambdaArrival()
    {
        return this.lambdaArrival;
    }

    public double getLambdaService()
    {
        return this.lambdaService;
    }

    public int getSeedArrival()
    {
        return this.seedArrival;
    }

    public int getSeedService()
    {
        return this.seedService;
    }

    public RandomGenerator getArrivalGenerator()
    {
        return this.arrivalGenerator;
    }

    public RandomGenerator getServiceGenerator()
    {
        return this.serviceGenerator;
    }

    public int getId()
    {
        return this.id;
    }

    /**
     * Lambda used for generating inter-arrival times with exponential distribution of lambda.
     */
    private final double lambdaArrival;

    /**
     * Lambda used for generating service times with exponential distribution of lambda.
     */
    private final double lambdaService;

    /**
     * Seed used for generating inter-arrival times.
     */
    private final int seedArrival;

    /**
     * Seed used for generating service times.
     */
    private final int seedService;

    /**
     * Generator for arrival time with exponential distribution.
     */
    private final RandomGenerator arrivalGenerator;

    /**
     * Generator for service time with exponential distribution.
     */
    private final RandomGenerator serviceGenerator;

    /**
     * Identifier of the category
     */
    private final int id;


    private int processedCategories;
    private double queuingTimeSum;
    private double serviceTimeSum;
}