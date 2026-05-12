package isp.facade;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import isp.eao.AccountEAOLocal;
import isp.eao.CategoryEAOLocal;
import isp.eao.TransactionEAOLocal;
import isp.eao.UserEAOLocal;
import isp.entity.Account;
import isp.entity.Category;
import isp.entity.Transaction;
import isp.entity.User;



@Stateless
public class SystemFacade implements SystemFacadeLocal {
	
	private static final int CURRENT_USER_ID = 1; // user with ID 1 is the current user for demonstration purposes
	
	@EJB
	private TransactionEAOLocal transactionEAO; 
	
	@EJB
	private CategoryEAOLocal categoryEAO;
	
	@EJB
	private AccountEAOLocal accountEAO;
	
	@EJB
	private UserEAOLocal userEAO;

	

    public SystemFacade() {
    }
    
    public List<Transaction> findTransactionsByAccount(Account account) {
        return transactionEAO.findTransactionsByAccount(account);
    }
    
    public List<Category> findAllCategories() {
        return categoryEAO.findAllCategories();
    }
    
    public Account findAccountByUser(User user) {
        return accountEAO.findAccountByUser(user);
    }

    public User findUser(int userId) {
        return userEAO.findUser(userId);
    }

    public Account findAccountForUser(int userId) {
        return accountEAO.findAccountByUserId(userId);
    }

    public List<Transaction> findTransactionsForUser(int userId) {
        return transactionEAO.findTransactionsByUserId(userId);
    }
    
    public User findCurrentUser() {
        return this.findUser(CURRENT_USER_ID);
    }

    public Account findCurrentUserAccount() {
        return this.findAccountForUser(CURRENT_USER_ID);
    }

    public List<Transaction> findTransactionsForCurrentUser() {
        return this.findTransactionsForUser(CURRENT_USER_ID);
    }

    public Transaction createTransactionForCurrentUser(int categoryId, Timestamp transactionDate,
            BigDecimal amount, String note, boolean repeatingTransaction) {

        Account account = this.findCurrentUserAccount();
        Category category = categoryEAO.findCategory(categoryId);

        if (account == null || category == null) {
            return null;
        }

        // Apply the transaction amount to the account balance after the transaction is saved.
        Transaction transaction = new Transaction(account, category, transactionDate, amount, note, repeatingTransaction);
        Transaction createdTransaction = transactionEAO.createTransaction(transaction);

        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        accountEAO.updateAccount(account);

        return createdTransaction;

    }
    
    public void deleteTransactionForCurrentUser(int transactionId) {
    	Transaction transaction = this.findTransactionForCurrentUser(transactionId);


        if (transaction == null || transaction.getAccount() == null) {
            return;
        }

        Account account = transaction.getAccount();
        account.setCurrentBalance(account.getCurrentBalance().subtract(transaction.getAmount()));
        accountEAO.updateAccount(account);

        transactionEAO.deleteTransaction(transactionId);
    }

    // Updates a transaction and adjusts the account balance by the difference between old and new amount.

    public Transaction updateTransactionForCurrentUser(int transactionId, int categoryId,
            Timestamp transactionDate, BigDecimal amount, String note, boolean repeatingTransaction) {

    	Transaction transaction = this.findTransactionForCurrentUser(transactionId);
        Category category = categoryEAO.findCategory(categoryId);

        if (transaction == null || transaction.getAccount() == null || category == null) {
            return null;
        }

        Account account = transaction.getAccount();
        BigDecimal oldAmount = transaction.getAmount();

        transaction.setCategory(category);
        transaction.setTransactionDate(transactionDate);
        transaction.setAmount(amount);
        transaction.setNote(note);
        transaction.setRepeatingTransaction(repeatingTransaction);

        BigDecimal difference = amount.subtract(oldAmount);
        account.setCurrentBalance(account.getCurrentBalance().add(difference));
        accountEAO.updateAccount(account);

        return transactionEAO.updateTransaction(transaction);
    }

    public Transaction findTransactionForCurrentUser(int transactionId) {
        Transaction transaction = transactionEAO.findTransaction(transactionId);
        Account account = this.findCurrentUserAccount();

        if (transaction == null || account == null || transaction.getAccount() == null) {
            return null;
        }

        if (transaction.getAccount().getAccountId() != account.getAccountId()) {
            return null;
        }

        return transaction;
    }
    
    public List<User> getAllUsers() {
        return userEAO.getAllUsers();
    }



}
