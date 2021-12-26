import java.util.ArrayList;
import java.util.Collections;

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
    public void whenRejectedForQuota(Student student) {
        if (getRegistrationSystem().isThereEmptyFacTechSection()) {
            student.getAdvisor().approveCourseSection(student, getRandomElective().getCourseSection());
            return;
        }

        ArrayList<FacultyTechnicalElectiveCourse> facTechCourses = new ArrayList<>(getRegistrationSystem().
                getFacultyElectiveCourses());

        facTechCourses.remove(this);//Remove this object from the list
        Collections.shuffle(facTechCourses);// shuffle the list
        for (Course c: facTechCourses) { //For each course, request one by one
            student.getAdvisor().approveCourseSection(student, c.getCourseSection());
        }
    }

    @Override
    public Course getRandomElective() {
        ArrayList<FacultyTechnicalElectiveCourse> electiveCourses = new ArrayList<>(getRegistrationSystem().
                getFacultyElectiveCourses());
        electiveCourses.remove(this);
        int index = (int) (Math.random() * electiveCourses.size());
        return electiveCourses.get(index);
    }

    public String toString() {
        return super.toString() + "(FTE)";
    }
}
