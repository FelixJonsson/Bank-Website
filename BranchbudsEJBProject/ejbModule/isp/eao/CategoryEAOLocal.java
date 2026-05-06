package isp.eao;

import jakarta.ejb.Local;
import isp.entity.Category;

@Local
public interface CategoryEAOLocal {
    public Category findCategory(int id);
    public Category createCategory(Category category);
    public Category updateCategory(Category category);
    public void deleteCategory(int id);
}