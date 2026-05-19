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
@jakarta.interceptor.Interceptors(isp.interceptor.LoggingInterceptor.class)
public class SystemFacade implements SystemFacadeLocal {
	
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

    public Account findAccount(int accountId) {
        return accountEAO.findAccount(accountId);
    }

    public Account findAccountByName(String accountName) {
        return accountEAO.findAccountByName(accountName);
    }

    public List<Account> findAllAccounts() {
        return accountEAO.findAllAccounts();
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

    public List<Transaction> findTransactionsForAccountId(int accountId) {
        Account account = accountEAO.findAccount(accountId);
        if (account == null) {
            return null;
        }
        return transactionEAO.findTransactionsByAccount(account);
    }

    public List<Transaction> findTransactionsForAccountName(String accountName) {
        Account account = accountEAO.findAccountByName(accountName);
        if (account == null) {
            return null;
        }
        return transactionEAO.findTransactionsByAccount(account);
    }
    
    public User findCurrentUser() {
        List<User> users = userEAO.getAllUsers();

        if (users == null || users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    public Account findCurrentUserAccount() {
        User currentUser = this.findCurrentUser();

        if (currentUser == null) {
            return null;
        }

        return this.findAccountForUser(currentUser.getUserId());
    }


    public List<Transaction> findTransactionsForCurrentUser() {
        User currentUser = this.findCurrentUser();

        if (currentUser == null) {
            return null;
        }

        return this.findTransactionsForUser(currentUser.getUserId());
    }

    public void deleteTransactionForCurrentUser(int transactionId) {
    	Transaction transaction = this.findTransactionForCurrentUser(transactionId);


        if (transaction == null || transaction.getAccount() == null) {
            return;
        }

        Account account = transaction.getAccount();
        account.setCurrentBalance(account.getCurrentBalance().subtract(BigDecimal.valueOf(transaction.getAmount())));
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
        double oldAmount = transaction.getAmount();

        transaction.setCategory(category);
        transaction.setTransactionDate(transactionDate);
        transaction.setAmount(amount.doubleValue());
        transaction.setNote(note);
        transaction.setRepeatingTransaction(repeatingTransaction);

        BigDecimal difference = amount.subtract(BigDecimal.valueOf(oldAmount));
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

	public double calculateTotalIncome(int accountId) {
		Account account = accountEAO.findAccount(accountId);
	    double totalIncome = 0;

	    if (account == null || account.getTransactions() == null) {
	    	return totalIncome;
	    }

	    for(Transaction t : account.getTransactions()) {
	        if(t.getAmount() > 0) { 
	            totalIncome += t.getAmount();
	        }
	    }
	    return totalIncome;
	}

	@Override
	public double calculateTotalExpenses(int accountId) {
	    Account account = accountEAO.findAccount(accountId);
	    double totalExpenses = 0;

	    if (account != null && account.getTransactions() != null) {
	        
	        for (Transaction t : account.getTransactions()) {
	            
	            if (t.getAmount() < 0) { 
	                
	                totalExpenses += Math.abs(t.getAmount());
	            }
	        }
	    }
	    return totalExpenses;
	}

	@Override
	public double calculateRecurringExpenses(int accountId) {
	    Account account = accountEAO.findAccount(accountId);
	    double recurringExpenses = 0;

	    if (account != null && account.getTransactions() != null) {
	        for (Transaction t : account.getTransactions()) {
	            
	        	if (t.getAmount() < 0 && t.isRepeatingTransaction()) { 
	        	    recurringExpenses += Math.abs(t.getAmount());
	        	}
	        }
	    }
	    return recurringExpenses;
	}

	@Override
	public Transaction createTransactionForCurrentUser(int categoryId, Timestamp transactionDate, BigDecimal amount,
			String note, boolean repeatingTransaction) {

        Account account = this.findCurrentUserAccount();
        Category category = categoryEAO.findCategory(categoryId);

        if (account == null || category == null || amount == null) {
            return null;
        }

        Transaction transaction = new Transaction(account, category, transactionDate, amount.doubleValue(), note,
                repeatingTransaction);
        Transaction createdTransaction = transactionEAO.createTransaction(transaction);

        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        accountEAO.updateAccount(account);

        return createdTransaction;
	}

	@Override
	public void updateTransaction(int transactionId, int newCategoryId, double newAmount) {
        Transaction transaction = transactionEAO.findById(transactionId);
        Category category = categoryEAO.findById(newCategoryId);

        if (transaction == null || category == null || transaction.getAccount() == null) {
            return;
        }

        Account account = transaction.getAccount();
        BigDecimal oldAmount = BigDecimal.valueOf(transaction.getAmount());
        BigDecimal updatedAmount = BigDecimal.valueOf(newAmount);

        transaction.setCategory(category);
        transaction.setAmount(newAmount);
        transactionEAO.updateTransaction(transaction);

        BigDecimal difference = updatedAmount.subtract(oldAmount);
        account.setCurrentBalance(account.getCurrentBalance().add(difference));
        accountEAO.updateAccount(account);
	}

}
