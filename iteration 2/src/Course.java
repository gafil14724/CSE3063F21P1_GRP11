import java.util.ArrayList;

public abstract class Course {

    private String courseCode;
    private int quota;
    private int credits;
    private int theoretical;
    private int practical;
    private CourseSection courseSection;
    private RegistrationSystem registrationSystem;


    protected Course(String courseCode, int quota,
                   int credits, int theoretical, int practical) {

        this.courseCode = courseCode;
        this.quota = quota;
        this.credits = credits;
        this.theoretical = theoretical;
        this.practical = practical;
        registrationSystem = RegistrationSystem.getInstance(); // Singleton Controller class
    }

    public abstract boolean isElligiblePastCourse(Student student);
    public abstract boolean isOfferableForStudent(Student student);


    /**Checks for collision first when requested for all of the courses*/
    public boolean onRequested(Student student) {
        ArrayList<CourseSection> collidedSections = student.getSchedule().getCollidedHours(courseSection);
        if (student.getSchedule().isCollision(courseSection)) {
            student.getExecutionTrace().append("\nAdvisor didn't approve " + courseSection.getCourse().toString() +
                    " because of more than one hour collision with -> ");
            for (CourseSection c: collidedSections) {
                student.getExecutionTrace().append(c.getCourse().toString() + " ");
            }
            student.getExecutionTrace().append(" in schedule");
            return false; //return false if there is a problem
        }
        return true;
    }

    public int getSectionHours() { //Returns the total section hours by summing theoretical and practical hours
        return theoretical + practical;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getQuota() {
        return quota;
    }

    public int getCredits() {
        return credits;
    }

    public int getTheoretical() {
        return theoretical;
    }

    public int getPractical() {
        return practical;
    }

    public RegistrationSystem getRegistrationSystem() {
        return registrationSystem;
    }

    public CourseSection getCourseSection() {
        return courseSection;
    }


    public void setCourseSection(CourseSection courseSection) {
        this.courseSection = courseSection;
    }

    public String toString() {
        return courseCode;
    }
}
