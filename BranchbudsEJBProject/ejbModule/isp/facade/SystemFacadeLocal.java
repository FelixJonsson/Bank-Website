package isp.facade;

import jakarta.ejb.Local;
import java.util.List;

import isp.entity.Account;
import isp.entity.Transaction;
import isp.entity.Category;
import isp.entity.User;




@Local
public interface SystemFacadeLocal {
	
	public List<Transaction> findTransactionsByAccount(Account account);
	public List<Category> findAllCategories();
	public Account findAccountByUser(User user);
	public User findCurrentUser();
	public Account findCurrentUserAccount();


}
