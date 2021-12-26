public class Advisor {

    private String firstName;
    private String lastName;


    public Advisor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void approveCourseSection(Student student, CourseSection courseSection) {
        courseSection.getCourse().onRequested(student);
    }


    public String getFirstName () {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

