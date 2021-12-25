import java.util.ArrayList;

public class TechnicalElectiveCourse extends ElectiveCourse{

    private int requiredCredits;
    private Course preRequisite;

    public TechnicalElectiveCourse(String courseCode, int quota, int credits, int theoretical,
                                   int practical, ArrayList<Integer> semesters, int requiredCredits, Course preRequisite) {
        super(courseCode, quota, credits, theoretical, practical, semesters);
        this.requiredCredits = requiredCredits;
        this.preRequisite = preRequisite;
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        return student.hasPassedCourse(this.getPreRequisite()) && checkCreditCondition(student);
    }

    @Override
    public Course getRandomElective() {
        ArrayList<TechnicalElectiveCourse> techCourses = getRegistrationSystem().getTechElectiveCourses();
        int index = (int) (Math.random() * techCourses.size());
        return techCourses.get(index);
    }



    @Override
    public boolean onRequested(Student student) {

        if (!checkCreditCondition(student)){
            student.getExecutionTrace().append("\nThe system didn't allow " + toString() +
                    " because Student completed credits is less than " + requiredCredits +
                    " -> (" + student.getTranscript().getCompletedCredits() + ")");
            return false;
        }
        else if (!student.hasPassedCourse(preRequisite)) {
            student.getExecutionTrace().append("\nThe system didn't allow " + toString() +
                    " because student failed prerequisite -> " + getPreRequisite().toString());
            student.requestCourse(getRandomElective().getCourseSection());
            return false;
        }

        return super.onRequested(student);
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
        return super.toString() + "(TE)";
    }
}
