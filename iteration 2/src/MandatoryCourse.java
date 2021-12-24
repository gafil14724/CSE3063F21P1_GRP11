public abstract class MandatoryCourse extends Course {

    private float semesterNumber;
    Semester semester;
    private Course preRequisite; //Every mandatory course has a prerequisite course

    public MandatoryCourse(String courseCode, float semester, int quota,
                  int credits, int theoretical, int practical, Course preRequisite) {

        super(courseCode, quota, credits, theoretical, practical);
        setSemesterNumber(semester);
        this.preRequisite = preRequisite;
        setSemester();
        setCourseSection(new CourseSection(this));
        //super.getRegistrationSystem().getCourseSections().add(new CourseSection(this)); //Add new courseSection based on this course to RegSystem
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        return student.hasPassedCourse(this.getPreRequisite()) && student.getSemesterNumber() > this.getSemesterNumber();
    }

    @Override
    public boolean isOfferableForStudent(Student student) {
        return !student.hasPassedCourse(this) && (student.getSemesterNumber() == getSemesterNumber() || (student.getSemesterNumber() > getSemesterNumber() &&
                    getRegistrationSystem().getSemester() == getSemester()));
    }


    public boolean isApprovableForStudent(Student student) {
        return  super.isApprovableForStudent(student) && student.hasPassedCourse(this.preRequisite);
    }

    public void setSemesterNumber(float semesterNumber) {
        if (semesterNumber < 0 || semesterNumber > 8) {
            System.out.println("Incorrect semester for the mandatory course!!");
            System.exit(-1);
        }
        this.semesterNumber = semesterNumber;
    }

    public float getSemesterNumber() {
        return semesterNumber;
    }

    public Semester getSemester() {
        return semester;
    }

    public Semester setSemester() {
        int multipliedSemester = (int) (this.semesterNumber * 10); //We multiply the semesters by 10 and cast to int to avoid float arithmetic errors

        if (multipliedSemester % 10 == 5) { //If semester multiplied by 10 ends with 5, its semester is summer.
            semester = Semester.SUMMER;
        }
        else if ((multipliedSemester / 10) % 2 == 1) {
            semester = Semester.FALL;
        }
        else  {
            semester = Semester.SPRING;
        }

        return semester;
    }

    public Course getPreRequisite() {
        return preRequisite;
    }

    public void setPreRequisite(Course preRequisite) {
        this.preRequisite = preRequisite;
    }

    public String toString() {
        return "";
    }
}
