public class Course {

    private String courseCode;
    private String courseName;
    private boolean fall;
    private String courseType;
    private int quota;
    private int credits;
    private int[] sectionHours = new int[2]; // {Theoretical, Practical}
    private int year;
    private int requiredCredits;
    private Course preRequisite;

    public Course() {

    }

    public Course(String courseCode, String courseName, String semester, String courseType, int quota,
                   int credits, int theoretical, int practical, int year, int requiredCredits, Course preRequisite) {

        this.courseCode = courseCode;
        this.courseName = courseName;
        setFall(semester);
        this.courseType = courseType;
        this.quota = quota;
        this.credits = credits;
        setSectionHours(theoretical, practical);
        this.year = year;
        this.requiredCredits = requiredCredits;
        this.preRequisite = preRequisite;

    }

    public int getSectionHours() { //Returns the total section hours by summing theoretical and practical hours
        return sectionHours[0] + sectionHours[1];
    }

    public void setSectionHours(int theoretical, int practical) {
        this.sectionHours = new int[]{theoretical, practical};
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

    public Course getPreRequisite() {
        return preRequisite;
    }

    public void setPreRequisite(Course preRequisite) {
        this.preRequisite = preRequisite;
    }

    public void setFall(boolean fall) {
        this.fall = fall;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public void setSectionHours(int[] sectionHours) {
        this.sectionHours = sectionHours;
    }

    public int getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(int requiredCredits) {
        this.requiredCredits = requiredCredits;
    }
}
