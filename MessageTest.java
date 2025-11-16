import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {
    
    @Test
    public void testCheckMessageID() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        assertTrue("Message ID should be valid", message.checkMessageID());
    }
    
    @Test
    public void testCheckRecipientCellCorrect() {
        Message message = new Message("+27718693002", "Test message");
        assertTrue("International format should work", message.checkRecipientCell() > 0);
    }
    
    @Test
    public void testCheckRecipientCellIncorrect() {
        Message message = new Message("08575975889", "Test message");
        assertEquals("Local format without + should fail", -1, message.checkRecipientCell());
    }
    
    @Test
    public void testCheckRecipientCellWithLetters() {
        Message message = new Message("+27S35968976", "Test message");
        assertEquals("Numbers with letters should fail", -1, message.checkRecipientCell());
    }
    
    @Test
    public void testCreateMessageHash() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        String hash = message.createMessageHash();
        assertNotNull("Message hash should not be null", hash);
        assertTrue("Message hash should contain colon", hash.contains(":"));
    }
    
    @Test
    public void testCheckMessageLengthSuccess() {
        Message message = new Message("+27718693002", "Short message");
        assertTrue("Short message should pass", message.checkMessageLength());
        assertEquals("Should return ready message", "Message ready to send.", message.getMessageLengthStatus());
    }
    
    @Test
    public void testCheckMessageLengthFailure() {
        // Create a long message (more than 250 characters)
        String longMessage = "This is a very long message that exceeds the 250 character limit. "
                + "This is a very long message that exceeds the 250 character limit. "
                + "This is a very long message that exceeds the 250 character limit. "
                + "This is a very long message that exceeds the 250 character limit. "
                + "This is a very long message that exceeds the 250 character limit.";
        Message message = new Message("+27718693002", longMessage);
        assertFalse("Long message should fail", message.checkMessageLength());
        assertTrue("Should mention character limit", message.getMessageLengthStatus().contains("exceeds 250 characters"));
    }
    
    @Test
    public void testGetRecipientNumberStatusSuccess() {
        Message message = new Message("+27718693002", "Test message");
        assertEquals("Should show success message", 
                     "Cell phone number successfully captured.", 
                     message.getRecipientNumberStatus());
    }
    
    @Test
    public void testGetRecipientNumberStatusFailure() {
        Message message = new Message("08575975889", "Test message");
        assertEquals("Should show failure message", 
                     "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", 
                     message.getRecipientNumberStatus());
    }
    
    @Test
    public void testSentMessageSend() {
        Message message = new Message("+27718693002", "Test message");
        String result = message.sentMessage(1);
        assertEquals("Should confirm message sent", "Message successfully sent.", result);
        assertTrue("Message should be marked as sent", message.isSent());
    }
    
    @Test
    public void testSentMessageDisregard() {
        Message message = new Message("+27718693002", "Test message");
        String result = message.sentMessage(2);
        assertEquals("Should confirm message disregarded", "Message successfully disregarded.", result);
        assertFalse("Message should not be sent", message.isSent());
    }
    
    @Test
    public void testSentMessageStore() {
        Message message = new Message("+27718693002", "Test message");
        String result = message.sentMessage(3);
        assertEquals("Should confirm message stored", "Message successfully stored.", result);
        assertTrue("Message should be marked as stored", message.isStored());
    }
    
    @Test
    public void testMessageHashFormat() {
        Message message = new Message("+27718693002", "Hi Mike thanks");
        String hash = message.createMessageHash();
        // Hash should be in format: FIRST_2_DIGITS:WORD_COUNT:FIRSTWORDLASTWORD
        assertTrue("Hash should be in correct format", hash.matches("\\d{2}:\\d+:HI[A-Z]+"));
    }
}