public abstract class Course {

    private String courseCode;
    private int quota;
    private int credits;
    private int theoretical;
    private int practical;
    private Course preRequisite;
    private RegistrationSystem registrationSystem;


    protected Course(String courseCode, int quota,
                   int credits, int theoretical, int practical, Course preRequisite) {

        this.courseCode = courseCode;
        this.quota = quota;
        this.credits = credits;
        this.theoretical = theoretical;
        this.practical = practical;
        this.preRequisite = preRequisite;
        registrationSystem = RegistrationSystem.getInstance(); // Singleton Controller class

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

    public Course getPreRequisite() {
        return preRequisite;
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
}
