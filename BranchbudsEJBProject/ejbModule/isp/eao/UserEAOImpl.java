package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.User;

@Stateless
public class UserEAOImpl implements UserEAOLocal {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public UserEAOImpl() {
        // Default constructor as shown on page 10
    }

    public User findUser(int id) {
        return em.find(User.class, id);
    }

    public User createUser(User user) {
        em.persist(user);
        return user; 
    }

    public User updateUser(User user) {
        em.merge(user);
        return user;
    }

    public void deleteUser(int id) {
        User user = this.findUser(id);
        if (user != null) {
            em.remove(user);
        }
    }
}