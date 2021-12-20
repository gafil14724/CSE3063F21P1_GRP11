import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RegistrationSystem {

    private static RegistrationSystem registrationSystem = null;

    private Semester semester;
    private int[] totalStudents = new int[4]; //Total students for each year(used in student id class)
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Advisor> advisors = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<CourseSection> courseSections = new ArrayList<>();
    private double passProbability;
    private int studentCount;
    private int advisorCount;
    private String statisticsBuffer = "";

    private RegistrationSystem( ) { //Prevent instantiation

    }

    public static RegistrationSystem getInstance() {
        if (registrationSystem == null) {
            registrationSystem = new RegistrationSystem();
        }
        return registrationSystem;
    }

    public void startTheSimulation() {
        readInput();
        initializeAdvisors();
        initializeStudents();
        appointAdvisors();
        addPastCourses();
        requestCourses();
        printRegistrationProcess();
      //  printStatistics();
       // registrationProcessOutput();
        //statisticsOutput();
    }

    private void statisticsOutput() {
        JSONObject statJson = new JSONObject();
        statJson.put("Overall Statistics", statisticsBuffer);
        JSONArray statList = new JSONArray();
        statList.add(statJson);

        try (FileWriter file = new FileWriter(new File(    "Statistics.json"))) {
            file.write(statList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registrationProcessOutput() {
        for (Student s: students) {
            JSONObject studentJson = new JSONObject();
            studentJson.put("Registration process: ", s.getBuffer());
            JSONArray jsonList = new JSONArray();
            jsonList.add(studentJson);

            try (FileWriter file = new FileWriter(new File( s.getStudentId().getStudentId() +  ".json"))) {
                file.write(jsonList.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void printRegistrationProcess() {
        for (Student s : students) {
            System.out.println("==========\nRegistration process for: " + s.getStudentId().getStudentId() + " " + s.getSemesterNumber());
            System.out.println(s.getBuffer());
            System.out.println("==============\n\n");
        }
    }

    private void printStatistics() {
        for (CourseSection c : courseSections) {
            if (((MandatoryCourse)c.getCourse()).getSemester() == semester) {
                statisticsBuffer += "\n\n\n============\nStatistics for: " + c.getCourseSectionCode() + "\n";
                statisticsBuffer += c.getCollisionStatistics() + " students couldn't register because of more than " +
                        "one hour collision with other courses\n";
                statisticsBuffer += c.getPrerequisiteStatistics() + " students couldn't register because of prerequisite " +
                        "conditions\n";
                statisticsBuffer += c.getQuotaStatistics() + " students couldn't register because of quota problem\n";
                statisticsBuffer += "==============";
            }
        }
        System.out.println(statisticsBuffer);
    }

    private void requestCourses() {
        for (Student s: students) {
            s.requestCourses();
        }
    }

    private void initializeAdvisors()  {
        for (int i = 0; i < advisorCount; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            advisors.add(new Advisor(name, surname));
        }
    }

    private void initializeStudents()  {
        for (int i = 0; i < studentCount; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            int year = (int)(Math.random() * 4) + 1;
            students.add(new Student(name, surname, year, ++totalStudents[year - 1], this));
        }
    }

    /**Sets a random advisor for each student in students list*/
    private void appointAdvisors() {
        for (Student s: students) {
            int index = (int) (Math.random() * advisors.size()); //Random advisor's index to be appointed to the student
            s.setAdvisor(advisors.get(index));
            s.setBuffer("Advisor: " + advisors.get(index).getFirstName() + " " + advisors.get(index).getLastName() + "\n");
        }
    }


    private void addPastCourses() {
        for (Student s : students) {
            ArrayList<Course> pastCourses = new ArrayList<>();

            for (Course c : courses) { //For each course, add it to past courses list if its semester is less than student's
                if (c instanceof MandatoryCourse) {
                    if (((MandatoryCourse) c).getSemesterNumber() < s.getSemesterNumber()) { //If course's semester is less than student's
                        pastCourses.add(c);
                    }
                }
            }

            s.setBuffer("Past Courses: ");
            for (Course c : pastCourses) { // Student pass the passed courses with a certain probability (passProbability)
                    if (Math.random() < passProbability && s.hasPassedCourse(c.getPreRequisite())) {
                        s.getTranscript().addPassedCourse(c);
                    }
                    else {
                        s.getTranscript().addFailedCourse(c);
                    }
            }
        }

    }


    public ArrayList<CourseSection> getOfferedCourseSections(Student student) {
        ArrayList<CourseSection> offeredCourseSections = new ArrayList<>();

        for (CourseSection c : courseSections) {// For each courseSection in the system
            Course course = c.getCourse(); //Course of the course section
            if (course instanceof MandatoryCourse) { //If it is a section of a mandatory course
                MandatoryCourse castedCourse = (MandatoryCourse) course;// Typecast the course here instead of if statement.
                int studentSemester = student.getSemesterNumber();

                if (castedCourse.getSemesterNumber() == studentSemester ||
                        (castedCourse.getSemesterNumber() < studentSemester && !student.hasPassedCourse(course) &&
                                castedCourse.getSemester() == semester)) {

                    offeredCourseSections.add(c);
                }
            }
        }

        return offeredCourseSections;

    }


    private void readInput() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject input = (JSONObject) parser.parse(new FileReader("input.json"));
            double prob =  ((Number)input.get("PassProbability")).doubleValue();
            int quota = (int)(long) input.get("Quota"); // Quota for each course is the same for the 1st iteration
            setPassProbability(prob);
            int advisorCount = (int)(long)input.get("Advisors");
            setAdvisorCount(advisorCount);
            int studentCount = (int)(long)input.get("Students");
            setStudentCount(studentCount);
            String semester = (String)input.get("CurrentSemester");
            setSemester(semester);

            JSONArray inputCourses = (JSONArray) input.get("MandatoryCourses");
            for(Object c: inputCourses) { //Read mandatory courses and initialize
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");
                float courseSemester = ((Number)course.get("semester")).floatValue();
                int credits = (int)(long)course.get("credits");
                int theoretical = (int)(long)course.get("theoretical");
                int practical = (int)(long) course.get("practical");
                String preRequisiteString = (String) course.get("preRequisites");
                Course preRequisite = findCourse(preRequisiteString);


                Course newCourse = new MandatoryCourse(courseCode,  courseSemester,  quota, credits, theoretical,
                        practical, preRequisite);
                courses.add(newCourse);
            }

            //Read nontechnical courses
            JSONArray nontechnicalSemesters = (JSONArray) input.get("nonTechnicalSemesters");
            ArrayList<Integer> nonTechSemNums = new ArrayList<>();
            for (int i = 0; i< nontechnicalSemesters.size(); i++) {
                nonTechSemNums.add((int)(long)nontechnicalSemesters.get(i));
            }
            int nonTechCredits = (int) (long) input.get("nonTechnicalCredits");
            int nonTechTheoretical = (int) (long) input.get("nonTechnicalTheoretical");
            int nonTechPractical = (int) (long) input.get("nonTechnicalPractical");
            String nonTechPreReqString = (String) input.get("nonTechnicalPreRequisites");
            Course nonTechPreRequisite = findCourse(nonTechPreReqString);

            JSONArray nonTechCourses = (JSONArray) input.get("nonTechnicalElectiveCourses");
            for (Object c: nonTechCourses) {
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");

                Course newNonTechElective = new ElectiveCourse(courseCode, quota, nonTechCredits, nonTechTheoretical,
                        nonTechPractical, nonTechPreRequisite, "nontechnical", nonTechSemNums);
                courses.add(newNonTechElective);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void setSemester(String semester) {
        switch (semester.toLowerCase()) {
            case "spring":
                this.semester = Semester.SPRING; break;
            case "fall":
                this.semester = Semester.FALL; break;
            default:
                System.out.println("Incorrect Semester for Registration System!!");
                System.exit(-1);
        }
    }


    /**Returns the course in courses list by its course code*/
    private  Course findCourse(String courseCode) {
        for (Course c: courses) {
            if (c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null;
    }

    public Semester getSemester() {
        return semester;
    }


    public double getPassProbability() {
        return passProbability;
    }

    public void setPassProbability(double passProbability) {
        this.passProbability = passProbability;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getAdvisorCount() {
        return advisorCount;
    }

    public void setAdvisorCount(int advisorCount) {
        this.advisorCount = advisorCount;
    }

    public ArrayList<CourseSection> getCourseSections() {
        return courseSections;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }
}
