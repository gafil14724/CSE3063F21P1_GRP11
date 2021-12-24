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

    @Override
    public Course getRandomElective() {
        ArrayList<NonTechnicalUniversityElectiveCourse> nonTechCourses = getRegistrationSystem().getNontechElectiveCourses();
        int index = (int) (Math.random() * nonTechCourses.size());
        return nonTechCourses.get(index);
    }


    public String toString() {
        return "(NTE/UE)";
    }
}
