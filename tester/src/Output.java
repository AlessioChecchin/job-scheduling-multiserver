import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Output {
    public Output(String str) {
        Scanner lineScanner = new Scanner(str);

        this.lastComparisonError = false;

        if (lineScanner.hasNextLine())
        {
            Scanner metaScanner = new Scanner(lineScanner.nextLine());
            metaScanner.useDelimiter(",");

            if (metaScanner.hasNext()) this.kServerNumber = Integer.parseInt(metaScanner.next());
            else throw new IllegalArgumentException("Missing server number");

            if (metaScanner.hasNext()) this.hCategoriesNumber = Integer.parseInt(metaScanner.next());
            else throw new IllegalArgumentException("Missing category number");

            if (metaScanner.hasNext()) this.nTotalJobs = Integer.parseInt(metaScanner.next());
            else throw new IllegalArgumentException("Missing number of jobs");

            if (metaScanner.hasNext()) this.rSimulationRepetitions = Integer.parseInt(metaScanner.next());
            else throw new IllegalArgumentException("Missing simulation repetitions");

            if (metaScanner.hasNext()) this.pSchedulingPolicyType = Integer.parseInt(metaScanner.next());
            else throw new IllegalArgumentException("Missing type of scheduling policy");

            metaScanner.close();
        }
        else throw new IllegalArgumentException("Empty string input");

        this.hasShortOutput = this.pSchedulingPolicyType == 0 && this.rSimulationRepetitions == 1 && this.nTotalJobs <= 10;

        if(hasShortOutput)
        {
            this.readShortOutput(lineScanner);
        }

        if(lineScanner.hasNextLine()) this.avgEta = Double.parseDouble(lineScanner.nextLine());
        else throw new IllegalArgumentException("Missing average end time");

        if(lineScanner.hasNextLine()) this.avgQueuingTime = Double.parseDouble(lineScanner.nextLine());
        else throw new IllegalArgumentException("Missing average queuing time (all)");

        defaultOutput = new ArrayList<>();

        int i = 0;
        while(i < hCategoriesNumber && lineScanner.hasNextLine())
        {
            Scanner metaScanner = new Scanner(lineScanner.nextLine());
            metaScanner.useDelimiter(",");

            DefaultOutputElement element = new DefaultOutputElement();

            if (metaScanner.hasNext()) element.nr = Double.parseDouble(metaScanner.next());
            else throw new IllegalArgumentException("Missing nr");

            if (metaScanner.hasNext()) element.avgQueuingTime = Double.parseDouble(metaScanner.next());
            else throw new IllegalArgumentException("Missing aqt");

            if (metaScanner.hasNext()) element.avgServiceTime = Double.parseDouble(metaScanner.next());
            else throw new IllegalArgumentException("Missing service time");

            this.defaultOutput.add(element);
            metaScanner.close();
        }

        lineScanner.close();
    }

    protected void readShortOutput(Scanner scanner)
    {
        this.shortOutput = new ArrayList<>();

        int i = 0;
        while(scanner.hasNextLine() && i < 2 * nTotalJobs)
        {
            Scanner metaScanner = new Scanner(scanner.nextLine());
            metaScanner.useDelimiter(",");

            ShortOutputElement element = new ShortOutputElement();

            if (metaScanner.hasNext()) element.te = Double.parseDouble(metaScanner.next());
            else throw new IllegalArgumentException("Missing time (short output)");

            if (metaScanner.hasNext()) element.se = Double.parseDouble(metaScanner.next());
            else throw new IllegalArgumentException("Missing service time (short output)");

            if (metaScanner.hasNext()) element.ce = Integer.parseInt(metaScanner.next());
            else throw new IllegalArgumentException("Missing category id (short output)");

            this.shortOutput.add(element);
            metaScanner.close();

            i++;
        }
    }

    public Report compare(Output out)
    {
        StringBuilder builder = new StringBuilder();

        builder
                .append(this.compare(this.kServerNumber, out.kServerNumber).report).append(',')
                .append(this.compare(this.hCategoriesNumber, out.hCategoriesNumber).report).append(',')
                .append(this.compare(this.nTotalJobs, out.nTotalJobs).report).append(',')
                .append(this.compare(this.rSimulationRepetitions, out.rSimulationRepetitions).report).append(',')
                .append(this.compare(this.pSchedulingPolicyType, out.pSchedulingPolicyType).report)
                .append(System.lineSeparator());

        if(this.hasShortOutput != out.hasShortOutput)
        {
            builder.append("[CRITICAL] Output types are different");
            return new Report(builder.toString(), 100, false);
        }

        if(this.hasShortOutput)
        {
            if(this.shortOutput.size() != out.shortOutput.size())
            {
                builder.append("[CRITICAL] Short output array have different size");
                return new Report(builder.toString(), 100, false);
            }

            for(int i = 0; i < this.shortOutput.size(); i++)
            {
                builder
                        .append(this.compare(this.shortOutput.get(i).te, out.shortOutput.get(i).te).report).append(',')
                        .append(this.compare(this.shortOutput.get(i).te, out.shortOutput.get(i).te).report).append(',')
                        .append(this.compare(this.shortOutput.get(i).te, out.shortOutput.get(i).te).report)
                        .append(System.lineSeparator());
            }
        }

        builder
                .append(this.compare(this.avgEta, out.avgEta).report).append(System.lineSeparator())
                .append(this.compare(this.avgQueuingTime, out.avgQueuingTime).report).append(System.lineSeparator());

        if(this.defaultOutput.size() != out.defaultOutput.size())
        {
            builder.append("[CRITICAL] Default output array have different size");
            return new Report(builder.toString(), 100, false);
        }

        for(int i = 0; i < this.defaultOutput.size(); i++)
        {
            builder
                    .append(this.compare(this.defaultOutput.get(i).nr, out.defaultOutput.get(i).nr).report).append(',')
                    .append(this.compare(this.defaultOutput.get(i).avgQueuingTime, out.defaultOutput.get(i).avgQueuingTime).report).append(',')
                    .append(this.compare(this.defaultOutput.get(i).avgServiceTime, out.defaultOutput.get(i).avgServiceTime).report).append(',')
                    .append(System.lineSeparator());
        }

        return new Report(builder.toString(), 0, !this.lastComparisonError);

    }

    private TypeComparison compare(double test, double correct) {
        TypeComparison comparison = new TypeComparison();

        if (Math.abs(test - correct) > EPSILON)
        {
            double error = Math.abs((test - correct));
            if(correct != 0) error = Math.abs(error / correct);

            comparison.report = String.format("%.18f [%.18f %.18f]", test, correct, error);
            this.lastComparisonError =  true;
        }
        else
        {
            comparison.report = String.format("%f", test);
        }

        return comparison;
    }

    private TypeComparison compare(int a, int b) {
        TypeComparison comparison = new TypeComparison();

        if (a != b)
        {
            comparison.report = String.format("%d [%d]", a, b);
            this.lastComparisonError =  true;
        }
        else
        {
            comparison.report = String.format("%d", a);
        }

        return comparison;
    }

    private int kServerNumber;
    private int hCategoriesNumber;
    private int nTotalJobs;
    private int rSimulationRepetitions;
    private int pSchedulingPolicyType;
    private boolean hasShortOutput;
    private ArrayList<ShortOutputElement> shortOutput;
    private double avgEta;
    private double avgQueuingTime;

    private static double EPSILON = 10E-16;
    private ArrayList<DefaultOutputElement> defaultOutput;

    private boolean lastComparisonError;

    private static class ShortOutputElement
    {
        public double te;
        public double se;
        public int ce;

        public ShortOutputElement()
        {
            this.te = 0;
            this.se = 0;
            this.ce = 0;
        }

        public String toString()
        {
            return String.format("%f,%f,%d", this.te, this.se, this.ce);
        }
    }

    private static class DefaultOutputElement
    {
        public double nr;
        public double avgQueuingTime;
        public double avgServiceTime;

        public DefaultOutputElement()
        {
            this.nr = 0;
            this.avgQueuingTime = 0;
            this.avgServiceTime = 0;
        }

        public String toString()
        {
            return String.format("%f,%f,%f", this.nr, this.avgQueuingTime, this.avgServiceTime);
        }
    }

    private static class TypeComparison
    {
        public String report;
        public double error;

        public TypeComparison()
        {
            this.report = "";
            this.error = 0;
        }

        public String toString()
        {
            return String.format("%s,%f", this.report, this.error);
        }
    }

    public static class Report
    {
        private final String text;
        private final double avgError;
        private final boolean acceptable;

        private Report(String text, double avgError, boolean acceptable)
        {
            this.text = text;
            this.avgError = avgError;
            this.acceptable = acceptable;
        }

        public String getText()
        {
            return this.text;
        }

        public double getAvgError()
        {
            return this.avgError;
        }

        public boolean isAcceptable()
        {
            return this.acceptable;
        }

    }
}
