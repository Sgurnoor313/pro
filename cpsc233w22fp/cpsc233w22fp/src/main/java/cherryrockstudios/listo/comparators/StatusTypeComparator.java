package cherryrockstudios.listo.comparators;

import cherryrockstudios.listo.unit.Project;

import java.util.Comparator;

public class StatusTypeComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        String p1_status = String.valueOf(p1.getProjectStatus().getStatus());
        String p2_status = String.valueOf(p2.getProjectStatus().getStatus());
        return p1_status.compareTo(p2_status);
    }
}

