import java.util.ArrayList;

public class Schedule {

    public static final int DAYS = 5;
    public static final int HOURS = 8;

    private CourseSection[][] program;

    public Schedule () {
        program = new CourseSection[DAYS][HOURS];
    }

    /**Takes a courseSection as argument and adds it to
     * the schedule by taking its courseProgram into consideration*/
    public void addToProgram(CourseSection courseSection) {
        boolean[][] courseProgram = courseSection.getCourseProgram();

        for (int i = 0; i < DAYS; i++) {
            for (int j = 0; j < HOURS; j++) {

                if (courseProgram[i][j]) {
                    program[i][j] = courseSection;
                }
            }
        }
    }

    /**Takes a course section as argument and compares it with
     * current schedule, if there is more than 1 hour collision between
     * courseProgram and current schedule returns true, otherwise false */
    public boolean isCollision(CourseSection courseSection) {
        boolean[][] courseProgram = courseSection.getCourseProgram();
        int collidedHours = 0; // total num of collided hours

        for (int i = 0; i < DAYS; i++) {
            for (int j = 0; j < HOURS; j++) {

                // If courseProgram and schedule has lectures in the same hour
                if (courseProgram[i][j] && program[i][j] != null) {
                    collidedHours++;
                }
            }
        }

        return collidedHours > 1; // Return true if collided hours is greater than one, false otherwise.

    }

    public CourseSection[][] getProgram() {
        return program;
    }

    public void setProgram(CourseSection[][] program) {
        this.program = program;
    }
}
