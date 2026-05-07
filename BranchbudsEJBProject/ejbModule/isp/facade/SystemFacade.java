package isp.facade;

import jakarta.ejb.Stateless;
import java.util.List;
import java.util.ArrayList;
import jakarta.ejb.EJB;
import isp.eao.TransactionEAOLocal;
import isp.entity.Account;
import isp.entity.Transaction;
import isp.eao.CategoryEAOLocal;
import isp.entity.Category;
import isp.eao.AccountEAOLocal;
import isp.entity.User;
import isp.eao.UserEAOLocal;
import java.math.BigDecimal;
import java.sql.Timestamp;




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
    
    public User findCurrentUser() {
        return userEAO.findUser(CURRENT_USER_ID);
    }

    public Account findCurrentUserAccount() {
        User user = this.findCurrentUser();

        if (user == null) {
            return null;
        }

        return accountEAO.findAccountByUser(user);
    }

    public List<Transaction> findTransactionsForCurrentUser() {
        Account account = this.findCurrentUserAccount();

        if (account == null) {
            return new ArrayList<Transaction>();
        }

        return transactionEAO.findTransactionsByAccount(account);
    }

    public Transaction createTransactionForCurrentUser(int categoryId, Timestamp transactionDate,
            BigDecimal amount, String note, boolean repeatingTransaction) {

        Account account = this.findCurrentUserAccount();
        Category category = categoryEAO.findCategory(categoryId);

        if (account == null || category == null) {
            return null;
        }

        // updates balance
        Transaction transaction = new Transaction(account, category, transactionDate, amount, note, repeatingTransaction);
        Transaction createdTransaction = transactionEAO.createTransaction(transaction);

        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        accountEAO.updateAccount(account);

        return createdTransaction;

    }
    
    public void deleteTransactionForCurrentUser(int transactionId) {
        Transaction transaction = transactionEAO.findTransaction(transactionId);

        if (transaction == null || transaction.getAccount() == null) {
            return;
        }

        Account account = transaction.getAccount();
        account.setCurrentBalance(account.getCurrentBalance().subtract(transaction.getAmount()));
        accountEAO.updateAccount(account);

        transactionEAO.deleteTransaction(transactionId);
    }

    // updates a transaction and adjusts the account balance accordingly
    public Transaction updateTransactionForCurrentUser(int transactionId, int categoryId,
            Timestamp transactionDate, BigDecimal amount, String note, boolean repeatingTransaction) {

        Transaction transaction = transactionEAO.findTransaction(transactionId);
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




}
