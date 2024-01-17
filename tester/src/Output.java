import java.util.*;

/**
 * Class that represents an output of a simulation of R runs.
 * @author Alessio Checchin
 */
public class Output
{
    /**
     * Constructor. Note that missing values (from the expected ones) are considered critical errors.
     * @param str Output string to parse.
     * @param EPSILON Error sensibility.
     */
    public Output(String str, double EPSILON)
    {
        Scanner lineScanner = new Scanner(str);

        this.EPSILON = EPSILON;
        this.lastComparisonError = false;
        this.precisionErrorNumber = 0;
        this.precisionErrorsSum = 0.0;

        // Reading line by line and checking if all the basic parameters are there and equals.
        if (lineScanner.hasNextLine())
        {
            Scanner metaScanner = new Scanner(lineScanner.nextLine());
            metaScanner.useDelimiter(",");

            if (metaScanner.hasNext()) this.kServerNumber = Integer.parseInt(metaScanner.next());
            else throw new InputMismatchException("Missing server number");

            if (metaScanner.hasNext()) this.hCategoriesNumber = Integer.parseInt(metaScanner.next());
            else throw new InputMismatchException("Missing category number");

            if (metaScanner.hasNext()) this.nTotalJobs = Integer.parseInt(metaScanner.next());
            else throw new InputMismatchException("Missing number of jobs");

            if (metaScanner.hasNext()) this.rSimulationRepetitions = Integer.parseInt(metaScanner.next());
            else throw new InputMismatchException("Missing simulation repetitions");

            if (metaScanner.hasNext()) this.pSchedulingPolicyType = Integer.parseInt(metaScanner.next());
            else throw new InputMismatchException("Missing type of scheduling policy");

            metaScanner.close();
        }
        else throw new InputMismatchException("Empty string input");

        // Checking the type of output.
        this.hasShortOutput = this.pSchedulingPolicyType == 0 && this.rSimulationRepetitions == 1 && this.nTotalJobs <= 10;

        if(hasShortOutput)
        {
            this.readShortOutput(lineScanner);
        }

        if(lineScanner.hasNextLine()) this.avgEta = Double.parseDouble(lineScanner.nextLine());
        else throw new InputMismatchException("Missing average end time");

        if(lineScanner.hasNextLine()) this.avgQueuingTime = Double.parseDouble(lineScanner.nextLine());
        else throw new InputMismatchException("Missing average queuing time (all)");

        defaultOutput = new ArrayList<>();

        int i = 0;
        while(i < hCategoriesNumber && lineScanner.hasNextLine())
        {
            Scanner metaScanner = new Scanner(lineScanner.nextLine());
            metaScanner.useDelimiter(",");

            DefaultOutputElement element = new DefaultOutputElement();

            if (metaScanner.hasNext()) element.nr = Double.parseDouble(metaScanner.next());
            else throw new InputMismatchException("Missing nr");

            if (metaScanner.hasNext()) element.avgQueuingTime = Double.parseDouble(metaScanner.next());
            else throw new InputMismatchException("Missing aqt");

            if (metaScanner.hasNext()) element.avgServiceTime = Double.parseDouble(metaScanner.next());
            else throw new InputMismatchException("Missing service time");

            this.defaultOutput.add(element);
            metaScanner.close();
        }

        lineScanner.close();
    }

    /**
     * Reads the short output.
     * @param scanner Scanner attached to the output string.
     */
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
            else throw new InputMismatchException("Missing time (short output)");

            if (metaScanner.hasNext()) element.se = Double.parseDouble(metaScanner.next());
            else throw new InputMismatchException("Missing service time (short output)");

            if (metaScanner.hasNext()) element.ce = Integer.parseInt(metaScanner.next());
            else throw new InputMismatchException("Missing category id (short output)");

            this.shortOutput.add(element);
            metaScanner.close();

            i++;
        }
    }

    /**
     * Compares two outputs.
     * @param out The output to compare.
     * @return A report about the comparison.
     */
    public Report compare(Output out)
    {
        StringBuilder builder = new StringBuilder();

        this.lastComparisonError = false;
        this.precisionErrorNumber = 0;
        this.precisionErrorsSum = 0;

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
            return new Report(builder.toString(), this.getAvgError(), false);
        }

        if(this.hasShortOutput)
        {
            if(this.shortOutput.size() != out.shortOutput.size())
            {
                builder.append("[CRITICAL] Short output array have different size");
                return new Report(builder.toString(), this.getAvgError(), false);
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
            return new Report(builder.toString(), this.getAvgError(), false);
        }

        for(int i = 0; i < this.defaultOutput.size(); i++)
        {
            builder
                    .append(this.compare(this.defaultOutput.get(i).nr, out.defaultOutput.get(i).nr).report).append(',')
                    .append(this.compare(this.defaultOutput.get(i).avgQueuingTime, out.defaultOutput.get(i).avgQueuingTime).report).append(',')
                    .append(this.compare(this.defaultOutput.get(i).avgServiceTime, out.defaultOutput.get(i).avgServiceTime).report)
                    .append(System.lineSeparator());
        }

        return new Report(builder.toString(), this.getAvgError(), !this.lastComparisonError);

    }

    private TypeComparison compare(double test, double correct)
    {
        TypeComparison comparison = new TypeComparison();

        if (Math.abs(test - correct) > EPSILON)
        {
            double error = (test - correct);
            if(correct != 0) error = (error / correct);

            String strTest = String.format(Locale.US, "%.18f", test);
            String strCorrect = String.format(Locale.US, "%.18f", correct);

            comparison.report = String.format(Locale.US, "%s [%s (error of: %s%.18f%%%s)]",
                    this.underlineDiff(strTest, strCorrect),
                    this.underlineDiff(strCorrect, strTest),
                    ConsoleColors.YELLOW_BOLD_BRIGHT,
                    error * 100,
                    ConsoleColors.RESET
            );

            this.lastComparisonError =  true;
            this.precisionErrorsSum += error;
            this.precisionErrorNumber++;
        }
        else
        {
            comparison.report = String.format(Locale.US, "%f", test);
        }

        return comparison;
    }

    private String underlineDiff(String target, String compare)
    {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        boolean streak = false;
        while(i < target.length() && i < compare.length())
        {
            if(target.charAt(i) != compare.charAt(i))
            {
                if(!streak)
                {
                    streak = true;
                    builder.append(ConsoleColors.RED_BOLD_BRIGHT);
                }

            }
            else
            {
                if(streak)
                {
                    builder.append(ConsoleColors.RESET);
                }
                streak = false;
            }

            builder.append(target.charAt(i));

            i++;
        }

        while(i < target.length())
        {
            if(!streak)
            {
                streak = true;
                builder.append(ConsoleColors.RED_BOLD_BRIGHT);
            }

            builder.append(target.charAt(i));
            i++;
        }

        if(streak)
        {
            builder.append(ConsoleColors.RESET);
        }

        return builder.toString();
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

    private double getAvgError()
    {
        if(this.precisionErrorNumber == 0) return 0;
        return  this.precisionErrorsSum / this.precisionErrorNumber;
    }


    private final int kServerNumber;
    private final int hCategoriesNumber;
    private final int nTotalJobs;
    private final int rSimulationRepetitions;
    private final int pSchedulingPolicyType;
    private final boolean hasShortOutput;
    private ArrayList<ShortOutputElement> shortOutput;
    private final double avgEta;
    private final double avgQueuingTime;

    private final double EPSILON;
    private final ArrayList<DefaultOutputElement> defaultOutput;
    private boolean lastComparisonError;
    private double precisionErrorsSum;
    private int precisionErrorNumber;

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
