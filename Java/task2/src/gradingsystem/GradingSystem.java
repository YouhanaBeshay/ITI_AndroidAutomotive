import java.util.Scanner;

public class GradingSystem {

    // ======= Members =======
    private static Scanner scanner = new Scanner(System.in);
    private static String delimiter = "\n";

    // ======= Methods =======
    private static void displayPrompt() {
        System.out.println("---------------------------------------------------");
        System.out.println("Welcome to my very simple student grading system :)");
        System.out.println("---------------------------------------------------");
        System.out.print("Please enter the Student Name: ");
    }

    // helper methods (SCANNER)
    private static int getSafeInteger() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            scanner.next(); // to clear the buffer
        }
        return scanner.nextInt();
    }

    private static int getSafeMark() {
        int mark = getSafeInteger();
        while (mark < 0 || mark > 100) {
            System.out.print("Please enter a valid mark (0-100): ");
            mark = getSafeInteger();
        }
        return mark;
    }

    private static int getSafeSubjects() {
        int subjects = getSafeInteger();
        while (subjects < 1 || subjects > 5) {
            System.out.print("Please enter a valid number of subjects (1-5) ");
            subjects = getSafeInteger();
        }
        return subjects;
    }

    // GRADE:
    private static char getGrade(double precentage) {
        if (precentage >= 90) {
            return 'A';
        } else if (precentage >= 80) {
            return 'B';
        } else if (precentage >= 70) {
            return 'C';
        } else if (precentage >= 60) {
            return 'D';
        } else {
            return 'F';
        }
    }

    // CLEAR SCREEN (terminal)
    // Credits:
    // https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
    public static void clearScreen() {
        System.out.print("\033\143");
    }

    public static void main(String[] args) {
        while (true) {

            displayPrompt();

            scanner.useDelimiter(delimiter);

            String studentName = scanner.next();
            System.out.print("Please enter the number of registered subjects (1-5): ");

            int subjects = getSafeSubjects();
            int totalMarks = 0;
            for (int i = 0; i < subjects; i++) {
                System.out.print("Please enter the mark for subject " + (i + 1) + ": ");
                totalMarks += getSafeMark();
            }
            double precentage = (double) totalMarks / (subjects * 100) * 100;

            System.out.println("----------Student Details--------------");
            System.out.println("Student Name: " + studentName);
            System.out.println("Total Marks: " + totalMarks + " / " + subjects * 100);
            System.out.println("Percentage: " + precentage + "%");
            System.out.println("Grade: " + getGrade(precentage));
            System.out.println("---------------------------------------");

            System.out.print("Press 1 to continue or 0 to exit: ");
            int choice = getSafeInteger();
            if (choice == 0) {
                break;
            }
            clearScreen();

        }
        System.out.println("Goodbye o7");
    }
}
