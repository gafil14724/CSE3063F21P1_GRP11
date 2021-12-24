public class NormalMandatoryCourse extends MandatoryCourse{


    public NormalMandatoryCourse(String courseCode, float semester, int quota, int credits,
                                 int theoretical, int practical, Course preRequisite) {

        super(courseCode, semester, quota, credits, theoretical, practical, preRequisite);
        super.getRegistrationSystem().getCourseSections().add(new CourseSection(this)); //Add new courseSection based on this course to RegSystem
    }

    @Override
    public void rejectBehaviour(Student student) {
        student.setBuffer("\nThe system didn't allow " + getCourseCode() +
                " because student failed prerequisite -> " + getPreRequisite().getCourseCode());
    }
}
