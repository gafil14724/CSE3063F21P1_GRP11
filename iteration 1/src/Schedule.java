public class Schedule {

    private CourseSection courseSection;
    private CourseSection[][] program;

    public Schedule (CourseSection courseSection, int lectueHours) {
        this.courseSection = courseSection;
        addToProgram(lectueHours);
    }

    public void addToProgram(int lecturehours) {

    }
}
