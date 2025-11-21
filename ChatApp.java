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
                // Add test data for Part 3 - FIXED VERSION
                addTestData();
                messagingSystem(scanner);
            }
        }
        
        scanner.close();
    }
    
    // Add test data for Part 3 arrays - FIXED VERSION
    private static void addTestData() {
        // Test Data Message 1 - Sent
        Message msg1 = new Message("+27834557896", "Did you get the cake?");
        messageManager.addMessage(msg1, 1);
        
        // Test Data Message 2 - SENT (FIXED - for longest message and recipient search)
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        messageManager.addMessage(msg2, 1);  // SENT
        
        // Test Data Message 3 - Disregarded
        Message msg3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        messageManager.addMessage(msg3, 2);
        
        // Test Data Message 4 - Sent
        Message msg4 = new Message("0838884567", "It is dinner time!");
        messageManager.addMessage(msg4, 1);
        
        // Test Data Message 5 - SENT (FIXED - was stored, now SENT for recipient search)
        Message msg5 = new Message("+27838884567", "Ok, I am leaving without you.");
        messageManager.addMessage(msg5, 1);  // CHANGED FROM 3 TO 1
        
        System.out.println("Test data loaded successfully for Part 3!");
    }
    
    private static void messagingSystem(Scanner scanner) {
        System.out.println("\n=== Welcome to QuickChat ===");
        
        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Message Reports and Search (Part 3)");
            System.out.println("4) Quit");
            System.out.print("Enter your choice (1-4): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    System.out.println(messageManager.printMessages());
                    break;
                case "3":
                    messageReportsMenu(scanner);
                    break;
                case "4":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
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
            
            // Add message to manager with the option
            messageManager.addMessage(message, option);
            
            // If message was sent, display details
            if (option == 1) {
                messageManager.incrementSentMessages();
                messageManager.displayMessageDetails(message);
            }
        }
        
        // Display total messages sent
        System.out.println("\nTotal number of messages sent: " + messageManager.returnTotalMessages());
    }
    
    private static void messageReportsMenu(Scanner scanner) {
        boolean inMenu = true;
        
        while (inMenu) {
            System.out.println("\n=== Message Reports and Search ===");
            System.out.println("1) Display senders and recipients of sent messages");
            System.out.println("2) Display longest message");
            System.out.println("3) Search message by ID");
            System.out.println("4) Search messages by recipient");
            System.out.println("5) Delete message by hash");
            System.out.println("6) Display full message report");
            System.out.println("7) Return to main menu");
            System.out.print("Enter your choice (1-7): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.println(messageManager.displaySentMessagesSenders());
                    break;
                case "2":
                    System.out.println(messageManager.displayLongestMessage());
                    break;
                case "3":
                    System.out.print("Enter Message ID to search: ");
                    String messageID = scanner.nextLine();
                    System.out.println(messageManager.searchMessageByID(messageID));
                    break;
                case "4":
                    System.out.print("Enter recipient number to search: ");
                    String recipient = scanner.nextLine();
                    System.out.println(messageManager.searchMessagesByRecipient(recipient));
                    break;
                case "5":
                    System.out.print("Enter Message Hash to delete: ");
                    String messageHash = scanner.nextLine();
                    System.out.println(messageManager.deleteMessageByHash(messageHash));
                    break;
                case "6":
                    System.out.println(messageManager.displayMessageReport());
                    break;
                case "7":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-7.");
            }
            
            if (inMenu) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
}