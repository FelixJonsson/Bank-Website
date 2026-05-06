package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.User;

@Stateless
public class UserEAO {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public void createUser(User user) {
        em.persist(user);
    }

    public User findUser(int id) {
        return em.find(User.class, id);
    }

    public User updateUser(User user) {
        return em.merge(user);
    }

    public void deleteUser(int id) {
        User user = findUser(id);
        if (user != null) {
            em.remove(user);
        }
    }
}
