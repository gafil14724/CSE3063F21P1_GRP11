import java.util.ArrayList;

public class FacultyTechnicalElectiveCourse extends ElectiveCourse{

    public FacultyTechnicalElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                                          int practical, ArrayList<Integer> semesters) {
        super (courseCode, quota, credits, theoretical, practical, semesters);
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {//It is always elligible
        return true;
    }

    @Override
    public Course getRandomElective() {
        ArrayList<FacultyTechnicalElectiveCourse> facTechCourses = getRegistrationSystem().getFacultyElectiveCourses();
        int index = (int) (Math.random() * facTechCourses.size());
        return facTechCourses.get(index);
    }

    public String toString() {
        return "(FTE)";
    }
}
