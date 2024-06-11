package cherryrockstudios.listo.util;

import cherryrockstudios.listo.enums.FormatType;
import cherryrockstudios.listo.enums.StatusType;
import cherryrockstudios.listo.unit.Listo;
import cherryrockstudios.listo.unit.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Class to assist reading the Listo file.
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public class Reader {

    /**
     * Loads a listo file from an input file provided on command line arguments
     *
     * @param inputFile: file to load data from
     * @return a Listo instance extracted from the file.
     */
    public static Listo loadFile(File inputFile) {
        // initialize Listo
        Listo loadedListo = null;

        // read file
        try (FileReader fileReader = new FileReader(inputFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader);) {

            // load Listo Name on the first line
            String name = bufferedReader.readLine();

            // create Listo
            loadedListo = new Listo(name);

            // load Listo Author on the second line
            String author = bufferedReader.readLine();
            loadedListo.setListoUserName(author);

            // load Listo Date Created on the third line
            LocalDate date = LocalDate.parse(bufferedReader.readLine());
            loadedListo.setDateCreated(date);

            // load Listo Description on the fourth line
            String description = bufferedReader.readLine();
            loadedListo.setListoDescription(description);

            // start reading the loaded file
            // take COURSES MARKER
            String courseMarker = bufferedReader.readLine();

            // loop until the project marker
            String line = bufferedReader.readLine();

            while (!(line.equals("#$!?-projects-?!$#"))) {
                // extract project details
                loadedListo.addCourse(line);

                line = bufferedReader.readLine();
            }

            // loop around the project list
            // this should the first instance of the Project class
            line = bufferedReader.readLine();

            while (!(line.equals("#$!?-count-?!$#"))) {
                // extract project details
                String[] projectDetails = line.split(",");

                // extract each data
                int projId = Integer.parseInt(projectDetails[0]);
                String projCourse = projectDetails[1];
                String projName = projectDetails[2];
                String projDate = projectDetails[3];
                // if LocalDate is null, then parse as null
                LocalDate projDue;
                if (projDate.equals("null")) {
                    projDue = null;
                } else {
                    projDue = LocalDate.parse(projectDetails[3]);
                }

                // if status is CANCELLED
                StatusType projStatus;
                if (projectDetails[4].equals("CANCELLED")) {
                    projStatus = StatusType.getStatusType('E');
                } else {
                    projStatus = StatusType.getStatusType(projectDetails[4].charAt(0));
                }

                FormatType projFormat = FormatType.getFormatType(projectDetails[5].charAt(0));
                Double projGrade = Double.parseDouble(projectDetails[6]);
                String projDesc = projectDetails[7];

                // make an instance of the Project
                Project projectInstance = new Project(projId, projName, projDue, projGrade, projDesc, projCourse, projStatus, projFormat);

                // add each project to Listo
                loadedListo.addProject(projectInstance);

                // read another line
                line = bufferedReader.readLine();
            }

            // read count
            int listoCount = Integer.parseInt(bufferedReader.readLine());
            Project.updateCount(listoCount);

        } catch (IOException e) {
            // exception to be ignored
        }

        return loadedListo;
    }
}
