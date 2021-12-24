
import java.util.ArrayList;

public class Student {

    private String name;
    private String surname;
    private StudentId studentId;
    private int registrationOrder;
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
        currentCourses = new ArrayList<>();
        studentId = new StudentId(currentYear, registrationOrder);
        this.registrationSystem = registrationSystem;
        transcript = new Transcript(this);
        schedule = new Schedule(this);
    }

    /**Takes a Student and an ElectiveType and returns the
     * number of past elective courses according to student's
     * current semester number*/
    protected int getNumOfPastElectives(ArrayList<Integer> semesterNums) {
        int count = 0;

        for (Integer i: semesterNums) {
            if (i < getSemesterNumber()) {
                count++;
            }
        }
        return count;
    }



    public int getSemesterNumber() {
        int semesterNumber = 0;
        switch (registrationSystem.getSemester()) {
            case FALL:
                semesterNumber = currentYear * 2 - 1; break;
            case SPRING:
                semesterNumber = currentYear * 2; break;
            default:
                System.out.println("Incorrect Semester for registration System!!");
                System.exit(-1);
        }
        return semesterNumber;
    }

    public int getCompletedCredits() {
        return transcript.getCompletedCredits();
    }

    public void addToCurrentCourses(CourseSection courseSection) {
        schedule.addToProgram(courseSection);
        getCurrentCourses().add(courseSection.getCourse());
    }


    private void requestCourse(CourseSection courseSection) {
         advisor.approveCourse(this, courseSection);
    }

    public boolean hasPassedCourse(Course course) {
        return transcript.hasPassedCourse(course);
    }

    public void requestCourses() {
        ArrayList<CourseSection> offeredCourses = registrationSystem.getOfferedCourseSections(this);
        setBuffer("\n\nOffered Courses: \n");
        for (CourseSection c : offeredCourses) {
            setBuffer(c.getCourseSectionCode() + c.getCourse().toString() +  " ");
        }
        setBuffer("\n");
        setBuffer("(" + registrationSystem.getNontechElectiveCourses().get(0).offeredElectiveCount(this) + " NTE), ");
        setBuffer("(" + registrationSystem.getTechElectiveCourses().get(0).offeredElectiveCount(this) + " TE), ");
        setBuffer("(" + registrationSystem.getFacultyElectiveCourses().get(0).offeredElectiveCount(this) + " FTE), ");
        setBuffer("\n");

        for (CourseSection c: offeredCourses) {
            requestCourse(c);
        }

        setBuffer("\n\nCurrent Courses: \n");
        for (Course c: currentCourses) {
            setBuffer(c.getCourseCode() + c.toString() + " ");
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


    public String getSurname() {
        return surname;
    }

    public String getFullName() {
        return getName() + " " + getSurname();
    }



    public int getRegistrationOrder() {
        return registrationOrder;
    }


    public String getStudentId() {
        return studentId.getStudentId();
    }



    public ArrayList<Course> getCurrentCourses() {
        return currentCourses;
    }


    public int getCurrentYear() {
        return currentYear;
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

}
