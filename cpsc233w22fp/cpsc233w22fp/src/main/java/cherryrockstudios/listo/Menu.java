package cherryrockstudios.listo;

import cherryrockstudios.listo.comparators.*;
import cherryrockstudios.listo.enums.FormatType;
import cherryrockstudios.listo.enums.StatusType;
import cherryrockstudios.listo.unit.Listo;
import cherryrockstudios.listo.unit.Project;
import cherryrockstudios.listo.util.Logger;
import cherryrockstudios.listo.util.Reader;

import java.io.File;
import java.time.LocalDate;
import java.util.*;


/**
 * Class containing menu commands of the program.
 * THIS IS NOT USED IN THE GUI PROGRAM. THIS IS THE COMMAND LINE VERSION OF IT.
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public class Menu {

    /**
     * The scanner to access System.in input commands
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * A Logger for saving program progress to an output file
     */
    private static Logger logger;

    /**
     * list of project info categories that can be modified by the user
     */
    public static ArrayList<String> modifiableTraits = new ArrayList<String>(List.of("Name","Due Date","Status","Description","Grade Weight","Format","Course"));

    /**
     * Mimics python's input() function, basically.
     *
     * @param string_message: a custom message displayed before asking for input.
     * @return String entered through input prompt.
     */
    public static String inputString(String string_message) {
        System.out.print(string_message);
        return scanner.nextLine();
    }

    /**
     * Like inputString() but this time, it asks for a date in the format YYYY-MM-DD
     * this also performs error-checking for date formats
     *
     * @param stringMessage: a custom message displayed before asking for input.
     * @return A valid date format entered through input prompt.
     */
    public static LocalDate inputDate(String stringMessage) {
        LocalDate validDate;
        String date;
            while (true) {
                // prompt for project due date
                System.out.println(stringMessage);
                System.out.print("Follow the form YYYY-MM-DD with dashes in between: ");
                date = scanner.nextLine();

                // error checking
                String[] dueComposition = date.strip().split("-");
                if (dueComposition.length == 3 &&
                            (dueComposition[0].length() == 4 &&
                            dueComposition[1].length() == 2 &&
                            dueComposition[2].length() == 2)) {
                    try {
                        LocalDate.parse(date.strip());
                        validDate = LocalDate.parse(date.strip());
                        return validDate;
                    } catch (Exception e) {;
                        System.out.println("The date you've entered is invalid! Please try again.");
                    }
                } else {
                    System.out.println("The date you've entered is invalid! Please try again.");
                }
            }
    }

    /**
     * Like inputString() but this time for integer values
     * also performs error checking
     *
     * @param string_message: a custom message displayed before asking for input.
     * @return int value entered through input prompt.
     */
    public static int inputInteger(String string_message) {
        int number;
        while (true) {
            System.out.print(string_message);
            int grade = 0;

            // prompt with error checking
            try {
                grade = scanner.nextInt();
                number = grade;
                return number;
            } catch (Exception e) {
                System.out.println("The value you've entered is not valid! Please try again.");
            }
            scanner.nextLine(); // remove escape line character
        }
    }

    /**
     * Like inputString() but this time for double values
     * also performs error checking
     *
     * @param string_message: a custom message displayed before asking for input.
     * @return double value entered through input prompt.
     */
    public static Double inputDouble(String string_message) {
        double number;
        while (true) {
            System.out.print(string_message);
            double grade = 0.0;

            // prompt with error checking
            try {
                grade = scanner.nextDouble();
                number = grade;
                return number;
            } catch (Exception e) {
                System.out.println("The value you've entered is not valid! Please try again.");
            }
            scanner.nextLine(); // remove escape line character
        }
    }

    /**
     * A boolean prompt with custom messages
     *
     * @param string_message: a custom message displayed before asking for input.
     * @return True if yes, otherwise False.
     */
    public static boolean YNprompt(String string_message) {
        System.out.println(string_message);
        String command;

        // ask for input on loop until user enters valid input
        while (true) {
            System.out.print("Enter Y for Yes, or N for No: ");
            command = scanner.nextLine();
            command = command.toUpperCase();

            // handle input
            if (command.equals("Y")) {
                return true;
            } else if (command.equals("N")) {
                return false;
            } else {
                System.out.println("\nThat is not a valid command! Please try again.");
            }
        }
    }

    /**
     * sets up the logger into the specified output file
     *
     * @param outputFile: the specified output file where we will store the data
     */
    public static void setOutputFile(File outputFile) {
        logger = new Logger(outputFile);
    }

    /**
     * prints and saves data into the specified output file.
     *
     * @param obj: the data to store into the output file.
     */
    public static void storeIntoOutput(Object obj) {
        if (logger == null) {
            throw new RuntimeException("Cannot save data into output file until the logger has been set up.");
        }
        logger.saveData(obj);
    }


    /**
     * Run the Listo program
     */
    public static void runProgram() {
        ArrayList<String> mainMenuOptions = new ArrayList<String>(List.of("Load Listo","Save Listo","Create a Project", "Update Project Information", "Add Courses", "View Basic Listo",
                        "View Full Listo", "View Courses", "View Projects by Filter", "Delete a Project", "Delete a Course", "Quit the Program"));

        System.out.println("Welcome to Listo! Your Assistant in your Academics!");
        String command = "";

        // program loop
        while (!(command.equals("Quit"))) {
            // show and prompt for a menu option
            System.out.println("\n---------------------------");
            System.out.println("Choose a menu option: ");
            command = promptForElementInList(mainMenuOptions, scanner);

            // analyze command
            if (command.equals(mainMenuOptions.get(0))) {
                // load a listo
                // prompt for a file name
                String inputName = inputString("Please enter the name of the file you want to load (include .txt): ");
                File loadfile = new File(inputName);
                // check file
                if (!loadfile.exists() || !loadfile.isFile() || !loadfile.canWrite()) {
                    System.err.println("The file "+loadfile.getAbsoluteFile()+" can not be accessed.");
                } else {
                    // YN Prompt on whether to close current working Listo to open the load file
                    if (YNprompt("Loading this file will change your current working Listo, do you want to proceed?")) {
                        System.out.println("Loading data from the file...");
                        // proceed to reset the current working Listo
                        Main.currentWorkingListo = Reader.loadFile(loadfile);
                        System.out.println("New Listo has been loaded successfully! It is now open and active!");
                    } else {
                        System.out.println("Loading the file has been cancelled!");
                    }
                }

            } else if (command.equals(mainMenuOptions.get(1))) {
                // save a listo
                storeIntoOutput(Main.currentWorkingListo.saveFormat());
                System.out.println("Listo "+Main.currentWorkingListo.getListoName()+" has been saved successfully!");

            } else if (command.equals(mainMenuOptions.get(2))) {
                // create a project
                // prompt for project name
                String name = inputString("Enter a name for the new project: ");
                Project project = new Project(name);
                // add project to Listo
                Main.currentWorkingListo.addProject(project);
                System.out.printf("A new project named %s has been created!\n", name);

            } else if (command.equals(mainMenuOptions.get(3))) {
                // update project info

                // prompt for project to be modified
                System.out.println("PLease choose a project that you want to modify.");
                Project projectAccessed = promptForProject(Main.currentWorkingListo, scanner);

                // make sure project list isn't null
                if (projectAccessed != null) {
                    // prompt for project info to update
                    System.out.println("\nPlease select the information you want to update.");
                    String infoCategory = promptForElementInList(modifiableTraits, scanner);

                    // analyze info category
                    if (infoCategory.equals(modifiableTraits.get(0))) {
                        // update name + format checking
                        String newName = inputString("Enter the new name of your project: ");
                        newName = newName.strip();
                        // rename project
                        System.out.printf("%s has been renamed to %s!", projectAccessed.getProjectName(), newName);
                        projectAccessed.setProjectName(newName);

                    } else if (infoCategory.equals(modifiableTraits.get(1))) {
                        // update due date
                        LocalDate newDueDate = inputDate("Enter the new due date of your project: ");
                        projectAccessed.setProjectDue(newDueDate);
                        System.out.println("Project Due Date Updated!");

                    } else if (infoCategory.equals(modifiableTraits.get(2))) {
                        // update status

                        // prompt for Status item
                        String newStatus = promptForElementInList(StatusType.statusTypeList, scanner);
                        if (newStatus.equals("CANCELLED")) {
                            projectAccessed.setProjectStatus(StatusType.getStatusType('E'));
                            System.out.println("Project is now CANCELLED!");
                        } else {
                            projectAccessed.setProjectStatus(StatusType.getStatusType(newStatus.charAt(0)));
                            System.out.println("Project is now "+StatusType.getStatusType(newStatus.charAt(0)));
                        }

                    } else if (infoCategory.equals(modifiableTraits.get(3))) {
                        // update description
                        String newDescription = inputString("Enter a description for your project: ");
                        projectAccessed.setProjectDescription(newDescription);
                        System.out.println("Project Description Updated!");

                    } else if (infoCategory.equals(modifiableTraits.get(4))) {
                        // update grade weight
                        double newGrade = inputDouble("Enter the grade weight of your project: ");
                        projectAccessed.setGradeWeight(newGrade);
                        System.out.println("Project Grade Weight Updated!");

                    } else if (infoCategory.equals(modifiableTraits.get(5))) {
                        // update format

                        // prompt for format item
                        String newFormat = promptForElementInList(FormatType.formatTypeList, scanner);
                        projectAccessed.setProjectFormat(FormatType.getFormatType(newFormat.charAt(0)));
                        System.out.println("Project has been set as "+FormatType.getFormatType(newFormat.charAt(0)));


                    } else if (infoCategory.equals(modifiableTraits.get(6))) {
                        // update course
                        // in the case where course list is empty, do nothing
                        if (Main.currentWorkingListo.getCourseList().size() != 0) {
                            String projectCourse = promptForElementInList(Main.currentWorkingListo.getCourseList(), scanner);
                            projectAccessed.setProjectCourse(projectCourse);
                            System.out.println("Project Course Updated!");
                        } else {
                            System.out.println("Project Course NOT Updated!");
                        }
                    } else {
                        System.out.println("You have entered an invalid index. Please try again.");
                    }
                }

            } else if (command.equals(mainMenuOptions.get(4))) {
                // add courses
                String courseName = inputString("Enter the name of your course: ");
                Main.currentWorkingListo.addCourse(courseName);
                System.out.printf("%s has been added to your course list!\n", courseName);

            } else if (command.equals(mainMenuOptions.get(5))) {
                // view basic listo
                String message = Main.currentWorkingListo.viewListo(true);
                System.out.println(message);

            } else if (command.equals(mainMenuOptions.get(6))) {
                // view full listo
                String message = Main.currentWorkingListo.viewListo(false);
                System.out.println(message);

            } else if (command.equals(mainMenuOptions.get(7))) {
                // view courses
                ArrayList<String> courseList = Main.currentWorkingListo.getCourseList();
                // display the course list
                System.out.println(printItems(courseList));

            } else if (command.equals(mainMenuOptions.get(8))) {
                // view projects by filter
                ArrayList<String> filterTypes = new ArrayList<String>(List.of("Sort Alphabetically", "Sort Chronologically", "Sort by Status", "Sort by Grade Weight", "Sort by Format"));

                System.out.println("Please specify a filter.");
                String filter = promptForElementInList(filterTypes, scanner);

                // analyze filter
                if (filter.equals(filterTypes.get(0))) {
                    // sort by name
                    // retrieve the projects
                    ArrayList<Project> projectArrayList = Main.currentWorkingListo.getProjectList();
                    ArrayList<Project> sortedProjects = new ArrayList<>(projectArrayList);
                    // sort the projects
                    sortedProjects.sort(new ProjectNameComparator());

                    // display the projects after prompting for order
                    if (YNprompt("Do you want to order the projects in Descending Order?")) {
                        Collections.reverse(sortedProjects);
                        String message = printProjects(sortedProjects, "Listing projects sorted by NAME in DESCENDING ORDER.");
                        System.out.println(message);
                    } else {
                        String message = printProjects(sortedProjects, "Listing projects sorted by NAME in ASCENDING ORDER.");
                        System.out.println(message);
                    }

                } else if (filter.equals(filterTypes.get(1))) {
                    // sort by due date
                    // retrieve the projects
                    ArrayList<Project> projectArrayList = Main.currentWorkingListo.getProjectList();
                    ArrayList<Project> sortedProjects = new ArrayList<>(projectArrayList);
                    // sort the projects
                    sortedProjects.sort(new DueDateComparator());

                    // display the projects after prompting for order
                    if (YNprompt("Do you want to order the projects in Descending Order?")) {
                        Collections.reverse(sortedProjects);
                        String message = printProjects(sortedProjects, "Listing projects sorted by DUE DATE in DESCENDING ORDER.");
                        System.out.println(message);
                    } else {
                        String message = printProjects(sortedProjects, "Listing projects sorted by DUE DATE in ASCENDING ORDER.");
                        System.out.println(message);
                    }


                } else if (filter.equals(filterTypes.get(2))) {
                    // sort by status
                    // retrieve the projects
                    ArrayList<Project> projectArrayList = Main.currentWorkingListo.getProjectList();
                    ArrayList<Project> sortedProjects = new ArrayList<>(projectArrayList);
                    // sort the projects
                    sortedProjects.sort(new StatusTypeComparator());

                    // display the projects after prompting for order
                    if (YNprompt("Do you want to order the projects in Descending Order?")) {
                        Collections.reverse(sortedProjects);
                        String message = printProjects(sortedProjects, "Listing projects sorted by STATUS in DESCENDING ORDER.");
                        System.out.println(message);
                    } else {
                        String message = printProjects(sortedProjects, "Listing projects sorted by STATUS in ASCENDING ORDER.");
                        System.out.println(message);
                    }

                } else if (filter.equals(filterTypes.get(3))) {
                    // sort by grade weight
                    // retrieve the projects
                    ArrayList<Project> projectArrayList = Main.currentWorkingListo.getProjectList();
                    ArrayList<Project> sortedProjects = new ArrayList<>(projectArrayList);
                    // sort the projects
                    sortedProjects.sort(new GradeWeightComparator());

                    // display the projects after prompting for order
                    if (YNprompt("Do you want to order the projects in Descending Order?")) {
                        Collections.reverse(sortedProjects);
                        String message = printProjects(sortedProjects, "Listing projects sorted by GRADE WEIGHT in DESCENDING ORDER.");
                        System.out.println(message);
                    } else {
                        String message = printProjects(sortedProjects, "Listing projects sorted by GRADE WEIGHT in ASCENDING ORDER.");
                        System.out.println(message);
                    }


                } else if (filter.equals(filterTypes.get(4))) {
                    // sort by format
                    // retrieve the projects
                    ArrayList<Project> projectArrayList = Main.currentWorkingListo.getProjectList();
                    ArrayList<Project> sortedProjects = new ArrayList<>(projectArrayList);
                    // sort the projects
                    sortedProjects.sort(new FormatTypeComparator());

                    // display the projects after prompting for order
                    if (YNprompt("Do you want to order the projects in Descending Order?")) {
                        Collections.reverse(sortedProjects);
                        String message = printProjects(sortedProjects, "Listing projects sorted by FORMAT in DESCENDING ORDER.");
                        System.out.println(message);
                    } else {
                        String message = printProjects(sortedProjects, "Listing projects sorted by FORMAT in ASCENDING ORDER.");
                        System.out.println(message);
                    }


                } else {
                    System.out.println("You have entered an invalid index. Please try again.");
                }

            } else if (command.equals(mainMenuOptions.get(9))) {
                // delete a project


                // prompt for project to be deleted
                System.out.println("Please choose a project that you want to delete.");
                Project projectAccessed = promptForProject(Main.currentWorkingListo, scanner);

                // make sure project isn't null
                if (projectAccessed != null) {
                    // make sure they want to delete the project
                    if (YNprompt("Are you sure that you want to delete " + projectAccessed.getProjectName() + "?")) {
                        Main.currentWorkingListo.deleteProject(projectAccessed);
                        System.out.println(projectAccessed.getProjectName() + " has been deleted successfully!");
                    } else {
                        System.out.println("Deletion Cancelled. " + projectAccessed.getProjectName() + " has NOT been deleted.");
                    }
                }

            } else if (command.equals(mainMenuOptions.get(10))) {
                // delete a course
                // make sure course list is not empty
                if (Main.currentWorkingListo.getCourseList().size() != 0) {
                    // deletion algorithm
                    ArrayList<String> courseList = Main.currentWorkingListo.getCourseList();
                    String courseToDelete = promptForElementInList(courseList, scanner);
                    Main.currentWorkingListo.deleteCourse(courseToDelete);
                    System.out.printf("The course %s has been deleted successfully!\n", courseToDelete);
                } else {
                    System.out.println("The course list is empty! No delete operation is possible!");
                }

            } else if (command.equals(mainMenuOptions.get(11))) {
                // quit the program

                // ask for save current changes
                if (YNprompt("Are you sure you want to quit the program? Any progress will not be saved.\nMake sure you have saved the file first before you quit!")) {
                    command = "Quit";
                    System.out.println("Quitting the Program...");
                } else {
                    System.out.println("Program Termination Cancelled!");
                }

            } else {
                System.out.println("<!!!> That is not a Command. Please Enter Again. <!!!>");
            }

        }
    }

    /**
     * prompts the user an index to access an element in a list.
     *
     * @param list: a list in which one of its elements will be selected and returned.
     * @param scanner: an instance of the Scanner class
     * @return a selected item according to the prompt.
     */
    public static String promptForElementInList(ArrayList<String> list, Scanner scanner) {
        String selectedItem = "";
        boolean continueLoop = true;
        // prompt loop
        while (continueLoop) {
            // stop loop if list is empty.
            if (list.size() == 0) {
                System.out.println("List is empty. Prompt terminated.");
                return "LIST_IS_EMPTY";
            }

            // display elements in list
            System.out.println(printItems(list));
            System.out.print("Pick a number to select the item: ");

            // ensure valid inputs using exceptions
            int itemIndex = list.size() + 1;
            try {
                itemIndex = (scanner.nextInt() - 1); // offset numbering
            } catch (Exception e) {
                System.out.println("That is not a number!");
            }
            scanner.nextLine(); // to remove the escape line character

            // error checking
            if ((0 <= itemIndex) && (itemIndex < list.size())) {
                selectedItem = list.get(itemIndex);
                continueLoop = false;
            } else {
                System.out.println("That is not a valid input. Please pick a number available above.");
            }
        }
        return selectedItem;
    }

    /**
     * prints the elements of a list in a legible format (indexing is offset by +1)
     *
     * @param list: an ArrayList<String>.
     * @return a String value containing the appropriate information for what has been requested.
     */
    public static String printItems(ArrayList<String> list) {
        String message = "There are " + list.size() + " item(s) on record.\n";
        // list each element for each line
        for (int i = 0; i < list.size(); i++) {
            message += ((i+1) + "\t| " + list.get(i) + "\n");
        }
        return message;
    }

    public static String printProjects(ArrayList<Project> projectList, String message) {
        String outputString = message+"\n";
        // add header
        outputString += "------------------------------------------------------------------------------------------------\n";
        outputString += "ID\t| Course\t| Project Name\t| Due Date\t| Status\t| Format\t| Grade Weight\t| Description\n";
        outputString += "------------------------------------------------------------------------------------------------\n";

        // print each project for each line
        for (Project project: projectList) {
            outputString += project.toString()+"\n";
        }
        return outputString;
    }

    /**
     * prompts the user an index to access a project in Listo.
     *
     * @param listo: the Listo that contains the Projects
     * @param scanner: a scanner instance to take inputs
     * @return a Project selected by the index provided
     */
    public static Project promptForProject(Listo listo, Scanner scanner) {
        // default value is null
        Project selectedProject = null;
        ArrayList<Project> list = listo.getProjectList();
        boolean continueLoop = true;
        // prompt loop
        while (continueLoop) {
            // stop loop if list is empty.
            if (list.size() == 0) {
                System.out.println("Listo is empty. Prompt terminated.");
                return selectedProject;
            }

            // display elements in list
            System.out.println(printItems(listo.getProjectNames()));
            System.out.print("Pick a number to select the item: ");

            // ensure valid inputs using exceptions
            int itemIndex = list.size() + 1;
            try {
                itemIndex = (scanner.nextInt() - 1); // offset numbering
            } catch (Exception e) {
                System.out.println("That is not a number!");
            }
            scanner.nextLine(); // to remove the escape line character

            // error checking
            if ((0 <= itemIndex) && (itemIndex < list.size())) {
                selectedProject = listo.getProject(itemIndex);
                continueLoop = false;
            } else {
                System.out.println("That is not a valid input. Please pick a number available above.");
            }
        }
        return selectedProject;

    }

}
