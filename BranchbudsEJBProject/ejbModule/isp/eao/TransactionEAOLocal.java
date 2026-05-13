package isp.eao;

import jakarta.ejb.Local;
import java.util.List;
import isp.entity.Transaction;
import isp.entity.Account;

@Local
public interface TransactionEAOLocal {
    public Transaction findTransaction(int id);
    public Transaction createTransaction(Transaction transaction);
    public Transaction updateTransaction(Transaction transaction);
    public void deleteTransaction(int id);
    public List<Transaction> findTransactionsByAccount(Account account);
    public List<Transaction> findTransactionsByUserId(int userId);
	public Transaction findById(int transactionId);


}
