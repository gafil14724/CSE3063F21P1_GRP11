import java.util.ArrayList;

public class NonTechnicalUniversityElectiveCourse extends ElectiveCourse{


    public NonTechnicalUniversityElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                                                int practical, ArrayList<Integer> semesters) {
        super (courseCode, quota, credits, theoretical, practical, semesters);
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        return true;
    }

  /*  public void onRequested(Student student) {
        super.onRequested(student);

    }*/

    @Override
    public Course getRandomElective() {
        ArrayList<NonTechnicalUniversityElectiveCourse> nonTechCourses = getRegistrationSystem().getNontechElectiveCourses();
        int index = (int) (Math.random() * nonTechCourses.size());
        return nonTechCourses.get(index);
    }


    public String toString() {
        return super.toString() + "(NTE/UE)";
    }
}
