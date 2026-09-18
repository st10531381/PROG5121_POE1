package com.mycompany.prog5121_poe1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    // Username correctly formatted
    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();

        boolean result = login.checkUserName("kyl_1");

        assertTrue(result);
    }

    // Username incorrectly formatted
    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();

        boolean result = login.checkUserName("kyle!!!!!!!");

        assertFalse(result);
    }

    // Password meets complexity requirements
    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login();

        boolean result = login.checkPasswordComplexity("Ch&&sec@ke99!");

        assertTrue(result);
    }

    // Password does not meet complexity requirements
    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login();

        boolean result = login.checkPasswordComplexity("password");

        assertFalse(result);
    }

    // Cell phone number correctly formatted
    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login();

        boolean result = login.checkCellPhoneNumber("+27838968976");

        assertTrue(result);
    }

    // Cell phone number incorrectly formatted
    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login();

        boolean result = login.checkCellPhoneNumber("08966553");

        assertFalse(result);
    }

    // Login successful
    @Test
    public void testLoginSuccessful() {
        Login login = new Login();

        login.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        boolean result = login.loginUser("kyl_1", "Ch&&sec@ke99!");

        assertTrue(result);
    }

    // Login failed
    @Test
    public void testLoginFailed() {
        Login login = new Login();

        login.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        boolean result = login.loginUser("kyl_1", "password");

        assertFalse(result);
    }
    // Test successful registration message
@Test
public void testSuccessfulRegistrationMessage() {
    Login login = new Login();

    String result = login.registerUser(
            "kyl_1",
            "Ch&&sec@ke99!",
            "+27838968976",
            "Kyle",
            "Smith"
    );

    assertEquals("User successfully registered.", result);
}


// Test incorrectly formatted username message
@Test
public void testIncorrectUsernameMessage() {
    Login login = new Login();

    String result = login.registerUser(
            "kyle!!!!!!!",
            "Ch&&sec@ke99!",
            "+27838968976",
            "Kyle",
            "Smith"
    );

    assertEquals(
            "Username is not correctly formatted; please ensure that your "
            + "username contains an underscore and is no more than five "
            + "characters in length.",
            result
    );
}


// Test correct password message
@Test
public void testCorrectPasswordMessage() {
    Login login = new Login();

    assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
}


// Test incorrect password message
@Test
public void testIncorrectPasswordMessage() {
    Login login = new Login();

    String result = login.registerUser(
            "kyl_1",
            "password",
            "+27838968976",
            "Kyle",
            "Smith"
    );

    assertEquals(
            "Password is not correctly formatted; please ensure that the "
            + "password contains at least eight characters, a capital "
            + "letter, a number, and a special character.",
            result
    );
}


// Test correctly formatted cellphone message
@Test
public void testCorrectCellPhoneMessage() {
    Login login = new Login();

    assertTrue(login.checkCellPhoneNumber("+27838968976"));
}


// Test incorrectly formatted cellphone message
@Test
public void testIncorrectCellPhoneMessage() {
    Login login = new Login();

    String result = login.registerUser(
            "kyl_1",
            "Ch&&sec@ke99!",
            "08966553",
            "Kyle",
            "Smith"
    );

    assertEquals(
            "Cell phone number incorrectly formatted or does not contain "
            + "international code.",
            result
    );
}
}
