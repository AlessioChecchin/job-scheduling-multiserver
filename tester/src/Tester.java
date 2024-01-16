import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.Reader;

import java.util.InputMismatchException;

/**
 * Class used for testing.
 * @author Alessio Checchin
 */
public class Tester
{
    // Tolerance for errors (afflicts only doubles).
    private static final double EPSILON = 10e-9;

    /**
     * Tester constructor.
     * @param basePath Path to the folder that contains a /input and /output folder.
     * @param buildCommand Command used to execute the program to test. Input parameters must be empty,
     *                     because the tester will inject all the necessary parameters.
     */
    public Tester(String basePath, String buildCommand)
    {
        this.basePath = basePath;
        this.buildCommand = buildCommand;

        // Generating paths to subdirectories.
        this.inputDir = basePath.concat("/input/");
        this.outputDir = basePath.concat("/output/");

        // Testing each input file.
        File[] list = new File(this.inputDir).listFiles();

        if(list != null)
        {
            for(File element: list)
            {
                this.processTree(element, "");
            }
        }
    }

    protected void processTree(File root, String subpath)
    {
        if(root.isDirectory())
        {
            File[] list = root.listFiles();

            if(list != null)
            {
                for(File element: list)
                {
                    this.processTree(element, subpath + root.getName() + "/");
                }
            }
        }
        else
        {
            this.processFile(root, subpath);
        }
    }



    /**
     * Runs the tester against the provided input file.
     * It runs the program to test and compares the result (from stdout) with the expected output file.
     * @param file Input file.
     * @param subpath The subpath relative to the input directory that contains the file.
     */
    protected void processFile(File file, String subpath)
    {
        // Obtains the skeleton file name.
        // It's expected that input file is in the form of input_<skeleton>.in
        // It's expected that output file
        String fileSkeleton = getSkeleton(file.getName());

        System.out.printf(">> Processing %s...", subpath + file.getName());

        try
        {
            // Formatting output from stdout
            Output testOutput = new Output(this.readStdout(this.buildCommand + " " + file.getPath()), EPSILON);

            // Formatting output from output file (this file is considered correct)
            Output correctOutput = new Output(this.readOutputFile(this.outputDir + subpath + "output" + fileSkeleton + ".out"), EPSILON);

            // Comparing outputs
            Output.Report report = testOutput.compare(correctOutput);

            if(!report.isAcceptable())
            {
                System.out.printf("%s[FAIL]%s%n", ConsoleColors.RED_BOLD_BRIGHT, ConsoleColors.RESET);
                System.out.print(report.getText());
                System.out.printf("%sAverage error: %.18f%%%s%n", ConsoleColors.YELLOW_UNDERLINED, report.getAvgError() * 100, ConsoleColors.RESET);
            }
            else
            {
                System.out.printf("%s[OK]%s%n", ConsoleColors.GREEN_BOLD_BRIGHT, ConsoleColors.RESET);
            }

        }
        catch(IOException e)
        {
            System.out.printf("%s[CRITICAL] IOException caught: %s%s", ConsoleColors.RED_BOLD_BRIGHT, e.getMessage(), ConsoleColors.RESET);
        }
        catch(InputMismatchException e)
        {
            System.out.printf("%s[CRITICAL] InputMismatch caught: %s%s", ConsoleColors.RED_BOLD_BRIGHT, e.getMessage(), ConsoleColors.RESET);
        }
    }

    /**
     * Reads output file
     * @param path The path of the file to read.
     * @return The content of the file.
     * @throws IOException If there are errors while reading the file.
     */
    protected String readOutputFile(String path) throws IOException
    {
        FileReader fr = new FileReader(path);
        String result = this.read(fr);
        fr.close();
        return result;
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

    /**
     * Gets the skeleton name of a file.
     * @param fileName The complete filename of the file.
     * @return The skeleton.
     */
    public static String getSkeleton(String fileName)
    {
        // Removing extension.
        String removedExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        // Removing prefix.
        return removedExtension.substring(removedExtension.indexOf('_'));
    }

    public static void main(String[] args) throws IOException
    {
        String buildCommand = "\"C:\\Program Files\\Java\\jdk-17\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2\\lib\\idea_rt.jar=56514:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\Utente\\Desktop\\job_scheduler\\out\\production\\job_scheduler Simulator";

        //String buildCommand = "java -classpath C:\\Users\\Utente\\Desktop\\job_scheduler\\out\\production\\job_scheduler Simulator";

        new Tester(System.getProperty("user.dir"), buildCommand);
    }

    private final String basePath;
    private final String inputDir;
    private final String outputDir;
    private final String buildCommand;
}