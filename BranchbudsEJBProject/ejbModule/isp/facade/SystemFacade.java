package isp.facade;

import jakarta.ejb.Stateless;
import java.util.List;

import jakarta.ejb.EJB;
import isp.eao.TransactionEAOLocal;
import isp.entity.Account;
import isp.entity.Transaction;
import isp.eao.CategoryEAOLocal;
import isp.entity.Category;



@Stateless
public class SystemFacade implements SystemFacadeLocal {
	
	@EJB
	private TransactionEAOLocal transactionEAO; 
	@EJB
	private CategoryEAOLocal categoryEAO;
	

    public SystemFacade() {
    }
    
    public List<Transaction> findTransactionsByAccount(Account account) {
        return transactionEAO.findTransactionsByAccount(account);
    }
    
    public List<Category> findAllCategories() {
        return categoryEAO.findAllCategories();
    }


}
