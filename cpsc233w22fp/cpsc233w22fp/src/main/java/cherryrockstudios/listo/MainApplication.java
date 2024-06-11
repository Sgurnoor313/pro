package cherryrockstudios.listo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Listo: Your Assistant in Academics! v1.3");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // check for arguments
        if (args.length > 1) {
            System.err.println("Program requires only one or no arguments!");
            System.err.println("Usage: MainApplication <file_to_be_loaded>");
            System.err.println("or");
            System.err.println("Usage: MainApplication");
            System.exit(1);
        }

        // handle arguments
        if (args.length == 1) {
            // set up file
            File fileToLoad = new File(args[0]);

            // check if the file is a valid file
            Main.checkInputFile(fileToLoad);

            // set the file as a file to be loaded once the GUI will be launched
            MainController.listo_preload = fileToLoad;
        }

        launch();
    }
}
