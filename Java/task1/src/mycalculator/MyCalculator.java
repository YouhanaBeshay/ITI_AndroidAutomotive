import java.util.Scanner;

public class MyCalculator {

    // ======= Members ========
    // static scanner to be used in all methods
    private static Scanner scanner = new Scanner(System.in);

    private static String delimiter = "\n"; // delim for the scanner

    // ======= Methods ========

    // main prompt
    private static void displayPrompt() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to my first Java app - a simple calculator :)");
        System.out.println("-----------------------------------------------------");
        System.out.println("Please enter the desired operation:");
        System.out.println("1. Add");
        System.out.println("2. Subtract");
        System.out.println("3. Multiply");
        System.out.println("4. Divide");
        System.out.println("5. Exit");
        System.out.println("-----------------------------------------------------");
    }

    // helper methods
    private static int getSafeInteger() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            scanner.next(); // to clear the buffer
        }
        return scanner.nextInt();
    }

    private static double getSafeDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid double: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    // CLEAR SCREEN (terminal)
    // Credits:
    // https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
    public static void clearScreen() {
        System.out.print("\033\143");
    }

    // main
    public static void main(String[] args) {
        while (true) {

            displayPrompt();

            // make scanner delimter the new line instead of white space
            // (so we dont take "word 1" as "1")
            scanner.useDelimiter(delimiter);

            int operation = getSafeInteger();
            if (operation == 5) {
                // exit
                break;
            }

            System.out.print("Please enter the first number: ");
            double a = getSafeDouble();
            System.out.print("Please enter the second number: ");
            double b = getSafeDouble();

            switch (operation) {
                case 1:
                    System.out.println("\n" + a + " + " + b + " = " + (a + b));
                    break;
                case 2:
                    System.out.println("\n" + a + " - " + b + " = " + (a - b));
                    break;
                case 3:
                    System.out.println("\n" + a + " * " + b + " = " + (a * b));
                    break;
                case 4:
                    if (b == 0) {
                        System.out.println("Cannot divide by zero!");
                        continue;
                    }
                    System.out.println("\n" + a + " / " + b + " = " + (a / b));
                    break;
                default:
                    System.out.println("\nInvalid operation!");
                    break;
            }

            System.out.print("Press 1 to continue or 0 to exit: ");
            int choice = getSafeInteger();
            if (choice == 0) {
                break;
            }
            clearScreen();

        }

        System.out.println("Goodbye fellow Java enjoyer!");
        scanner.close();

    }

}
