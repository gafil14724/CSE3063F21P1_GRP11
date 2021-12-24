public class NormalMandatoryCourse extends MandatoryCourse{


    public NormalMandatoryCourse(String courseCode, float semester, int quota, int credits,
                                 int theoretical, int practical, Course preRequisite) {

        super(courseCode, semester, quota, credits, theoretical, practical, preRequisite);
    }

    @Override
    public void rejectBehaviour(Student student) {
        if (!student.hasPassedCourse(getPreRequisite())) {
            student.setBuffer("\nThe system didn't allow " + getCourseCode() +
                    " because student failed prerequisite -> " + getPreRequisite().getCourseCode());
        }
    }
}
