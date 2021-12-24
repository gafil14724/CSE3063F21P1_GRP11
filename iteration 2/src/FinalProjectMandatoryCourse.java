public class FinalProjectMandatoryCourse extends MandatoryCourse {

    private int requiredCredits;

    public FinalProjectMandatoryCourse(String courseCode, float semester, int quota, int credits,
                                       int theoretical, int practical, Course preRequisite, int requiredCredits) {
        super (courseCode, semester, quota, credits, theoretical, practical, preRequisite);
        this.requiredCredits = requiredCredits;
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        //Returns true if student completed the prerequisite course and has enough credits to take this course
        return super.isElligiblePastCourse(student) && student.getCompletedCredits() >= requiredCredits;

    }


    @Override
    public boolean isApprovableForStudent(Student student) {
        return super.isApprovableForStudent(student) && student.getCompletedCredits() >= requiredCredits;
    }

    @Override
    public void rejectBehaviour(Student student) {
        if (!student.hasPassedCourse(getPreRequisite())) {
            student.setBuffer("\nThe system didn't allow " + getCourseCode() + toString() +
                    " because student failed prerequisite -> " + getPreRequisite().getCourseCode());
        }else {
            student.setBuffer("\nThe system didn't allow " + getCourseCode() + toString() +
            " because Student completed credits is less than 165 -> (" + student.getTranscript().getCompletedCredits() + ")");
        }
    }

    public int getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(int requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public String toString() {
        return "(Final Project)";
    }
}
