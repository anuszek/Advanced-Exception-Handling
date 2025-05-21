// ## Zadanie 3

// **Polecenie:**  
// Zmodyfikuj kod w pliku `BankAccount.java`, aby w przypadku ujemnego salda konta zgłaszany był własny wyjątek użytkownika.

// ---
//class InsufficientBalanceException extends ______ {
//   public InsufficientBalanceException(String message) {
//       ______;
//   }
//}
//
//class BankAccount {
//   private double balance;
//   public void withdraw(double amount) throws InsufficientBalanceException {
//       if (amount > balance) {
//           throw new ______("Balance cannot be negative!");
//       }
//       balance -= amount;
//   }
//}