import java.util.ArrayList;

public class FinalProjectMandatoryCourse extends MandatoryCourse {

    private int requiredCredits;
    private int creditStats;

    public FinalProjectMandatoryCourse(String courseCode, float semester, int quota, int credits,
                                       int theoretical, int practical, ArrayList<Course> preRequisites, int requiredCredits) {
        super (courseCode, semester, quota, credits, theoretical, practical, preRequisites);
        this.requiredCredits = requiredCredits;
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        //Returns true if student completed the prerequisite course and has enough credits to take this course
        return super.isElligiblePastCourse(student) && student.getCompletedCredits() >= requiredCredits;

    }


    @Override
    public boolean onRequested(Student student) {
        if (!checkReqCredits(student)){
            student.getExecutionTrace().append("\nThe system didn't allow " + toString() +
                     " because Student completed credits is less than " + requiredCredits +  "-> (" +
                     student.getTranscript().getCompletedCredits() + ")");
            setCreditStats();
            return false;
        }

        return super.onRequested(student);
    }

    public int getRequiredCredits() {
        return requiredCredits;
    }

    private boolean checkReqCredits(Student student) {
        return student.getCompletedCredits() >= requiredCredits;
    }

    public void setRequiredCredits(int requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public int getCreditStats() {
        return creditStats;
    }

    public void setCreditStats() {
        creditStats++;
    }

    public String toString() {
        return super.toString() + "(Final Project)";
    }
}
