import java.util.ArrayList;

public class MandatoryCourse extends Course {

    private float semesterNumber;
    Semester semester;
    private int prerequisiteStats;
    private ArrayList<Course> preRequisites; //Every mandatory course has a prerequisite course

    public MandatoryCourse(String courseCode, float semester, int quota,
                  int credits, int theoretical, int practical, ArrayList<Course> preRequisites) {

        super(courseCode, quota, credits, theoretical, practical);
        setSemesterNumber(semester);
        this.preRequisites = preRequisites;
        setSemester();
        setCourseSection(new CourseSection(this));
    }

    @Override
    public boolean isElligiblePastCourse(Student student) {
        return student.getTranscript().hasPassedCourses(this.getPreRequisites()) &&
                student.getSemesterNumber() > this.getSemesterNumber();
    }

    @Override
    public boolean isOfferableForStudent(Student student) {
        return !student.hasPassedCourse(this) && (student.getSemesterNumber() == getSemesterNumber() || (student.getSemesterNumber() > getSemesterNumber() &&
                    getRegistrationSystem().getSemester() == getSemester()));
    }



    @Override
    public boolean onRequested(Student student) {
        if (!student.getTranscript().hasPassedCourses(preRequisites)) {
            student.getExecutionTrace().append("\nThe system didn't allow " +  toString() +
                    " because student failed prerequisites -> " );
            for (Course c: preRequisites) {
                if (!student.hasPassedCourse(c)) {
                    student.getExecutionTrace().append(c.toString() + " ");
                }
            }
            setPrerequisiteStats();
            return false;
        }else if (!super.onRequested(student)) {
            return false;
        }

        getCourseSection().addStudent(student);
        return true;

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

    public ArrayList<Course> getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(ArrayList<Course> preRequisites) {
        this.preRequisites = preRequisites;
    }

    public int getPrerequisiteStats() {
        return prerequisiteStats;
    }

    public void setPrerequisiteStats() {
        prerequisiteStats++;
    }


}
