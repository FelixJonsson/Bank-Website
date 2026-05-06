package isp.eao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CategoryEAO {

    @PersistenceContext(unitName = "BranchbudsEJBProject")
    private EntityManager em;

}
