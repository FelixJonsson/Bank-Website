package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import isp.entity.Category;

@Stateless
public class CategoryEAOImpl implements CategoryEAOLocal {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

    public CategoryEAOImpl() {
    }

    public Category findCategory(int id) {
        return em.find(Category.class, id);
    }

    public Category createCategory(Category category) {
        em.persist(category);
        return category;
    }

    public Category updateCategory(Category category) {
        em.merge(category);
        return category;
    }

    public void deleteCategory(int id) {
        Category category = this.findCategory(id);
        if (category != null) {
            em.remove(category);
        }
    }
}