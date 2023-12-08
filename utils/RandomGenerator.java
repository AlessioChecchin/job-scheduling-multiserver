package utils;

import java.util.Random;

public class RandomGenerator extends Random {
    public RandomGenerator(long seed)
    {
        super(seed);
    }

    public double exponentialDistribution(double lambda)
    {
        return ( -1.0 / lambda ) * Math.log( 1.0 - nextFloat() );
    }
}
