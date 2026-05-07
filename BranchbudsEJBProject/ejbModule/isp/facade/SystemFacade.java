package isp.facade;

import jakarta.ejb.Stateless;
import java.util.List;

import jakarta.ejb.EJB;
import isp.eao.TransactionEAOLocal;
import isp.entity.Account;
import isp.entity.Transaction;
import isp.eao.CategoryEAOLocal;
import isp.entity.Category;
import isp.eao.AccountEAOLocal;
import isp.entity.User;




@Stateless
public class SystemFacade implements SystemFacadeLocal {
	
	@EJB
	private TransactionEAOLocal transactionEAO; 
	@EJB
	private CategoryEAOLocal categoryEAO;
	@EJB
	private AccountEAOLocal accountEAO;
	

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



}
