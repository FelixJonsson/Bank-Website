package isp.entity;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;


@Entity
@NamedQueries({
    @NamedQuery(
        name = "Category.findAll",
        query = "SELECT c FROM Category c ORDER BY c.categoryName"
    )
})

@Table(name = "[Category]", schema = "dbo")
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

    private int categoryId;
    private String categoryName;
    private String description;
    private String categoryType;

    public Category() {
    }

    public Category(String categoryName, String description, String categoryType) {
        this.categoryName = categoryName;
        this.description = description;
        this.categoryType = categoryType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryId")
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "CategoryName")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CategoryType")
    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
