package cherryrockstudios.listo;

import cherryrockstudios.listo.comparators.DueDateComparator;
import cherryrockstudios.listo.enums.FormatType;
import cherryrockstudios.listo.enums.StatusType;
import cherryrockstudios.listo.unit.Listo;
import cherryrockstudios.listo.unit.Project;
import cherryrockstudios.listo.util.Reader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * author: Gabriel Rodriguez
 * author: Sharoon Thabal
 */
public class MainController {

    // store the data of the editor
    private static Listo listo;

    // the currently edited project
    private Project edit_project;

    // the currently working directory
    private static File listo_directory;

    // the state of the program (NO_FILE, NEW_FILE, LOAD_FILE, EDIT_FILE, EDIT_PROJ)
    private static String program_state;

    // the data to be loaded from the command line arguments
    public static File listo_preload = null;

    /**
     * Starts the GUI on default start-up settings
     */
    @FXML
    public void initialize() {
        // initialize global variables
        listo = null;
        listo_directory = null;
        edit_project = null;

        // program state
        NO_FILE();

        // hide about Listo
        showListoSector(false);

        // set Choices for Status
        projectStatusChoiceBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        projectStatusChoiceBox.getSelectionModel().selectFirst();

        // set Choices for Format
        projectFormatChoiceBox.setItems(FXCollections.observableArrayList(FormatType.values()));
        projectFormatChoiceBox.getSelectionModel().selectFirst();

        // set choices for Quick Status Update
        updateStatusChoiceBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        updateStatusChoiceBox.getSelectionModel().selectFirst();

        // set up the Table
        IDColumn.setCellValueFactory(new PropertyValueFactory<Project, Integer>("projectID"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<Project, String>("projectCourse"));
        projectColumn.setCellValueFactory(new PropertyValueFactory<Project, String>("projectName"));
        dueColumn.setCellValueFactory(new PropertyValueFactory<Project, LocalDate>("projectDue"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Project, StatusType>("projectStatus"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<Project, FormatType>("projectFormat"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<Project, Double>("gradeWeight"));
        descColumn.setCellValueFactory(new PropertyValueFactory<Project, String>("projectDescription"));

        // IMPORTANT! Disables editable portion so savages won't just input any invalid values.
        dueDateTextfield.setEditable(false);

        // preload the command line argument, if it exists
        if (listo_preload != null) {
            // load the file to our listo global variable and GUI if it isn't null
            boolean loadSuccess = readFile(listo_preload);

            // only update the global listo directory if a listo has been loaded
            if (loadSuccess) {
                // update the global listo directory
                listo_directory = listo_preload;

                // update program state
                if (listo.getProjectList().size() != 0) {
                    // if project list isn't empty, initiate load file state
                    LOAD_FILE();
                } else {
                    // if project list is empty, initiate new file state
                    NEW_FILE();
                }

                // update status
                statusLabel.setText(listo.getListoName()+" has been loaded into the app!");
            }
        }
        // if the above fails to load, then it should be okay. Listo and Listo Directory will remain null as desired in the beginning.
    }

    /**
     * Create Sector Elements
     */
    @FXML
    private Label createLabelHeader;

    @FXML
    private TextField courseNameTextfield;

    @FXML
    private Button courseNameAddButton;

    @FXML
    private ChoiceBox<String> projectCourseChoiceBox;

    @FXML
    private TextField projectNameTextfield;

    @FXML
    private DatePicker dueDateTextfield;

    @FXML
    private ChoiceBox<StatusType> projectStatusChoiceBox;

    @FXML
    private ChoiceBox<FormatType> projectFormatChoiceBox;

    @FXML
    private TextField projectGradeWeightTextfield;

    @FXML
    private TextArea projectDescriptionTextfield;

    @FXML
    private Button createProjectButton;


    /**
     * Modify Sector Elements
     */
    @FXML
    private Button editProjectButton;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Button deleteProjectButton;

    @FXML
    private ChoiceBox<String> deleteCourseChoiceBox;

    @FXML
    private Button deleteCourseButton;


    /**
     * Quick Status Update Sector Elements
     */
    @FXML
    private ChoiceBox<StatusType> updateStatusChoiceBox;

    @FXML
    private Button updateStatusButton;


    /**
     * View Description Sector Elements
     */
    @FXML
    private Button viewDescSelectButton;

    @FXML
    private TextArea descViewArea;


    /**
     * Menubar Elements
     */
    @FXML
    private MenuItem newListoMenubar;

    @FXML
    private MenuItem loadListoMenubar;

    @FXML
    private MenuItem saveAsMenubar;

    @FXML
    private MenuItem saveMenubar;

    @FXML
    private MenuItem quitMenubar;

    @FXML
    private MenuItem aboutMenubar;

    @FXML
    private MenuItem aboutListoMenubar;

    @FXML
    private MenuItem editListoMenuBar;

    @FXML
    private MenuItem deleteListoMenubar;

    /**
     * Main View Elements
     */
    @FXML
    private TableView<Project> viewListoTable;

    @FXML
    private TableColumn<Project, Integer> IDColumn;

    @FXML
    private TableColumn<Project, String> courseColumn;

    @FXML
    private TableColumn<Project, String> projectColumn;

    @FXML
    private TableColumn<Project, LocalDate> dueColumn;

    @FXML
    private TableColumn<Project, StatusType> statusColumn;

    @FXML
    private TableColumn<Project, FormatType> formatColumn;

    @FXML
    private TableColumn<Project, Double> gradeColumn;

    @FXML
    private TableColumn<Project, String> descColumn;

    /**
     * Other Sectors Elements (Never Disabled)
     */
    @FXML
    private Label currentCountLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label todaysDateLabel;

    @FXML
    private Label listoNameLabel;

    /**
     * Listo Sector Elements
     */
    @FXML
    private Button listo_createListoButton;

    @FXML
    private TextField listo_listoAuthorTextfield;

    @FXML
    private TextField listo_listoDescTextfield;

    @FXML
    private Label listo_listoLabel;

    @FXML
    private TextField listo_listoNameTextfield;


    // Enable, Disable logic. Main States of program
    /**
     * The state of the program when there is no file loaded.
     */
    public void NO_FILE() {
        // Update Program State
        program_state = "NO_FILE";

        // Create Sector - Course Section
        courseNameTextfield.setDisable(true);
        courseNameAddButton.setDisable(true);
        deleteCourseChoiceBox.setDisable(true);
        deleteCourseButton.setDisable(true);

        // Create Sector - Project Section
        projectCourseChoiceBox.setDisable(true);
        projectNameTextfield.setDisable(true);
        dueDateTextfield.setDisable(true);
        projectStatusChoiceBox.setDisable(true);
        projectFormatChoiceBox.setDisable(true);
        projectGradeWeightTextfield.setDisable(true);
        projectDescriptionTextfield.setDisable(true);
        createProjectButton.setDisable(true);

        // disable save changes button and enable create project button
        saveChangesButton.setVisible(false);
        createProjectButton.setVisible(true);
        // revert Create label name
        createLabelHeader.setText("Create");

        // Modify Sector
        editProjectButton.setDisable(true);
        deleteProjectButton.setDisable(true);

        // Quick Status Sector
        updateStatusChoiceBox.setDisable(true);
        updateStatusButton.setDisable(true);

        // View Description Sector
        viewDescSelectButton.setDisable(true);
        descViewArea.setDisable(true);

        // Menu Bar Sector
        aboutListoMenubar.setVisible(false);
        editListoMenuBar.setVisible(false);
        deleteListoMenubar.setVisible(false);
        saveAsMenubar.setDisable(true);
        saveMenubar.setDisable(true);
        newListoMenubar.setDisable(false);
        loadListoMenubar.setDisable(false);

        // View Table Sector
        viewListoTable.setDisable(true);
        // Reset the Table
        viewListoTable.getItems().clear();

        // Clear Course ChoiceBox
        projectCourseChoiceBox.getItems().clear();
        deleteCourseChoiceBox.getItems().clear();

        // Clear Text Boxes
        courseNameTextfield.clear();
        projectDescriptionTextfield.clear();
        projectGradeWeightTextfield.clear();
        projectNameTextfield.clear();
        dueDateTextfield.setValue(null);
        descViewArea.clear();
        listo_listoNameTextfield.clear();
        listo_listoDescTextfield.clear();
        listo_listoAuthorTextfield.clear();

        // Text Labels
        statusLabel.setText("Welcome to Listo: Your Assistant in Academics! To get started, go to File > New Listo.\n" +
                "or you could load any Listo file you have: Go to File > Load Listo.");
        currentCountLabel.setText("0");
        listoNameLabel.setText("<No Opened Listo>");
        todaysDateLabel.setText(String.valueOf(LocalDate.now()));

    }

    /**
     * The state of the program when there is a new file loaded.
     */
    public void NEW_FILE() {
        // Update Program State
        program_state = "NEW_FILE";

        // update past dues
        updateStatusBasedToday();

        // Create Sector - Course Section
        courseNameTextfield.setDisable(false);
        courseNameAddButton.setDisable(false);
        deleteCourseChoiceBox.setDisable(false);
        deleteCourseButton.setDisable(false);

        // Create Sector - Project Section
        projectCourseChoiceBox.setDisable(false);
        projectNameTextfield.setDisable(false);
        dueDateTextfield.setDisable(false);
        projectStatusChoiceBox.setDisable(false);
        projectFormatChoiceBox.setDisable(false);
        projectGradeWeightTextfield.setDisable(false);
        projectDescriptionTextfield.setDisable(false);
        createProjectButton.setDisable(false);

        // disable save changes button and enable create project button
        saveChangesButton.setVisible(false);
        createProjectButton.setVisible(true);
        // revert Create label name
        createLabelHeader.setText("Create");

        // Modify Sector
        editProjectButton.setDisable(true);
        deleteProjectButton.setDisable(true);

        // Quick Status Sector
        updateStatusChoiceBox.setDisable(true);
        updateStatusButton.setDisable(true);

        // View Description Sector
        viewDescSelectButton.setDisable(true);
        descViewArea.setDisable(true);

        // Menu Bar Sector
        aboutListoMenubar.setVisible(true);
        editListoMenuBar.setVisible(true);
        deleteListoMenubar.setVisible(true);
        saveAsMenubar.setDisable(false);
        newListoMenubar.setDisable(false);
        loadListoMenubar.setDisable(false);

        // Check if Listo has never been saved before
        saveMenubar.setDisable(listo_directory == null);

        // View Table Sector
        viewListoTable.setDisable(false);
        // Reset the Table
        viewListoTable.getItems().clear();

        // Clear Course ChoiceBox
        projectCourseChoiceBox.getItems().clear();
        deleteCourseChoiceBox.getItems().clear();

        // Clear Text Boxes
        courseNameTextfield.clear();
        projectDescriptionTextfield.clear();
        projectGradeWeightTextfield.clear();
        projectNameTextfield.clear();
        dueDateTextfield.setValue(null);
        descViewArea.clear();
        listo_listoNameTextfield.clear();
        listo_listoDescTextfield.clear();
        listo_listoAuthorTextfield.clear();

        // Text Labels
        statusLabel.setText("A new Listo named '"+listo.getListoName()+"' has been created by "+listo.getListoUserName()+".");
        currentCountLabel.setText(String.valueOf(Project.count));
        listoNameLabel.setText(listo.getListoName());
        todaysDateLabel.setText(String.valueOf(LocalDate.now()));
    }

    /**
     * The state of the program when there is a file loaded.
     */
    public void LOAD_FILE() {
        // Update Program State
        program_state = "LOAD_FILE";

        // update past dues
        updateStatusBasedToday();

        // Create Sector - Course Section
        courseNameTextfield.setDisable(false);
        courseNameAddButton.setDisable(false);
        deleteCourseChoiceBox.setDisable(false);
        deleteCourseButton.setDisable(false);

        // Create Sector - Project Section
        projectCourseChoiceBox.setDisable(false);
        projectNameTextfield.setDisable(false);
        dueDateTextfield.setDisable(false);
        projectStatusChoiceBox.setDisable(false);
        projectFormatChoiceBox.setDisable(false);
        projectGradeWeightTextfield.setDisable(false);
        projectDescriptionTextfield.setDisable(false);
        createProjectButton.setDisable(false);

        // disable save changes button and enable create project button
        saveChangesButton.setVisible(false);
        createProjectButton.setVisible(true);
        // revert Create label name
        createLabelHeader.setText("Create");

        // Modify Sector
        editProjectButton.setDisable(false);
        deleteProjectButton.setDisable(false);

        // Quick Status Sector
        updateStatusChoiceBox.setDisable(false);
        updateStatusButton.setDisable(false);

        // View Description Sector
        viewDescSelectButton.setDisable(false);
        descViewArea.setDisable(false);

        // Menu Bar Sector
        aboutListoMenubar.setVisible(true);
        editListoMenuBar.setVisible(true);
        deleteListoMenubar.setVisible(true);
        saveAsMenubar.setDisable(false);
        newListoMenubar.setDisable(false);
        loadListoMenubar.setDisable(false);
        // Check if Listo has never been saved before
        saveMenubar.setDisable(listo_directory == null);

        // View Table Sector
        viewListoTable.setDisable(false);
        // Repopulate the Table
        viewListoTable.getItems().clear();
        ObservableList<Project> projects = FXCollections.observableArrayList(listo.getProjectList());
        viewListoTable.setItems(projects);
        // Select First Item
        viewListoTable.getSelectionModel().selectFirst();

        // Repopulate Course ChoiceBox
        projectCourseChoiceBox.setItems(FXCollections.observableArrayList(listo.getCourseList()));
        projectCourseChoiceBox.getSelectionModel().selectFirst();
        deleteCourseChoiceBox.setItems(FXCollections.observableArrayList(listo.getCourseList()));
        deleteCourseChoiceBox.getSelectionModel().selectFirst();

        // Clear Text Boxes
        courseNameTextfield.clear();
        projectDescriptionTextfield.clear();
        projectGradeWeightTextfield.clear();
        projectNameTextfield.clear();
        dueDateTextfield.setValue(null);
        descViewArea.clear();
        listo_listoNameTextfield.clear();
        listo_listoDescTextfield.clear();
        listo_listoAuthorTextfield.clear();

        // Text Labels
        // no update on StatusLabel because it requires individual cases
        currentCountLabel.setText(String.valueOf(Project.count));
        listoNameLabel.setText(listo.getListoName());
        todaysDateLabel.setText(String.valueOf(LocalDate.now()));
    }


    // Edit Mode States
    /**
     * The state of the program when the Listo is being edited
     */
    public void EDIT_FILE() {
        // Update Program State
        program_state = "EDIT_FILE";

        // Create Sector - Course Section
        courseNameTextfield.setDisable(true);
        courseNameAddButton.setDisable(true);
        deleteCourseChoiceBox.setDisable(true);
        deleteCourseButton.setDisable(true);

        // Create Sector - Project Section
        projectCourseChoiceBox.setDisable(true);
        projectNameTextfield.setDisable(true);
        dueDateTextfield.setDisable(true);
        projectStatusChoiceBox.setDisable(true);
        projectFormatChoiceBox.setDisable(true);
        projectGradeWeightTextfield.setDisable(true);
        projectDescriptionTextfield.setDisable(true);
        createProjectButton.setDisable(true);

        // disable save changes button and enable create project button
        saveChangesButton.setVisible(false);
        createProjectButton.setVisible(true);
        // revert Create label name
        createLabelHeader.setText("Create");

        // Modify Sector
        editProjectButton.setDisable(true);
        deleteProjectButton.setDisable(true);

        // Quick Status Sector
        updateStatusChoiceBox.setDisable(true);
        updateStatusButton.setDisable(true);

        // View Description Sector
        viewDescSelectButton.setDisable(true);
        descViewArea.setDisable(true);

        // Menu Bar Sector
        aboutListoMenubar.setVisible(false);
        editListoMenuBar.setVisible(false);
        deleteListoMenubar.setVisible(false);
        saveAsMenubar.setDisable(true);
        saveMenubar.setDisable(true);
        newListoMenubar.setDisable(true);
        loadListoMenubar.setDisable(true);

        // View Table Sector
        viewListoTable.setDisable(true);

        // Clear Text Boxes
        courseNameTextfield.clear();
        projectDescriptionTextfield.clear();
        projectGradeWeightTextfield.clear();
        projectNameTextfield.clear();
        dueDateTextfield.setValue(null);
        descViewArea.clear();

        // Text Labels
        currentCountLabel.setText("EDIT MODE");
        listoNameLabel.setText("EDIT MODE");
        todaysDateLabel.setText(String.valueOf(LocalDate.now()));

    }

    /**
     * The state of the program when a Project is being edited
     */
    public void EDIT_PROJ() {
        // Update Program State
        program_state = "EDIT_PROJ";

        // Create Sector - Course Section
        courseNameTextfield.setDisable(true);
        courseNameAddButton.setDisable(true);
        deleteCourseChoiceBox.setDisable(true);
        deleteCourseButton.setDisable(true);

        // Create Sector - Project Section
        projectCourseChoiceBox.setDisable(false);
        projectNameTextfield.setDisable(false);
        dueDateTextfield.setDisable(false);
        projectStatusChoiceBox.setDisable(false);
        projectFormatChoiceBox.setDisable(false);
        projectGradeWeightTextfield.setDisable(false);
        projectDescriptionTextfield.setDisable(false);
        createProjectButton.setDisable(false);

        // enable save changes button and disable create project button
        saveChangesButton.setVisible(true);
        createProjectButton.setVisible(false);
        // revert Create label name
        createLabelHeader.setText("Edit Project");

        // Modify Sector
        editProjectButton.setDisable(true);
        deleteProjectButton.setDisable(true);

        // Quick Status Sector
        updateStatusChoiceBox.setDisable(true);
        updateStatusButton.setDisable(true);

        // View Description Sector
        viewDescSelectButton.setDisable(true);
        descViewArea.setDisable(true);

        // Menu Bar Sector
        aboutListoMenubar.setVisible(false);
        editListoMenuBar.setVisible(false);
        deleteListoMenubar.setVisible(false);
        saveAsMenubar.setDisable(true);
        saveMenubar.setDisable(true);
        newListoMenubar.setDisable(true);
        loadListoMenubar.setDisable(true);

        // View Table Sector
        viewListoTable.setDisable(true);

        // Clear Text Boxes
        courseNameTextfield.clear();
        descViewArea.clear();
        listo_listoNameTextfield.clear();
        listo_listoDescTextfield.clear();
        listo_listoAuthorTextfield.clear();

        // Text Labels
        currentCountLabel.setText("EDIT MODE");
        listoNameLabel.setText("EDIT MODE");
        todaysDateLabel.setText(String.valueOf(LocalDate.now()));

    }


    // --------------------------------------------------------------------------
    // FUNCTIONS AND EVENT HANDLERS PAST THIS POINT
    // --------------------------------------------------------------------------

    /**
     * Launches an alert window displaying information about the application
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void aboutAction(ActionEvent event) {
        // create a new Alert object
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // set text information
        alert.setTitle("About");
        alert.setHeaderText("About the Application");
        alert.setContentText("This application is developed by Cherry Rock Studios as a group project for the course, CPSC 233.\n" +
                "\nDevelopers: \tGabriel Rodriguez\n" +
                "\t\t\tSharoon Thabal\n" +
                "\n" +
                "Version: 1.3\n" +
                "What's new? We now have a GUI for Listo!");
        // show the Alert window
        alert.show();
    }

    /**
     * Launches an alert window displaying information about the current working Listo
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void aboutListoAction(ActionEvent event) {
        // create a new Alert object
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // set text information
        alert.setTitle("About the Listo");
        alert.setHeaderText("Listo: \t"+listo.getListoName());
        alert.setContentText("Listo Name: \t\t"+listo.getListoName()+"\n"+
                "Listo Author: \t\t"+listo.getListoUserName()+"\n"+
                "Date Created: \t\t"+listo.getDateCreated()+"\n"+
                "Listo Description: \t"+listo.getListoDescription()+"\n");
        // show the Alert window
        alert.show();
    }

    /**
     * This is used to add a course to the listo.
     * @param event: an ActionEvent to handle
     */
    @FXML
    void addCourseAction(ActionEvent event) {
        // make sure if to create a non-empty course
        if (!courseNameTextfield.getText().equals("")) {
            // adds course to Listo's course list
            String courseToAdd = courseNameTextfield.getText();
            listo.addCourse(courseToAdd);

            // update the Status label
            statusLabel.setText(courseToAdd + " has been added to the course list.");

            // clear the textbox
            courseNameTextfield.clear();

            // update course choice boxes
            String currentlySelected = projectCourseChoiceBox.getValue();
            projectCourseChoiceBox.setItems(FXCollections.observableArrayList(listo.getCourseList()));
            deleteCourseChoiceBox.setItems(FXCollections.observableArrayList(listo.getCourseList()));

            // select first if it's the first course
            if (listo.getCourseList().size() == 1) {
                projectCourseChoiceBox.getSelectionModel().selectFirst();
                deleteCourseChoiceBox.getSelectionModel().selectFirst();
            } else {
                // select the currently selected course before the new course is added
                projectCourseChoiceBox.getSelectionModel().select(currentlySelected);
                deleteCourseChoiceBox.getSelectionModel().select(currentlySelected);
            }

        } else {
            // alert for invalid input
            invalidInputAlert("You cannot add an empty course! Please input a name for the course if you were to add one.");
        }
    }

    /**
     * A multi-purpose button for handling Listo functions.
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void createListoAction(ActionEvent event) {
        // if button is a create button, initiate the called helper method
        if (listo_createListoButton.getText().equals("Create Listo")) {
            // launch the create new listo algorithm
            newListo_createListoAction(event);
        } else {
            // otherwise, it must be an edit button, then initiate the appropriate helper method
            editListo_createListoAction(event);
        }
    }

    /**
     * Creates a new project given information entered in the appropriate text fields.
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void createProjectAction(ActionEvent event) {
        // If the project name text field isn't empty, then create a project
        if (!projectNameTextfield.getText().equals("")) {

            // PROJECT COURSE
            String projectCourse;
            // make sure that the choice box is not empty
            if (projectCourseChoiceBox.getItems().size()!=0) {
                // if it's not empty, then get the currently selected value
                projectCourse = projectCourseChoiceBox.getValue();
            } else {
                // else, assign the project course as our default, "Unassigned"
                projectCourse = "Unassigned";
            }

            // PROJECT NAME
            String projectName = projectNameTextfield.getText();

            // PROJECT DUE
            LocalDate projectDue;
            // make sure that the DatePicker is not empty
            if (dueDateTextfield.getValue() != null) {
                // if it's not empty, then get the value in the DatePicker
                projectDue = dueDateTextfield.getValue();
            } else {
                // else, assign the project due as nothing by default
                projectDue = null;
            }

            // PROJECT STATUS AND FORMAT
            StatusType projectStatus = projectStatusChoiceBox.getValue();
            FormatType projectFormat = projectFormatChoiceBox.getValue();

            // PROJECT GRADE WEIGHT
            double projectGradeWeight;
            // make sure that the text field is not empty
            if (!projectGradeWeightTextfield.getText().isBlank()) {
                // catch exception for cases where the input isn't a numerical value
                try {
                    // parse String input into Double
                    projectGradeWeight = Double.parseDouble(projectGradeWeightTextfield.getText());
                } catch (Exception e) {
                    // default value is 0.0 if input parsing fails
                    projectGradeWeight = 0.0;
                    // throw error
                    invalidInputAlert("Your Grade Weight input is invalid! We have now just reset its value to its default, 0.0.");
                }
            } else {
                // else, assign the project grade weight as 0.0 by default
                projectGradeWeight = 0.0;
            }

            // PROJECT DESCRIPTION
            String projectDescription;
            // if the description field is empty
            if (projectDescriptionTextfield.getText().equals("")) {
                // assign the project description by default content
                projectDescription = "No description provided";
            } else {
                // else, set whatever description is in the text field
                projectDescription = projectDescriptionTextfield.getText();
            }

            // create a new project and add the details to it
            Project project = new Project(projectName);
            project.setProjectCourse(projectCourse);
            project.setProjectDue(projectDue);
            project.setProjectStatus(projectStatus);
            project.setProjectFormat(projectFormat);
            project.setGradeWeight(projectGradeWeight);
            project.setProjectDescription(projectDescription);

            // add the new project to the listo
            listo.addProject(project);

            // update status label
            statusLabel.setText(project.getProjectName() + " has been added to your listo, " + listo.getListoName() + ".");

            // clear the text boxes
            projectNameTextfield.clear();
            dueDateTextfield.setValue(null);
            projectGradeWeightTextfield.clear();
            projectDescriptionTextfield.clear();

            // update program state
            LOAD_FILE();

            // select the new project on the table
            viewListoTable.getSelectionModel().select(project);

        } else {
            // otherwise, throw an empty project error.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Project Error!");
            alert.setHeaderText("You cannot create a new empty Project!");
            alert.setContentText("You must add AT LEAST a name for the Project to create a new one!");
            alert.showAndWait();
        }
    }

    /**
     * Deletes a course selected at the choice box for course deletion.
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void deleteCourseAction(ActionEvent event) {
        // check if the choice box has elements remaining
        if (deleteCourseChoiceBox.getItems().size()!=0) {
            // delete course
            String courseToDelete = deleteCourseChoiceBox.getValue();
            listo.deleteCourse(courseToDelete);

            // update status
            statusLabel.setText(courseToDelete + " has been deleted from your course list.");

            // update course choice box in Create Sector
            projectCourseChoiceBox.setItems(FXCollections.observableArrayList(listo.getCourseList()));
            projectCourseChoiceBox.getSelectionModel().selectFirst();

            // update course choice box in Modify Sector
            deleteCourseChoiceBox.setItems(FXCollections.observableArrayList(listo.getCourseList()));
            deleteCourseChoiceBox.getSelectionModel().selectFirst();

            // update table upon deletion
            viewListoTable.getItems().clear();
            ObservableList<Project> projects = FXCollections.observableArrayList(listo.getProjectList());
            viewListoTable.setItems(projects);

        } else {
            // else, update status to tell the user that the course list is already empty.
            statusLabel.setText("Your course list is empty. You cannot delete further!");
        }
    }

    /**
     * launches a pop-up confirmation to delete the current Listo.
     * cancel deletion if confirmation box has been closed or vetoed.
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void deleteListoAction(ActionEvent event) {
        // throw a delete confirmation alert stage
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Listo?");
        alert.setHeaderText("Confirm Delete "+listo.getListoName()+"?");
        alert.setContentText("Are you sure you want to delete your current Listo?");
        alert.showAndWait();

        // handle answer from confirmation alert
        if (alert.getResult() == ButtonType.OK) {
            // set listo to null (to delete)
            listo = null;

            // clear text boxes for next use
            listo_listoNameTextfield.clear();
            listo_listoAuthorTextfield.clear();
            listo_listoDescTextfield.clear();

            // update program state
            NO_FILE();

            // delete the file from the directory if not null
            if (listo_directory != null) {
                File file = listo_directory;

                try {
                    boolean deleteStatus = file.delete();
                    if (deleteStatus) {
                        statusLabel.setText("Listo deleted successfully");
                    } else {
                        statusLabel.setText("Failed to delete Listo!");
                    }
                } catch (Exception e) {
                    statusLabel.setText("Failed to delete Listo!");
                }
            }

            // update Listo directory
            listo_directory = null;

        } else {
            // if deletion is cancelled, update the user about it.
            statusLabel.setText("Listo has not been deleted.");
        }
    }

    /**
     * Deletes the project that is currently selected in the tableView
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void deleteProjectAction(ActionEvent event) {
        // make sure that the listo is not empty (so there is something to delete)
        if (listo.getProjectList().size()!=0) {
            // access item
            int selectedID = viewListoTable.getSelectionModel().getSelectedIndex();
            Project projectToDelete = viewListoTable.getItems().get(selectedID);

            // update change in listo
            listo.deleteProject(projectToDelete);

            // update change in tableView
            viewListoTable.getItems().remove(selectedID);

            // if it's the sole project in the listo, reset everything
            if (listo.getProjectList().size()==0) {
                // update program state
                NEW_FILE();
            }

            // update status
            statusLabel.setText(projectToDelete.getProjectName()+" has been deleted from your listo, "+listo.getListoName());

            // set current selection to the item above it
            // -> it does this by default, so don't worry about it for now.

        } else {
            // indicating that the listo is empty
            statusLabel.setText("There are no projects left to delete!");
        }
    }

    /**
     * Loads the selected project into the appropriate textfields
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void editProjectAction(ActionEvent event) {
        // update program state
        EDIT_PROJ();

        // access selected project for editing
        int selectedID = viewListoTable.getSelectionModel().getSelectedIndex();
        Project selectedProject = viewListoTable.getItems().get(selectedID);

        // access project in Listo
        Project projectToEdit = listo.getProjectByID(selectedProject.getProjectID());

        // load project details in Create Sector
        // PROJECT COURSE
        // make sure that the project course is not a null-type
        if (projectToEdit.getProjectCourse().equals("Unassigned") || projectToEdit.getProjectCourse().equals("Deleted")) {
            // if it is, then select the first course on the choice box
            projectCourseChoiceBox.getSelectionModel().selectFirst();
        } else {
            // else, select the project course as it is.
            projectCourseChoiceBox.getSelectionModel().select(projectToEdit.getProjectCourse());
        }

        // PROJECT NAME
        projectNameTextfield.setText(projectToEdit.getProjectName());
        // PROJECT DUE
        dueDateTextfield.setValue(projectToEdit.getProjectDue());
        // PROJECT STATUS
        projectStatusChoiceBox.getSelectionModel().select(projectToEdit.getProjectStatus());
        // PROJECT FORMAT
        projectFormatChoiceBox.getSelectionModel().select(projectToEdit.getProjectFormat());
        // PROJECT GRADE WEIGHT
        projectGradeWeightTextfield.setText(String.valueOf(projectToEdit.getGradeWeight()));
        // PROJECT DESCRIPTION
        projectDescriptionTextfield.setText(projectToEdit.getProjectDescription());

        // set project edit variable (to transfer edit data to save changes button)
        edit_project = projectToEdit;

        // update status
        statusLabel.setText("You are currently editing '"+projectToEdit.getProjectName()+"' on Edit Mode.");
    }

    /**
     * Handles the event of the Edit Listo Menubar
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void editListoAction(ActionEvent event) {
        // disable everything in the background
        EDIT_FILE();

        // enable prompts for Listo info on Status pane
        statusLabel.setText("Please fill in the following information to edit your Listo: ");
        listo_createListoButton.setText("Edit Listo");

        // update blanks with current data
        listo_listoNameTextfield.setText(listo.getListoName());
        listo_listoAuthorTextfield.setText(listo.getListoUserName());
        listo_listoDescTextfield.setText(listo.getListoDescription());
        showListoSector(true);
    }

    /**
     * Loads a listo given by a FileChooser
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void loadListoAction(ActionEvent event) {
        // if there are no files open
        if (program_state.equals("NO_FILE")) {
            // load listo given there are no files open
            loadListoWithoutWarning();

        } else if (program_state.equals("NEW_FILE") || program_state.equals("LOAD_FILE")) {
            // loaf listo given there is a file open
            loadListoWithWarning();

        } else {
            // throw an error since you can't load during edit modes
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot Load File Error!");
            alert.setHeaderText("You cannot load a Listo during Edit Modes!");
            alert.setContentText("You must exit Edit Mode first to proceed!");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event of the New Listo Menubar
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void newListoAction(ActionEvent event) {
        // disable everything in the background
        NO_FILE();

        // create Listo
        statusLabel.setText("Please fill in the following information to create a Listo: ");

        // enable prompts for Listo info on Status pane
        listo_createListoButton.setText("Create Listo");
        showListoSector(true);
    }

    /**
     * modifies any changes made to the project during edit mode
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void saveChangesAction(ActionEvent event) {
        // make sure that the edit name isn't empty
        if (!projectNameTextfield.getText().equals("")) {

            // PROJECT COURSE
            String projectCourse;
            // make sure that the choice box is not empty
            if (projectCourseChoiceBox.getItems().size() != 0) {
                // if it's not empty, then get the currently selected value
                projectCourse = projectCourseChoiceBox.getValue();
            } else {
                // else, assign the project course as our default, "Unassigned"
                projectCourse = "Unassigned";
            }

            // PROJECT NAME
            String projectName = projectNameTextfield.getText();

            // PROJECT DUE
            LocalDate projectDue;
            // make sure that the DatePicker is not empty
            if (dueDateTextfield.getValue() != null) {
                // if it's not empty, then get the value in the DatePicker
                projectDue = dueDateTextfield.getValue();
            } else {
                // else, assign the project due as nothing by default
                projectDue = null;
            }

            // PROJECT STATUS AND FORMAT
            StatusType projectStatus = projectStatusChoiceBox.getValue();
            FormatType projectFormat = projectFormatChoiceBox.getValue();

            // PROJECT GRADE WEIGHT
            double projectGradeWeight;
            // make sure that the text field is not empty
            if (!projectGradeWeightTextfield.getText().isBlank()) {
                // catch exception for cases where the input isn't a numerical value
                try {
                    // parse String input into Double
                    projectGradeWeight = Double.parseDouble(projectGradeWeightTextfield.getText());
                } catch (Exception e) {
                    // default value is 0.0 if input parsing fails
                    projectGradeWeight = 0.0;
                    // throw error
                    invalidInputAlert("Your Grade Weight input is invalid! We have now just reset its value to its default, 0.0.");
                }
            } else {
                // else, assign the project grade weight as 0.0 by default
                projectGradeWeight = 0.0;
            }

            // PROJECT DESCRIPTION
            String projectDescription = projectDescriptionTextfield.getText();

            // reflect project info changes on listo
            Project project = edit_project;
            project.setProjectName(projectName);
            project.setProjectCourse(projectCourse);
            project.setProjectDue(projectDue);
            project.setProjectStatus(projectStatus);
            project.setProjectFormat(projectFormat);
            project.setGradeWeight(projectGradeWeight);
            project.setProjectDescription(projectDescription);

            // update program state
            LOAD_FILE();

            // update status label
            statusLabel.setText(project.getProjectName() + " has been updated in your listo, " + listo.getListoName() + ".");

            // select the new project on the table
            viewListoTable.getSelectionModel().select(project);

            // clear edit_project global variable
            edit_project = null;

        } else {
            // otherwise, throw an empty project name error.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Project Name Error!");
            alert.setHeaderText("You cannot empty a Project's name!");
            alert.setContentText("You must keep a name for the Project to proceed!");
            alert.showAndWait();
        }
    }

    /**
     * Handles the actions when the Save as Menubar is clicked.
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void saveAsAction(ActionEvent event) {
        // if there are no files open
        if (program_state.equals("LOAD_FILE") || program_state.equals("NEW_FILE")) {
            // create a FileChooser object
            FileChooser fc = new FileChooser();
            // set initial directory where the code is
            fc.setInitialDirectory(new File("."));
            // set initial filename to 'ListoName.txt'
            fc.setInitialFileName(listo.getListoName()+".txt");
            // returns a null file if file chooser is cancelled
            // launch Save dialog to select a file to save
            File fileToSave = fc.showSaveDialog(new Stage());

            // check if the file selection has been cancelled
            boolean saveSuccess = false;
            if (fileToSave != null) {
                // save Listo into the file
                saveSuccess = saveFile(fileToSave);
            } else {
                statusLabel.setText("Save as listo prompt has been cancelled.");
            }

            // only update the global listo directory if a listo has been saved as a new file
            if (saveSuccess) {
                // update the global listo directory
                listo_directory = fileToSave;

                // update program state
                LOAD_FILE();

                // update status
                statusLabel.setText(listo.getListoName()+" has been saved successfully!");
            }

        } else {
            // throw an error since you can't save during edit modes
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot Save File Error!");
            alert.setHeaderText("You cannot save a Listo during Edit Modes!");
            alert.setContentText("You must exit Edit Mode first to proceed!");
            alert.showAndWait();
        }
    }

    /**
     * Offers a quick save action
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void saveAction(ActionEvent event) {
        // save
        saveFile(listo_directory);

        // update status
        statusLabel.setText("Your changes have been saved successfully!");
    }

    /**
     * updates a selected project status
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void updateStatusAction(ActionEvent event) {
        // access project to edit
        int selectedID = viewListoTable.getSelectionModel().getSelectedIndex();
        Project selectedProject = viewListoTable.getItems().get(selectedID);

        // access project in Listo
        Project projectToUpdate = listo.getProjectByID(selectedProject.getProjectID());

        // update the project status in Listo
        projectToUpdate.setProjectStatus(updateStatusChoiceBox.getValue());

        // update program state
        LOAD_FILE();

        // update status
        statusLabel.setText(projectToUpdate.getProjectName()+" has been set to status: "+projectToUpdate.getProjectStatus());
    }

    /**
     * grabs the full description of the Project and puts it into the text area
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void viewDescAction(ActionEvent event) {
        // access project through selection from tableView
        int selectedID = viewListoTable.getSelectionModel().getSelectedIndex();
        Project selectedProject = viewListoTable.getItems().get(selectedID);

        // access project in Listo
        Project projectToViewDesc = listo.getProjectByID(selectedProject.getProjectID());

        // access project description
        descViewArea.setText(projectToViewDesc.getProjectDescription());
    }

    /**
     * Handles the event of the Quit Menubar
     *
     * @param event: an ActionEvent to handle
     */
    @FXML
    void quitAction(ActionEvent event) {
        // throw a delete confirmation alert stage
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Close the Program?");
        alert.setHeaderText("Confirm Close?");
        alert.setContentText("Are you sure you want to close your Listo? Your new changes will not be saved.");
        alert.showAndWait();

        // handle answer from confirmation alert
        if (alert.getResult() == ButtonType.OK) {
            // exit everything
            Platform.exit();
            System.exit(0);

        } else {
            // if deletion is cancelled, update the user about it.
            statusLabel.setText("The program will not close. Please save your changes before you quit.");
        }
    }

    // -------------------------------------------------------------------
    // EVERYTHING BELOW THIS ARE ALL HELPER METHODS
    // -------------------------------------------------------------------

    /** HELPER METHOD
     * Creates a new Listo based on given information
     *
     * @param event: an ActionEvent to handle
     */
    void newListo_createListoAction(ActionEvent event) {
        // check if the required parameters are filled in
        if (!listo_listoNameTextfield.getText().equals("") && !listo_listoAuthorTextfield.getText().equals("")) {
            // initialize a Listo
            listo = new Listo(listo_listoNameTextfield.getText());
            listo.setListoUserName(listo_listoAuthorTextfield.getText());

            // update Project count
            Project.updateCount(1);

            // set description to default if none provided
            if (!listo_listoDescTextfield.getText().equals("")) {
                listo.setListoDescription(listo_listoDescTextfield.getText());
            }

            // hide prompts for Listo
            showListoSector(false);

            // reset listo directory
            listo_directory = null;

            // update program state
            NEW_FILE();

        } else {
            // else if the required parameters are not filled in, remind the user to fill them in using an Alert pop-up!
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("A new Listo has not been created due to an error!");
            alert.setHeaderText("Not Enough Information to Create Listo!");
            alert.setContentText("You have left one or more blanks empty! Please fill them in to continue creating a Listo.");
            alert.show();
        }
    }

    /** HELPER METHOD
     * Updates current Listo based on given information
     *
     * @param event: an ActionEvent to handle
     */
    void editListo_createListoAction(ActionEvent event) {
        // check if the required parameters are not filled in with the same info as last time
        if (!listo_listoNameTextfield.getText().equals(listo.getListoName()) || !listo_listoAuthorTextfield.getText().equals(listo.getListoUserName()) || !listo_listoDescTextfield.getText().equals(listo.getListoDescription())) {
            // update the Listo
            listo.setListoName(listo_listoNameTextfield.getText());
            listo.setListoUserName(listo_listoAuthorTextfield.getText());
            listo.setListoDescription(listo_listoDescTextfield.getText());

            // update the user about the change
            statusLabel.setText("Your Listo has been updated! See 'About > About the Listo' to see the changes.");

            // hide prompts for Listo
            showListoSector(false);

            // enable everything
            LOAD_FILE();

            // clear textboxes
            listo_listoNameTextfield.clear();
            listo_listoAuthorTextfield.clear();
            listo_listoDescTextfield.clear();

        } else {
            // else if the required parameters are not filled in, remind the user to fill them in using an Alert pop-up!
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("A Listo has not been modified!");
            alert.setHeaderText("No Changes has been made!");
            alert.setContentText("You have not modified any information about the Listo! Please change at least one thing to exit editing.");
            alert.show();
        }
    }

    /** HELPER METHOD
     * It launches another alert stage to tell the user that there is an invalid input.
     *
     * @param message: message to display as the content of the alert
     */
    public void invalidInputAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input Error!");
        alert.setHeaderText("You have entered an invalid input!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** HELPER METHOD
     * Disables the Listo Sector of the Program
     *
     * @param bool: a boolean value that determines whether or not something should be disabled
     */
    public void showListoSector(boolean bool) {
        listo_createListoButton.setVisible(bool);
        listo_listoAuthorTextfield.setVisible(bool);
        listo_listoDescTextfield.setVisible(bool);
        listo_listoLabel.setVisible(bool);
        listo_listoNameTextfield.setVisible(bool);
    }

    /**
     * HELPER METHOD
     * writes data into the selected output file (fileToSave) using PrintWriter
     *
     * @param fileToSave: the File where data shall be saved
     * @return true if saved file successfully, else false.
     */
    public boolean saveFile(File fileToSave) {
        // instantiate PrintWriter object
        try (PrintWriter printWriter = new PrintWriter(fileToSave)) {
            // call subroutine that deals with putting the world into a save format comma separated values.
            String obj = listo.saveFormat();
            // write the save format data into the output file
            printWriter.println(obj);
            // don't forget to flush and close!
            printWriter.flush();
            printWriter.close();

            // update status
            statusLabel.setText("Your changes have been saved successfully!");
            return true;

        } catch (Exception e) {
            // throw error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save File Error!");
            alert.setHeaderText("Something wrong happened while the file is saving!");
            alert.setContentText("The Listo "+listo.getListoName()+" file cannot be saved into "+fileToSave.getName()+"!");
            alert.showAndWait();
            return false;
        }
    }

    /** HELPER METHOD
     *  Loads a Listo given provided file, throws an alert if file can't be loaded
     *
     * @param fileToLoad: a file to be loaded
     * @return true if loaded file successfully, else false.
     */
    public boolean readFile(File fileToLoad) {
        // load Listo
        try {
            // try to load listo file to our listo
            listo = Reader.loadFile(fileToLoad);
            return true;

        } catch (Exception e) {
            // throw error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load File Error!");
            alert.setHeaderText("Something wrong happened during file loading!");
            alert.setContentText("The file "+fileToLoad+" can not be loaded into the Listo!");
            alert.showAndWait();
            return false;
        }
    }

    /** HELPER METHOD
     *  Loads a file right away and updates the interface without warning the user.
     */
    public void loadListoWithoutWarning() {
        // create a FileChooser object
        FileChooser fc = new FileChooser();
        // set initial directory where the code is
        fc.setInitialDirectory(new File("."));
        // launch open dialog to select a file to load
        File fileToLoad = fc.showOpenDialog(new Stage());

        // load the file to our listo global variable if it isn't null
        boolean loadSuccess = false;
        if (fileToLoad != null) {
            loadSuccess = readFile(fileToLoad);
        } else {
            statusLabel.setText("Load listo prompt has been cancelled.");
        }

        // only update the global listo directory if a listo has been loaded
        if (loadSuccess) {
            // update the global listo directory
            listo_directory = fileToLoad;

            // update program state
            if (listo.getProjectList().size() != 0) {
                // if project list isn't empty, initiate load file state
                LOAD_FILE();
            } else {
                // if project list is empty, initiate new file state
                NEW_FILE();
            }

            // update status
            statusLabel.setText(listo.getListoName()+" has been loaded into the app!");
        }
    }

    /** HELPER METHOD
     *  Loads a file and updates the interface after warning the user.
     */
    public void loadListoWithWarning() {
        // throw an overwrite confirmation alert stage
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Overwrite the Program?");
        alert.setHeaderText("Confirm Overwrite?");
        alert.setContentText("Are you sure you want to load a new Listo? Your current Listo will be overwritten.");
        alert.showAndWait();

        // handle answer from confirmation alert
        if (alert.getResult() == ButtonType.OK) {
            // open a new file if overwritten prompt is positive
            loadListoWithoutWarning();

        } else {
            // if prompt is cancelled, update the user about it.
            statusLabel.setText("A Listo will not be loaded. Please save your changes before you quit.");
        }
    }

    /** HELPER METHOD
     * Uses equals() and compareTo() override methods, to update past dues as Late Statuses
     */
    public void updateStatusBasedToday() {
        // sort a copy of the Project List based on due date
        ArrayList<Project> projectCopy = new ArrayList<Project>(listo.getProjectList());
        ArrayList<Project> sortedProjects = new ArrayList<Project>();
        // exclude the ones that don't have due dates
        for (Project project: projectCopy) {
            if (project.getProjectDue() != null) {
                sortedProjects.add(project);
            }
        }

        // only sort the ones that have due dates
        sortedProjects.sort(new DueDateComparator());

        // use a dummy Project with today's as due date and use as comparison
        Project dueTodayProject = new Project("Test");
        dueTodayProject.setProjectDue(LocalDate.now());
        // offset the count due to the dummy project
        Project.updateCount(Project.count - 1);

        // differential value = current index - today's date <- the moment we stop getting negative values is where our pointer index should be
        int diffValue = 0;

        // index to use as a reference from what is past
        int presentIndex = 0;

        // loop through each Listo and check which are past dues, then update their status
        for (int i = 0; i < sortedProjects.size(); i++) {
            // update differential value
            diffValue = sortedProjects.get(i).getProjectDue().compareTo(dueTodayProject.getProjectDue());

            // check if the differential value has finally hit a positive
            if (diffValue >= 0) {
                // set current loop index as present index
                presentIndex = i;
                // end the loop once the first instance of a match occurs
                break;
            }

            // if the loop is about to end and still hasn't found a positive differential value,
            // then this implies that all the dates are past today's date
            if (i == (sortedProjects.size() - 1)) {
                presentIndex = sortedProjects.size();
            }
        }

        // loop through each Project again in Listo to update their statuses
        for (int j = 0; j < presentIndex; j++) {
            // if it hasn't been completed, postponed, late, or cancelled already, set it to late
            if (sortedProjects.get(j).getProjectStatus().equals(StatusType.ACTIVE) || sortedProjects.get(j).getProjectStatus().equals(StatusType.IN_PROGRESS)) {
                listo.getProjectByID(sortedProjects.get(j).getProjectID()).setProjectStatus(StatusType.LATE);
            }
        }
    }
}