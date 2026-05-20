package isp.entity.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import isp.entity.Transaction;

class TransactionTest {

    private double expectedAmount;
    private String expectedNote;
    private boolean expectedRepeating;
    private Transaction transaction;

    @BeforeEach
    void setUp() throws Exception {
        expectedAmount = 500.50;
        expectedNote = "Food from grocery store";
        expectedRepeating = false;
        
        transaction = new Transaction();
        transaction.setAmount(expectedAmount);
        transaction.setNote(expectedNote);
        transaction.setRepeatingTransaction(expectedRepeating);
    }

    @AfterEach
    void tearDown() throws Exception {
        transaction = null;
    }

    @Test
    final void testGetAmount() {
        assertEquals(expectedAmount, transaction.getAmount(), "Amount does not match expected value.");
    }

    @Test
    final void testSetNote() {
        String newNote = "Dinner at restaurant";
        transaction.setNote(newNote);
        
        assertEquals(newNote, transaction.getNote());
    }

    @Test
    final void testIsRepeatingTransaction() {
        assertFalse(transaction.isRepeatingTransaction());
    }
}