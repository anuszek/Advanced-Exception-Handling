// Task: Complete the code to throw a custom exception if balance is negative
//class InsufficientBalanceException extends ______ {
//    public InsufficientBalanceException(String message) {
//        ______;
//    }
//}
//
//class BankAccount {
//    private double balance;
//    public void withdraw(double amount) throws InsufficientBalanceException {
//        if (amount > balance) {
//            throw new ______("Balance cannot be negative!");
//        }
//        balance -= amount;
//    }
//}


class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class BankAccount {
    private double balance = 100.0;

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException("Balance cannot be negative!");
        }
        balance -= amount;
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        try {
            account.withdraw(150.0); // Test insufficient balance
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}