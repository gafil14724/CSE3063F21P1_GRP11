import java.util.ArrayList;
import java.util.Collections;

public class NonTechnicalUniversityElectiveCourse extends ElectiveCourse{


    public NonTechnicalUniversityElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                                                int practical, ArrayList<Integer> semesters) {
        super (courseCode, quota, credits, theoretical, practical, semesters);
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        return true;
    }



    public String toString() {
        return "(NTE/UE)";
    }
}
