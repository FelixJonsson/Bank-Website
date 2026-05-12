package isp.eao;

import jakarta.ejb.Stateless;
import java.util.List;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.Transaction;
import isp.entity.Account;

@Stateless
public class TransactionEAOImpl implements TransactionEAOLocal {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public TransactionEAOImpl() {
    }

    public Transaction findTransaction(int id) {
        return em.find(Transaction.class, id);
    }

    public Transaction createTransaction(Transaction transaction) {
        em.persist(transaction);
        return transaction;
    }

    public Transaction updateTransaction(Transaction transaction) {
        em.merge(transaction);
        return transaction;
    }

    public void deleteTransaction(int id) {
        Transaction transaction = this.findTransaction(id);
        if (transaction != null) {
            em.remove(transaction);
        }
    }
    
    public List<Transaction> findTransactionsByAccount(Account account) {
        TypedQuery<Transaction> query =
            em.createNamedQuery("Transaction.findByAccount", Transaction.class);

        query.setParameter("account", account);

        List<Transaction> results = query.getResultList();
        return results;
    }

    public List<Transaction> findTransactionsByUserId(int userId) {
        TypedQuery<Transaction> query =
            em.createNamedQuery("Transaction.findByUserId", Transaction.class);

        query.setParameter("userId", userId);

        return query.getResultList();
    }

}
