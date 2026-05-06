package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.Account;

@Stateless
public class AccountEAOImpl implements AccountEAOLocal {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public AccountEAOImpl() {
    }

    public Account findAccount(int id) {
        return em.find(Account.class, id);
    }

    public Account createAccount(Account account) {
        em.persist(account);
        return account;
    }

    public Account updateAccount(Account account) {
        em.merge(account);
        return account;
    }

    public void deleteAccount(int id) {
        Account account = this.findAccount(id);
        if (account != null) {
            em.remove(account);
        }
    }
}