package config;

/**
 * This class represents the configuration of the project.
 */
public class ProjectConfig
{
    /**
     * The number of servers of the simulation.
     */
    public final int kServerNumber;

    /**
     * The number of categories.
     */
    public final int hCategoriesNumber;

    /**
     * The number of jobs to simulate.
     */
    public final int nTotalJobs;

    /**
     * The number of repetitions of the simulation.
     */
    public final int rSimulationRepetitions;

    /**
     * The scheduling policy to use.
     */
    public final int pSchedulingPolicyType;

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

        eta = 0;
        processedJobs = 0;
        qtSum = 0;
        executedRuns = 0;
    }

    /**
     * This method which form of output the simulation uses.
     * @return The form of output the simulation uses.
     */
    public boolean hasShortOutput()
    {
        return pSchedulingPolicyType == 0 && rSimulationRepetitions == 1 && nTotalJobs <= 10;
    }

    /**
     * Adds an end time of a run A
     * @param eta End time of a run
     */
    public void addEta(double eta)
    {
        this.eta += eta;
        executedRuns++;
    }

    /**
     * Get the average end time of the simulations.
     * @return AVG end time.
     */
    public double getAvgEta()
    {
        if(executedRuns == 0) throw new IllegalStateException();

        return eta / executedRuns;
    }

    /**
     * Adds a queuing time of a job (general category).
     * @param time Queuing time.
     */
    public void addQueuingTime(double time)
    {
        qtSum += time;
        processedJobs++;
    }

    /**
     * Returns the average queuing time of a job.
     * @return AVG queuing time.
     */
    public double getAvgQueuingTime()
    {
        if(processedJobs == 0) throw new IllegalStateException();

        return qtSum / processedJobs;
    }

    public String toString()
    {
        return String.format("%s[kServerNumber=%d, hCategoriesNumber=%d, nTotalJobs=%d, rSimulationRepetitions=%d, pSchedulingPolicyType=%d, qtSum=%f]",
                getClass().getName(), kServerNumber, hCategoriesNumber, nTotalJobs, rSimulationRepetitions, pSchedulingPolicyType, qtSum);
    }

    private double eta;
    private int executedRuns;
    private int processedJobs;
    private double qtSum;

}