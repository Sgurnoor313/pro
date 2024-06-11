package cherryrockstudios.listo.comparators;

import cherryrockstudios.listo.unit.Project;

import java.util.Comparator;

public class FormatTypeComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        String p1_format = String.valueOf(p1.getProjectFormat().getformat());
        String p2_format = String.valueOf(p2.getProjectFormat().getformat());
        return p1_format.compareTo(p2_format);
    }
}
