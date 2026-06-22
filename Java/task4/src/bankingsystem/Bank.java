package bankingsystem;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private final SecureRandom random = new SecureRandom();

    // a little overkill but why not
    private int generateUniqueId() {
        int id;
        do {
            id = 100000 + random.nextInt(900000);
        } while (accounts.containsKey(id));
        return id;
    }

    public Account createAccount(String name) {
        int id = generateUniqueId();
        Account a = new Account(id, name);
        accounts.put(id, a);
        return a;
    }

    public Account getAccount(int id) {
        Account a = accounts.get(id);
        if (a == null)
            throw new IllegalArgumentException("Account not found");
        return a;
    }

    public void deleteAccount(int id) {
        if (!accounts.containsKey(id))
            throw new IllegalArgumentException("Account not found");
        else if (getAccount(id).getBalance() != 0) {
            throw new IllegalArgumentException("Cant delete account with non-zero balance , it is illegal or smth");
        }
        accounts.remove(id);
    }

    public void transfer(int fromId, int toId, double amount) {
        Account from = accounts.get(fromId);
        if (from == null)
            throw new IllegalArgumentException("Source account not found: " + fromId);

        Account to = accounts.get(toId);
        if (to == null)
            throw new IllegalArgumentException("Destination account not found: " + toId);

        from.withdraw(amount);
        to.deposit(amount);
    }

}