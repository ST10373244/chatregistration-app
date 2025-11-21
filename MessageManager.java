import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageManager {
    private List<Message> messages;
    private int totalMessagesSent;
    
    // Arrays for Part 3 requirements
    private List<String> sentMessages;
    private List<String> disregardedMessages;
    private List<String> storedMessages;
    private List<String> messageHashes;
    private List<String> messageIDs;
    
    public MessageManager() {
        this.messages = new ArrayList<>();
        this.totalMessagesSent = 0;
        
        // Initialize arrays for Part 3
        this.sentMessages = new ArrayList<>();
        this.disregardedMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        this.messageHashes = new ArrayList<>();
        this.messageIDs = new ArrayList<>();
        
        // Load stored messages from JSON file
        loadStoredMessagesFromJSON();
    }
    
    // Add a message to the list and update arrays
    public void addMessage(Message message, int option) {
        messages.add(message);
        
        // Update arrays based on message option
        switch (option) {
            case 1: // Sent
                sentMessages.add(message.getMessage());
                messageHashes.add(message.getMessageHash());
                messageIDs.add(message.getMessageID());
                totalMessagesSent++;
                // Mark message as sent
                message.sentMessage(1);
                break;
            case 2: // Disregarded
                disregardedMessages.add(message.getMessage());
                // Mark message as disregarded
                message.sentMessage(2);
                break;
            case 3: // Stored
                storedMessages.add(message.getMessage());
                // Mark message as stored
                message.sentMessage(3);
                break;
        }
    }
    
    // Load stored messages from JSON file (using ChatGPT for JSON reading)
    private void loadStoredMessagesFromJSON() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("stored_messages.json")));
            // Split by lines since we stored each message as a separate JSON object
            String[] lines = content.split("\n");
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonMessage = new JSONObject(line.trim());
                    storedMessages.add(jsonMessage.getString("message"));
                    messageHashes.add(jsonMessage.getString("messageHash"));
                    messageIDs.add(jsonMessage.getString("messageID"));
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, that's OK
            System.out.println("No stored messages file found yet.");
        }
    }
    
    // Get all messages
    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }
    
    // Get total messages sent
    public int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    // Increment sent messages counter
    public void incrementSentMessages() {
        totalMessagesSent++;
    }
    
    // Print all sent messages
    public String printMessages() {
        if (messages.isEmpty()) {
            return "No messages sent yet.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Recently Sent Messages ===\n");
        
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.isSent()) {
                sb.append("Message ").append(i + 1).append(":\n");
                sb.append(msg.printMessage()).append("\n\n");
            }
        }
        
        return sb.toString();
    }
    
    // Display message details in JOptionPane
    public void displayMessageDetails(Message message) {
        if (message.isSent()) {
            String details = "MessageID: " + message.getMessageID() + "\n" +
                           "Message Hash: " + message.getMessageHash() + "\n" +
                           "Recipient: " + message.getRecipient() + "\n" +
                           "Message: " + message.getMessage();
            
            JOptionPane.showMessageDialog(null, details, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ===== PART 3 METHODS =====
    
    // 2a. Display sender and recipient of all sent messages
    public String displaySentMessagesSenders() {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Senders and Recipients of Sent Messages ===\n");
        
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.isSent()) {
                sb.append("Message ").append(i + 1).append(":\n");
                sb.append("Recipient: ").append(msg.getRecipient()).append("\n");
                sb.append("Message: ").append(msg.getMessage()).append("\n\n");
            }
        }
        
        return sb.toString();
    }
    
    // 2b. Display the longest sent message
    public String displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        
        String longestMessage = "";
        for (String message : sentMessages) {
            if (message.length() > longestMessage.length()) {
                longestMessage = message;
            }
        }
        
        return "Longest Message: " + longestMessage + "\nLength: " + longestMessage.length() + " characters";
    }
    
    // 2c. Search for a message ID and display the corresponding recipient and message
    public String searchMessageByID(String messageID) {
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.getMessageID().equals(messageID)) {
                return "Message Found:\n" +
                       "Message ID: " + msg.getMessageID() + "\n" +
                       "Recipient: " + msg.getRecipient() + "\n" +
                       "Message: " + msg.getMessage();
            }
        }
        
        // Also check stored messages from JSON
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(messageID)) {
                return "Message Found (Stored):\n" +
                       "Message ID: " + messageIDs.get(i) + "\n" +
                       "Message Hash: " + messageHashes.get(i) + "\n" +
                       "Message: " + storedMessages.get(i);
            }
        }
        
        return "Message with ID " + messageID + " not found.";
    }
    
    // 2d. Search for all messages sent to a particular recipient - FIXED VERSION
    public String searchMessagesByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        
        sb.append("Messages for recipient: ").append(recipient).append("\n");
        sb.append("================================\n");
        
        // Check ALL messages (not just sent ones)
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.getRecipient().equals(recipient)) {
                String status = msg.isSent() ? "SENT" : (msg.isStored() ? "STORED" : "DISREGARDED");
                sb.append(status).append(": ").append(msg.getMessage()).append("\n");
                found = true;
            }
        }
        
        if (!found) {
            return "No messages found for recipient: " + recipient;
        }
        
        return sb.toString();
    }
    
    // 2e. Delete a message using the message hash
    public String deleteMessageByHash(String messageHash) {
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.getMessageHash().equals(messageHash)) {
                String deletedMessage = msg.getMessage();
                messages.remove(i);
                
                // Also remove from arrays
                if (sentMessages.contains(deletedMessage)) {
                    sentMessages.remove(deletedMessage);
                }
                if (messageHashes.contains(messageHash)) {
                    messageHashes.remove(messageHash);
                }
                if (messageIDs.contains(msg.getMessageID())) {
                    messageIDs.remove(msg.getMessageID());
                }
                
                return "Message \"" + deletedMessage + "\" successfully deleted.";
            }
        }
        
        return "Message with hash " + messageHash + " not found.";
    }
    
    // 2f. Display a report that lists the full details of all sent messages
    public String displayMessageReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to display in report.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== MESSAGE REPORT ===\n");
        sb.append("======================\n\n");
        
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.isSent()) {
                sb.append("Message ").append(i + 1).append(":\n");
                sb.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
                sb.append("Recipient: ").append(msg.getRecipient()).append("\n");
                sb.append("Message: ").append(msg.getMessage()).append("\n");
                sb.append("----------------------------------------\n");
            }
        }
        
        return sb.toString();
    }
    
    // Getters for unit testing
    public List<String> getSentMessages() { return new ArrayList<>(sentMessages); }
    public List<String> getDisregardedMessages() { return new ArrayList<>(disregardedMessages); }
    public List<String> getStoredMessages() { return new ArrayList<>(storedMessages); }
    public List<String> getMessageHashes() { return new ArrayList<>(messageHashes); }
    public List<String> getMessageIDs() { return new ArrayList<>(messageIDs); }
}