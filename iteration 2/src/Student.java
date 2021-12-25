
import java.util.ArrayList;

public class Student {

    private String name;
    private String surname;
    private StudentId studentId;
    private int registrationOrder;
    private int currentYear;
    private Advisor advisor;
    private Schedule schedule;
    private Transcript transcript;
    private RegistrationSystem registrationSystem;
    private StringBuilder executionTrace = new StringBuilder();

    public Student(String name, String surname, int currentYear, int registrationOrder, RegistrationSystem registrationSystem) {
        this.name = name;
        this.surname = surname;
        this.currentYear = currentYear;
        this.registrationOrder = registrationOrder;
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
        transcript.getCurrentCourses().add(courseSection.getCourse());
    }


    public void requestCourse(CourseSection courseSection) {
         advisor.approveCourse(this, courseSection);
    }

    public boolean hasPassedCourse(Course course) {
        return transcript.hasPassedCourse(course);
    }

    public void requestMandatoryCourses() {
        ArrayList<CourseSection> offeredCourses = registrationSystem.getOfferedCourseSections(this);
        executionTrace.append("Offered Courses: \n");
        for (CourseSection c : offeredCourses) {
            executionTrace.append(c.getCourse().toString() +  ", ");
        }
        executionTrace.append("\n");
        executionTrace.append("(" + registrationSystem.getNontechElectiveCourses().get(0).offeredElectiveCount(this) + " NTE), ");
        executionTrace.append("(" + registrationSystem.getTechElectiveCourses().get(0).offeredElectiveCount(this) + " TE), ");
        executionTrace.append("(" + registrationSystem.getFacultyElectiveCourses().get(0).offeredElectiveCount(this) + " FTE), ");
        executionTrace.append("\n");

        for (CourseSection c: offeredCourses) {
            requestCourse(c);
        }

    }

    public void requestElectiveCourses() {
        ArrayList<CourseSection> offeredCourses = registrationSystem.getOfferedElectiveCourseSections(this);

        for (CourseSection c: offeredCourses) {
            requestCourse(c);
        }
    }

    public StringBuilder getExecutionTrace() {
        return executionTrace;
    }

    public void setExecutionTrace(StringBuilder executionTrace) {
        this.executionTrace = executionTrace;
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

    public String toString() {

        String studentStr = "Past Courses: \n" + transcript.toString() + "\n";

        return studentStr;
    }

}
