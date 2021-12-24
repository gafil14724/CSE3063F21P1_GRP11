public abstract class Course {

    private String courseCode;
    private int quota;
    private int credits;
    private int theoretical;
    private int practical;
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
    public abstract boolean isApprovableForStudent(Student student);

    public abstract void rejectBehaviour(Student student);


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

}
