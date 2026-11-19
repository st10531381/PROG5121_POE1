package com.mycompany.prog5121_poe1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * QuickChat message. Holds the Part 2 message logic and the Part 3
 * arrays (sent, disregarded, stored, hashes, IDs) plus JSON storage.
 */
public class Message {

    // ---------------------------------------------------------------
    // Part 3 arrays (populated by the program, no hard-coding)
    // ---------------------------------------------------------------
    public static final ArrayList<Message> sentMessages = new ArrayList<>();
    public static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static final ArrayList<Message> storedMessages = new ArrayList<>();
    public static final ArrayList<String> messageHashes = new ArrayList<>();
    public static final ArrayList<String> messageIDs = new ArrayList<>();

    // Total number of messages sent while the program runs
    private static int totalSent = 0;

    // File used to store messages in JSON format
    public static String jsonFile = "storedMessages.json";

    // ---------------------------------------------------------------
    // Fields of one message
    // ---------------------------------------------------------------
    private String messageID;
    private int messageNumber;   // number of this message (starts at 0)
    private String sender;
    private String recipient;
    private String text;
    private String messageHash;
    private String flag = "";    // Sent / Stored / Disregard

    // Creates a message with a randomly generated ten digit ID
    public Message(int messageNumber, String sender, String recipient, String text) {
        this(generateMessageID(), messageNumber, sender, recipient, text);
    }

    // Creates a message with a given ID (used for loading from JSON and tests)
    public Message(String messageID, int messageNumber, String sender,
                   String recipient, String text) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

    // Generates a random ten digit number (leading zeros allowed)
    private static String generateMessageID() {
        long number = (long) (new Random().nextDouble() * 10_000_000_000L);
        return String.format("%010d", number);
    }

    // ---------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }

    // ---------------------------------------------------------------
    // Part 2 methods
    // ---------------------------------------------------------------

    // Message ID must not be more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Recipient must start with an international code (+) and be a valid length
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+\\d{10,12}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    // Message must not exceed 250 characters
    public String checkMessageLength() {
        if (text.length() <= 250) {
            return "Message ready to send.";
        }
        int over = text.length() - 250;
        return "Message exceeds 250 characters by " + over
                + ", please reduce the size.";
    }

