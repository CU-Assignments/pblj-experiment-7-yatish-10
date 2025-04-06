import java.sql.*;
import java.util.Scanner;

// Model
class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return "StudentID: " + studentID + ", Name: " + name + ", Department: " + department + ", Marks: " + marks;
    }
}

// Controller
class StudentController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name"; // Change to your database URL
    private static final String USER = "your_username"; // Change to your database username
    private static final String PASS = "your_password"; // Change to your database password

    public void createStudent(Student student) {
        String query = "INSERT INTO Student (Name, Department, Marks) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setDouble(3, student.getMarks());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                System.out.println("Student created successfully with ID: " + generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Error creating student: " + e.getMessage());
        }
    }

    public Student readStudent(int studentID) {
        String query = "SELECT * FROM Student WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getInt("StudentID"), rs.getString("Name"), rs.getString("Department"), rs.getDouble("Marks"));
            }
        } catch (SQLException e) {
            System.out.println("Error reading student: " + e.getMessage());
        }
        return null;
    }

    public void updateStudent(Student student) {
        String query = "UPDATE Student SET Name = ?, Department = ?, Marks = ? WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setDouble(3, student.getMarks());
            pstmt.setInt(4, student.getStudentID());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    public void deleteStudent(int studentID) {
        String query = "DELETE FROM Student WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
}

// Main Application
public class StudentManagementApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentController controller = new StudentController();
        int choice;

        do {
            System.out.println("Menu:");
            System.out.println("1. Create Student");
            System.out
