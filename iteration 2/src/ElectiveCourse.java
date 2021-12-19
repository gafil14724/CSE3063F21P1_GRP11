import java.util.ArrayList;
import java.util.Arrays;

public class ElectiveCourse extends Course {

    private ElectiveType electiveType;

    public ElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                          int practical, Course preRequisite, String electiveType) {

        super(courseCode, quota, credits, theoretical, practical, preRequisite);
        setElectiveType(electiveType);
    }

    public void setElectiveType(String electiveType) {
        switch (electiveType.toLowerCase()) {
            case "nontechnical": this.electiveType = ElectiveType.NONTECHNICAL; break;
            case "technical": this.electiveType = ElectiveType.TECHNICAL; break;
            case "faculty": this.electiveType = ElectiveType.FACULTY; break;
            case "university": this.electiveType = ElectiveType.UNIVERSITY; break;
            default:
                System.out.println("Incorrect Elective Type!!!");
                System.exit(-1);
        }
    }
}
