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

    /**Checks if the prerequisite condition is satisfied by the student.
     * Returns true if prerequisite condition is met*/
   /* private boolean checkPrerequisite(Student student, CourseSection courseSection) {
        if (courseSection.getCourse().getPreRequisite() == null) {
            return true;
        }

        return (student.hasPassedCourse(courseSection.getCourse().getPreRequisite()));

    }*/


    /**Checks if the student schedule has a collision with the requested course
     * Section by invoking the collision check method inside student's schedule.
     * Returns true if there is more than one hour collision*/
    public boolean checkCollision(Student student, CourseSection courseSection) {
        return student.getSchedule().isCollision(courseSection);
    }

    public String getFirstName () {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

