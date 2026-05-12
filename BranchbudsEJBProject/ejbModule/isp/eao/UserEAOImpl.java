package isp.eao;

import jakarta.ejb.Stateless;
import java.util.List;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import isp.entity.User;

@Stateless
public class UserEAOImpl implements UserEAOLocal {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public UserEAOImpl() {
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
    
    public User findUserByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", User.class)
                     .setParameter("email", email) 
                     .getSingleResult();
                     
        } catch (NoResultException e) {
            return null; 
        }
    }
    
    public List<User> getAllUsers() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }
}