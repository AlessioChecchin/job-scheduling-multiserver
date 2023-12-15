import java.io.*;
import java.util.Scanner;

public class Tester
{
    public Tester(String basePath, String buildCommand) throws IOException
    {
        this.basePath = basePath;
        this.inputDir = basePath.concat("/input/");
        this.outputDir = basePath.concat("/output/");


        for(File file: getFileList(inputDir))
        {
            String fileSkeleton = getSkeleton(file.getName());

            String strTest = "";
            String strCorrect = "";

            try
            {
                Output testOutput = new Output(strTest = this.readStdout(buildCommand + file.getPath()));
                Output correctOutput = new Output(strCorrect = this.readOutputFile(this.outputDir + "output" + fileSkeleton + ".out"));

                Output.Report report = testOutput.compare(correctOutput);
                if(!report.isAcceptable())
                {
                    System.out.println(report.getText());
                }

            }
            catch(Exception e)
            {
                System.out.println("Error at " + file.getName());
                System.out.println("Test: ");
                System.out.println(strTest);
                System.out.println("Correct: ");
                System.out.println(strCorrect);
                e.printStackTrace();
            }

        }
    }

    protected String readOutputFile(String path) throws IOException
    {
        FileReader fr = new FileReader(path);

        String result = this.read(fr);

        fr.close();
        return result;
    }

    protected String readStdout(String command) throws IOException
    {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = process.inputReader();

        String result = this.read(reader);

        reader.close();
        return result;
    }

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

    public static String getSkeleton(String fileName)
    {
        // Removing extension.
        String removedExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        // Removing prefix.
        return removedExtension.substring(removedExtension.indexOf('_'));
    }

    public static File[] getFileList(String folder)
    {
        File dir = new File(folder);
        return dir.listFiles();
    }

    public static void main(String[] args) throws IOException
    {
        String buildCommand = "\"C:\\Program Files\\Java\\jdk-17\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2\\lib\\idea_rt.jar=56514:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\Utente\\Desktop\\job_scheduler\\out\\production\\job_scheduler Simulator ";
        new Tester(System.getProperty("user.dir"), buildCommand);
    }

    private String basePath;
    private String inputDir;
    private String outputDir;
}