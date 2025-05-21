//import java.util.Scanner;
//import java.util.InputMismatchException;
//
//// Task: Add a finally block to close the Scanner
//
//public class FinallyBlock {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        ______ {
//            int num = scanner.nextInt();
//            System.out.println("Number: " + num);
//        } ______ (InputMismatchException e) {
//            System.out.println("Invalid input!");
//        } ______ {
//            System.out.println("Closing scanner...");
//        }
//    }
//}