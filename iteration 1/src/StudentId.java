public class StudentId {

    private final String depCode = "1501";
    String studentId;


    public StudentId(Student student) {
        setStudentId(student);
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(Student student) {
        studentId = depCode + getYearString(student) + getRegistrationString(student);
    }

    private String getYearString(Student student) {
        int year = (2021 - student.getCurrentYear()) % 100;
        String yearString = String.valueOf(year);
        return yearString;
    }

    private String getRegistrationString(Student student) {
        int registrationOrder = student.getRegistrationOrder();
        return String.format("%03d", registrationOrder);
    }

    public String toString() {
        return getStudentId();
    }
}

