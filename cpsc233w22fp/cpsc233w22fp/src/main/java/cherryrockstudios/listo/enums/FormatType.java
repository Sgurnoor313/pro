package cherryrockstudios.listo.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class but it will act as an enumeration for the format types.
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public enum FormatType {
    // multiple formats of projects
    NO_FORMAT('N'), MIDTERM('M'), ASSIGNMENT('A'), QUIZ('Q'), DISCUSSION('D'), GROUP_PROJECT('G'), LAB_TASK('L'), FINAL_EXAM('F');

    /**
     * Format list for prompting purposes
     */
    public static final ArrayList<String> formatTypeList = new ArrayList<String>(List.of("MIDTERM", "ASSIGNMENT", "QUIZ", "DISCUSSION", "GROUP PROJECT", "LAB TASK", "FINAL EXAM"));

    /**
     * Each format has its own shortcut key
     */
    private final char format;

    /**
     * Constructor enum to store the formats
     *
     * @param format: the format to be stored
     */
    FormatType(char format) {
        this.format = format;
    }

    /**
     * A helper method to look up a format type using its char symbol
     *
     * @param type: the char symbol 'index'
     * @return the Format Type enum state
     */
    public static FormatType getFormatType(char type) {
        if (type == 'N') {
            return NO_FORMAT;
        } else if (type == 'M') {
            return MIDTERM;
        } else if (type == 'A') {
            return ASSIGNMENT;
        } else if (type == 'Q') {
            return QUIZ;
        } else if (type == 'D') {
            return DISCUSSION;
        } else if (type == 'G') {
            return GROUP_PROJECT;
        } else if (type == 'L') {
            return LAB_TASK;
        } else if (type == 'F') {
            return FINAL_EXAM;
        } else {
            throw new IllegalArgumentException("Format type " + type + " is not valid!");
        }
    }

    /**
     * The symbol for the menu access purposes
     *
     * @return a char of the format type
     */
    public char getformat(){
        return format;
    }


}