    // Hash = first two digits of ID : message number : FIRSTWORDLASTWORD
    public String createMessageHash() {
        String[] words = text.trim().split("\\s+");
        String first = cleanWord(words[0]);
        String last = cleanWord(words[words.length - 1]);
        String idStart = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        return (idStart + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    // Removes punctuation from a word (e.g. "tonight?" -> "tonight")
    private static String cleanWord(String word) {
        StringBuilder clean = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (Character.isLetterOrDigit(word.charAt(i))) {
                clean.append(word.charAt(i));
            }
        }
        return clean.toString();
    }

    // Choice: 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        messageHashes.add(messageHash);
        messageIDs.add(messageID);

        switch (choice) {
            case 1:
                flag = "Sent";
                sentMessages.add(this);
                totalSent++;
                return "Message successfully sent.";
            case 2:
                flag = "Disregard";
                disregardedMessages.add(this);
                return "Press 0 to delete the message.";
            case 3:
                flag = "Stored";
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Returns all the messages sent while the program is running
    public static String printMessages() {
        StringBuilder out = new StringBuilder();
        for (Message m : sentMessages) {
            out.append(m.details()).append("\n");
        }
        return out.toString();
    }

    // Returns the total number of messages sent
    public static int returnTotalMessages() {
        return totalSent;
    }

    // Full details of one message in the required order
    public String details() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + text + "\n";
    }

    // Stores this message in the stored array and the JSON file
    public void storeMessage() {
        storedMessages.add(this);
        saveStoredMessages();
    }

    // ---------------------------------------------------------------
    // Part 3 methods
    // ---------------------------------------------------------------

    // a. Sender and recipient of all stored messages
    public static String displaySenderAndRecipient() {
        StringBuilder out = new StringBuilder();
        for (Message m : storedMessages) {
            out.append("Sender: ").append(m.sender)
               .append(" | Recipient: ").append(m.recipient).append("\n");
        }
        return out.toString();
    }

    // b. Longest stored message
    public static String longestStoredMessage() {
        Message longest = null;
        for (Message m : storedMessages) {
            if (longest == null || m.text.length() > longest.text.length()) {
                longest = m;
            }
        }
        return longest == null ? "No stored messages." : longest.text;
    }

    // c. Search for a message ID, show recipient and message
    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                Message m = findByID(id);
                if (m != null) {
                    return "Recipient: " + m.recipient + " | Message: " + m.text;
                }
            }
        }
        return "Message ID not found.";
    }

    private static Message findByID(String id) {
        for (Message m : sentMessages) if (m.messageID.equals(id)) return m;
        for (Message m : storedMessages) if (m.messageID.equals(id)) return m;
        for (Message m : disregardedMessages) if (m.messageID.equals(id)) return m;
        return null;
    }

    // d. All messages sent or stored for a particular recipient
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.recipient.equals(recipient)) found.add(m.text);
        }
        for (Message m : storedMessages) {
            if (m.recipient.equals(recipient)) found.add(m.text);
        }
        return found;
    }

    // e. Delete a message using its hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).messageHash.equals(hash)) {
                String text = storedMessages.get(i).text;
                removeFromIndexArrays(storedMessages.get(i));
                storedMessages.remove(i);
                saveStoredMessages();
                return "Message \"" + text + "\" successfully deleted.";
            }
        }
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).messageHash.equals(hash)) {
                String text = sentMessages.get(i).text;
                removeFromIndexArrays(sentMessages.get(i));
                sentMessages.remove(i);
                return "Message \"" + text + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    private static void removeFromIndexArrays(Message m) {
        messageHashes.remove(m.messageHash);
        messageIDs.remove(m.messageID);
    }

    // f. Report of all stored messages (full details)
    public static String displayStoredReport() {
        return buildReport("STORED MESSAGES REPORT", storedMessages);
    }

    // Report of all sent messages
    public static String displaySentReport() {
        return buildReport("SENT MESSAGES REPORT", sentMessages);
    }

    private static String buildReport(String title, List<Message> list) {
        StringBuilder out = new StringBuilder();
        out.append("===== ").append(title).append(" =====\n");
        for (Message m : list) {
            out.append("Message Hash: ").append(m.messageHash).append("\n");
            out.append("Recipient: ").append(m.recipient).append("\n");
            out.append("Message: ").append(m.text).append("\n");
            out.append("-----------------------------\n");
        }
        return out.toString();
    }

    // Clears all arrays and counters (used at start of tests)
    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalSent = 0;
    }

    // Adds a message directly into the arrays by its flag (used to populate test data)
    public static void addToArrays(Message m, String flag) {
        m.flag = flag;
        messageHashes.add(m.messageHash);
        messageIDs.add(m.messageID);
        if (flag.equals("Sent")) {
            sentMessages.add(m);
            totalSent++;
        } else if (flag.equals("Stored")) {
            storedMessages.add(m);
        } else if (flag.equals("Disregard")) {
            disregardedMessages.add(m);
        }
    }

    // ---------------------------------------------------------------
    // JSON storage (no external library needed)
    // JSON format follows the specification at https://www.json.org
    // (array of objects with string key/value pairs); written by hand.
    // ---------------------------------------------------------------

    // Writes every stored message to the JSON file
    public static void saveStoredMessages() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(esc(m.messageID)).append("\",\n");
            json.append("    \"messageNumber\": \"").append(m.messageNumber).append("\",\n");
            json.append("    \"messageHash\": \"").append(esc(m.messageHash)).append("\",\n");
            json.append("    \"sender\": \"").append(esc(m.sender)).append("\",\n");
            json.append("    \"recipient\": \"").append(esc(m.recipient)).append("\",\n");
            json.append("    \"message\": \"").append(esc(m.text)).append("\"\n");
            json.append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]\n");
        try {
            Files.write(Paths.get(jsonFile), json.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Could not write JSON file: " + e.getMessage());
        }
    }

    // Reads the JSON file into the stored messages array
    public static void loadStoredMessages() {
        Path path = Paths.get(jsonFile);
        if (!Files.exists(path)) {
            return;
        }
        try {
            String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            for (Message m : parseJson(json)) {
                m.flag = "Stored";
                storedMessages.add(m);
                messageHashes.add(m.messageHash);
                messageIDs.add(m.messageID);
            }
        } catch (IOException e) {
            System.out.println("Could not read JSON file: " + e.getMessage());
        }
    }

    // Escapes special characters for JSON strings
    private static String esc(String s) {
        StringBuilder out = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default:
                    if (c < 0x20) out.append(String.format("\\u%04x", (int) c));
                    else out.append(c);
            }
        }
        return out.toString();
    }

    // Small JSON reader for a list of flat objects with string values
    private static List<Message> parseJson(String json) {
        List<Message> result = new ArrayList<>();
        java.util.Map<String, String> current = null;
        String key = null;
        int i = 0;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '{') {
                current = new java.util.HashMap<>();
                key = null;
            } else if (c == '}') {
                if (current != null) {
                    String number = current.getOrDefault("messageNumber", "0");
                    int n;
                    try { n = Integer.parseInt(number); } catch (NumberFormatException e) { n = 0; }
                    result.add(new Message(current.getOrDefault("messageID", ""), n,
                            current.getOrDefault("sender", ""),
                            current.getOrDefault("recipient", ""),
                            current.getOrDefault("message", "")));
                }
                current = null;
            } else if (c == '"') {
                StringBuilder sb = new StringBuilder();
                i++;
                while (i < json.length() && json.charAt(i) != '"') {
                    char d = json.charAt(i);
                    if (d == '\\' && i + 1 < json.length()) {
                        i++;
                        char e = json.charAt(i);
                        switch (e) {
                            case 'n': sb.append('\n'); break;
                            case 'r': sb.append('\r'); break;
                            case 't': sb.append('\t'); break;
                            case 'u':
                                sb.append((char) Integer.parseInt(json.substring(i + 1, i + 5), 16));
                                i += 4;
                                break;
                            default: sb.append(e);
                        }
                    } else {
                        sb.append(d);
                    }
                    i++;
                }
                if (current != null) {
                    if (key == null) {
                        key = sb.toString();
                    } else {
                        current.put(key, sb.toString());
                        key = null;
                    }
                }
            }
            i++;
        }
        return result;
    }
}
