package cherryrockstudios.listo.comparators;

import cherryrockstudios.listo.unit.Project;

import java.util.Comparator;

public class DueDateComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        return p1.getProjectDue().compareTo(p2.getProjectDue());
    }
}
