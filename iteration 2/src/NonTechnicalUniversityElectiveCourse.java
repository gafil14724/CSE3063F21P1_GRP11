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
    public void whenRejectedForQuota(Student student) {
        if (getRegistrationSystem().isThereEmptyTechSection()) {
            student.getAdvisor().approveCourseSection(student,getRandomElective().getCourseSection());
            return;
        }

        ArrayList<NonTechnicalUniversityElectiveCourse> nonTechCourses = new ArrayList<>(getRegistrationSystem().
                getNontechElectiveCourses());
        nonTechCourses.remove(this);//Remove this object from the list
        Collections.shuffle(nonTechCourses);// shuffle the list
        for (Course c: nonTechCourses) { //For each course, request one by one
            student.getAdvisor().approveCourseSection(student, c.getCourseSection());
        }
    }

    @Override
    public Course getRandomElective() {
        ArrayList<NonTechnicalUniversityElectiveCourse> electiveCourses = new ArrayList<>(getRegistrationSystem().
                getNontechElectiveCourses());
        electiveCourses.remove(this);
        int index = (int) (Math.random() * electiveCourses.size());
        return electiveCourses.get(index);
    }


    public String toString() {
        return super.toString() + "(NTE/UE)";
    }
}
