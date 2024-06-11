package cherryrockstudios.listo.comparators;

import cherryrockstudios.listo.unit.Project;
import java.util.Comparator;

public class ProjectNameComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        return p1.getProjectName().compareTo(p2.getProjectName());
    }
}