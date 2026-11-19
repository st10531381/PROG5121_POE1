package com.mycompany.prog5121_poe1;

import java.util.Scanner;

public class PROG5121_POE1 {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        Login login = new Login();

        // ---------------- PART 1: REGISTRATION ----------------
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

        String registrationResult = login.registerUser(
                username, password, cellPhoneNumber, firstName, lastName);

        System.out.println();
        System.out.println(registrationResult);

        if (!registrationResult.equals("User successfully registered.")) {
            input.close();
            return;
        }

        // ---------------- PART 1: LOGIN ----------------
        System.out.println();
        System.out.println("=================================");
        System.out.println("              LOGIN");
        System.out.println("=================================");

        boolean loggedIn = false;
        int attempts = 0;
        while (!loggedIn && attempts < 3) {
            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            System.out.println();
            System.out.println(login.returnLoginStatus(loginUsername, loginPassword));
            loggedIn = login.loginUser(loginUsername, loginPassword);
            attempts++;
        }

        // Only logged in users may use QuickChat
        if (!loggedIn) {
            System.out.println("Too many failed attempts. Goodbye.");
            input.close();
            return;
        }

        // ---------------- PART 2 + 3: QUICKCHAT ----------------
        Message.loadStoredMessages();   // read JSON file into the stored array
        runQuickChat(login.getFirstName());
        input.close();
    }

    // Runs until the user selects Quit
    private static void runQuickChat(String sender) {
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        int messageNumber = 0;   // number of the next message (starts at 0)
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Option: ");
            int option = readInt();

            switch (option) {
                case 1:
                    messageNumber = sendMessages(sender, messageNumber);
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    System.out.println("Goodbye.");
                    running = false;
                    break;
                case 4:
                    storedMessagesMenu();
                    break;
                default:
                    System.out.println("Invalid option, please choose 1 to 4.");
            }
        }
    }

    // Option 1: user decides how many messages to enter
    private static int sendMessages(String sender, int messageNumber) {
        System.out.print("How many messages do you wish to enter? ");
        int count = readInt();

        // A for loop runs once for each message the user chose to enter
        for (int i = 0; i < count; i++) {
            System.out.println();
            System.out.println("--- Message " + (i + 1) + " of " + count + " ---");

            // Recipient (repeat until valid)
            String recipient;
            while (true) {
                System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                recipient = input.nextLine().trim();
                Message check = new Message(messageNumber, sender, recipient, "check");
                String result = check.checkRecipientCell();
                System.out.println(result);
                if (result.equals("Cell phone number successfully captured.")) break;
            }

            // Message text (repeat until 250 characters or less)
            String text;
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                text = input.nextLine();
                if (text.trim().isEmpty()) {
                    System.out.println("Message cannot be empty.");
                    continue;
                }
                if (text.length() > 250) {
                    System.out.println("Please enter a message of less than 250 characters.");
                    System.out.println(new Message(messageNumber, sender, recipient, text).checkMessageLength());
                    continue;
                }
                System.out.println("Message sent");
                break;
            }

            Message message = new Message(messageNumber, sender, recipient, text);
            System.out.println("Message ID generated: " + message.getMessageID());

            // Send / Disregard / Store
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Option: ");
            int choice = readInt();
            while (choice < 1 || choice > 3) {
                System.out.print("Please choose 1, 2 or 3: ");
                choice = readInt();
            }

            String response = message.sentMessage(choice);
            System.out.println(response);

            if (choice == 2) {
                System.out.print("Press 0 to delete the message: ");
                while (readInt() != 0) {
                    System.out.print("Press 0 to delete the message: ");
                }
                System.out.println("Message deleted.");
            }

            if (choice == 1) {
                System.out.println();
                System.out.println(message.details());
            }

            messageNumber++;
        }

        System.out.println("Total messages sent: " + Message.returnTotalMessages());
        return messageNumber;
    }

    // Option 4: Stored Messages (Part 3)
    private static void storedMessagesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- Stored Messages ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search all messages for a particular recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display report of all stored messages");
            System.out.println("7) Back to main menu");
            System.out.print("Option: ");
            int option = readInt();

            switch (option) {
                case 1:
                    System.out.print(Message.displaySenderAndRecipient());
                    break;
                case 2:
                    System.out.println(Message.longestStoredMessage());
                    break;
                case 3:
                    System.out.print("Enter message ID: ");
                    System.out.println(Message.searchByMessageID(input.nextLine().trim()));
                    break;
                case 4:
                    System.out.print("Enter recipient: ");
                    for (String m : Message.searchByRecipient(input.nextLine().trim())) {
                        System.out.println(m);
                    }
                    break;
                case 5:
                    System.out.print("Enter message hash: ");
                    System.out.println(Message.deleteByHash(input.nextLine().trim().toUpperCase()));
                    break;
                case 6:
                    System.out.print(Message.displayStoredReport());
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1 to 7.");
            }
        }
    }

    // Reads a whole number, asks again if the user types something else
    private static int readInt() {
        while (true) {
            String line = input.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number: ");
            }
        }
    }
}
