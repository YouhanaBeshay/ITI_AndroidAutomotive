// ConsoleApp.java
package bankingsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bank bank = new Bank();
    private static final String delimiter = "\\n";

    // for the receipt
    private static final String receipt_prefix = "receipt_";
    private static Account currentAccount = null;

    // main prompt
    private static void displayPrompt() {

        // not logged in
        if (currentAccount == null) {
            System.out.println("-----------------------------------");
            System.out.println("Welcome to my OOP Banking System :)");
            System.out.println("-----------------------------------");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.println("-----------------------------------");

        } else {
            System.out.println("-----------------------------------------------------");
            System.out.println("Welcome, " + currentAccount.getName() + "!");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Check Balance (Create receipt)");
            System.out.println("5. View Account Info");
            System.out.println("6. Logout");
            System.out.println("0. Exit");
            System.out.println("-----------------------------------------------------");
        }
    }

    // validation
    private static boolean isValidOperation(int operation) {
        if (currentAccount == null) {
            return operation >= 1 && operation <= 2;
        } else {
            return operation >= 1 && operation <= 6;
        }
    }

    // operation routing
    private static void executeOperation(int operation) {
        if (currentAccount == null) {
            switch (operation) {
                case 1 -> createAccount();
                case 2 -> login();
            }
        } else {
            switch (operation) {
                case 1 -> deposit();
                case 2 -> withdraw();
                case 3 -> transfer();
                case 4 -> checkBalance();
                case 5 -> viewAccountInfo();
                case 6 -> logout();
            }
        }
    }

    // operation methods
    private static void createAccount() {
        System.out.print("Enter account name: ");
        String name = scanner.nextLine();
        System.out.print("Set password: ");
        String password = scanner.nextLine();
        Account a = bank.createAccount(name, password);
        System.out.println("Account created successfully. Account #: " + a.getId());
        System.out.println("Please copy your account number you will need it :)");
    }

    private static void login() {
        System.out.print("Enter account #: ");
        int id = getSafeInteger();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        currentAccount = bank.login(id, password);
        System.out.println("Login successful!");
    }

    private static void deposit() {
        System.out.print("Enter amount: ");
        double amount = getSafeDouble();
        currentAccount.deposit(amount);
        System.out.println("Deposit successful. New balance: $" + currentAccount.getBalance());
    }

    private static void withdraw() {
        System.out.print("Enter amount: ");
        double amount = getSafeDouble();
        currentAccount.withdraw(amount);
        System.out.println("Withdrawal successful. New balance: $" + currentAccount.getBalance());
    }

    private static void transfer() {
        System.out.print("Enter destination account #: ");
        int toId = getSafeInteger();

        // validate destination account exists before asking for amount
        bank.getAccount(toId);

        System.out.print("Enter amount: ");
        double amount = getSafeDouble();
        bank.transfer(currentAccount.getId(), toId, amount);
        System.out.println("Transfer successful. New balance: $" + currentAccount.getBalance());
    }

    private static void checkBalance() {
        double balance = currentAccount.getBalance();
        System.out.println("Current balance: $" + balance);

        // create thread to write receipt file 
        Thread receiptThread = new Thread(new ReceiptWriter(currentAccount, balance));
        receiptThread.start();
        System.out.println("Receipt is being generated in the background...");
    }

    private static void viewAccountInfo() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Account Information");
        System.out.println("-----------------------------------------------------");
        System.out.println("Account Name: " + currentAccount.getName());
        System.out.println("Account Number: " + currentAccount.getId());
        System.out.println("Current Balance: $" + currentAccount.getBalance());
        System.out.println("-----------------------------------------------------");
    }

    private static void logout() {
        currentAccount = null;
        System.out.println("Logged out successfully.");
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

    // receipt thread inner class
    private static class ReceiptWriter implements Runnable {
        private final String name;
        private final int id;
        private final double balance;

        public ReceiptWriter(Account account, double balance) {
            this.name = account.getName();
            this.id = account.getId();
            this.balance = balance;
        }

        @Override
        public void run() {
            String filename = receipt_prefix + id + ".txt";
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("========================================\n");
                writer.write("          BANKING RECEIPT\n");
                writer.write("========================================\n");
                writer.write("Account Name: " + name + "\n");
                writer.write("Account Number: " + id + "\n");
                writer.write("Balance: $" + balance + "\n");
                writer.write("Date: " + new java.util.Date() + "\n");
                writer.write("========================================\n");
                System.out.println("\nReceipt saved to: " + filename);
            } catch (IOException e) {
                System.out.println("\nFailed to write receipt: " + e.getMessage());
            }
        }
    }
}