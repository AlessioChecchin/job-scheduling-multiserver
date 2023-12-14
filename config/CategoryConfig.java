package config;

import utils.RandomGenerator;

public class CategoryConfig
{
    public final double lambdaArrival;
    public final double lambdaService;
    public final int seedArrival;
    public final int seedService;
    public final RandomGenerator arrivalGenerator;
    public final RandomGenerator serviceGenerator;
    public final int id;

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

    public void addCategoryStats(double queuingTime, double serviceTime)
    {
        qtSum += queuingTime;
        svTimeSum += serviceTime;
        processedCategories++;
    }

    public double getAvgQueuingTime()
    {
        if(processedCategories == 0) throw new IllegalStateException();

        return qtSum / processedCategories;
    }

    public int getProcessedCategories()
    {
        return processedCategories;
    }

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