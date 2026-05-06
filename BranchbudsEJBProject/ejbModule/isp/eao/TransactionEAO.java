package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.Transaction;

@Stateless
public class TransactionEAO {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public void createTransaction(Transaction transaction) {
        em.persist(transaction);
    }

    public Transaction findTransaction(int id) {
        return em.find(Transaction.class, id);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return em.merge(transaction);
    }

    public void deleteTransaction(int id) {
        Transaction transaction = findTransaction(id);
        if (transaction != null) {
            em.remove(transaction);
        }
    }
}