package config;

public class ProjectConfig
{
    public final int kServerNumber;
    public final int hCategoriesNumber;
    public final int nTotalJobs;
    public final int rSimulationRepetitions;
    public final int pSchedulingPolicyType;
    private int processedCategories;
    private double qtSum;

    public ProjectConfig(int kServerNumber, int hCategoriesNumber, int nTotalJobs, int rSimulationRepetitions, int pSchedulingPolicyType)
    {
        this.kServerNumber = kServerNumber;
        this.hCategoriesNumber = hCategoriesNumber;
        this.nTotalJobs = nTotalJobs;
        this.rSimulationRepetitions = rSimulationRepetitions;
        this.pSchedulingPolicyType = pSchedulingPolicyType;

        this.processedCategories = 0;
        this.qtSum = 0;

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


}