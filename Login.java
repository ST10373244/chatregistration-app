import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Login {
    private String username;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;
    
    // Constructor
    public Login(String firstName, String lastName, String username, String password, String cellNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
    }
    
    // Check if username contains underscore and is ≤5 characters
    public boolean checkUserName() {
        return username != null && 
               username.length() <= 5 && 
               username.contains("_");
    }
    
    // Check password complexity requirements
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasCapital && hasNumber && hasSpecial;
    }
    
    // Check cell phone format (using regex from AI tool - ChatGPT)
    // MODIFIED: Now allows 1-12 digits after + (instead of 1-10)
    public boolean checkCellPhoneNumber() {
        if (cellNumber == null || cellNumber.isEmpty()) {
            return false;
        }
        
        // Must start with +
        if (!cellNumber.startsWith("+")) {
            return false;
        }
        
        // Get digits after the +
        String digitsOnly = cellNumber.substring(1);
        
        // Check if all characters after + are digits and length is 1-12
        return digitsOnly.matches("\\d{1,12}");
    }
    
    // Register user with validation messages
    public String registerUser() {
        StringBuilder result = new StringBuilder();
        
        if (!checkUserName()) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        } else {
            result.append("Username successfully captured.\n");
        }
        
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        } else {
            result.append("Password successfully captured.\n");
        }
        
        if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        } else {
            result.append("Cell phone number successfully added.");
        }
        
        return result.toString();
    }
    
    // Login verification
    public boolean loginUser(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }
    
    // Return login status message
    public String returnLoginStatus(String inputUsername, String inputPassword) {
        if (loginUser(inputUsername, inputPassword)) {
            return "Welcome " + firstName + "_" + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    
    // Getters for unit testing
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCellNumber() { return cellNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}