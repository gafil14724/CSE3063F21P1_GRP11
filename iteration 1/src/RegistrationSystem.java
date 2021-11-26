import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.midi.Soundbank;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RegistrationSystem {

    private String semester;
    private int[] totalStudents = new int[4]; //Total students for each year(used in student id class)
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Advisor> advisors = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<CourseSection> courseSections = new ArrayList<>();
    private double passProbability;
    private int studentCount;
    private int advisorCount;

    public  RegistrationSystem( ) throws IOException, ParseException {

        startTheSimulation();

        for (Student s : students) {
            for (Course c: s.getPassedCourses()) {
                System.out.println(c.getCourseCode());
            }
            System.out.println("\n\n\n");
            for (Course g: s.getCurrentCourses()) {
                System.out.println(g.getCourseCode());
            }
            System.out.println("\n\n");
        }


        /*for (Course c : courses) {
            if(c.getPreRequisite() != null)
            System.out.println(c.getPreRequisite().getCourseCode());
        }*/


    }

    private void startTheSimulation() {
        readInput();
        initializeAdvisors();
        initializeStudents();
        appointAdvisors();
        addPastCourses();
        requestCourses();
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
            int year = 1; // For now, we create only first year students.
            students.add(new Student(name, surname, year, ++totalStudents[year - 1], this));
        }
    }

    /**Sets a random advisor for each student in students list*/
    private void appointAdvisors() {
        for (Student s: students) {
            int index = (int) (Math.random() * advisors.size()); //Random advisor's index to be appointed to the student
            s.setAdvisor(advisors.get(index));
        }
    }


    private void addPastCourses() {
        ArrayList<Course> pastCourses = new ArrayList<>();
        for (Course c : courses) {
            if (c.getSemester().equals("fall")) {
                pastCourses.add(c);
            }
        }

        for (Student s : students) {
            for (Course c: pastCourses) {
                if (Math.random() < passProbability) { //Passed courses are added according to the probability given as input
                    s.addPassedCourse(c);
                }
            }
        }


    }

    public ArrayList<CourseSection> getOfferedCourses(Student student) {
        ArrayList<CourseSection> offeredCourses = new ArrayList<>();

        for (CourseSection c: courseSections) {
            if (!student.hasPassedCourse(c.getCourse())) {
                offeredCourses.add(c);
            }
        }
        return offeredCourses;
    }


    private void readInput() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject input = (JSONObject) parser.parse(new FileReader("input.json"));
            int quota = Integer.parseInt((String)input.get("Quota")); // Quota for each course is the same for the 1st iteration
            double prob = Double.parseDouble((String)input.get("PassProbability"));
            setPassProbability(prob);
            int advisorCount = Integer.parseInt((String)input.get("Advisors"));
            setAdvisorCount(advisorCount);
            int studentCount = Integer.parseInt((String)input.get("Students"));
            setStudentCount(studentCount);
            semester = (String)input.get("CurrentSemester");

            JSONArray inputCourses = (JSONArray) input.get("Courses");

            for(Object c: inputCourses) {
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");
                String courseName = (String) course.get("courseName");
                String courseSemester = (String) course.get("semester");
                String courseType = (String) course.get("courseType");
                int credits = Integer.parseInt((String) course.get("credits"));
                int theoretical = Integer.parseInt((String) course.get("theoretical"));
                int practical = Integer.parseInt((String) course.get("practical"));
                int year = Integer.parseInt((String) course.get("year"));
                int requiredCredits = Integer.parseInt((String) course.get("requiredCredits"));
                String preRequisite = (String) course.get("preRequisites");




                Course newCourse = new Course(courseCode, courseName, courseSemester, courseType, quota, credits, theoretical,
                        practical, year, requiredCredits, findCourse(preRequisite));
                courses.add(newCourse); //Initialize each course and add it to the courses list
                courseSections.add(new CourseSection(newCourse));

                //System.out.println(newCourse.getPreRequisite() == null);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }


    }

    /**Returns the course in courses list by its course code*/
    public  Course findCourse(String courseCode) {
        for (Course c: courses) {
            if (c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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
}
