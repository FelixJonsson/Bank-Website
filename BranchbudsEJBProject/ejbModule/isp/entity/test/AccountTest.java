package isp.entity.test;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import isp.entity.Account;

class AccountTest {

    private String expectedName;
    private String expectedType;
    private BigDecimal expectedBalance;
    private Account account; 
    
    @BeforeEach
    void setUp() throws Exception {
        expectedName = "Savings account";
        expectedType = "Private";
        expectedBalance = new BigDecimal("1000.50");
        
        account = new Account(null, expectedName, expectedType, expectedBalance);
    }

    @AfterEach
    void tearDown() throws Exception {
        account = null; 
    }

    @Test
    final void testGetAccountName() {
        assertEquals(expectedName, account.getAccountName());
    }

    @Test
    final void testSetAccountName() {
        String newName = "Salary account"; 
        account.setAccountName(newName); 
        assertEquals(newName, account.getAccountName());
    }

    @Test
    final void testGetCurrentBalance() {
        assertEquals(expectedBalance, account.getCurrentBalance());
    }
}