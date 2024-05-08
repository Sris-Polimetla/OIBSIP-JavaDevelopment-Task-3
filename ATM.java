import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ATM {
    private Map<String, String> users;
    private Map<String, List<String>> transactionHistory;
    private String currentUser;
    private double balance;

    public ATM() {
        this.users = new HashMap<>();
        this.transactionHistory = new HashMap<>();
        this.balance = 0.0;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ATM!");

        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Quit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerUser(Scanner scanner) {
        System.out.println("\nUser Registration:");
        System.out.print("Enter desired user ID: ");
        String userId = scanner.nextLine();
        if (users.containsKey(userId)) {
            System.out.println("User ID already exists. Please choose a different one.");
            return;
        }
        System.out.print("Enter desired PIN: ");
        String pin = scanner.nextLine();
        users.put(userId, pin);
        transactionHistory.put(userId, new ArrayList<>());
        System.out.println("User registered successfully!");
    }

    private void loginUser(Scanner scanner) {
        System.out.println("\nUser Login:");
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        if (!users.containsKey(userId)) {
            System.out.println("User ID not found. Please register first.");
            return;
        }
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        if (!users.get(userId).equals(pin)) {
            System.out.println("Invalid PIN. Please try again.");
            return;
        }
        currentUser = userId;
        System.out.println("Login successful. Welcome, " + currentUser + "!");
        showLoggedInMenu(scanner);
    }

    private void showLoggedInMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nLogged In Menu:");
            System.out.println("1. View Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Your current balance is: $" + balance);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: $");
                    double withdrawAmount = scanner.nextDouble();
                    withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: $");
                    double depositAmount = scanner.nextDouble();
                    deposit(depositAmount);
                    break;
                case 4:
                    System.out.print("Enter recipient's user ID: ");
                    String recipientId = scanner.nextLine();
                    System.out.print("Enter amount to transfer: $");
                    double transferAmount = scanner.nextDouble();
                    transfer(recipientId, transferAmount);
                    break;
                case 5:
                    displayTransactionHistory();
                    break;
                case 6:
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawal successful. Remaining balance: $" + balance);
            addToTransactionHistory("Withdrawal: -$" + amount);
        } else {
            System.out.println("Insufficient funds for withdrawal.");
        }
    }

    private void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. New balance: $" + balance);
        addToTransactionHistory("Deposit: +$" + amount);
    }

    private void transfer(String recipientId, double amount) {
        if (!users.containsKey(recipientId)) {
            System.out.println("Recipient ID not found. Please enter a valid user ID.");
            return;
        }
        System.out.print("Enter your PIN to confirm the transfer: ");
        Scanner scanner = new Scanner(System.in);
        String pin = scanner.nextLine();
        if (!users.get(currentUser).equals(pin)) {
            System.out.println("Invalid PIN. Transfer cancelled.");
            return;
        }
        if (balance < amount) {
            System.out.println("Insufficient funds for transfer.");
            return;
        }

        // Perform transfer
        balance -= amount;
        double recipientBalance = 0;
        for (Map.Entry<String, String> entry : users.entrySet()) {
            if (entry.getKey().equals(recipientId)) {
                // Update recipient's balance
                recipientBalance = balance + amount;
                break;
            }
        }
        System.out.println("Transfer of $" + amount + " to user " + recipientId + " successful.");
        System.out.println("Remaining balance: $" + balance);
        addToTransactionHistory("Transfer to " + recipientId + ": -$" + amount);
        users.put(currentUser, Double.toString(balance));
        users.put(recipientId, Double.toString(recipientBalance));
    }

    private void addToTransactionHistory(String transaction) {
        List<String> history = transactionHistory.get(currentUser);
        history.add(transaction);
        transactionHistory.put(currentUser, history);
    }

    private void displayTransactionHistory() {
        System.out.println("\nTransaction History:");
        List<String> history = transactionHistory.get(currentUser);
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (String transaction : history) {
                System.out.println(transaction);
            }
        }
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
