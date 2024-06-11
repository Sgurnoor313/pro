package cherryrockstudios.listo.unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Each user has a Listo, which is basically just a set of Projects.
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public class Listo {

    /**
     * The name of a Listo
     */
    private String listoName;

    /**
     * The date when a Listo was created
     */
    private LocalDate dateCreated;

    /**
     * The name of the person who owns or authored this Listo
     */
    private String listoUserName;

    /**
     * Brief description of a Listo
     */
    private String listoDescription;

    /**
     * Listo is a datatype that stores a list of projects.
     * Therefore, it needs a list that stores all the projects contained in it.
     */
    private final ArrayList<Project> listoProjects;

    /**
     * Each listo has its own set of courses. This is where it is all stored.
     */
    private final ArrayList<String> courseList;

    /**
     * A constructor for the Listo type. This creates an instance of the Listo class.
     *
     * @param name: the name of the Listo.
     */
    public Listo(String name) {
        this.listoName = name;
        this.dateCreated = LocalDate.now();
        this.listoUserName = "Unspecified Author";
        this.listoDescription = "No Description Provided";
        this.listoProjects = new ArrayList<Project>();
        this.courseList = new ArrayList<String>();
    }


    // Mutator Methods
    /**
     * Update the name of the Listo.
     *
     * @param name: the name of a Listo
     */
    public void setListoName(String name) {
        this.listoName = name;
    }

    /**
     * Set or update the name of the person who owns or authored the Listo
     *
     * @param username: The name of the person who owns or authored this Listo
     */
    public void setListoUserName(String username) {
        this.listoUserName = username;
    }

    /**
     * Set or update the brief description of the Listo
     *
     * @param listoDescription: a brief description of a Listo
     */
    public void setListoDescription(String listoDescription) {
        this.listoDescription = listoDescription;
    }

    /**
     * Loads the date created. (From Reader.loadFile())
     *
     * @param date: the date when the Listo was created
     */
    public void setDateCreated(LocalDate date) {
        this.dateCreated = date;
    }

    /**
     * Add a project to the Listo's project list.
     *
     * @param project: a project to be added to the project list.
     */
    public void addProject(Project project) {
        this.listoProjects.add(project);
    }

    /**
     * Deletes a projects from the Listo's project list.
     *
     * @param project: a project to be deleted (removed) in the project list.
     */
    public void deleteProject(Project project) {
        this.listoProjects.remove(project);
    }

    /**
     * Returns a copy of the Listo's project list for display purposes.
     *
     * @return an ArrayList<Project> copy of the Listo's project list.
     */
    public ArrayList<Project> getProjectList() {
        return new ArrayList<Project>(this.listoProjects);
    }

    /**
     * Adds a course to the Listo's course list
     *
     * @param course: the name of the course
     */
    public void addCourse(String course) {
        this.courseList.add(course);
    }

    /**
     * Removes a course from the Listo's course list
     *
     * @param course: the name of the course
     */
    public void deleteCourse(String course) {
        this.courseList.remove(course);

        // make sure it's also removed for all projects containing it.
        for (Project project: listoProjects) {
            if (project.getProjectCourse().equals(course)) {
                project.setProjectCourse("Deleted");
            }
        }
    }


    // Accessor Methods
    /**
     * A getter method for the Listo's name.
     *
     * @return the name of the Listo
     */
    public String getListoName() {
        return listoName;
    }

    /**
     * A getter method for the date created of the Listo.
     *
     * @return a LocalDate type date of when the Listo was created.
     */
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    /**
     * A getter method for the Listo's author or owner.
     *
     * @return the name of the person who owns or authored this Listo.
     */
    public String getListoUserName() {
        return listoUserName;
    }

    /**
     * A getter method for the Listo's description.
     *
     * @return a brief description of the Listo.
     */
    public String getListoDescription() {
        return listoDescription;
    }

    /**
     * A getter method for the list of courses of Listo.
     *
     * @return a list of courses in a Listo
     */
    public ArrayList<String> getCourseList() {
        return courseList;
    }

    /**
     * Accesses a project stored in the Listo by an index
     *
     * @param index: an index provided by Menu.promptForProject method to access the Listo
     * @return a Project instance
     */
    public Project getProject(int index) {
        return listoProjects.get(index);
    }

    /**
     * Loops through each project in the Listo's project list, then extracts their names to be returned
     * in an ArrayList<String>
     *
     * @return an ArrayList<String> value that contains all the names of the projects in the Listo.
     */
    public ArrayList<String> getProjectNames() {
        ArrayList<String> namesList = new ArrayList<String>();
        for (Project project: this.listoProjects) {
            namesList.add(project.getProjectName());
        }
        return namesList;
    }

    /**
     * Loops through the project list to get a project by its project ID.
     * Returns null if project can't be found
     *
     * @param projectID: the ID of the project we want to access
     * @return the Project to be acessed by its ID, nul if no project found
     */
    public Project getProjectByID(int projectID) {
        for (Project project: this.listoProjects) {
            if (project.getProjectID() == projectID) {
                return project;
            }
        }
        return null;
    }

    // Helper Methods
    /**
     * Overrides the toString() method to return as a format readable to the reader
     *
     * @return the Listo's information separated by tab
     */
    @Override
    public String toString() {
        // info about Listo
        String formatString = "Listo Name: "+listoName+"\t Author: "+listoUserName+"\t Created On: "+dateCreated+"\t Description: "+listoDescription+"\n";
        // add header
        formatString += "------------------------------------------------------------------------------------------------\n";
        formatString += "ID\t| Course\t| Project Name\t| Due Date\t| Status\t| Format\t| Grade Weight\t| Description\n";
        formatString += "------------------------------------------------------------------------------------------------\n";
        // loop through each project's toString() method
        for (Project project: listoProjects) {
            formatString += project.toString()+"\n";
        }
        return formatString;
    }

    /**
     * Returns an organized way to display the Listo in the terminal
     *
     * @param isBasicView: true if you want to display the Basic Listo, otherwise Full Listo
     * @return a Listo String formatted in a way that is easy to view
     */
    public String viewListo(boolean isBasicView) {
        if (isBasicView) {
            // basic info about Listo
            String formatString = "Listo Name: "+listoName+"\t Author: "+listoUserName+"\t Created On: "+dateCreated+"\n";
            // add header
            formatString += "-------------------------------------------------------\n";
            formatString += "Course\t| Project Name\t| Due Date\t| Status\n";
            formatString += "-------------------------------------------------------\n";
            // loop through each project
            for (Project project: listoProjects) {
                formatString += project.getProjectCourse()+"\t| "+project.getProjectName()+"\t| "+project.getProjectDue()+"\t| "+project.getProjectStatus()+"\n";
            }
            return formatString;
        } else {
            return toString();
        }
    }

    /**
     * Returns a format of the listo that will be stored in the output file
     *
     * @return a comma-separated values about the Listo
     */
    public String saveFormat() {
        String saveString = listoName+"\n"+listoUserName+"\n"+dateCreated+"\n"+listoDescription+"\n";
        // mark to tell logger that we are now listing courses
        saveString += "#$!?-courses-?!$#\n";
        // loop through each course
        for (String course: courseList) {
            saveString += course+"\n";
        }
        // mark to tell logger that we are now listing projects
        saveString += "#$!?-projects-?!$#\n";
        // loop through each project
        for (Project project: listoProjects) {
            saveString += project.saveFormat()+"\n";
        }
        // mark to tell logger the count
        saveString += "#$!?-count-?!$#\n";
        // put project count information last
        saveString += Project.saveCount()+"\n";
        return saveString;
    }

}
