import java.util.Scanner;

public class ChatApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Registration ===");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter username (must contain _ and be less than or equal to 5 characters): ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password (must be greater than or equal to 8 chars, with capital, number, and special char): ");
        String password = scanner.nextLine();
        
        System.out.print("Enter cell number (with international code, e.g., +27838968976): ");
        String cellNumber = scanner.nextLine();
        
        // Create login object
        Login userLogin = new Login(firstName, lastName, username, password, cellNumber);
        
        // Register user
        String registrationResult = userLogin.registerUser();
        System.out.println(registrationResult);
        
        // If registration successful, proceed to login
        if (registrationResult.contains("successfully")) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();
            
            // Attempt login
            String loginResult = userLogin.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginResult);
        }
        
        scanner.close();
    }
}