import java.util.ArrayList;
import java.util.List;

public class Student {

    private String name;
    private String surname;
    private StudentId studentId;
    private int registrationOrder;
    private ArrayList<Grade> grades;
    private ArrayList<Course> currentCourses;
    private int currentYear;
    private Advisor advisor;
    private Schedule schedule;
    private Transcript transcript;

    public Student(String name, String surname, int currentYear, int registrationOrder) {
        this.name = name;
        this.surname = surname;
        this.currentYear = currentYear;
        this.registrationOrder = registrationOrder;
        studentId = new StudentId(this);
    }

    public ArrayList<Course> getPassedCourses() {
        ArrayList<Course> passedCourses = new ArrayList<>();
        for (Grade g : grades) {
            passedCourses.add(g.getCourse());
        }
        return passedCourses;
    }

    public void addPassedCourse(Course course, int grade) {
        grades.add(new Grade(course, grade));
    }

    public void requestCourse(CourseSection courseSection) {
        // Advisor.approveCourse(courseSection, this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getRegistrationOrder() {
        return registrationOrder;
    }

    public void setRegistrationOrder(int registrationOrder) {
        this.registrationOrder = registrationOrder;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public void setStudentId(StudentId studentId) {
        this.studentId = studentId;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    public ArrayList<Course> getCurrentCourses() {
        return currentCourses;
    }

    public void setCurrentCourses(ArrayList<Course> currentCourses) {
        this.currentCourses = currentCourses;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }
}
