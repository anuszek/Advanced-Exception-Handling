//// Task: Complete the code to throw a custom exception if balance is negative
//
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