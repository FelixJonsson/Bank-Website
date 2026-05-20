package isp.facade;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.YearMonth;
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
    
    public List<Category> findAllCategories() {
        return categoryEAO.findAllCategories();
    }

    public Account findAccount(int accountId) {
        return accountEAO.findAccount(accountId);
    }

    public List<Account> findAllAccounts() {
        return accountEAO.findAllAccounts();
    }

    private Account findAccountForUser(int userId) {
        return accountEAO.findAccountByUserId(userId);
    }

    private List<Transaction> findTransactionsForUser(int userId) {
        return transactionEAO.findTransactionsByUserId(userId);
    }

    public List<Transaction> findTransactionsForAccountId(int accountId) {
        Account account = accountEAO.findAccount(accountId);
        if (account == null) {
            return null;
        }
        return transactionEAO.findTransactionsByAccount(account);
    }

    public Transaction findTransactionById(int transactionId) {
        return transactionEAO.findTransaction(transactionId);
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

    public void deleteTransactionById(int transactionId) {
        Transaction transaction = transactionEAO.findTransaction(transactionId);

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

        if (transaction == null || transaction.getAccount() == null || category == null || amount == null) {
            return null;
        }

        Account account = transaction.getAccount();
        double oldAmount = transaction.getAmount();
        BigDecimal normalizedAmount = normalizeAmountForCategory(category, amount);

        transaction.setCategory(category);
        transaction.setTransactionDate(transactionDate);
        transaction.setAmount(normalizedAmount.doubleValue());
        transaction.setNote(note);
        transaction.setRepeatingTransaction(repeatingTransaction);

        BigDecimal difference = normalizedAmount.subtract(BigDecimal.valueOf(oldAmount));
        account.setCurrentBalance(account.getCurrentBalance().add(difference));
        accountEAO.updateAccount(account);

        return transactionEAO.updateTransaction(transaction);
    }

    public Transaction updateTransactionById(int transactionId, int categoryId,
            Timestamp transactionDate, BigDecimal amount, String note, boolean repeatingTransaction) {

        Transaction transaction = transactionEAO.findTransaction(transactionId);
        Category category = categoryEAO.findCategory(categoryId);

        if (transaction == null || transaction.getAccount() == null || category == null || amount == null) {
            return null;
        }

        Account account = transaction.getAccount();
        double oldAmount = transaction.getAmount();
        BigDecimal normalizedAmount = normalizeAmountForCategory(category, amount);

        transaction.setCategory(category);
        transaction.setTransactionDate(transactionDate);
        transaction.setAmount(normalizedAmount.doubleValue());
        transaction.setNote(note);
        transaction.setRepeatingTransaction(repeatingTransaction);

        BigDecimal difference = normalizedAmount.subtract(BigDecimal.valueOf(oldAmount));
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
	        if(t.getAmount() > 0 && isTransactionInCurrentMonth(t)) { 
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
	            
	            if (t.getAmount() < 0 && isTransactionInCurrentMonth(t)) { 
	                
	                totalExpenses += Math.abs(t.getAmount());
	            }
	        }
	    }
	    return totalExpenses;
	}

	@Override
	public Transaction createTransactionForCurrentUser(int categoryId, Timestamp transactionDate, BigDecimal amount,
			String note, boolean repeatingTransaction) {

        Account account = this.findCurrentUserAccount();
        Category category = categoryEAO.findCategory(categoryId);

        if (account == null || category == null || amount == null) {
            return null;
        }

        BigDecimal normalizedAmount = normalizeAmountForCategory(category, amount);

        Transaction transaction = new Transaction(account, category, transactionDate, normalizedAmount.doubleValue(), note,
                repeatingTransaction);
        Transaction createdTransaction = transactionEAO.createTransaction(transaction);

        account.setCurrentBalance(account.getCurrentBalance().add(normalizedAmount));
        accountEAO.updateAccount(account);

        return createdTransaction;
	}

    @Override
    public Transaction createTransactionForAccount(int accountId, int categoryId, Timestamp transactionDate,
            BigDecimal amount, String note, boolean repeatingTransaction) {

        Account account = accountEAO.findAccount(accountId);
        Category category = categoryEAO.findCategory(categoryId);

        if (account == null || category == null || amount == null) {
            return null;
        }

        BigDecimal normalizedAmount = normalizeAmountForCategory(category, amount);

        Transaction transaction = new Transaction(account, category, transactionDate, normalizedAmount.doubleValue(), note,
                repeatingTransaction);
        Transaction createdTransaction = transactionEAO.createTransaction(transaction);

        account.setCurrentBalance(account.getCurrentBalance().add(normalizedAmount));
        accountEAO.updateAccount(account);

        return createdTransaction;
    }

    private BigDecimal normalizeAmountForCategory(Category category, BigDecimal amount) {
        BigDecimal absoluteAmount = amount.abs();

        if (isExpenseCategory(category)) {
            return absoluteAmount.negate();
        }

        return absoluteAmount;
    }

    private boolean isExpenseCategory(Category category) {
        if (category == null || category.getCategoryType() == null) {
            return false;
        }

        String categoryType = category.getCategoryType().trim();

        return "expense".equalsIgnoreCase(categoryType)
                || "expenses".equalsIgnoreCase(categoryType)
                || "utgift".equalsIgnoreCase(categoryType);
    }

    private boolean isTransactionInCurrentMonth(Transaction transaction) {
        if (transaction == null || transaction.getTransactionDate() == null) {
            return false;
        }

        YearMonth currentMonth = YearMonth.now();
        YearMonth transactionMonth = YearMonth.from(transaction.getTransactionDate().toLocalDateTime());

        return currentMonth.equals(transactionMonth);
    }

}
