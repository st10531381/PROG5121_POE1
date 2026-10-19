package com.mycompany.prog5121_poe1;

import java.util.Scanner;

public class PROG5121_POE1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=================================");
        System.out.println("       ACCOUNT REGISTRATION");
        System.out.println("=================================");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();
        System.out.print("Enter a username: ");
        String username = input.nextLine();
        System.out.print("Enter a password: ");
        String password = input.nextLine();
        System.out.print("Enter your South African cellphone number (+27): ");
        String cellPhoneNumber = input.nextLine();

        String registrationResult = login.registerUser(username, password,
                cellPhoneNumber, firstName, lastName);
        System.out.println("\n" + registrationResult);
        if (!"User successfully registered.".equals(registrationResult)) {
            input.close();
            return;
        }

        System.out.println("\n=================================");
        System.out.println("              LOGIN");
        System.out.println("=================================");
        System.out.print("Enter your username: ");
        String loginUsername = input.nextLine();
        System.out.print("Enter your password: ");
        String loginPassword = input.nextLine();

        String loginResult = login.returnLoginStatus(loginUsername, loginPassword);
        System.out.println("\n" + loginResult);
        if (!login.loginUser(loginUsername, loginPassword)) {
            input.close();
            return;
        }

        System.out.println("\nWelcome to QuickChat.");
        System.out.print("How many messages would you like to send? ");
        int messageLimit;
        try {
            messageLimit = Integer.parseInt(input.nextLine().trim());
            if (messageLimit < 0) {
                System.out.println("Please enter zero or a positive number.");
                input.close();
                return;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number. Please restart and enter a whole number.");
            input.close();
            return;
        }

        int messagesCreated = 0;
        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    if (messagesCreated >= messageLimit) {
                        System.out.println("You have reached the number of messages you selected.");
                        break;
                    }
                    System.out.print("Enter recipient cellphone number (include international code, e.g. +27718693002): ");
                    String recipient = input.nextLine().trim();
                    System.out.print("Enter your message (maximum 250 characters): ");
                    String body = input.nextLine();
                    Message message = new Message(recipient, body);
                    if (!message.checkRecipientCell().equals("Cell phone number successfully captured.")) {
                        System.out.println(message.checkRecipientCell());
                        break;
                    }
                    String lengthResult = message.checkMessageLength();
                    if (!lengthResult.equals("Message ready to send.")) {
                        System.out.println(lengthResult);
                        break;
                    }
                    System.out.println("Message Hash: " + message.createMessageHash());
                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message to send later");
                    System.out.print("Choose an option: ");
                    System.out.println(message.SentMessage(input.nextLine().trim()));
                    messagesCreated++;
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
        input.close();
    }
}
