import java.util.ArrayList;

public class FacultyTechnicalElectiveCourse extends ElectiveCourse{

    public FacultyTechnicalElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                                          int practical, ArrayList<Integer> semesters) {
        super (courseCode, quota, credits, theoretical, practical, semesters);
        super.getRegistrationSystem().getCourseSections().add(new CourseSection(this)); //Add new courseSection based on this course to RegSystem

    }

    @Override
    public boolean isElligiblePastCourse(Student student) {//It is always elligible
        return true;
    }

    public String toString() {
        return "(FTE)";
    }
}
