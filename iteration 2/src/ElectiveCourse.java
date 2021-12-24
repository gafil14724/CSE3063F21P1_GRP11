import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class ElectiveCourse extends Course {


    private ArrayList<Integer> semesters; //Elective courses have more than one semester

    public ElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                          int practical, ArrayList<Integer> semesters) {

        super(courseCode, quota, credits, theoretical, practical);
        this.semesters = semesters;
    }

    @Override
    public boolean isOfferableForStudent(Student student) {
        return false;
    }

    @Override
    public boolean isApprovableForStudent(Student student) {
        return false;
    }

    @Override
    public void rejectBehaviour(Student student) {
        return;
    }

    public int offeredElectiveCount(Student student) {
        int stuSemester = student.getSemesterNumber();
        return Collections.frequency(getSemesters(), stuSemester);
    }


    public ArrayList<Integer> getSemesters() {
        return semesters;
    }

    public void setSemesters(ArrayList<Integer> semesters) {
        this.semesters = semesters;
    }
}
