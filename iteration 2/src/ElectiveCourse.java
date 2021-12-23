import java.util.ArrayList;
import java.util.Arrays;

public class ElectiveCourse extends Course {

    private ElectiveType electiveType;
    private ArrayList<Integer> semesters;

    public ElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                          int practical, Course preRequisite, String electiveType, ArrayList<Integer> semesters) {

        super(courseCode, quota, credits, theoretical, practical, preRequisite);
        setElectiveType(electiveType);
        this.semesters = semesters;
        super.getRegistrationSystem().getCourseSections().add(new CourseSection(this)); //Add new courseSection based on this course to RegSystem
    }

    public void setElectiveType(String electiveType) {
        switch (electiveType.toLowerCase()) {
            case "nontechnical": this.electiveType = ElectiveType.NONTECHNICAL; break;
            case "technical": this.electiveType = ElectiveType.TECHNICAL; break;
            case "faculty": this.electiveType = ElectiveType.FACULTY; break;
            default:
                System.out.println("Incorrect Elective Type For Elective Course!!!");
                System.exit(-1);
        }
    }

    public ElectiveType getElectiveType() {
        return electiveType;
    }

    public ArrayList<Integer> getSemesters() {
        return semesters;
    }
}
