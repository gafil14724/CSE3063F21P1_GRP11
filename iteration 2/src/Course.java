public abstract class Course {

    private String courseCode;
    private int quota;
    private int credits;
    private int[] sectionHours = new int[2]; // {Theoretical, Practical}
    private Course preRequisite;


    protected Course(String courseCode, int quota,
                   int credits, int theoretical, int practical, Course preRequisite) {

        this.courseCode = courseCode;
        this.quota = quota;
        this.credits = credits;
        setSectionHours(theoretical, practical);
        this.preRequisite = preRequisite;
    }


    public int getSectionHours() { //Returns the total section hours by summing theoretical and practical hours
        return sectionHours[0] + sectionHours[1];
    }

    public void setSectionHours(int theoretical, int practical) {
        this.sectionHours = new int[]{theoretical, practical};
    }

    public String getCourseCode() {
        return courseCode;
    }



    public int getQuota() {
        return quota;
    }


    public int getCredits() {
        return credits;
    }



    public Course getPreRequisite() {
        return preRequisite;
    }


}
