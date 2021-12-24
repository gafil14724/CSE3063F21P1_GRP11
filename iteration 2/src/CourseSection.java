import java.util.ArrayList;

public class CourseSection {

    private Course course;
    private RegistrationSystem registrationSystem;
    private boolean full;
    private int sectionHour;
    ArrayList<Student> students;
    private boolean[][] courseProgram;
    private int quotaStatistics;
    private int prerequisiteStatistics;
    private int collisionStatistics;

    public CourseSection(Course course) {
        this.course = course;
        setSectionHour();
        this.registrationSystem = RegistrationSystem.getInstance();
        students = new ArrayList<>();
        setFull();
        courseProgram = new boolean[Schedule.HOURS][Schedule.DAYS];
        setCourseProgram();
        registrationSystem.getCourseSections().add(this);
    }

    /**Sets the courseProgram by adding all the lecture
     * hours to a random day and hour*/
    public void setCourseProgram() {
        for (int i = 0; i < sectionHour; i++) {
            int randomHour = (int)(Math.random() * Schedule.HOURS);
            int randomDay = (int)(Math.random() * Schedule.DAYS);

            //If course program is empty for that hour and same semester course has no lectures in that hour
            if (!courseProgram[randomHour][randomDay] && !CollidesWithSameSemester(randomHour, randomDay)) {
                courseProgram[randomHour][randomDay] = true;
            }else {
                i--;
            }
        }
    }

    private boolean CollidesWithSameSemester(int randomHour, int randomDay) {
        if (!(course instanceof MandatoryCourse)) {
            return false;
        }

        ArrayList<CourseSection> mandatoryCourseSections = new ArrayList<>();
        for (CourseSection cs: registrationSystem.getCourseSections()) {
            if (cs.getCourse() instanceof MandatoryCourse) {
                mandatoryCourseSections.add(cs);
            }
        }

        for (CourseSection cs: mandatoryCourseSections) {
            if (((MandatoryCourse) course).getSemesterNumber() == ((MandatoryCourse)cs.getCourse()).getSemesterNumber() &&
                    cs.getCourseProgram()[randomHour][randomDay]) {
                return true;
            }
        }
        return false;
    }

    public void addStudent(Student student) {
        if (!full) {
            students.add(student);
            student.addToCurrentCourses(this);
            setFull();
        }else {
            student.setBuffer("\nThe system didn't allow " + getCourseSectionCode() + " because " +
                    "course section is full. ("  +  students.size() + ")");
            quotaStatistics++;
        }

    }

    public int getQuota() {
        return course.getQuota();
    }

    public boolean isFull() {
        return full;
    }

    public Course getCourse() {
        return course;
    }

    public String getCourseSectionCode() {
        return getCourse().getCourseCode();
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public void setFull() {
        full = students.size() == getQuota();
    }

    public int getCollisionStatistics() {
        return collisionStatistics;
    }

    public void setCollisionStatistics(int collisionStatistics) {
        this.collisionStatistics = collisionStatistics;
    }

    public int getSectionHour() {
        return sectionHour;
    }

    public void setSectionHour() {
        sectionHour = course.getSectionHours();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setCourse(Course course) {
        this.course = course;
    }



    public int getQuotaStatistics() {
        return quotaStatistics;
    }

    public void setQuotaStatistics(int quotaStatistics) {
        this.quotaStatistics = quotaStatistics;
    }

    public int getPrerequisiteStatistics() {
        return prerequisiteStatistics;
    }

    public void setPrerequisiteStatistics(int prerequisiteStatistics) {
        this.prerequisiteStatistics = prerequisiteStatistics;
    }

    public boolean[][] getCourseProgram() {
        return courseProgram;
    }


}
