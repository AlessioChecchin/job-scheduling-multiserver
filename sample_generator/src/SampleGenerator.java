import java.io.*;
import java.util.Random;

public class SampleGenerator {

    private final Random generator;

    public static final int MAX_K = 10000;
    public static final int MAX_H = 1000;
    public static final int MAX_N = 1000000;
    public static final int MAX_R = 100;
    public static final int MAX_ARRIVAL = 200;
    public static final int MAX_SERVICE = 200;
    public static final int MAX_SEED_ARRIVAL = 400000;
    public static final int MAX_SEED_SERVICE = 400000;

    public SampleGenerator(String buildCommand) throws IOException
    {
        this.generator = new Random();
        int tot = 10;

        for(int i = 0; i < tot; i++)
        {
            System.out.printf("Generating input...%d%n",(i + 1));
            String inputFile = this.generateInput(i);
            String outputFile = this.generateOutput(i, inputFile, buildCommand);
        }
    }

    protected String generateInput(int key) throws FileNotFoundException {
        int K = Math.abs(this.generator.nextInt()) % MAX_K + 1;
        int H = Math.abs(this.generator.nextInt()) % MAX_H + 1;
        int N = Math.abs(this.generator.nextInt()) % MAX_N + 1;
        int R = Math.abs(this.generator.nextInt()) % MAX_R + 1;

        StringBuilder builder = new StringBuilder();

        builder
                .append(K).append(',')
                .append(H).append(',')
                .append(N).append(',')
                .append(R).append(',')
                .append(0).append(System.lineSeparator());

        for(int i = 0; i < H; i++)
        {
            double lambdaArrival = this.generator.nextDouble() * MAX_ARRIVAL + 0.5;
            double lambdaService = this.generator.nextDouble() * MAX_SERVICE + 0.5;

            int seedArrival = Math.abs(this.generator.nextInt()) % MAX_SEED_ARRIVAL + 1;
            int seedService = Math.abs(this.generator.nextInt()) % MAX_SEED_SERVICE + 1;

            builder
                    .append(lambdaArrival).append(',')
                    .append(lambdaService).append(',')
                    .append(seedArrival).append(',')
                    .append(seedService).append(System.lineSeparator());
        }

        String inputPath = System.getProperty("user.dir") + "/generated_input/input_sample_" + key + ".in";

        PrintWriter pw = new PrintWriter(inputPath);
        pw.write(builder.toString());
        pw.close();

        return inputPath;
    }


    protected String generateOutput(int key, String inputPath, String buildCommand) throws IOException
    {
        String outputPath = System.getProperty("user.dir") + "/generated_output/output_sample_" + key + ".out";
        String stdout = this.readStdout(buildCommand + " " + inputPath);

        System.out.println(stdout);

        PrintWriter pw = new PrintWriter(outputPath);
        pw.write(stdout);
        pw.close();
        return outputPath;
    }

    /**
     * Reads the output of the program to test
     * @param command Command to start the program.
     * @return The output of the program.
     * @throws IOException If there are errors while reading from stdout.
     */
    protected String readStdout(String command) throws IOException
    {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = process.inputReader();
        String result = this.read(reader);
        reader.close();
        return result;
    }

    /**
     * Reads all the content of a stream.
     * @param reader A reader associate to a stream.
     * @return The content read.
     * @throws IOException If there are errors while reading from the stream.
     */
    protected String read(Reader reader) throws IOException
    {
        StringBuilder builder = new StringBuilder();

        final int BUFFER_SIZE = 4096;
        char[] buffer = new char[BUFFER_SIZE];

        int read = 0;
        while((read = reader.read(buffer, 0, BUFFER_SIZE)) != -1)
        {
            char[] resized = new char[read];
            System.arraycopy(buffer, 0, resized, 0, read);
            builder.append(resized);
        }

        return builder.toString();
    }


    public static void main(String[] args) throws IOException
    {
        String buildCommand = "\"C:\\Program Files\\Java\\jdk-17\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2\\lib\\idea_rt.jar=56514:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\Utente\\Desktop\\job_scheduler\\out\\production\\job_scheduler Simulator";

        new SampleGenerator(buildCommand);
    }
}