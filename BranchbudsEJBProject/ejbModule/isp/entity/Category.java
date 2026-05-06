package isp.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Category implements Serializable {

	public Category() {
		
	}
	
	public Category(String categoryName, String description, String categoryType) {
		this.categoryName = categoryName;
		this.description = description;
		this.categoryType = categoryType;
		
	}
	
    @Id
    @GeneratedValue
    private int categoryId;
    private String categoryName;
    private String description;
    private String categoryType;

    public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

}
