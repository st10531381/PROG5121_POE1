/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe1;

/**
 *
 * @author ST10531381
 */
public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Checks if the username contains "_" and is 5 characters or less
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Checks the password complexity requirements
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {
            char currentCharacter = password.charAt(i);

            if (Character.isUpperCase(currentCharacter)) {
                hasCapital = true;
            }

            if (Character.isDigit(currentCharacter)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(currentCharacter)) {
                hasSpecialCharacter = true;
            }
        }

        return password.length() >= 8
                && hasCapital
                && hasNumber
                && hasSpecialCharacter;
    }

   // Checks for a South African international cell phone number.
// Regular expression syntax adapted from Java Pattern documentation:
// https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
public boolean checkCellPhoneNumber(String cellPhoneNumber) {
    return cellPhoneNumber.matches("^\\+27\\d{9}$");
}
    // Registers the user if all details are correctly formatted
public String registerUser(String username, String password,
        String cellPhoneNumber, String firstName, String lastName) {

    if (!checkUserName(username)) {
        return "Username is not correctly formatted; please ensure that your "
                + "username contains an underscore and is no more than five "
                + "characters in length.";
    }

    if (!checkPasswordComplexity(password)) {
        return "Password is not correctly formatted; please ensure that the "
                + "password contains at least eight characters, a capital "
                + "letter, a number, and a special character.";
    }

   if (!checkCellPhoneNumber(cellPhoneNumber)) {
    return "Cell number is incorrectly formatted or does not contain "
            + "an international code; please correct the number and try again.";
}

    this.username = username;
    this.password = password;
    this.cellPhoneNumber = cellPhoneNumber;
    this.firstName = firstName;
    this.lastName = lastName;

    return "User successfully registered.";
}


// Checks whether entered login details match the registered details
public boolean loginUser(String enteredUsername, String enteredPassword) {
    return enteredUsername.equals(username)
            && enteredPassword.equals(password);
}


// Returns the correct login message
public String returnLoginStatus(String enteredUsername, String enteredPassword) {

    if (loginUser(enteredUsername, enteredPassword)) {
        return "Welcome " + firstName + ", " + lastName
                + " it is great to see you again.";
    }

    return "Username or password incorrect, please try again.";
}
}  
