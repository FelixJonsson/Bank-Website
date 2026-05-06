package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.Account;

@Stateless
public class AccountEAO {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public void createAccount(Account account) {
        em.persist(account);
    }

    public Account findAccount(int id) {
        return em.find(Account.class, id);
    }

    public Account updateAccount(Account account) {
        return em.merge(account);
    }

    public void deleteAccount(int id) {
        Account account = findAccount(id);
        if (account != null) {
            em.remove(account);
        }
    }
}