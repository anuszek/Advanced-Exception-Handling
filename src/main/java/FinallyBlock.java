import java.util.Scanner;
import java.util.InputMismatchException;
//
// ## Zadanie 2

// **Polecenie:**  
// Uzupełnij blok `finally` oraz kod w pliku `FinallyBlock.java`,
// aby obiekt `scanner` został zamknięty.
//
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