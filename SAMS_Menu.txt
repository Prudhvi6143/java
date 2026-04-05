import java.sql.*;
import java.util.Scanner;

public class SAMS_Menu {

    static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    static final String USER = "system";
    static final String PASSWORD = "76143";

    static final String APP_USER = "admin";
    static final String APP_PASS = "1234";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;

        // LOGIN LOOP
        while (!loggedIn) {
            System.out.println("===== LOGIN =====");
            System.out.print("Username: ");
            String u = sc.nextLine().trim();
            System.out.print("Password: ");
            String p = sc.nextLine().trim();

            if (!u.equals(APP_USER) || !p.equals(APP_PASS)) {
                System.out.println("Invalid Login! Try again.");
            } else {
                loggedIn = true;
                System.out.println("Login Successful!");
            }
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            con.setAutoCommit(false);

            int choice = 0;

            do {
                System.out.println("\n===== STUDENT ATTENDANCE MANAGEMENT SYSTEM =====");
                System.out.println("1.  Add Student");
                System.out.println("2.  View Students");
                System.out.println("3.  Add Subject");
                System.out.println("4.  View Subjects");
                System.out.println("5.  Mark Attendance");
                System.out.println("6.  View Attendance");
                System.out.println("7.  Date-wise Attendance");
                System.out.println("8.  Weekly Attendance");
                System.out.println("9.  Monthly Attendance");
                System.out.println("10. Course-wise Attendance");
                System.out.println("11. Delete Student");
                System.out.println("12. Logout");
                System.out.print("Enter choice: ");

                try {
                    choice = Integer.parseInt(sc.nextLine().trim()); // FIXED - no more sc.nextInt()
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Enter a number between 1 and 12.");
                    choice = 0;
                    continue;
                }

                switch (choice) {

                    case 1: // Add Student
                        try {
                            System.out.print("Enter Student ID: ");
                            int sid = Integer.parseInt(sc.nextLine().trim()); // FIXED
                            System.out.print("Enter Name: ");
                            String name = sc.nextLine().trim();
                            System.out.print("Enter Course: ");
                            String course = sc.nextLine().trim();
                            System.out.print("Enter Section: ");
                            String section = sc.nextLine().trim();

                            PreparedStatement ps1 = con.prepareStatement(
                                    "INSERT INTO student (student_id, student_name, course, section) VALUES (?, ?, ?, ?)");
                            ps1.setInt(1, sid);
                            ps1.setString(2, name);
                            ps1.setString(3, course);
                            ps1.setString(4, section);
                            ps1.executeUpdate();
                            con.commit();
                            ps1.close();
                            System.out.println("Student Added Successfully!");
                        } catch (NumberFormatException e) {
                            System.out.println("Error! Student ID must be a number.");
                        } catch (Exception e) {
                            con.rollback();
                            System.out.println("Error! " + e.getMessage());
                        }
                        break;

                    case 2: // View Students
                        try {
                            Statement st1 = con.createStatement();
                            ResultSet rs1 = st1.executeQuery("SELECT * FROM student");
                            System.out.println("\nID\tName\t\tCourse\t\tSection");
                            System.out.println("--------------------------------------------------");
                            boolean found = false;
                            while (rs1.next()) {
                                found = true;
                                System.out.println(rs1.getInt(1) + "\t" +
                                        rs1.getString(2) + "\t\t" +
                                        rs1.getString(3) + "\t\t" +
                                        rs1.getString(4));
                            }
                            if (!found) System.out.println("No students found.");
                            rs1.close();
                            st1.close();
                        } catch (Exception e) {
                            System.out.println("Error fetching students: " + e.getMessage());
                        }
                        break;

                    case 3: // Add Subject
                        try {
                            System.out.print("Enter Subject ID: ");
                            int subid = Integer.parseInt(sc.nextLine().trim()); // FIXED
                            System.out.print("Enter Subject Name: ");
                            String subname = sc.nextLine().trim();

                            PreparedStatement ps2 = con.prepareStatement(
                                    "INSERT INTO subject (subject_id, subject_name) VALUES (?, ?)");
                            ps2.setInt(1, subid);
                            ps2.setString(2, subname);
                            ps2.executeUpdate();
                            con.commit();
                            ps2.close();
                            System.out.println("Subject Added Successfully!");
                        } catch (NumberFormatException e) {
                            System.out.println("Error! Subject ID must be a number.");
                        } catch (Exception e) {
                            con.rollback();
                            System.out.println("Error! " + e.getMessage());
                        }
                        break;

                    case 4: // View Subjects
                        try {
                            Statement st2 = con.createStatement();
                            ResultSet rs2 = st2.executeQuery("SELECT * FROM subject");
                            System.out.println("\nID\tSubject Name");
                            System.out.println("--------------------");
                            boolean found = false;
                            while (rs2.next()) {
                                found = true;
                                System.out.println(rs2.getInt(1) + "\t" + rs2.getString(2));
                            }
                            if (!found) System.out.println("No subjects found.");
                            rs2.close();
                            st2.close();
                        } catch (Exception e) {
                            System.out.println("Error fetching subjects: " + e.getMessage());
                        }
                        break;

                    case 5: // Mark Attendance
                        try {
                            System.out.print("Enter Attendance ID: ");
                            int aid = Integer.parseInt(sc.nextLine().trim()); // FIXED

                            // Check duplicate attendance ID
                            PreparedStatement psCheck = con.prepareStatement(
                                    "SELECT COUNT(*) FROM attendance WHERE attendance_id = ?");
                            psCheck.setInt(1, aid);
                            ResultSet rsCheck = psCheck.executeQuery();
                            rsCheck.next();
                            if (rsCheck.getInt(1) > 0) {
                                System.out.println("Error: Attendance ID already exists! Use a different ID.");
                                rsCheck.close();
                                psCheck.close();
                                break;
                            }
                            rsCheck.close();
                            psCheck.close();

                            System.out.print("Enter Student ID: ");
                            int sid2 = Integer.parseInt(sc.nextLine().trim()); // FIXED
                            System.out.print("Enter Subject ID: ");
                            int subid2 = Integer.parseInt(sc.nextLine().trim()); // FIXED
                            System.out.print("Enter Date (YYYY-MM-DD): ");
                            String dateStr = sc.nextLine().trim();
                            System.out.print("Enter Status (Present/Absent): ");
                            String status = sc.nextLine().trim();

                            // Validate status
                            if (!status.equalsIgnoreCase("Present") && !status.equalsIgnoreCase("Absent")) {
                                System.out.println("Invalid status! Use 'Present' or 'Absent'.");
                                break;
                            }

                            // Validate date
                            if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                System.out.println("Invalid date format! Use YYYY-MM-DD.");
                                break;
                            }

                            Date attDate;
                            try {
                                attDate = Date.valueOf(dateStr);
                            } catch (IllegalArgumentException ex) {
                                System.out.println("Invalid date! Use YYYY-MM-DD.");
                                break;
                            }

                            PreparedStatement psInsert = con.prepareStatement(
                                    "INSERT INTO attendance (attendance_id, student_id, subject_id, attendance_date, status) VALUES (?, ?, ?, ?, ?)");
                            psInsert.setInt(1, aid);
                            psInsert.setInt(2, sid2);
                            psInsert.setInt(3, subid2);
                            psInsert.setDate(4, attDate);
                            psInsert.setString(5, status);
                            psInsert.executeUpdate();
                            con.commit();
                            psInsert.close();
                            System.out.println("Attendance Recorded Successfully!");

                        } catch (NumberFormatException e) {
                            System.out.println("Error! IDs must be numbers.");
                        } catch (SQLException e) {
                            con.rollback();
                            System.out.println("Database error: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Unexpected error: " + e.getMessage());
                        }
                        break;

                    case 6: // View Attendance
                        try {
                            Statement st3 = con.createStatement();
                            ResultSet rs3 = st3.executeQuery(
                                    "SELECT s.student_name, sub.subject_name, a.attendance_date, a.status " +
                                    "FROM attendance a " +
                                    "JOIN student s ON a.student_id = s.student_id " +
                                    "JOIN subject sub ON a.subject_id = sub.subject_id " +
                                    "ORDER BY a.attendance_date");
                            System.out.println("\nStudent\t\tSubject\t\tDate\t\tStatus");
                            System.out.println("----------------------------------------------------");
                            boolean found = false;
                            while (rs3.next()) {
                                found = true;
                                System.out.println(rs3.getString(1) + "\t\t" +
                                        rs3.getString(2) + "\t\t" +
                                        rs3.getDate(3) + "\t\t" +
                                        rs3.getString(4));
                            }
                            if (!found) System.out.println("No attendance records found.");
                            rs3.close();
                            st3.close();
                        } catch (Exception e) {
                            System.out.println("Error fetching attendance: " + e.getMessage());
                        }
                        break;

                    case 7: // Date-wise Attendance
                        try {
                            System.out.print("Enter Date (YYYY-MM-DD): ");
                            String d = sc.nextLine().trim();

                            if (!d.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                System.out.println("Invalid date format! Use YYYY-MM-DD.");
                                break;
                            }

                            Date queryDate;
                            try {
                                queryDate = Date.valueOf(d);
                            } catch (IllegalArgumentException ex) {
                                System.out.println("Invalid date! Use YYYY-MM-DD.");
                                break;
                            }

                            PreparedStatement ps4 = con.prepareStatement(
                                    "SELECT s.student_name, sub.subject_name, a.status " +
                                    "FROM attendance a " +
                                    "JOIN student s ON a.student_id = s.student_id " +
                                    "JOIN subject sub ON a.subject_id = sub.subject_id " +
                                    "WHERE a.attendance_date = ?");
                            ps4.setDate(1, queryDate);
                            ResultSet rs4 = ps4.executeQuery();
                            System.out.println("\nStudent\t\tSubject\t\tStatus");
                            System.out.println("--------------------------------------------");
                            boolean found = false;
                            while (rs4.next()) {
                                found = true;
                                System.out.println(rs4.getString(1) + "\t\t" +
                                        rs4.getString(2) + "\t\t" +
                                        rs4.getString(3));
                            }
                            if (!found) System.out.println("No attendance found for this date.");
                            rs4.close();
                            ps4.close();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 8: // Weekly Attendance
                        try {
                            System.out.print("Enter Week Start Date (YYYY-MM-DD): ");
                            String startDateStr = sc.nextLine().trim();
                            System.out.print("Enter Week End Date (YYYY-MM-DD): ");
                            String endDateStr = sc.nextLine().trim();

                            if (!startDateStr.matches("\\d{4}-\\d{2}-\\d{2}") ||
                                !endDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                System.out.println("Invalid date format! Use YYYY-MM-DD.");
                                break;
                            }

                            PreparedStatement ps8 = con.prepareStatement(
                                    "SELECT s.student_name, sub.subject_name, a.status, a.attendance_date " +
                                    "FROM attendance a " +
                                    "JOIN student s ON a.student_id = s.student_id " +
                                    "JOIN subject sub ON a.subject_id = sub.subject_id " +
                                    "WHERE a.attendance_date BETWEEN ? AND ? " +
                                    "ORDER BY a.attendance_date");
                            ps8.setDate(1, Date.valueOf(startDateStr));
                            ps8.setDate(2, Date.valueOf(endDateStr));
                            ResultSet rs8 = ps8.executeQuery();
                            System.out.println("\nStudent\t\tSubject\t\tStatus\t\tDate");
                            System.out.println("----------------------------------------------------");
                            boolean found = false;
                            while (rs8.next()) {
                                found = true;
                                System.out.println(rs8.getString(1) + "\t\t" +
                                        rs8.getString(2) + "\t\t" +
                                        rs8.getString(3) + "\t\t" +
                                        rs8.getDate(4));
                            }
                            if (!found) System.out.println("No attendance found for this week.");
                            rs8.close();
                            ps8.close();
                        } catch (Exception e) {
                            System.out.println("Error! Check your date input: " + e.getMessage());
                        }
                        break;

                    case 9: // Monthly Attendance - FIXED for Oracle (TO_CHAR instead of DATE_FORMAT)
                        try {
                            System.out.print("Enter Month (YYYY-MM): ");
                            String monthStr = sc.nextLine().trim();

                            if (!monthStr.matches("\\d{4}-\\d{2}")) {
                                System.out.println("Invalid format! Use YYYY-MM (e.g. 2024-01).");
                                break;
                            }

                            PreparedStatement ps9 = con.prepareStatement(
                                    "SELECT s.student_name, sub.subject_name, a.status, a.attendance_date " +
                                    "FROM attendance a " +
                                    "JOIN student s ON a.student_id = s.student_id " +
                                    "JOIN subject sub ON a.subject_id = sub.subject_id " +
                                    "WHERE TO_CHAR(a.attendance_date, 'YYYY-MM') = ? " + // FIXED: Oracle syntax
                                    "ORDER BY a.attendance_date");
                            ps9.setString(1, monthStr);
                            ResultSet rs9 = ps9.executeQuery();
                            System.out.println("\nStudent\t\tSubject\t\tStatus\t\tDate");
                            System.out.println("----------------------------------------------------");
                            boolean found = false;
                            while (rs9.next()) {
                                found = true;
                                System.out.println(rs9.getString(1) + "\t\t" +
                                        rs9.getString(2) + "\t\t" +
                                        rs9.getString(3) + "\t\t" +
                                        rs9.getDate(4));
                            }
                            if (!found) System.out.println("No attendance found for this month.");
                            rs9.close();
                            ps9.close();
                        } catch (Exception e) {
                            System.out.println("Error! Check your month input: " + e.getMessage());
                        }
                        break;

                    case 10: // Course-wise Attendance
                        try {
                            System.out.print("Enter Course Name: ");
                            String courseName = sc.nextLine().trim();

                            PreparedStatement ps10 = con.prepareStatement(
                                    "SELECT s.student_name, sub.subject_name, a.status, a.attendance_date " +
                                    "FROM attendance a " +
                                    "JOIN student s ON a.student_id = s.student_id " +
                                    "JOIN subject sub ON a.subject_id = sub.subject_id " +
                                    "WHERE s.course = ? " +
                                    "ORDER BY a.attendance_date");
                            ps10.setString(1, courseName);
                            ResultSet rs10 = ps10.executeQuery();
                            System.out.println("\nStudent\t\tSubject\t\tStatus\t\tDate");
                            System.out.println("----------------------------------------------------");
                            boolean found = false;
                            while (rs10.next()) {
                                found = true;
                                System.out.println(rs10.getString(1) + "\t\t" +
                                        rs10.getString(2) + "\t\t" +
                                        rs10.getString(3) + "\t\t" +
                                        rs10.getDate(4));
                            }
                            if (!found) System.out.println("No attendance found for this course.");
                            rs10.close();
                            ps10.close();
                        } catch (Exception e) {
                            System.out.println("Error! Check your input: " + e.getMessage());
                        }
                        break;

                    case 11: // Delete Student
                        try {
                            System.out.print("Enter Student ID to delete: ");
                            int delId = Integer.parseInt(sc.nextLine().trim()); // FIXED

                            // Delete attendance records first (foreign key)
                            PreparedStatement psA = con.prepareStatement(
                                    "DELETE FROM attendance WHERE student_id = ?");
                            psA.setInt(1, delId);
                            psA.executeUpdate();
                            psA.close();

                            // Then delete student
                            PreparedStatement psDel = con.prepareStatement(
                                    "DELETE FROM student WHERE student_id = ?");
                            psDel.setInt(1, delId);
                            int rows = psDel.executeUpdate();
                            con.commit();
                            psDel.close();

                            if (rows > 0)
                                System.out.println("Student and related attendance deleted successfully!");
                            else
                                System.out.println("Student not found!");
                        } catch (NumberFormatException e) {
                            System.out.println("Error! Student ID must be a number.");
                        } catch (Exception e) {
                            con.rollback();
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 12:
                        System.out.println("Logged out successfully!");
                        break;

                    default:
                        System.out.println("Invalid choice! Enter a number between 1 and 12.");
                }

            } while (choice != 12);

            con.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}