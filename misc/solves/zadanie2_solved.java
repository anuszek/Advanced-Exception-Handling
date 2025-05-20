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