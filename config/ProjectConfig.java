package config;

public class ProjectConfig
{
    public final int kServerNumber;
    public final int hCategoriesNumber;
    public final int nTotalJobs;
    public final int rSimulationRepetitions;
    public final int pSchedulingPolicyType;


    public ProjectConfig(int kServerNumber, int hCategoriesNumber, int nTotalJobs, int rSimulationRepetitions, int pSchedulingPolicyType)
    {
        this.kServerNumber = kServerNumber;
        this.hCategoriesNumber = hCategoriesNumber;
        this.nTotalJobs = nTotalJobs;
        this.rSimulationRepetitions = rSimulationRepetitions;
        this.pSchedulingPolicyType = pSchedulingPolicyType;

        this.eta = 0;
        this.processedJobs = 0;
        this.qtSum = 0;
        this.executedRuns = 0;

    }

    public String toString()
    {
        return String.format("%s[kServerNumber=%d, hCategoriesNumber=%d, nTotalJobs=%d, rSimulationRepetitions=%d, pSchedulingPolicyType=%d, qtSum=%f]",
                getClass().getName(), kServerNumber, hCategoriesNumber, nTotalJobs, rSimulationRepetitions, pSchedulingPolicyType, qtSum);
    }

    public boolean showShortOutput()
    {
        return pSchedulingPolicyType == 0 && rSimulationRepetitions == 1 && nTotalJobs <= 10;
    }

    public void addEta(double eta)
    {
        this.eta += eta;
        executedRuns++;
    }

    public double getAvgEta()
    {
        if(executedRuns == 0) throw new IllegalStateException();

        return eta / executedRuns;
    }

    public void addQueuingTime(double time)
    {
        qtSum += time;
        processedJobs++;
    }

    public double getAvgQueuingTime()
    {
        if(processedJobs == 0) throw new IllegalStateException();

        return qtSum / processedJobs;
    }

    private double eta;
    private int executedRuns;
    private int processedJobs;
    private double qtSum;

}