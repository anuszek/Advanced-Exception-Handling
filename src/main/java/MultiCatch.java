// Task: Complete the catch block to handle both ArithmeticException and NullPointerException
//try {
//int result = 10 / 0;
//String str = null;
//    System.out.println(str.length());
//        } catch (______ e) {
//        System.out.println("Error: " + e.getMessage());
//        }

public class MultiCatch {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; // ArithmeticException
            String str = null;
            System.out.println(str.length()); // NullPointerException
        } catch (ArithmeticException | NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
