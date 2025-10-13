import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {
    
    // Test username formatting - USING ASSIGNMENT TEST DATA
    @Test
    public void testCheckUserNameCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkUserName());
    }
    
    @Test
    public void testCheckUserNameIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName());
    }
    
    // Test password complexity - USING ASSIGNMENT TEST DATA
    @Test
    public void testCheckPasswordComplexityMeetsRequirements() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkPasswordComplexity());
    }
    
    @Test
    public void testCheckPasswordComplexityDoesNotMeetRequirements() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertFalse(login.checkPasswordComplexity());
    }
    
    // Test cell phone formatting - USING ASSIGNMENT TEST DATA
    @Test
    public void testCheckCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }
    
    @Test
    public void testCheckCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(login.checkCellPhoneNumber());
    }
    
    // Test registration messages - USING ASSIGNMENT TEST DATA AND EXPECTED MESSAGES
    @Test
    public void testRegisterUserUsernameIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        String expected = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        assertEquals(expected, login.registerUser());
    }
    
    @Test
    public void testRegisterUserPasswordDoesNotMeetRequirements() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        String expected = "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        assertEquals(expected, login.registerUser());
    }
    
    @Test
    public void testRegisterUserCellPhoneIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        String expected = "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        assertEquals(expected, login.registerUser());
    }
    
    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String expected = "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
        assertEquals(expected, login.registerUser());
    }
    
    // Test login functionality
    @Test
    public void testLoginUserSuccessful() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }
    
    @Test
    public void testLoginUserFailed() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wronguser", "wrongpass"));
    }
    
    // Test login status messages
    @Test
    public void testReturnLoginStatusSuccessful() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String expected = "Welcome Kyle_Smith it is great to see you again.";
        assertEquals(expected, login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }
    
    @Test
    public void testReturnLoginStatusFailed() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String expected = "Username or password incorrect, please try again.";
        assertEquals(expected, login.returnLoginStatus("wronguser", "wrongpass"));
    }
}