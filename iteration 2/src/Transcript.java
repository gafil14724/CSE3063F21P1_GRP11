import java.util.ArrayList;

public class Transcript {

    private Student student;
    private ArrayList<Grade> grades = new ArrayList<>();

    public Transcript(Student student) {
        this.student = student;
    }

    public int getCompletedCredits() {
        int credits = 0;
        for (Grade g: grades) {
            if (g.isPassed()) { // If grade is greater or equal to 50
                credits += g.getCourse().getCredits();
            }
        }
        return credits;
    }

    public ArrayList<Course> getPassedCourses() {
        ArrayList<Course> passedCourses = new ArrayList<>();
        for (Grade g : grades) {
            if (g.getIntGrade() >= 50) {
                passedCourses.add(g.getCourse());
            }
        }
        return passedCourses;
    }

    public ArrayList<Course> getTakenCourses() {
        ArrayList<Course> takenCourses = new ArrayList<>();

        for (Grade g: grades) {
            takenCourses.add(g.getCourse());
        }

        return takenCourses;
    }

    /**Takes a course as argument and checks if student
     * has passed that course by iterating over student's
     * grades*/
    public boolean hasPassedCourse(Course course) {
        if (course == null) { // If course is null, return true (Used for courses that have no prerequisite)
            return true;
        }

        ArrayList<Course> passedCourses = getPassedCourses();

        return passedCourses.contains(course);
    }

    public void addPassedCourse(Course course) { //Adds a passed course with a random grade that is greater than 50
        int grade = 0;
        if (course instanceof MandatoryCourse && ((MandatoryCourse) course).getSemester() == Semester.SUMMER) {
            grade = 100; //Grade is 100 for summer courses (Internships)
        }
        else {
            grade = (int) (Math.random() * 51) + 50; // random grade that is greater than 50
        }

        grades.add(new Grade(course, grade));
        student.setBuffer("\n" + course.getCourseCode() + ": " + grades.get(grades.size() - 1).getLetterGrade());
        if (course instanceof FinalProjectMandatoryCourse) {
            student.setBuffer(" (Final Project)" );
        }

        if (course instanceof ElectiveCourse) {
            student.setBuffer(" " + course.toString());
        }

    }

    public void addFailedCourse(Course course) {
        int grade;
        if (course instanceof MandatoryCourse && ((MandatoryCourse) course).getSemester() == Semester.SUMMER) {
            grade = 0; //For summer internships (Either FF or AA)
        }
        else {
            grade = (int) (Math.random() * 50); //random grade between 0-49
        }

        grades.add(new Grade(course, grade));
        student.setBuffer("\n" + course.getCourseCode() + ": " + grades.get(grades.size() - 1).getLetterGrade());

        if (course instanceof FinalProjectMandatoryCourse) {
            student.setBuffer(" (Final Project)" );
        }

        if (course instanceof ElectiveCourse) {
            student.setBuffer(" " + course.toString());
        }
    }

}
