public class MandatoryCourse extends Course {

    private float semester;

    public MandatoryCourse(String courseCode, float semester, int quota,
                  int credits, int theoretical, int practical, Course preRequisite) {

        super(courseCode, quota, credits, theoretical, practical, preRequisite);
        setSemester(semester);
    }

    public void setSemester(float semester) {
        if (semester < 0 || semester > 8) {
            System.out.println("Incorrect semester for the mandatory course!!");
            System.exit(-1);
        }
        this.semester = semester;
    }

    public float getSemesterNumber() {
        return semester;
    }

    public Semester getSemester() {
        Semester semester;
        int multipliedSemester = (int) (this.semester * 10); //We multiply the semesters by 10 and cast to int to avoid float arithmetic errors

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

}
