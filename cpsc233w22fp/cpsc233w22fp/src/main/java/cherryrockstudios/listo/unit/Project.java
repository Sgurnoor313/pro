package cherryrockstudios.listo.unit;

import cherryrockstudios.listo.enums.FormatType;
import cherryrockstudios.listo.enums.StatusType;

import java.time.LocalDate;


/**
 * Each Listo has projects. A Project is a main unit of academic task, and specific
 * details can be added about each project.
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public class Project {

    /**
     * Counter for projects to determine their ID's
     */
    public static int count = 1;

    /**
     * Projects have ID's for indexing purposes.
     */
    private final int projectID;

    /**
     * The name of the project
     */
    private String projectName;

    /**
     * The due date of the project
     */
    private LocalDate projectDue;

    /**
     * A brief description of the project
     */
    private String projectDescription;

    /**
     * The grade weight of the project.
     */
    private Double gradeWeight;

    /**
     * The course of the project
     */
    private String projectCourse;

    /**
     * The status of the project
     */
    private StatusType projectStatus;

    /**
     * The format of the project
     */
    private FormatType projectFormat;

    /**
     * The constructor for the Project Type. This creates an instance of the Project class.
     *
     * @param name: the name of the project
     */
    public Project(String name){
        this.projectName = name;

        // default values for qualitative details are null
        this.projectDue = null;
        this.gradeWeight = 0.0;

        // default value for String details
        this.projectDescription = "No description provided";
        this.projectCourse = "Unassigned";

        // default values for enum details
        this.projectStatus = StatusType.ACTIVE;
        this.projectFormat = FormatType.NO_FORMAT;

        // set ID for project
        this.projectID = count;
        count++;
    }

    /**
     * The constructor for the Project Type (used for Reader.loadFile()). This creates an instance of the Project class.
     *
     * @param id: the id of the project
     * @param name: the name of the project
     * @param duedate: the due date of the project
     * @param gradeWeight: the grade weight of the project
     * @param descripton: the description of the project
     * @param course: the course of the project
     * @param status: the status of the project
     * @param format: the format of the project
     */
    public Project(int id, String name, LocalDate duedate, Double gradeWeight, String descripton, String course, StatusType status, FormatType format) {
        // set appropriate details
        this.projectID = id;
        this.projectName = name;
        this.projectDue = duedate;
        this.gradeWeight = gradeWeight;
        this.projectDescription = descripton;
        this.projectCourse = course;
        this.projectStatus = status;
        this.projectFormat = format;
    }


    // Mutator Methods
    /**
     * Updates the name of the project
     *
     * @param name: name of the project
     */
    public void setProjectName(String name){
        this.projectName = name;
    }

    /**
     * Sets or updates the due date of the project
     *
     * @param date: a String date to be parsed as Local Date type (must be error checked beforehand)
     */
    public void setProjectDue(LocalDate date) {
        this.projectDue = date;
    }

    /**
     * Sets or updates the grade weight of the project
     *
     * @param grade: a Double that is the grade weight of the project.
     */
    public void setGradeWeight(Double grade) {
        this.gradeWeight = grade;
    }

    /**
     * Sets or updates the description of the project
     *
     * @param descripton: a brief description of the project
     */
    public void setProjectDescription(String descripton) {
        this.projectDescription = descripton;
    }

    /**
     * Sets or updates the course of the project.
     *
     * @param course: a Course type that is the project course
     */
    public void setProjectCourse(String course) {
        this.projectCourse = course;
    }

    /**
     * Sets or updates the status of the project.
     *
     * @param status: a Status type that is the project status
     */
    public void setProjectStatus(StatusType status) {
        this.projectStatus = status;
    }

    /**
     * Sets or updates the format of the project.
     *
     * @param format: a Format type that is the project format
     */
    public void setProjectFormat(FormatType format) {
        this.projectFormat = format;
    }

    /**
     * Updates the count variable. (Only used when loading a Listo)
     *
     * @param newCount: the new count value extracted form a loaded Listo.
     */
    public static void updateCount(int newCount) {
        count = newCount;
    }


    // Accessor Methods
    /**
     * A getter method for the project's ID
     *
     * @return the ID of the project
     */
    public int getProjectID() {
        return projectID;
    }

    /**
     * A getter method for the project's name
     *
     * @return the name of the project
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * A getter method for the project's due date
     *
     * @return the due date of the project
     */
    public LocalDate getProjectDue() {
        return projectDue;
    }

    /**
     * A getter method for the project's grade weight
     *
     * @return the grade weight of the project
     */
    public Double getGradeWeight() {
        return gradeWeight;
    }

    /**
     * A getter method for the project description
     *
     * @return a brief description of the project
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * A getter method for the project course
     *
     * @return the course of the project
     */
    public String getProjectCourse() {
        return projectCourse;
    }

    /**
     * A getter method for the project status
     *
     * @return the status of the project
     */
    public StatusType getProjectStatus() {
        return projectStatus;
    }

    /**
     * A getter method for the project format
     *
     * @return the format of the project
     */
    public FormatType getProjectFormat() {
        return projectFormat;
    }


    // Helper Methoda
    /**
     * Overrides the toString() method to return as a format readable to the reader
     *
     * @return the project's information separated by tab
     */
    @Override
    public String toString() {
        return projectID+"\t| "+projectCourse+"\t| "+projectName+"\t| "+projectDue+"\t| "+projectStatus+"\t| "+projectFormat+"\t| "+gradeWeight+"\t| "+ projectDescription;
    }

    /**
     * Returns a format of the project that will be stored in the output file
     *
     * @return a comma-separated values about the project
     */
    public String saveFormat() {
        String formatString;
        formatString = projectID+","+projectCourse+","+projectName+","+projectDue+","+projectStatus+","+projectFormat+","+gradeWeight+","+ projectDescription;
        return formatString;
    }

    /**
     * Returns the count so numbering will make sense once loaded
     *
     * @return the count value as String
     */
    public static String saveCount() {
        return String.valueOf(count);
    }

    /**
     * Overrides the equals() method of the Project class. Usually we only always compare Project by their due dates in this program.
     * So we compare it based on their due dates.
     *
     * @param obj: the other instance to compare our project instance with
     * @return a boolean value that if equal, we return true, else false.
     */
    @Override
    public boolean equals(Object obj) {
        // check if we are comparing our instance to itself
        if (obj == this) {
            return true;
        }
        // check if we are comparing the instances of the same class
        if (obj instanceof Project) {
            // then return the appropriate boolean depending on whether or not they have the same due dates
            return this.getProjectDue() == ((Project) obj).getProjectDue();
        }
        // if none of the comparisons match above, we return as not equal by default
        return false;

    }
}