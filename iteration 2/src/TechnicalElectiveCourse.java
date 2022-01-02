import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TechnicalElectiveCourse extends ElectiveCourse{

    private int requiredCredits;
    private int creditStats;
    private ArrayList<Course> preRequisites;
    private int preRequisiteStats;
    private Set<Student> unregisteredStudents = new HashSet<>();

    public TechnicalElectiveCourse(String courseCode, int quota, int credits, int theoretical, int practical,
                                   ArrayList<Integer> semesters, int requiredCredits, ArrayList<Course> preRequisites) {
        super(courseCode, quota, credits, theoretical, practical, semesters);
        this.requiredCredits = requiredCredits;
        this.preRequisites = preRequisites;
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        return student.getTranscript().hasPassedCourses(this.getPreRequisites()) && checkCreditCondition(student);
    }

    @Override
    public void whenRejectedForQuota(Student student) {
        if (getRegistrationSystem().isThereEmptyTechSection()) {
            student.getAdvisor().approveCourseSection(student, getRandomElective().getCourseSection());
            return;
        }
        ArrayList<TechnicalElectiveCourse> techCourses = new ArrayList<>(getRegistrationSystem().getTechElectiveCourses());
        techCourses.remove(this);
        Collections.shuffle(techCourses);
        for (Course c: techCourses) {
            student.getAdvisor().approveCourseSection(student, c.getCourseSection());
        }
    }

    @Override
    public Course getRandomElective() {
        ArrayList<TechnicalElectiveCourse> electiveCourses = new ArrayList<>(getRegistrationSystem().
                getTechElectiveCourses());
        electiveCourses.remove(this);
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
            unregisteredStudents.add(student);
            return false;
        }
        if (!student.getTranscript().hasPassedCourses(preRequisites)) {
            student.getExecutionTrace().append("\nThe system didn't allow " +  toString() +
                    " because student failed prerequisites -> " );
            for (Course c: preRequisites) {
                if (!student.getTranscript().hasPassedCourse(c)) {
                    student.getExecutionTrace().append(c.toString() + " ");
                }
            }
            setPreRequisiteStats();
                return false;
        }

        return super.onRequested(student);
    }



    public boolean checkCreditCondition(Student student) {
         return student.getTranscript().getCompletedCredits() >= requiredCredits;
    }

    public int getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(int requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public ArrayList<Course> getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(ArrayList<Course> preRequisites) {
        this.preRequisites = preRequisites;
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


    public Set<Student> getUnregisteredStudents() {
        return unregisteredStudents;
    }

    public void setUnregisteredStudents(Set<Student> unregisteredStudents) {
        this.unregisteredStudents = unregisteredStudents;
    }

    public String toString() {
        return super.toString() + "(TE)";
    }
}
