
import java.util.ArrayList;

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
    private RegistrationSystem registrationSystem;
    private String buffer = "";

    public Student(String name, String surname, int currentYear, int registrationOrder, RegistrationSystem registrationSystem) {
        this.name = name;
        this.surname = surname;
        this.currentYear = currentYear;
        this.registrationOrder = registrationOrder;
        grades = new ArrayList<>();
        currentCourses = new ArrayList<>();
        studentId = new StudentId(currentYear, registrationOrder);
        this.registrationSystem = registrationSystem;
        transcript = new Transcript(this);
        schedule = new Schedule(this);
    }

    public ArrayList<Course> getPassedCourses() {
        ArrayList<Course> passedCourses = new ArrayList<>();
        for (Grade g : grades) {
            passedCourses.add(g.getCourse());
        }
        return passedCourses;
    }

    /**Takes a course as argument and checks if student
     * has passed that course by iterating over student's
     * grades*/
    public boolean hasPassedCourse(Course course) {
        ArrayList<Course> passedCourses = getPassedCourses();

        for (Course c : passedCourses) {
           if (course.getCourseCode().equals(c.getCourseCode())) {
                return true;
            }
        }
        return false;
    }

    public void addToCurrentCourses(CourseSection courseSection) {
        schedule.addToProgram(courseSection);
        getCurrentCourses().add(courseSection.getCourse());
    }

    public void addPassedCourse(Course course) {
        int grade = (int) (Math.random() * 51) + 50; // random grade that is greater than 50
        grades.add(new Grade(course, grade));
        buffer += "\n" + course.getCourseCode() + ": " + grades.get(grades.size() - 1).getLetterGrade();
    }

    public void requestCourse(CourseSection courseSection) {
         advisor.approveCourse(this, courseSection);
    }


    public void requestCourses() {
        ArrayList<CourseSection> offeredCourses = registrationSystem.getOfferedCourses(this);
        setBuffer("\n\nOffered Courses: \n");
        for (CourseSection c : offeredCourses) {
            setBuffer(c.getCourseSectionCode() + " ");
        }
        setBuffer("\n");
        for (CourseSection c: offeredCourses) {
            requestCourse(c);
        }

        setBuffer("\n\nCurrent Courses: \n");
        for (Course c: currentCourses) {
            setBuffer(c.getCourseCode() + " ");
        }
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer += buffer;
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
