package isp.facade;

import jakarta.ejb.Local;
import java.util.List;
import isp.entity.Account;
import isp.entity.Transaction;
import isp.entity.Category;
import isp.entity.User;
import java.math.BigDecimal;
import java.sql.Timestamp;




@Local
public interface SystemFacadeLocal {
	
	public List<Category> findAllCategories();
	public Account findAccount(int accountId);
	public List<Account> findAllAccounts();
	public List<Transaction> findTransactionsForAccountId(int accountId);
	public Transaction findTransactionById(int transactionId);
	public User findCurrentUser();
	public Account findCurrentUserAccount();
	public List<Transaction> findTransactionsForCurrentUser();
	public Transaction createTransactionForAccount(int accountId, int categoryId, Timestamp transactionDate,
	        BigDecimal amount, String note, boolean repeatingTransaction);
	public Transaction createTransactionForCurrentUser(int categoryId, Timestamp transactionDate,
	        BigDecimal amount, String note, boolean repeatingTransaction);
	public void deleteTransactionById(int transactionId);
	public void deleteTransactionForCurrentUser(int transactionId);
	public Transaction updateTransactionById(int transactionId, int categoryId, Timestamp transactionDate,
	        BigDecimal amount, String note, boolean repeatingTransaction);
	public Transaction updateTransactionForCurrentUser(int transactionId, int categoryId,
	        Timestamp transactionDate, BigDecimal amount, String note, boolean repeatingTransaction);
	public Transaction findTransactionForCurrentUser(int transactionId);
	public List<User> getAllUsers();
	public double calculateTotalIncome(int accountId);
	public double calculateTotalExpenses(int accountId);
}
