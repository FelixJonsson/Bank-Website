package isp.facade;

import jakarta.ejb.Stateless;
import java.util.List;

import jakarta.ejb.EJB;
import isp.eao.TransactionEAOLocal;
import isp.entity.Account;
import isp.entity.Transaction;


@Stateless
public class SystemFacade implements SystemFacadeLocal {
	
	@EJB
	private TransactionEAOLocal transactionEAO;


    public SystemFacade() {
    }
    
    public List<Transaction> findTransactionsByAccount(Account account) {
        return transactionEAO.findTransactionsByAccount(account);
    }

}
