import java.util.Scanner;

public class BankSystem {

    static Scanner sc = new Scanner(System.in);

    static final int MAX_USERS = 10;
    static final int MAX_TXN   = 100;

    static String[] accountNumber  = new String[MAX_USERS];
    static String[] accountHolder  = new String[MAX_USERS];
    static double[] balance        = new double[MAX_USERS];
    static int      totalUsers     = 0;

    static String[][] txnType    = new String[MAX_USERS][MAX_TXN];
    static double[][] txnAmount  = new double[MAX_USERS][MAX_TXN];
    static double[][] txnBalance = new double[MAX_USERS][MAX_TXN];
    static String[][] txnNote    = new String[MAX_USERS][MAX_TXN];
    static int[]      txnCount   = new int[MAX_USERS];

    static int currentUser = -1;

    public static void main(String[] args) {

        System.out.println("===================================");
        System.out.println("  BANK ACCOUNT MANAGEMENT SYSTEM  ");
        System.out.println("===================================");

        int choice = 0;
        while (choice != 4) {
            System.out.println("\n-----------------------------------");
            System.out.println("           MAIN MENU");
            System.out.println("-----------------------------------");
            System.out.println("1. Create New Account");
            System.out.println("2. Login to Account");
            System.out.println("3. List All Accounts");
            System.out.println("4. Exit");
            System.out.println("-----------------------------------");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                createAccount();
            } else if (choice == 2) {
                loginMenu();
            } else if (choice == 3) {
                listAccounts();
            } else if (choice == 4) {
                System.out.println("\nThank you for banking with us. Goodbye!");
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void createAccount() {
        if (totalUsers >= MAX_USERS) {
            System.out.println("Maximum number of accounts reached!");
            return;
        }

        sc.nextLine();
        System.out.print("\nEnter account holder name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter initial deposit: Rs.");
        double initial = sc.nextDouble();

        if (initial < 0) {
            System.out.println("Initial deposit cannot be negative.");
            return;
        }

        String accNo = "ACC" + (1001 + totalUsers);

        accountNumber[totalUsers] = accNo;
        accountHolder[totalUsers] = name;
        balance[totalUsers]       = initial;
        txnCount[totalUsers]      = 0;

        addTransaction(totalUsers, "DEPOSIT", initial, initial, "Initial deposit");

        System.out.println("\nAccount created successfully!");
        System.out.println("Account Number : " + accNo);
        System.out.println("Account Holder : " + name);
        System.out.println("Opening Balance: Rs." + initial);

        totalUsers++;
    }

    static void loginMenu() {
        if (totalUsers == 0) {
            System.out.println("No accounts exist yet. Please create one first.");
            return;
        }

        System.out.print("\nEnter Account Number (e.g. ACC1001): ");
        String input = sc.next().trim().toUpperCase();

        int idx = -1;
        for (int i = 0; i < totalUsers; i++) {
            if (accountNumber[i].equalsIgnoreCase(input)) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            System.out.println("Account not found!");
            return;
        }

        currentUser = idx;
        System.out.println("\nWelcome back, " + accountHolder[currentUser] + "!");
        System.out.println("Current Balance: Rs." + balance[currentUser]);

        accountMenu();
    }

    static void accountMenu() {
        int choice = 0;
        while (choice != 4) {
            System.out.println("\n-----------------------------------");
            System.out.println(" Account: " + accountNumber[currentUser]
                    + " | " + accountHolder[currentUser]);
            System.out.println("-----------------------------------");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transaction History");
            System.out.println("4. Logout");
            System.out.println("-----------------------------------");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                deposit();
            } else if (choice == 2) {
                withdraw();
            } else if (choice == 3) {
                showHistory();
            } else if (choice == 4) {
                System.out.println("Logged out. Goodbye, " + accountHolder[currentUser] + "!");
                currentUser = -1;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void deposit() {
        System.out.print("\nEnter deposit amount: Rs.");
        double amount = sc.nextDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        balance[currentUser] += amount;
        sc.nextLine();
        System.out.print("Enter note (or press Enter to skip): ");
        String note = sc.nextLine().trim();
        if (note.isEmpty()) note = "Deposit";

        addTransaction(currentUser, "DEPOSIT", amount, balance[currentUser], note);
        System.out.println("Deposited Rs." + amount + " successfully!");
        System.out.println("New Balance: Rs." + balance[currentUser]);
    }

    static void withdraw() {
        System.out.print("\nEnter withdrawal amount: Rs.");
        double amount = sc.nextDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        if (amount > balance[currentUser]) {
            System.out.println("Insufficient balance! Available: Rs." + balance[currentUser]);
            return;
        }

        balance[currentUser] -= amount;
        sc.nextLine();
        System.out.print("Enter note (or press Enter to skip): ");
        String note = sc.nextLine().trim();
        if (note.isEmpty()) note = "Withdrawal";

        addTransaction(currentUser, "WITHDRAWAL", amount, balance[currentUser], note);
        System.out.println("Withdrawn Rs." + amount + " successfully!");
        System.out.println("New Balance: Rs." + balance[currentUser]);
    }

    static void showHistory() {
        int u = currentUser;
        System.out.println("\n===================================");
        System.out.println("      TRANSACTION HISTORY");
        System.out.println("  " + accountHolder[u] + " | " + accountNumber[u]);
        System.out.println("===================================");

        if (txnCount[u] == 0) {
            System.out.println("No transactions yet.");
            return;
        }

        for (int i = 0; i < txnCount[u]; i++) {
            System.out.println((i + 1) + ". " + txnType[u][i]
                    + " | Rs." + txnAmount[u][i]
                    + " | Balance: Rs." + txnBalance[u][i]
                    + " | " + txnNote[u][i]);
        }

        System.out.println("-----------------------------------");
        System.out.println("Current Balance: Rs." + balance[u]);
        System.out.println("===================================");
    }

    static void listAccounts() {
        if (totalUsers == 0) {
            System.out.println("\nNo accounts found.");
            return;
        }

        System.out.println("\n===================================");
        System.out.println("         ALL ACCOUNTS");
        System.out.println("===================================");
        for (int i = 0; i < totalUsers; i++) {
            System.out.println((i + 1) + ". " + accountNumber[i]
                    + " | " + accountHolder[i]
                    + " | Rs." + balance[i]);
        }
        System.out.println("===================================");
    }

    static void addTransaction(int userIdx, String type, double amount, double bal, String note) {
        int t = txnCount[userIdx];
        txnType[userIdx][t]    = type;
        txnAmount[userIdx][t]  = amount;
        txnBalance[userIdx][t] = bal;
        txnNote[userIdx][t]    = note;
        txnCount[userIdx]++;
    }
}