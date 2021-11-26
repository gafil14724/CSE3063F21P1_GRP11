import java.util.ArrayList;

public class CourseSection {

    private Course course;
    private boolean full;
    private int sectionHour;
    ArrayList<Student> students;
    private boolean[][] courseProgram;

    public CourseSection(Course course) {
        this.course = course;
        setSectionHour();
        courseProgram = new boolean[Schedule.DAYS][Schedule.HOURS];
        students = new ArrayList<>();
        setCourseProgram();
    }

    /**Sets the courseProgram by adding all the lecture
     * hours to a random day and hour*/
    public void setCourseProgram() {
        for (int i = 0; i < sectionHour; i++) {
            int randomDay = (int)(Math.random() * Schedule.DAYS);
            int randomHour = (int)(Math.random() * Schedule.HOURS);

            if (!courseProgram[randomDay][randomHour]) {
                courseProgram[randomDay][randomHour] = true;
            }else {
                i--;
            }
        }
    }

    public void addStudent(Student student) {
        if (!isFull()) {
            students.add(student);
            student.addToCurrentCourses(this);
            if (getQuota() == students.size()) { // Set the full true if after the addition, course section is full
                setFull(true);
            }
        }else {
            System.out.println("Quota is full for course section " + getCourse().getCourseCode());
        }

    }

    public int getQuota() {
        return course.getQuota();
    }

    public boolean isFull() {
        return students.size() == getQuota();
    }

    public Course getCourse() {
        return course;
    }

    public void setFull(boolean full) {
        this.full = full;
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


    public boolean[][] getCourseProgram() {
        return courseProgram;
    }

    public void setCourseProgram(boolean[][] courseProgram) {
        this.courseProgram = courseProgram;
    }
}
