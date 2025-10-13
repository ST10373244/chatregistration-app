import java.util.Random;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class Message {
    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;
    private boolean sent;
    private boolean stored;
    
    // Constructor
    public Message(String recipient, String message) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
        this.sent = false;
        this.stored = false;
    }
    
    // Generate random 10-digit message ID
    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }
    
    // Check if message ID is valid (not more than 10 characters)
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
    
    // Check recipient cell number format - STRICT VALIDATION (MUST start with +)
    public int checkRecipientCell() {
        if (recipient == null || recipient.isEmpty()) {
            return -1;
        }
        
        // Remove any spaces or dashes
        String cleanNumber = recipient.replaceAll("[\\s-]", "");
        
        // MUST start with + (international code) and have 1-12 digits after
        if (cleanNumber.startsWith("+")) {
            String digitsOnly = cleanNumber.substring(1);
            if (digitsOnly.matches("\\d{1,12}")) {
                return digitsOnly.length();
            }
        }
        
        return -1;
    }
    
    // Create message hash
    public String createMessageHash() {
        String[] words = message.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        
        return (firstTwoID + ":" + words.length + ":" + firstWord + lastWord).toUpperCase();
    }
    
    // Check message length
    public boolean checkMessageLength() {
        return message != null && message.length() <= 250;
    }
    
    // Get message length status
    public String getMessageLengthStatus() {
        if (message == null) {
            return "Message is null.";
        }
        
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }
    
    // Get recipient number status - UPDATED to mention international code
    public String getRecipientNumberStatus() {
        int result = checkRecipientCell();
        if (result > 0) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    // Send message options - UPDATED messages
    public String sentMessage(int option) {
        switch (option) {
            case 1: // Send
                this.sent = true;
                this.stored = false;
                return "Message successfully sent.";
            case 2: // Disregard
                this.sent = false;
                this.stored = false;
                return "Message successfully disregarded.";
            case 3: // Store
                this.sent = false;
                this.stored = true;
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }
    
    // Store message in JSON file
    public void storeMessage() {
        try {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("messageID", this.messageID);
            jsonMessage.put("recipient", this.recipient);
            jsonMessage.put("message", this.message);
            jsonMessage.put("messageHash", this.messageHash);
            jsonMessage.put("timestamp", System.currentTimeMillis());
            
            String filename = "stored_messages.json";
            FileWriter file = new FileWriter(filename, true);
            file.write(jsonMessage.toString() + "\n");
            file.close();
            
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
    
    // Print message details
    public String printMessage() {
        return "MessageID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + message;
    }
    
    // Getters for unit testing
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public boolean isSent() { return sent; }
    public boolean isStored() { return stored; }
}