package cherryrockstudios.listo;

import cherryrockstudios.listo.unit.Listo;
import cherryrockstudios.listo.util.Reader;

import java.io.File;
import java.io.IOException;

/**
 * Listo App
 * A project manager to help students manage their time during their academic years.
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public final class Main {
    /**
     * The Listo that is currently open
     */
    public static Listo currentWorkingListo;

    public static void main(String[] args) {
        // check for arguments
        checkArgument(args);

        // set up files
        File outputFile;
        File inputFile;

        // handle arguments
        if (args.length == 0) {
            // if no arguments, ask for output name and set null to input file
            String outputName = Menu.inputString("Please enter a name for your output file: ");
            outputFile = new File(outputName+".txt");

            // no input files
            inputFile = null;
        } else if (args.length == 1) {
            // if one argument, ask for output name and load input file
            String outputName = Menu.inputString("Please enter a name for your output file: ");
            outputFile = new File(outputName+".txt");

            // input file is first argument
            inputFile = new File(args[0]);
        } else {
            // if two arguments, set the files to the corresponding arguments
            inputFile = new File(args[0]);
            outputFile = new File(args[1]);
        }

        // check if each file is accessible
        if (inputFile != null) {
            checkInputFile(inputFile);
        }
        checkOutputFile(outputFile);

        // set up writer for output file
        Menu.setOutputFile(outputFile);

        // if input file is not null, load data from provided input file
        if (inputFile != null) {
            // load a Listo environment
            currentWorkingListo = Reader.loadFile(inputFile);
        } else {
            // initialize a new Listo environment
            String name = Menu.inputString("Please enter a name for your Listo: ");
            currentWorkingListo = new Listo(name);
        }

        // run program
        Menu.runProgram();
    }


    /**
     * Verify that the program has none, one, or two command line arguments needed.
     *
     * @param args: the program arguments
     */
    public static void checkArgument(String[] args) {
        // if arguments is more than two, then exit the program
        if (args.length > 2) {
            System.err.println("Program requires only none, one, or two arguments!");
            System.err.println("Usage: Main <input_file> <output_file> or");
            System.err.println("Usage: Main <input_file>");
            System.err.println("Usage: Main");
            System.exit(1);
        }
    }

    /**
     * Checks if the input file exists and can be accessed successfully.
     *
     * @param inputFile: an input file created on Main using provided command line arguments
     */
    public static void checkInputFile(File inputFile) {
        if (!inputFile.exists() || !inputFile.isFile() || !inputFile.canWrite()) {
            System.err.println("The file "+inputFile.getAbsoluteFile()+" can not be accessed. It may not exist!");
            System.exit(1);
        }
    }

    /**
     * Checks if the output file exists and can be accessed successfully. If the output file doesn't exist,
     * then it creates a new output file and asks for confirmation to overwrite existing output files.
     *
     * @param outputFile: an output file created on Main using provided command line arguments
     */
    public static void checkOutputFile(File outputFile) {
        // Prompt if we want to overwrite the output file
        if (outputFile.exists() && outputFile.isFile() && outputFile.canWrite()) {
            System.out.println("It seems that there are some data stored in the output file provided.");
            if (Menu.YNprompt("Do you want to overwrite that data?")) {
                outputFile.delete();
            } else {
                System.err.println("User refused to overwrite the output file. Program Terminated.");
                System.exit(0);
            }
        }
        // create an output file if it doesn't exist
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                System.err.println("The new output file "+outputFile.getAbsoluteFile()+" can not be created.");
                System.exit(0);
            }
        }

        // check output file
        checkInputFile(outputFile);
    }

}
