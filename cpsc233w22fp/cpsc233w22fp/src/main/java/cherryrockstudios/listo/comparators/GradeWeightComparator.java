package cherryrockstudios.listo.comparators;

import cherryrockstudios.listo.unit.Project;

import java.util.Comparator;

public class GradeWeightComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        if (p1.getGradeWeight().equals(p2.getGradeWeight())) {
            return 0;
        } else if (p1.getGradeWeight() > p2.getGradeWeight()) {
            return 1;
        } else {
            return -1;
        }
    }
}
