package bankingsystem;

import java.util.Scanner;

public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bank bank = new Bank();
    private static final String delimiter = "\\n";

    // main prompt
    private static void displayPrompt() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to my simple banking system :)");
        System.out.println("-----------------------------------------------------");
        System.out.println("Please enter the desired operation:");
        System.out.println("1. Create Account");
        System.out.println("2. Delete Account");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Check Balance");
        System.out.println("6. Transfer");
        System.out.println("0. Exit");
        System.out.println("-----------------------------------------------------");
    }

    // validation
    private static boolean isValidOperation(int operation) {
        return operation >= 1 && operation <= 6;
    }

    // operation routing
    private static void executeOperation(int operation) {
        switch (operation) {
            case 1 -> createAccount();
            case 2 -> deleteAccount();
            case 3 -> deposit();
            case 4 -> withdraw();
            case 5 -> checkBalance();
            case 6 -> transfer();
        }
    }

    // operation methods
    private static void createAccount() {
        System.out.print("Enter account holder name: ");
        String name = scanner.nextLine();
        Account a = bank.createAccount(name);
        System.out.println("Account created successfully. Account #: " + a.getId());
    }

    private static void deleteAccount() {
        System.out.print("Enter account #: ");
        int id = getSafeInteger();
        bank.deleteAccount(id);
        System.out.println("Account deleted successfully.");
    }

    private static void deposit() {
        System.out.print("Enter account #: ");
        int id = getSafeInteger();
        System.out.print("Enter amount: ");
        double amount = getSafeDouble();
        bank.getAccount(id).deposit(amount);
        System.out.println("Deposit successful.");
    }

    private static void withdraw() {
        System.out.print("Enter account #: ");
        int id = getSafeInteger();
        System.out.print("Enter amount: ");
        double amount = getSafeDouble();
        bank.getAccount(id).withdraw(amount);
        System.out.println("Withdrawal successful.");
    }

    private static void checkBalance() {
        System.out.print("Enter account #: ");
        int id = getSafeInteger();
        Account a = bank.getAccount(id);
        System.out.println("Current balance: $" + a.getBalance());
    }

    private static void transfer() {
        System.out.print("Enter source account #: ");
        int fromId = getSafeInteger();

        System.out.print("Enter destination account #: ");
        int toId = getSafeInteger();

        // just to make sure both accounts exist and are valid instead of waiting for
        // "bank.transfer()" to fail
        bank.getAccount(fromId);
        bank.getAccount(toId);

        System.out.print("Enter amount: ");
        double amount = getSafeDouble();
        bank.transfer(fromId, toId, amount);
        System.out.println("Transfer successful.");
    }

    // helper methods
    private static int getSafeInteger() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static double getSafeDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    // main
    public static void main(String[] args) {
        while (true) {
            displayPrompt();

            scanner.useDelimiter(delimiter);
            int operation = getSafeInteger();

            if (operation == 0) {
                System.out.println("Goodbye mon ami!");
                break;
            }

            if (!isValidOperation(operation)) {
                System.out.println("Invalid operation!");
                continue;
            }

            try {
                executeOperation(operation);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                System.out.print("\nPress 1 to continue or 0 to exit: ");
                int choice = getSafeInteger();
                if (choice == 0) {
                    System.out.println("Goodbye mon ami!");
                    break;
                }
            }
        }
    }

}