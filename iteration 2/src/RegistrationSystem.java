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
    private ArrayList<NormalMandatoryCourse> normalMandatoryCourses = new ArrayList<>();
    private ArrayList<FinalProjectMandatoryCourse> finalProjectMandatoryCourses = new ArrayList<>();
    private ArrayList<NonTechnicalUniversityElectiveCourse> nontechElectiveCourses = new ArrayList<>();
    private ArrayList<TechnicalElectiveCourse> techElectiveCourses = new ArrayList<>();
    private ArrayList<FacultyTechnicalElectiveCourse> facultyElectiveCourses = new ArrayList<>();
    private ArrayList<CourseSection> courseSections = new ArrayList<>();
    private double passProbability;
    private int studentCount;
    private int advisorCount;
    private ArrayList<Integer> nonTechElectiveSemesters = new ArrayList<>();
    private ArrayList<Integer> techElectiveSemesters = new ArrayList<>();
    private ArrayList<Integer> facTechElectiveSemesters = new ArrayList<>();
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
            studentJson.put("Registration process: ", s.getExecutionTrace());
            JSONArray jsonList = new JSONArray();
            jsonList.add(studentJson);

            try (FileWriter file = new FileWriter(new File( s.getStudentId() +  ".json"))) {
                file.write(jsonList.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void printRegistrationProcess() {
        for (Student s : students) {
            System.out.println("==========\nRegistration process for: " + s.getFullName() +  ": " + s.getStudentId() +
               " " +    s.getSemesterNumber());
            System.out.println("Advisor: " + s.getAdvisor().getFirstName() + " " + s.getAdvisor().getLastName() + "\n");

         /*   System.out.println("Past Courses: ");
            System.out.println(s.getTranscript().toString());


*/          s.getExecutionTrace().append("\n\nCurrent Courses: \n");
            for (Course c: s.getCurrentCourses()) {
                s.getExecutionTrace().append(c.toString() + ", ");
            }
            System.out.println(s.toString());
            System.out.println(s.getExecutionTrace());
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
        }
    }

    private void addPastNTEs(Student student) {
        int count = student.getNumOfPastElectives(nonTechElectiveSemesters);

        for (int i = 0; i < count; i++) { //Add non technical electives to the past courses list
            int index = (int) (Math.random() * nontechElectiveCourses.size());
            ElectiveCourse elective = nontechElectiveCourses.get(index);
            if (elective.isElligiblePastCourse(student)) {// If elective is an elligible past course for the student
                addPastCourse(student, elective);
            }
            else { //decrease i by 1 to have another random elective in case of choosing the same random elective
                i--;
            }
        }
    }

    private void addPastFTEs(Student student) {
        int count = student.getNumOfPastElectives(facTechElectiveSemesters);

        for (int i = 0; i < count; i++) { //Add non technical electives to the past courses list
            int index = (int) (Math.random() * facultyElectiveCourses.size());
            ElectiveCourse elective = facultyElectiveCourses.get(index);
            if (elective.isElligiblePastCourse(student)) {// If elective is an elligible past course for the student
                addPastCourse(student, elective);
            }
            else { //decrease i by 1 to have another random elective in case of choosing the same random elective
                i--;
            }
        }
    }

    private void addPastTEs(Student student) {
        int count = student.getNumOfPastElectives(techElectiveSemesters);

        for (int i = 0; i < count; i++) { //Add non technical electives to the past courses list
            int index = (int) (Math.random() * techElectiveCourses.size());
            TechnicalElectiveCourse elective = techElectiveCourses.get(index);
            if (elective.isElligiblePastCourse(student)) {// If elective is an elligible past course for the student
                addPastCourse(student, elective);
            }
            else if (!elective.checkCreditCondition(student)) {
                break;
            }
            else { //decrease i by 1 to have another random elective in case of choosing the same random elective
                i--;
            }
        }
    }


    private void addPastElectives(Student student) {
        addPastNTEs(student);
        addPastFTEs(student);
        addPastTEs(student);
    }


    private void addPastCourse(Student student, Course course) {
        if (Math.random() < passProbability) {
            student.getTranscript().addPassedCourse(course);
        }
        else {
            student.getTranscript().addFailedCourse(course);
        }
    }

    private void addPastMandatory(Student s) {
        for (Course c : courses) { //For each course, add it to past courses list if its semester is less than student's
            if (c.isElligiblePastCourse(s)) { //If course's semester is less than student's
                addPastCourse(s, c);
            }
        }
    }


    /**Adds past courses for each student by calling their methods*/
    private void addPastCourses() {
        for (Student s : students) {
            addPastMandatory(s);
            addPastElectives(s);
        }
    }

    private void requestCourses() {
        for (Student s: students) {
            s.requestMandatoryCourses();
            s.requestElectiveCourses();
        }
    }

    public ArrayList<CourseSection> getOfferedCourseSections(Student student) {
        ArrayList<CourseSection> offeredCourseSections = new ArrayList<>();
        for (CourseSection c: courseSections) {
            if (c.getCourse() instanceof MandatoryCourse) {
                if (c.getCourse().isOfferableForStudent(student)) {
                    offeredCourseSections.add(c);
                }
            }
        }
        return offeredCourseSections;
    }

    public ArrayList<CourseSection> getOfferedElectiveCourseSections(Student student) {
        int nteCount = nontechElectiveCourses.get(0).offeredElectiveCount(student);
        int teCount = techElectiveCourses.get(0).offeredElectiveCount(student);
        int fteCount = facultyElectiveCourses.get(0).offeredElectiveCount(student);

        ArrayList<CourseSection> offeredCourses = new ArrayList<>();
        for (int i = 0; i < nteCount; i++) {
            CourseSection courseSection = nontechElectiveCourses.get(0).getRandomElective().getCourseSection();
            if (!offeredCourses.contains(courseSection)) {
                offeredCourses.add(courseSection);
            }else {
                i--;
            }
        }

        for (int i = 0; i < teCount; i++) {
            CourseSection courseSection = techElectiveCourses.get(0).getRandomElective().getCourseSection();
            if (!offeredCourses.contains(courseSection)) {
                offeredCourses.add(courseSection);
            }else {
                i--;
            }
        }

        for (int i = 0; i < fteCount; i++) {
            CourseSection courseSection = facultyElectiveCourses.get(0).getRandomElective().getCourseSection();
            if (!offeredCourses.contains(courseSection)) {
                offeredCourses.add(courseSection);
            }else {
                i--;
            }
        }


        return offeredCourses;
    }

    /**Reads the input file and creates courses according to the
     * input file*/
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


                NormalMandatoryCourse newCourse = new NormalMandatoryCourse(courseCode,  courseSemester,  quota, credits, theoretical,
                        practical, preRequisite);
                courses.add(newCourse);
                normalMandatoryCourses.add(newCourse);
            }

            //Read finalProjectMandatory courses
            int finalProjectReqCredit = (int) (long) input.get("FinalProjectRequiredCredits");
            JSONArray finalProjCourses = (JSONArray) input.get("FinalProjectMandatoryCourses");
            for (Object c: finalProjCourses) {
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");
                float courseSemester = ((Number)course.get("semester")).floatValue();
                int credits = (int)(long)course.get("credits");
                int theoretical = (int)(long)course.get("theoretical");
                int practical = (int)(long) course.get("practical");
                String preRequisiteString = (String) course.get("preRequisites");
                Course preRequisite = findCourse(preRequisiteString);

                Course newCourse = new FinalProjectMandatoryCourse(courseCode,  courseSemester,  quota, credits,
                        theoretical, practical, preRequisite, finalProjectReqCredit);
                courses.add(newCourse);

            }



            //Read nontechnical courses
            JSONArray nontechnicalSemesters = (JSONArray) input.get("nonTechnicalSemesters");
            for (int i = 0; i< nontechnicalSemesters.size(); i++) {
                nonTechElectiveSemesters.add((int)(long)nontechnicalSemesters.get(i));
            }
            int nonTechCredits = (int) (long) input.get("nonTechnicalCredits");
            int nonTechTheoretical = (int) (long) input.get("nonTechnicalTheoretical");
            int nonTechPractical = (int) (long) input.get("nonTechnicalPractical");

            JSONArray nonTechCourses = (JSONArray) input.get("nonTechnicalElectiveCourses");
            for (Object c: nonTechCourses) {
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");

                NonTechnicalUniversityElectiveCourse newNonTechElective = new NonTechnicalUniversityElectiveCourse(courseCode, quota,
                        nonTechCredits, nonTechTheoretical, nonTechPractical, nonTechElectiveSemesters);
             //   courses.add(newNonTechElective);
                nontechElectiveCourses.add(newNonTechElective);
            }

            //Read Technical Elective Courses
            int techReqCredits = (int)(long)input.get("technicalRequiredCredits");
            JSONArray technicalSemesters = (JSONArray) input.get("technicalSemesters");
            for (int i = 0; i< technicalSemesters.size(); i++) {
                techElectiveSemesters.add((int)(long)technicalSemesters.get(i));
            }
            int techCredits = (int) (long) input.get("technicalCredits");
            int techTheoretical = (int) (long) input.get("technicalTheoretical");
            int techPractical = (int) (long) input.get("technicalPractical");

            JSONArray techCourses = (JSONArray) input.get("technicalElectiveCourses");
            for (Object c: techCourses) {
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");
                String techPreReqStr = (String) course.get("preRequisites");
                Course techPreRequisite = findCourse(techPreReqStr);

                TechnicalElectiveCourse newTechElective = new TechnicalElectiveCourse( courseCode, quota, techCredits, techTheoretical,
                        techPractical,techElectiveSemesters,techReqCredits, techPreRequisite);
               // courses.add(newTechElective);
                techElectiveCourses.add(newTechElective);
            }

            //Read Faculty Technical Electives
            JSONArray facTechnicalSemesters = (JSONArray) input.get("facultyTechnicalSemesters");

            for (int i = 0; i< facTechnicalSemesters.size(); i++) {
                facTechElectiveSemesters.add((int)(long)facTechnicalSemesters.get(i));
            }
            int facTechCredits = (int) (long) input.get("facultyTechnicalCredits");
            int facTechTheoretical = (int) (long) input.get("facultyTechnicalTheoretical");
            int facTechPractical = (int) (long) input.get("facultyTechnicalPractical");

            JSONArray facTechCourses = (JSONArray) input.get("facultyTechnicalElectiveCourses");
            for (Object c: facTechCourses) {
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");

                FacultyTechnicalElectiveCourse newFacTechElective = new FacultyTechnicalElectiveCourse(courseCode, quota, facTechCredits,
                        facTechTheoretical, facTechPractical, facTechElectiveSemesters);
               // courses.add(newFacTechElective);
                facultyElectiveCourses.add(newFacTechElective);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void setSemester(String semester) {
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

    public static RegistrationSystem getRegistrationSystem() {
        return registrationSystem;
    }

    public int[] getTotalStudents() {
        return totalStudents;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Advisor> getAdvisors() {
        return advisors;
    }

    public ArrayList<NormalMandatoryCourse> getNormalMandatoryCourses() {
        return normalMandatoryCourses;
    }

    public ArrayList<FinalProjectMandatoryCourse> getFinalProjectMandatoryCourses() {
        return finalProjectMandatoryCourses;
    }

    public ArrayList<NonTechnicalUniversityElectiveCourse> getNontechElectiveCourses() {
        return nontechElectiveCourses;
    }

    public ArrayList<TechnicalElectiveCourse> getTechElectiveCourses() {
        return techElectiveCourses;
    }

    public ArrayList<FacultyTechnicalElectiveCourse> getFacultyElectiveCourses() {
        return facultyElectiveCourses;
    }

    public ArrayList<Integer> getNonTechElectiveSemesters() {
        return nonTechElectiveSemesters;
    }

    public ArrayList<Integer> getTechElectiveSemesters() {
        return techElectiveSemesters;
    }

    public ArrayList<Integer> getFacTechElectiveSemesters() {
        return facTechElectiveSemesters;
    }

    public String getStatisticsBuffer() {
        return statisticsBuffer;
    }
}
