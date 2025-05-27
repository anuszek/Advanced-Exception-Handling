 // Zadanie 3
 // Uzupełnij kod, aby w przypadku ujemnego salda konta zgłaszany
 // był własny wyjątek.


class InsufficientBalanceException extends ______ {
   public InsufficientBalanceException(String message) {
       ______;
   }
}

class BankAccount {
   private double balance;
   public void withdraw(double amount) throws InsufficientBalanceException {
       if (amount > balance) {
           throw new ______("Balance cannot be negative!");
       }
       balance -= amount;
   }

    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        // Przetestuj działanie

    }
}