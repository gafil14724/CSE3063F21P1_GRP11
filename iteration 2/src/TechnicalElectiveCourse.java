import java.util.ArrayList;
import java.util.Collections;

public class TechnicalElectiveCourse extends ElectiveCourse{

    private int requiredCredits;
    private int creditStats;
    private Course preRequisite;
    private int preRequisiteStats;

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
    public void whenRejectedForQuota(Student student) {
        if (getRegistrationSystem().isThereEmptyTechSection()) {
            student.requestCourseSection(getRandomElective().getCourseSection());
            return;
        }
        ArrayList<TechnicalElectiveCourse> techCourses = getRegistrationSystem().getTechElectiveCourses();
        techCourses.remove(this);
        Collections.shuffle(techCourses);
        for (Course c: techCourses) {
            student.requestCourseSection(c.getCourseSection());
        }

      /*  ArrayList<TechnicalElectiveCourse> randElectives = new ArrayList<>();
        for (TechnicalElectiveCourse c:  getRegistrationSystem().getTechElectiveCourses()) {
            if (c != this) {
                randElectives.add(c);
            }
        }
        if (randElectives.size() == 0 ) {
            return null;
        }
        int index = (int) (Math.random() * randElectives.size());
        return randElectives.get(index);*/
    }

    @Override
    public Course getRandomElective() {
        ArrayList<TechnicalElectiveCourse> electiveCourses = getRegistrationSystem().getTechElectiveCourses();
        int index = (int) (Math.random() * electiveCourses.size());
        return electiveCourses.get(index);
    }



    @Override
    public boolean onRequested(Student student) {

        if (!checkCreditCondition(student)){
            student.getExecutionTrace().append("\nThe system didn't allow " + toString() +
                    " because Student completed credits is less than " + requiredCredits +
                    " -> (" + student.getTranscript().getCompletedCredits() + ")");
            setCreditStats();
            return false;
        }
        else if (!student.hasPassedCourse(preRequisite)) {
            student.getExecutionTrace().append("\nThe system didn't allow " + toString() +
                    " because student failed prerequisite -> " + getPreRequisite().toString());
            student.requestCourseSection(getRandomElective().getCourseSection());
            setPreRequisiteStats();
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

    public int getCreditStats() {
        return creditStats;
    }

    public void setCreditStats() {
        creditStats++;
    }

    public int getPreRequisiteStats() {
        return preRequisiteStats;
    }

    public void setPreRequisiteStats() {
        preRequisiteStats++;
    }

    public String toString() {
        return super.toString() + "(TE)";
    }
}
