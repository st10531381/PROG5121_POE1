package com.mycompany.prog5121_poe1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Message {
    private static int totalMessages = 0;
    private static int totalCreated = 0;
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final Random RANDOM = new Random();

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String status = "Pending";

    public Message(String recipient, String messageText) {
        this.messageID = String.format("%010d", Math.abs(RANDOM.nextLong() % 10_000_000_000L));
        this.messageNumber = totalCreated++;
        this.recipient = recipient == null ? "" : recipient.trim();
        this.messageText = messageText == null ? "" : messageText.trim();
        this.messageHash = createMessageHash();
    }

    // Message ID must be exactly ten digits.
    public boolean checkMessageID() {
        return messageID.matches("\\d{10}");
    }

    // South African +27 mobile numbers have 12 characters including the plus.
    // General international numbers are accepted if they begin with + and contain 8-15 digits.
    public String checkRecipientCell() {
        String compact = recipient.replaceAll("[\\s()-]", "");
        if (compact.matches("\\+\\d{8,15}")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) return "Message ready to send.";
        return "Message exceeds 250 characters by " + (messageText.length() - 250)
                + "; please reduce the size.";
    }

    public String createMessageHash() {
        String[] words = messageText.trim().isEmpty() ? new String[0] : messageText.trim().split("\\s+");
        String first = words.length == 0 ? "" : words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words.length == 0 ? "" : words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String firstTwoID = messageID == null ? "00" : messageID.substring(0, 2);
        return (firstTwoID + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    // Prompts the user to send, disregard, or store the current message.
    public String SentMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Choose an option: ");
        return SentMessage(scanner.nextLine().trim());
    }

    public String SentMessage(String option) {
        switch (option == null ? "" : option.trim()) {
            case "1":
                status = "Sent";
                totalMessages++;
                sentMessages.add(this);
                System.out.println("Message successfully sent.");
                printDetails();
                return "Message successfully sent.";
            case "2":
                status = "Disregarded";
                System.out.println("Press 0 to delete the message.");
                return "Press 0 to delete the message.";
            case "3":
                status = "Stored";
                storedMessages.add(this);
                storeMessage();
                System.out.println("Message successfully stored.");
                printDetails();
                return "Message successfully stored.";
            default:
                return "Invalid option. Message was not sent.";
        }
    }

    public String printMessages() {
        StringBuilder result = new StringBuilder();
        for (Message m : sentMessages) {
            if (result.length() > 0) result.append(System.lineSeparator());
            result.append(m.details());
        }
        return result.toString();
    }

    public int returnTotalMessagess() {
        return totalMessages;
    }

    // Stores all currently stored messages as a JSON array in messages.json.
    public void storeMessage() {
        Path path = Path.of("messages.json");
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\"messageID\":\"").append(escape(m.messageID))
                .append("\",\"messageNumber\":").append(m.messageNumber)
                .append(",\"messageHash\":\"").append(escape(m.messageHash))
                .append("\",\"recipient\":\"").append(escape(m.recipient))
                .append("\",\"message\":\"").append(escape(m.messageText))
                .append("\",\"status\":\"").append(escape(m.status)).append("\"}");
            if (i < storedMessages.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]\n");
        try {
            Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Could not store messages: " + ex.getMessage());
        }
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private String details() {
        return "Message ID: " + messageID + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient + "\nMessage: " + messageText;
    }

    private void printDetails() {
        System.out.println(details());
    }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }

    // Test helper: clear in-memory state between unit tests.
    static void resetForTests() {
        totalMessages = 0;
        totalCreated = 0;
        sentMessages.clear();
        storedMessages.clear();
    }
}
