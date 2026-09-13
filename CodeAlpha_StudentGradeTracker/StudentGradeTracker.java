package codealpha_tasks.CodeAlpha_StudentGradeTracker;

import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private String name;
    private ArrayList<Double> grades;

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public double calculateAverage() {
        if (grades.isEmpty()) return 0.0;
        double sum = 0;
        for (double g : grades) {
            sum += g;
        }
        return sum / grades.size();
    }
}

public class StudentGradeTracker {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== STUDENT GRADE TRACKER ===");
            System.out.println("1. Add Student & Grades");
            System.out.println("2. Display Summary Report");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    displayReport();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting Grade Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        Student student = new Student(name);
        System.out.print("How many grades do you want to enter for " + name + "? ");
        int count = getIntInput();

        for (int i = 1; i <= count; i++) {
            System.out.print("Enter grade #" + i + " (0-100): ");
            double grade = getDoubleInput();
            if (grade >= 0 && grade <= 100) {
                student.addGrade(grade);
            } else {
                System.out.println("Invalid score. Defaulting to 0.0.");
                student.addGrade(0.0);
            }
        }

        students.add(student);
        System.out.println("Student record added successfully.");
    }

    private static void displayReport() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        System.out.println("\n------------------------------------------------------------");
        System.out.printf("%-20s %-25s %-10s\n", "Student Name", "Grades", "Average");
        System.out.println("------------------------------------------------------------");

        double totalClassSum = 0;
        int totalClassGradesCount = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        String topStudent = "N/A";
        String lowStudent = "N/A";

        for (Student s : students) {
            System.out.printf("%-20s %-25s %-10.2f\n", s.getName(), s.getGrades().toString(), s.calculateAverage());
            for (double g : s.getGrades()) {
                totalClassSum += g;
                totalClassGradesCount++;

                if (g > highest) {
                    highest = g;
                    topStudent = s.getName() + " (" + g + ")";
                }
                if (g < lowest) {
                    lowest = g;
                    lowStudent = s.getName() + " (" + g + ")";
                }
            }
        }

        System.out.println("------------------------------------------------------------");
        if (totalClassGradesCount > 0) {
            double classAvg = totalClassSum / totalClassGradesCount;
            System.out.printf("Class Average Score : %.2f\n", classAvg);
            System.out.println("Highest Score       : " + topStudent);
            System.out.println("Lowest Score        : " + lowStudent);
        }
        System.out.println("------------------------------------------------------------");
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter an integer: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }
}
