package com.mycompany.prog5121_poe1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private Message test1;
    private Message test2;

    @BeforeEach
    public void setUp() throws IOException {
        // Use a temporary JSON file so tests never touch real data
        Path temp = Files.createTempFile("quickchat", ".json");
        temp.toFile().deleteOnExit();
        Message.jsonFile = temp.toString();
        Message.resetAll();

        // Part 2 test data
        test1 = new Message("0012345678", 0, "Developer", "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        test2 = new Message("0098765432", 1, "Developer", "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    // Part 2 ------------------------------------------------------

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", test1.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longText = "a".repeat(260);
        Message m = new Message(0, "Developer", "+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 10, please reduce the size.",
                m.checkMessageLength());
    }

    @Test
    public void testRecipientSuccess() {
        assertEquals("Cell phone number successfully captured.", test1.checkRecipientCell());
    }

    @Test
    public void testRecipientFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                test2.checkRecipientCell());
    }

    @Test
    public void testMessageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", test1.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        Message[] messages = {test1, test2};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    @Test
    public void testMessageIDCreated() {
        assertTrue(test1.checkMessageID());
        Message random = new Message(0, "Developer", "+27718693002", "Hello there");
        assertEquals(10, random.getMessageID().length());
        assertTrue(random.checkMessageID());
    }

    @Test
    public void testSentMessageSend() {
        assertEquals("Message successfully sent.", test1.sentMessage(1));
    }

    @Test
    public void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", test2.sentMessage(2));
    }

    @Test
    public void testSentMessageStore() {
        assertEquals("Message successfully stored.", test1.sentMessage(3));
    }

    @Test
    public void testTotalMessagesSent() {
        test1.sentMessage(1);   // sent
        test2.sentMessage(2);   // disregarded - not counted
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages() {
        test1.sentMessage(1);
        String printed = Message.printMessages();
        assertTrue(printed.contains("0012345678"));
        assertTrue(printed.contains("00:0:HITONIGHT"));
        assertTrue(printed.contains("+27718693002"));
        assertTrue(printed.contains("Hi Mike, can you join us for dinner tonight?"));
    }

    // Part 3 ------------------------------------------------------

    // Populates the arrays with Part 3 test data messages 1 to 5
    private void populateTestData() {
        Message.addToArrays(new Message("0011111111", 0, "Developer", "+27834557896",
                "Did you get the cake?"), "Sent");
        Message.addToArrays(new Message("0022222222", 1, "Developer", "+27838884567",
                "Where are you? You are late! I have asked you to be on time."), "Stored");
        Message.addToArrays(new Message("0033333333", 2, "Developer", "+27834484567",
                "Yohoooo, I am at your gate."), "Disregard");
        Message.addToArrays(new Message("0044444444", 3, "Developer", "0838884567",
                "It is dinner time !"), "Sent");
        Message.addToArrays(new Message("0055555555", 4, "Developer", "+27838884567",
                "Ok, I am leaving without you."), "Stored");
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        populateTestData();
        assertEquals(2, Message.sentMessages.size());
        assertEquals("Did you get the cake?", Message.sentMessages.get(0).getText());
        assertEquals("It is dinner time !", Message.sentMessages.get(1).getText());
    }

    @Test
    public void testOtherArraysPopulated() {
        populateTestData();
        assertEquals(2, Message.storedMessages.size());
        assertEquals(1, Message.disregardedMessages.size());
        assertEquals(5, Message.messageHashes.size());
        assertEquals(5, Message.messageIDs.size());
    }

    @Test
    public void testLongestMessage() {
        populateTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.longestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        populateTestData();
        assertEquals("Recipient: 0838884567 | Message: It is dinner time !",
                Message.searchByMessageID("0044444444"));
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        populateTestData();
        assertEquals("Message ID not found.", Message.searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchByRecipient() {
        populateTestData();
        ArrayList<String> found = Message.searchByRecipient("+27838884567");
        assertEquals(2, found.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", found.get(0));
        assertEquals("Ok, I am leaving without you.", found.get(1));
    }

    @Test
    public void testDeleteByHash() {
        populateTestData();
        String hash = Message.storedMessages.get(0).getMessageHash();
        assertEquals("Message \"Where are you? You are late! I have asked you to be on time.\" "
                + "successfully deleted.", Message.deleteByHash(hash));
        assertEquals(1, Message.storedMessages.size());
    }

    @Test
    public void testDeleteByHashNotFound() {
        populateTestData();
        assertEquals("Message hash not found.", Message.deleteByHash("NOPE"));
    }

    @Test
    public void testDisplayReport() {
        populateTestData();
        String report = Message.displayStoredReport();
        assertTrue(report.contains("00:1:WHERETIME"));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(report.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testJsonStoreAndRead() {
        test1.sentMessage(3);                       // stores to JSON file
        Message.storedMessages.clear();             // empty the array
        Message.messageHashes.clear();
        Message.messageIDs.clear();
        Message.loadStoredMessages();               // read it back from JSON
        assertEquals(1, Message.storedMessages.size());
        assertEquals("Hi Mike, can you join us for dinner tonight?",
                Message.storedMessages.get(0).getText());
        assertEquals("+27718693002", Message.storedMessages.get(0).getRecipient());
    }
}
