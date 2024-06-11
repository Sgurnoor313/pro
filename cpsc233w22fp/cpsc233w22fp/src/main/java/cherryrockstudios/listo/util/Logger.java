package cherryrockstudios.listo.util;

import javafx.scene.control.Alert;

import java.io.*;

/**
 * Class to handle logging information to an external file.
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public class Logger {

    /**
     * we need a PrintWriter to help output data into file
     */
    private PrintWriter printWriter;

    /**
     * constructor method for logger that sets up the printWriter with exception handling.
     *
     * @param outputFile: the output file where to store data.
     */
    public Logger(File outputFile) {
        try {
            printWriter = new PrintWriter(outputFile);
        } catch (Exception e) {
            // throw error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Internal File Error!");
            alert.setHeaderText("Something wrong happened within the file configurations!");
            alert.setContentText("Unable to open output file " + outputFile.getAbsoluteFile());
            alert.showAndWait();
        }
    }

    /**
     * stores the given Object as a String into the output file and flushes right after.
     *
     * @param obj: the data to store in the output file. A new line is added at the end on each call of this method.
     */
    public void saveData(Object obj) {
        printWriter.println(obj);
        printWriter.flush();
    }
}
