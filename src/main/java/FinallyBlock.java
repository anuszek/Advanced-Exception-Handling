import java.util.Scanner;
import java.util.InputMismatchException;

// Task: Add a finally block to close the Scanner
//Scanner scanner = new Scanner(System.in);
// ______ {
//        int num = scanner.nextInt();
//    System.out.println("Number: " + num);
//} ______ (InputMismatchException e) {
//        System.out.println("Invalid input!");
//} ______ {
//    System.out.println("Closing scanner...");
//}


public class FinallyBlock {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter a number: ");
            int num = scanner.nextInt();
            System.out.println("Number: " + num);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input!");
        } finally {
            System.out.println("Closing scanner...");
            scanner.close();
        }
    }
}