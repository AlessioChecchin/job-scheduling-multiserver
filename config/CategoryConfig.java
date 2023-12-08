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
    private int processedCategories;
    private double qtSum;

    public CategoryConfig(int id, double lambdaArrival, double lambdaService, int seedArrival, int seedService, RandomGenerator arrivalGenerator, RandomGenerator serviceGenerator)
    {
        this.id               = id;
        this.lambdaArrival    = lambdaArrival;
        this.lambdaService    = lambdaService;
        this.seedArrival      = seedArrival;
        this.seedService      = seedService;
        this.arrivalGenerator = arrivalGenerator;
        this.serviceGenerator = serviceGenerator;
    }

    public void addQueuingTime(double time)
    {
        qtSum += time;
        processedCategories++;
    }

    public String toString()
    {
        return String.format("%s[id=%d, lambdaArrival=%f, lambdaService=%f, seedArrival=%d, seedService=%d, aqt=%f]",
                getClass().getName(), id, lambdaArrival, lambdaService, seedArrival, seedService, qtSum);
    }

}