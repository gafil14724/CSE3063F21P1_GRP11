import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RegistrationSystem {

    private static RegistrationSystem registrationSystem = null;

    private boolean isRegenerate;
    private Semester semester;
    private int[] totalStudents = new int[4]; //Total students for each year(used in student id class)
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Advisor> advisors = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<MandatoryCourse> mandatoryCourses = new ArrayList<>();
    private ArrayList<FinalProjectMandatoryCourse> finalProjectMandatoryCourses = new ArrayList<>();
    private ArrayList<NonTechnicalUniversityElectiveCourse> nontechElectiveCourses = new ArrayList<>();
    private ArrayList<TechnicalElectiveCourse> techElectiveCourses = new ArrayList<>();
    private ArrayList<FacultyTechnicalElectiveCourse> facultyElectiveCourses = new ArrayList<>();
    private double passProbability;
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
        regenerateCheck();
        requestCourses();
        printRegistrationProcess();
        printStatistics();
        registrationProcessOutput();
        //statisticsOutput();
    }

    private void regenerateCheck() {
        if (isRegenerate) {
            readStudents();
        }else {
            initializeAdvisors();
            //initializeStudents();
            appointAdvisors();
            addPastCourses();
        }


    }

    private void readStudents() {
        File folder = new File("Students/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject input = (JSONObject) parser.parse(new FileReader("Students/" + file.getName()));

                    String fName = (String)input.get("StudentName");
                    String lName = (String)input.get("StudentSurname");
                    String studentId = (String)input.get("StudentId");
                    String advisorFName = (String)input.get("AdvisorName");
                    String advisorLName = (String)input.get("AdvisorSurname");
                    int semesterNum = (int) (long) input.get("SemesterNumber");
                    int completedCredits = (int) (long) input.get("CompletedCredits");

                    Advisor newAdvisor = new Advisor(advisorFName, advisorLName);
                    advisors.add(newAdvisor);
                    Student newStudent = new Student(fName, lName, studentId, this, 2);
                    newStudent.setAdvisor(newAdvisor);

                    if (completedCredits == 258 || ((getSemester() == Semester.FALL && semesterNum % 2 == 1) || (getSemester() == Semester.SPRING && semesterNum % 2 == 0))) {
                        newStudent.setSemesterNumber(semesterNum);
                    }else {
                        newStudent.setSemesterNumber(++semesterNum);
                    }

                    students.add(newStudent);


                    JSONArray pastCourses = (JSONArray) input.get("Past Courses");
                    ArrayList<Grade> grades = new ArrayList<>();
                    for (Object c: pastCourses) {
                        JSONObject grade = (JSONObject) c;
                        String courseCode = (String) grade.get("Course");
                        Course course = findCourse(courseCode);
                        int intGrade = (int)(long)grade.get("intGrade");
                        grades.add(new Grade(course, intGrade));
                    }


                    newStudent.getTranscript().setGrades(grades);



                    JSONArray currentCourses = (JSONArray) input.get("Current Courses");
                    ArrayList<Course> stuCurrCourses = new ArrayList<>();
                    for (int i = 0; i< currentCourses.size(); i++) {
                        //techElectiveSemesters.add((int)(long)technicalSemesters.get(i));
                        stuCurrCourses.add(findCourse((String) currentCourses.get(i)));
                    }

                    stuCurrCourses.forEach(c -> addPastCourse(newStudent, c));

                    /*JSONArray inputCourses = (JSONArray) input.get("MandatoryCourses");
                    for(Object c: inputCourses) { //Read mandatory courses and initialize
                        JSONObject course = (JSONObject) c;
                        String courseCode = (String) course.get("courseCode");
                        float courseSemester = ((Number)course.get("semester")).floatValue();
                        int credits = (int)(long)course.get("credits");
                        int theoretical = (int)(long)course.get("theoretical");
                        int practical = (int)(long) course.get("practical");*/


                }
                catch (IOException e) {
                    e.printStackTrace();
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

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

        new File("Students").mkdir();
        for (Student s: students) {
            org.json.JSONObject studentJson = new org.json.JSONObject();
            studentJson.put("StudentName", s.getName());
            studentJson.put("StudentSurname", s.getSurname());
            studentJson.put("StudentId", s.getStudentId());
            studentJson.put("SemesterNumber", s.getSemesterNumber());
            studentJson.put("CompletedCredits", s.getTranscript().getCompletedCredits());
           /* studentJson.put("CurrentYear", s.getCurrentYear());
            studentJson.put("CurrentSemester", s.getSemesterNumber());*/


            ArrayList<Grade> stuGrades = s.getTranscript().getGrades();

            JSONArray pastCourses = new JSONArray();
            for (Grade g: stuGrades) {
                JSONObject grades = new JSONObject();
                grades.put("Course", g.getCourse().getCourseCode());
                grades.put("LetterGrade", g.getLetterGrade());
                grades.put("intGrade", g.getIntGrade());
                pastCourses.add(grades);
            }
            studentJson.put("Past Courses", pastCourses);

            JSONArray currentCourses = new JSONArray();
            ArrayList<Course> stuCurrentCourses = s.getTranscript().getCurrentCourses();

            for (Course c: stuCurrentCourses) {
                currentCourses.add(c.getCourseCode());
            }

            studentJson.put("Current Courses", currentCourses);

            JSONArray messages = new JSONArray();
            String[] executionMessages = s.getExecutionTrace().toString().split("\\n");
            for (String st: executionMessages) {
                messages.add(st);
            }
            studentJson.put("Execution Trace", messages);


            studentJson.put("AdvisorName", s.getAdvisor().getFirstName());
            studentJson.put("AdvisorSurname", s.getAdvisor().getLastName());


            /*JSONArray jsonList = new JSONArray();
            jsonList.add(studentJson);*/

            try (FileWriter file = new FileWriter(new File( "Students/" + s.getStudentId() +  ".json"))) {
                file.write(studentJson.toString(4));
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void printRegistrationProcess() {
        for (Student s : students) {
            System.out.println("==========\nRegistration process for: " + s.getFullName() +  ": " + s.getStudentId() +
                    " \nSemester Number: " +    s.getSemesterNumber() + "\nCompleted Credits: " + s.getCompletedCredits());
            System.out.println("Advisor: " + s.getAdvisor().getFirstName() + " " + s.getAdvisor().getLastName() + "\n");

         /*   System.out.println("Past Courses: ");
            System.out.println(s.getTranscript().toString());
*/          s.getExecutionTrace().append("\n\nCurrent Courses: \n");
            for (Course c: s.getTranscript().getCurrentCourses()) {
                s.getExecutionTrace().append(c.toString() + ", ");
            }
            System.out.println(s.toString());
            System.out.println(s.getExecutionTrace());
            System.out.println("==============\n\n");
        }
    }

   private void printMandatoryStatistics() {
        for (MandatoryCourse c: mandatoryCourses) {
            if (c.getNonRegisteredCollision().size() > 0) {
                System.out.print(c.getNonRegisteredCollision().size() + " Students couldn't register to " +
                        c.toString() + " Because of a collision problem: (");
                c.getNonRegisteredCollision().forEach(s -> System.out.print(s.getStudentId() + " "));
                System.out.println(")");
            }

            if (c.getNonRegisteredQuota().size() > 0) {
                System.out.print(c.getNonRegisteredQuota().size() + " students couldn't register to " +
                        c.toString() + " because of quota problem: (");
                c.getNonRegisteredQuota().forEach(s -> System.out.print(s.getStudentId() + " "));
                System.out.println(")");
            }

            if (c.getNonRegisteredPrereq().size() > 0) {
                System.out.print(c.getNonRegisteredPrereq().size() + " Students couldn't register to " +
                        c.toString() + " Because of a Prerequisite Problem: (");
                c.getNonRegisteredPrereq().forEach(s -> System.out.print(s.getStudentId() + " "));
                System.out.println(")");
            }


        }
    }

    private void printFinalProjectStatistics() {
        for (FinalProjectMandatoryCourse c: finalProjectMandatoryCourses) {
            if (c.getNonRegisteredCredit().size() > 0) {
                System.out.print(c.getNonRegisteredCredit().size() + " Students couldn't register to " +
                        c.toString() + " Because of credit problem: (");
                c.getNonRegisteredCredit().forEach(s -> System.out.print(s.getStudentId() + " "));
                System.out.println(")");
            }
        }
    }

    private void printElectiveStatistics() {
        Set<Student> teStudents = new HashSet<>();
        for (TechnicalElectiveCourse te: techElectiveCourses) {
            teStudents.addAll(te.getUnregisteredStudents());
        }
        System.out.print(teStudents.size() + " Student couldn't register to a Technical Elective " +
                "(TE) this semester: (");

        teStudents.forEach(s -> System.out.print(s.getStudentId() + ", "));
        System.out.println(")");
    }

    private void printStatistics() {
        System.out.println("\n\n");
        printMandatoryStatistics();
        printFinalProjectStatistics();
        printElectiveStatistics();
    }



    private void initializeAdvisors()  {
        for (int i = 0; i < advisorCount; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            advisors.add(new Advisor(name, surname));
        }
    }

    private void initializeStudents(int first, int second, int third, int fourth)  {
        for (int i = 0; i < first; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            students.add(new Student(name, surname, 1, ++totalStudents[0], this));
        }

        for (int i = 0; i < second; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            students.add(new Student(name, surname, 2, ++totalStudents[1], this));
        }

        for (int i = 0; i < third; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            students.add(new Student(name, surname, 3, ++totalStudents[2], this));
        }

        for (int i = 0; i < fourth; i++) {
            String name = Main.getNamesList().get((int) (Math.random() * Main.getNamesList().size() - 1));
            String surname = Main.getSurnamesList().get((int) (Math.random() * Main.getSurnamesList().size() - 1));
            students.add(new Student(name, surname, 4, ++totalStudents[3], this));
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
        for (Course c : mandatoryCourses) { //For each course, add it to past courses list if its semester is less than student's
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
        for (MandatoryCourse c: mandatoryCourses) {
            if (c.isOfferableForStudent(student)) {
                offeredCourseSections.add(c.getCourseSection());
            }
        }

        /* for (CourseSection c: courseSections) {
            if (c.getCourse() instanceof MandatoryCourse) {
                if (c.getCourse().isOfferableForStudent(student)) {
                    offeredCourseSections.add(c);
                }
            }
        }*/
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
            setPassProbability(prob);
            int advisorCount = (int)(long)input.get("Advisors");
            setAdvisorCount(advisorCount);
            int studentCount = (int)(long)input.get("Students");
            //setStudentCount(studentCount);
            String semester = (String)input.get("CurrentSemester");
            setSemester(semester);
            isRegenerate = (boolean) input.get("RegenerateStudents");
            int first = (int) (long) input.get("1stYearStudents");
            int second = (int) (long) input.get("2ndYearStudents");
            int third = (int) (long) input.get("3rdYearStudents");
            int fourth = (int) (long) input.get("4thYearStudents");

            if (!isRegenerate) {
                initializeStudents(first, second, third, fourth);
            }

            JSONArray inputCourses = (JSONArray) input.get("MandatoryCourses");
            for(Object c: inputCourses) { //Read mandatory courses and initialize
                JSONObject course = (JSONObject) c;
                String courseCode = (String) course.get("courseCode");
                float courseSemester = ((Number)course.get("semester")).floatValue();
                int credits = (int)(long)course.get("credits");
                int theoretical = (int)(long)course.get("theoretical");
                int practical = (int)(long) course.get("practical");
                int mandQuota = (int) (long) course.get("quota");
                ArrayList<Course> preRequisiteCourses = new ArrayList<>();
                JSONArray preRequisites = (JSONArray) course.get("preRequisites");
                for (Object p: preRequisites) {
                    preRequisiteCourses.add(findCourse((String)p));
                }

                MandatoryCourse newCourse = new MandatoryCourse(courseCode,  courseSemester,  mandQuota, credits, theoretical,
                        practical, preRequisiteCourses);
                courses.add(newCourse);
                mandatoryCourses.add(newCourse);
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
                int finalQuota = (int) (long) course.get("quota");
                ArrayList<Course> preRequisiteCourses = new ArrayList<>();
                JSONArray preRequisites = (JSONArray) course.get("preRequisites");
                for (Object p: preRequisites) {
                    preRequisiteCourses.add(findCourse((String)p));
                }

                FinalProjectMandatoryCourse newCourse = new FinalProjectMandatoryCourse(courseCode,  courseSemester,  finalQuota, credits,
                        theoretical, practical, preRequisiteCourses, finalProjectReqCredit);
                courses.add(newCourse);
                mandatoryCourses.add(newCourse);
                finalProjectMandatoryCourses.add(newCourse);
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
                int nonTechQuota = (int) (long) course.get("quota");

                NonTechnicalUniversityElectiveCourse newNonTechElective = new NonTechnicalUniversityElectiveCourse(courseCode, nonTechQuota,
                        nonTechCredits, nonTechTheoretical, nonTechPractical, nonTechElectiveSemesters);
                courses.add(newNonTechElective);
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
                int techQuota = (int) (long) course.get("quota");
                ArrayList<Course> preRequisiteCourses = new ArrayList<>();
                JSONArray preRequisites = (JSONArray) course.get("preRequisites");
                for (Object p: preRequisites) {
                    preRequisiteCourses.add(findCourse((String)p));
                }

                TechnicalElectiveCourse newTechElective = new TechnicalElectiveCourse( courseCode, techQuota, techCredits, techTheoretical,
                        techPractical,techElectiveSemesters,techReqCredits, preRequisiteCourses);
                courses.add(newTechElective);
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
                int facTechQuota = (int) (long) course.get("quota");

                FacultyTechnicalElectiveCourse newFacTechElective = new FacultyTechnicalElectiveCourse(courseCode, facTechQuota, facTechCredits,
                        facTechTheoretical, facTechPractical, facTechElectiveSemesters);
                courses.add(newFacTechElective);
                facultyElectiveCourses.add(newFacTechElective);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public boolean isThereEmptyNonTechSection() {
        for (Course c: nontechElectiveCourses) {
            if (!c.getCourseSection().isFull()) {
                return true;
            }
        }
        return false;
    }

    public boolean isThereEmptyTechSection() {
        for (Course c: techElectiveCourses) {
            if (!c.getCourseSection().isFull()) {
                return true;
            }
        }
        return false;
    }

    public boolean isThereEmptyFacTechSection() {
        for (Course c: facultyElectiveCourses) {
            if (!c.getCourseSection().isFull()) {
                return true;
            }
        }
        return false;
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


    public int getAdvisorCount() {
        return advisorCount;
    }

    public void setAdvisorCount(int advisorCount) {
        this.advisorCount = advisorCount;
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

    public ArrayList<MandatoryCourse> getMandatoryCourses() {
        return mandatoryCourses;
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