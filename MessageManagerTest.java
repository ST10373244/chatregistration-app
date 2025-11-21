import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class MessageManagerTest {
    
    @Test
    public void testSentMessagesArrayPopulated() {
        MessageManager manager = new MessageManager();
        Message message1 = new Message("+27834557896", "Did you get the cake?");
        Message message2 = new Message("0838884567", "It is dinner time!");
        
        manager.addMessage(message1, 1); // Sent
        manager.addMessage(message2, 1); // Sent
        
        List<String> sentMessages = manager.getSentMessages();
        assertTrue("Should contain cake message", sentMessages.contains("Did you get the cake?"));
        assertTrue("Should contain dinner message", sentMessages.contains("It is dinner time!"));
    }
    
    @Test
    public void testDisplayLongestMessage() {
        MessageManager manager = new MessageManager();
        Message message1 = new Message("+27834557896", "Did you get the cake?");
        Message message2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        
        manager.addMessage(message1, 1);
        manager.addMessage(message2, 1);
        
        String result = manager.displayLongestMessage();
        assertTrue("Should contain longest message", result.contains("Where are you? You are late! I have asked you to be on time."));
    }
    
    @Test
    public void testSearchMessageByID() {
        MessageManager manager = new MessageManager();
        Message message = new Message("0838884567", "It is dinner time!");
        String messageID = message.getMessageID();
        
        manager.addMessage(message, 1);
        
        String result = manager.searchMessageByID(messageID);
        assertTrue("Should find message by ID", result.contains("It is dinner time!"));
    }
    
    @Test
    public void testSearchMessagesByRecipient() {
        MessageManager manager = new MessageManager();
        Message message1 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message message2 = new Message("+27838884567", "Ok, I am leaving without you.");
        
        manager.addMessage(message1, 1);
        manager.addMessage(message2, 3); // Stored
        
        String result = manager.searchMessagesByRecipient("+27838884567");
        assertTrue("Should find messages for recipient", result.contains("Where are you? You are late!"));
    }
    
    @Test
    public void testDeleteMessageByHash() {
        MessageManager manager = new MessageManager();
        Message message = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        String messageHash = message.getMessageHash();
        
        manager.addMessage(message, 1);
        
        String result = manager.deleteMessageByHash(messageHash);
        assertTrue("Should confirm deletion", result.contains("successfully deleted"));
    }
    
    @Test
    public void testDisplayMessageReport() {
        MessageManager manager = new MessageManager();
        Message message1 = new Message("+27834557896", "Did you get the cake?");
        Message message2 = new Message("0838884567", "It is dinner time!");
        
        manager.addMessage(message1, 1);
        manager.addMessage(message2, 1);
        
        String result = manager.displayMessageReport();
        assertTrue("Should contain message hash", result.contains("Message Hash:"));
        assertTrue("Should contain recipient", result.contains("Recipient:"));
        assertTrue("Should contain message", result.contains("Did you get the cake?"));
    }
}
