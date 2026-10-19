package com.mycompany.prog5121_poe1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    @BeforeEach
    public void reset() {
        Message.resetForTests();
    }

    @Test
    public void messageIdIsTenDigits() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    @Test
    public void recipientWithInternationalCodeIsAccepted() {
        Message message = new Message("+27718693002", "Hi");
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    public void recipientWithoutInternationalCodeIsRejected() {
        Message message = new Message("08575975889", "Hi");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell());
    }

    @Test
    public void messageOf250CharactersIsAccepted() {
        Message message = new Message("+27718693002", "a".repeat(250));
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void messageOver250CharactersIsRejected() {
        Message message = new Message("+27718693002", "a".repeat(255));
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", message.checkMessageLength());
    }

    @Test
    public void hashContainsIdPrefixMessageNumberAndWords() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String expected = message.getMessageID().substring(0, 2) + ":0:HITONIGHT";
        assertEquals(expected, message.createMessageHash());
    }

    @Test
    public void totalSentMessagesStartsAtZero() {
        Message message = new Message("+27718693002", "Hi");
        assertEquals(0, message.returnTotalMessagess());
    }
    @Test
    public void sendMessageActionReturnsSuccessAndIncrementsTotal() {
        Message message = new Message("+27718693002", "Hi Mike");
        assertEquals("Message successfully sent.", message.SentMessage("1"));
        assertEquals(1, message.returnTotalMessagess());
        assertEquals("Sent", message.getStatus());
    }

    @Test
    public void disregardMessageActionDoesNotIncrementTotal() {
        Message message = new Message("+27718693002", "Hi Keegan");
        assertEquals("Press 0 to delete the message.", message.SentMessage("2"));
        assertEquals(0, message.returnTotalMessagess());
        assertEquals("Disregarded", message.getStatus());
    }

    @Test
    public void storeMessageActionReturnsSuccess() {
        Message message = new Message("+27718693002", "Save this message");
        assertEquals("Message successfully stored.", message.SentMessage("3"));
        assertEquals("Stored", message.getStatus());
        java.io.File json = new java.io.File("messages.json");
        assertTrue(json.exists());
        assertTrue(json.length() > 0);
        assertTrue(json.delete());
    }

}
