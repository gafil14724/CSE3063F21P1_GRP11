public class Advisor {

    String firstName;
    String lastName;


    public Advisor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void approveCourse(Student student, CourseSection courseSection) {

    }

    private boolean checkQuota(CourseSection courseSection) {
        return true;
    }

    private boolean checkCredits(Student student, CourseSection courseSection) {
        return true;
    }

    private boolean checkCollision(Student student, CourseSection courseSection) {
        return true;
    }
}

