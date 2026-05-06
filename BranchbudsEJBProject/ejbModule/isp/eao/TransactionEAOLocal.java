package isp.eao;

import jakarta.ejb.Local;
import isp.entity.Transaction;

@Local
public interface TransactionEAOLocal {
    public Transaction findTransaction(int id);
    public Transaction createTransaction(Transaction transaction);
    public Transaction updateTransaction(Transaction transaction);
    public void deleteTransaction(int id);
}