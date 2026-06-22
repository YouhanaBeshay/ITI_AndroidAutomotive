package bankingsystem;

public class Account {
    private final int id;
    private String name;
    private double balance;

    public Account(int id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Dont try to be smart my man , amount must be positive");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Dont try to be smart my man , amount must be positive");
        if (amount > balance)
            throw new IllegalArgumentException("Okay i dont know how to break this to you, but you are P.O.O.R");
        balance -= amount;
    }
}