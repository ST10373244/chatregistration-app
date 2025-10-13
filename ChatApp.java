import java.util.Scanner;
import javax.swing.JOptionPane;

public class ChatApp {
    private static MessageManager messageManager = new MessageManager();
    
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
        
        // If registration successful, proceed to login and messaging
        if (registrationResult.contains("successfully")) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();
            
            // Attempt login
            String loginResult = userLogin.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginResult);
            
            // If login successful, proceed to messaging system
            if (loginResult.contains("Welcome")) {
                messagingSystem(scanner);
            }
        }
        
        scanner.close();
    }
    
    private static void messagingSystem(Scanner scanner) {
        System.out.println("\n=== Welcome to QuickChat ===");
        
        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Enter your choice (1-3): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    // Uncomment the line below when ready to implement
                    // System.out.println(messageManager.printMessages());
                    break;
                case "3":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }
    
    private static void sendMessages(Scanner scanner) {
        System.out.print("How many messages do you wish to send? ");
        int numMessages;
        
        try {
            numMessages = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Please enter a valid integer.");
            return;
        }
        
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n=== Message " + (i + 1) + " ===");
            
            System.out.print("Enter recipient cell number (with international code): ");
            String recipient = scanner.nextLine();
            
            System.out.print("Enter your message (max 250 characters): ");
            String messageText = scanner.nextLine();
            
            // Create message object
            Message message = new Message(recipient, messageText);
            
            // Validate message length
            String lengthStatus = message.getMessageLengthStatus();
            System.out.println(lengthStatus);
            
            if (!message.checkMessageLength()) {
                System.out.println("Message not sent due to length restrictions.");
                continue;
            }
            
            // Validate recipient number
            String recipientStatus = message.getRecipientNumberStatus();
            System.out.println(recipientStatus);
            
            if (message.checkRecipientCell() == -1) {
                System.out.println("Message not sent due to invalid recipient number.");
                continue;
            }
            
            // Show message options
            System.out.println("\nChoose an option for this message:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Enter your choice (1-3): ");
            
            String optionStr = scanner.nextLine();
            int option;
            
            try {
                option = Integer.parseInt(optionStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Message disregarded.");
                option = 2;
            }
            
            String result = message.sentMessage(option);
            System.out.println(result);
            
            // If message was sent, add to manager and display details
            if (option == 1) {
                messageManager.addMessage(message);
                messageManager.incrementSentMessages();
                messageManager.displayMessageDetails(message);
            }
        }
        
        // Display total messages sent
        System.out.println("\nTotal number of messages sent: " + messageManager.returnTotalMessages());
    }
}