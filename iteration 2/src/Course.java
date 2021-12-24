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

    public  boolean isApprovableForStudent(Student student) {
        return (!student.getAdvisor().checkCollision(student, courseSection));
    }

    public void rejectBehaviour(Student student) {
        if (student.getSchedule().isCollision(courseSection)) {

            courseSection.setCollisionStatistics(courseSection.getCollisionStatistics()+1);
        }
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
