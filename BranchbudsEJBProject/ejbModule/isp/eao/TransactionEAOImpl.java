package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.Transaction;

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
}