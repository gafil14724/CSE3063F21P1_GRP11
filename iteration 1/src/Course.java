public class Course {

    private String courseCode;
    private String courseName;
    private boolean fall;
    private int quota;
    private int credits;
    private int year;
    private Course[] preRequisites;

    public Course(String courseCode, String courseName, String semester,
                  int quota, int credits, int year, Course[] preRequisites) {

        this.courseCode = courseCode;
        this.courseName = courseName;
        setFall(semester);
        this.quota = quota;
        this.credits = credits;
        this.year = year;
        this.preRequisites = preRequisites;

    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isFall() {
        return fall;
    }

    public void setFall(String semester) {
        semester = semester.toLowerCase();
        if (semester.equals("fall")) {
            fall = true;
        }else if (semester.equals("spring")) {
            fall = false;
        }else {
            System.out.println("Wrong semester!");
            System.exit(-1);
        }
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Course[] getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(Course[] preRequisites) {
        this.preRequisites = preRequisites;
    }
}
