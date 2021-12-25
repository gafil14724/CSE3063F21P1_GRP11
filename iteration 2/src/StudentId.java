public class StudentId {

    private final String depCode = "1501";
    private int year;
    private int registrationOrder;


    public StudentId(Student student) {
        this.year = student.getCurrentYear();
        this.registrationOrder = student.getRegistrationOrder();
    }

    public String getStudentId() {
        return depCode + getYearString() + getRegistrationString();
    }

    private String getYearString() {
        int idYear = (2021 - year) % 100;
        String yearString = String.valueOf(idYear);
        return yearString;
    }

    private String getRegistrationString() {
        return String.format("%03d", registrationOrder);
    }

    public String toString() {
        return getStudentId();
    }
}

