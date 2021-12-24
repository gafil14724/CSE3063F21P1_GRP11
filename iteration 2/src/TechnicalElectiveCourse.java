import java.util.ArrayList;

public class TechnicalElectiveCourse extends ElectiveCourse{

    private int requiredCredits;
    private Course preRequisite;

    public TechnicalElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                                   int practical, ArrayList<Integer> semesters, int requiredCredits, Course preRequisite) {
        super(courseCode, quota, credits, theoretical, practical, semesters);
        this.requiredCredits = requiredCredits;
        this.preRequisite = preRequisite;
        super.getRegistrationSystem().getCourseSections().add(new CourseSection(this)); //Add new courseSection based on this course to RegSystem

    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        return student.hasPassedCourse(this.getPreRequisite()) && checkCreditCondition(student);
    }



    public boolean checkCreditCondition(Student student) {
         return student.getCompletedCredits() >= requiredCredits;
    }

    public int getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(int requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public Course getPreRequisite() {
        return preRequisite;
    }

    public void setPreRequisite(Course preRequisite) {
        this.preRequisite = preRequisite;
    }

    public String toString() {
        return "(TE)";
    }
}
