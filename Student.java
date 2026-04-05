

import java.util.ArrayList;
import java.util.Scanner;

class StudentRecord {
    int id;
    String name;
    boolean present;

    StudentRecord(int id, String name) {
        this.id = id;
        this.name = name;
        this.present = false;
    }
}

public class Student {   // 🔴 MUST match file name

    static ArrayList<StudentRecord> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n--- Student Attendance Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. Mark Attendance");
            System.out.println("3. View Attendance");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    markAttendance();
                    break;
                case 3:
                    viewAttendance();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    static void addStudent() {
        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();

        students.add(new StudentRecord(id, name));
        System.out.println("Student added successfully");
    }

    static void markAttendance() {
        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();

        for (StudentRecord s : students) {
            if (s.id == id) {
                System.out.print("Present? (true/false): ");
                s.present = sc.nextBoolean();
                System.out.println("Attendance marked");
                return;
            }
        }
        System.out.println("Student not found");
    }

    static void viewAttendance() {
        System.out.println("\nID\tName\tAttendance");
        for (StudentRecord s : students) {
            System.out.println(s.id + "\t" + s.name + "\t" +
                    (s.present ? "Present" : "Absent"));
        }
    }
}
