package oopcalculator;
import java.util.Scanner;

import oopcalculator.strategies.IOperationStrategy;
import oopcalculator.factory.OperationFactory;

public class MyNewCalculator {

    // ======= Members ========
    // static scanner to be used in all methods
    private static Scanner scanner = new Scanner(System.in);

    private static final OperationFactory operationFactory = new OperationFactory();

    private static String delimiter = "\n"; // delim for the scanner

    // ======= Methods ========

    // main prompt
    private static void displayPrompt() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to my OOP simple calculator :)");
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
            if (!operationFactory.isValidStrategy(operation)) {
                System.out.println("Invalid operation!");
                continue;
            }

            System.out.print("Please enter the first number: ");
            double a = getSafeDouble();
            System.out.print("Please enter the second number: ");
            double b = getSafeDouble();

            try {

                IOperationStrategy strategy = operationFactory.getStrategy(operation);
                double result = strategy.execute(a, b);
                System.out.println("\n" + a + " " + strategy.getOperator() + " " + b + " = " + result);

            } catch (Exception e) {
                System.out.println(e.getMessage());

            } finally {
                System.out.print("\nPress 1 to continue or 0 to exit: ");
                int choice = getSafeInteger();
                if (choice == 0) {
                    break;
                }
                clearScreen();
            }

        }
    }
}
