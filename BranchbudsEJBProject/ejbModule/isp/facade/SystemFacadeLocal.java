package isp.facade;

import jakarta.ejb.Local;
import java.util.List;

import isp.entity.Account;
import isp.entity.Transaction;


@Local
public interface SystemFacadeLocal {
	
	public List<Transaction> findTransactionsByAccount(Account account);

}
