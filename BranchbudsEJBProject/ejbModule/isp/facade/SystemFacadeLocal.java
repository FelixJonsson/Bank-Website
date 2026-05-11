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
	
	public List<Transaction> findTransactionsByAccount(Account account);
	public List<Category> findAllCategories();
	public Account findAccountByUser(User user);
	public User findCurrentUser();
	public Account findCurrentUserAccount();
	public List<Transaction> findTransactionsForCurrentUser();
	public Transaction createTransactionForCurrentUser(int categoryId, Timestamp transactionDate,
	        BigDecimal amount, String note, boolean repeatingTransaction);
	public void deleteTransactionForCurrentUser(int transactionId);
	public Transaction updateTransactionForCurrentUser(int transactionId, int categoryId,
	        Timestamp transactionDate, BigDecimal amount, String note, boolean repeatingTransaction);
	public Transaction findTransactionForCurrentUser(int transactionId);

}
