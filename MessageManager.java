import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MessageManager {
    private List<Message> messages;
    private int totalMessagesSent;
    
    public MessageManager() {
        this.messages = new ArrayList<>();
        this.totalMessagesSent = 0;
    }
    
    // Add a message to the list
    public void addMessage(Message message) {
        messages.add(message);
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
}
