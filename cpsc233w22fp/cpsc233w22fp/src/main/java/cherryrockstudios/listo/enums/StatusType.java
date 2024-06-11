package cherryrockstudios.listo.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of Status Types
 *
 * @author Gabriel Rodriguez
 * @author Sharoon Thabal
 * @version 1.0
 */
public enum StatusType {
    // available statuses
    ACTIVE('A'), COMPLETED('C'), IN_PROGRESS('I'), LATE('L'), CANCELLED('E'), POSTPONED('P');

    /**
     * Format list for prompting purposes
     */
    public static final ArrayList<String> statusTypeList = new ArrayList<String>(List.of("ACTIVE", "COMPLETED", "IN PROGRESS", "LATE", "CANCELLED", "POSTPONED"));

    /**
     * The status symbol used for each status typew
     */
    private final char status;

    /**
     * Constructor enum to store the status
     *
     * @param status: the status to be stored
     */
    StatusType(char status){
        this.status = status;
    }

    /**
     * A helper method to look up a status type using its char symbol
     *
     * @param type: the char symbol 'index'
     * @return the Status Type enum state
     */
    public static StatusType getStatusType(char type) {
        if (type == 'A') {
            return ACTIVE;
        } else if (type == 'C') {
            return COMPLETED;
        } else if (type == 'I') {
            return IN_PROGRESS;
        } else if (type == 'L') {
            return LATE;
        } else if (type == 'E') {
            return CANCELLED;
        } else if (type == 'P') {
            return POSTPONED;
        } else {
            throw new IllegalArgumentException("Status type " + type + " is not valid!");
        }
    }
    /**
     * The symbol for the menu access purposes
     *
     * @return a char of the format type
     */
    public char getStatus(){
        return status;
    }


}
