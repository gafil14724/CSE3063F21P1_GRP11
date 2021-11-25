import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RegistrationSystem {

    private String semester;
    private int[] totalStudents = new int[4]; //Total students for each year(used in student id class)
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Advisor> advisors = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();

    public  RegistrationSystem(String semester) throws IOException, ParseException {

        readInput();
        setAdvisors();
        setStudents();
        for (Student s: students) {
            System.out.println(s.getStudentId().getStudentId());
        }


        /*for (Course c : courses) {
            if(c.getPreRequisite() != null)
            System.out.println(c.getPreRequisite().getCourseCode());
        }*/


    }

    public void setAdvisors () throws IOException, ParseException {
        for (int i = 0; i < 10; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            advisors.add(new Advisor(name, surname));
        }

    }

    public void setStudents() throws IOException, ParseException {
        for (int i = 0; i < 270; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            int year = 1; // For now, we create only first year students.
            students.add(new Student(name, surname, year, ++totalStudents[year - 1]));

        }
    }



    public void readInput() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject input = (JSONObject) parser.parse(new FileReader("input.json"));

        int prob = Integer.parseInt((String)input.get("Probability"));
        int quota = Integer.parseInt((String)input.get("Quota"));
        System.out.println(prob);
        System.out.println(quota);

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
            courses.add(newCourse);


        }
    }

    public  Course findCourse(String courseCode) {
        for (Course c: courses) {
            if (c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null;
    }


}
