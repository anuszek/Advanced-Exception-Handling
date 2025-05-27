// Zadanie 1
// Uzupełnij obsługę błędów, stosując odpowiednie wyjątki
// klas ArithmeticException oraz NullPointerException.


public class MultiCatch {
    public static void main(String[] args) {
        try {
            int result = 10 / 0;
            String str = null;
            System.out.println(str.length());
       } catch (______ e) {
        System.out.println("Error: " + e.getMessage());
       }
    }
}
