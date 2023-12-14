package utils;

import java.util.Random;

/**
 * Random generator that implements exponential distribution.
 */
public class RandomGenerator extends Random
{
    /**
     * Constructor.
     * @param seed Seed.
     */
    public RandomGenerator(long seed)
    {
        super(seed);
    }

    /**
     * Generates a random number with exponential distribution.
     * @param lambda The parameter for the exponential distribution. Over
     *               many generated numbers, the mean of them converges to
     *               1 / lambda.
     * @return The random number.
     */
    public double exponentialDistribution(double lambda)
    {
        return ( -1.0 / lambda ) * Math.log( 1.0 - nextFloat() );
    }
}
