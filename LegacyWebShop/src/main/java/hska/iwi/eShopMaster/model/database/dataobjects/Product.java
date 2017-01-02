package hska.iwi.eShopMaster.model.database.dataobjects;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import hska.iwi.eShopMaster.model.database.dataAccessObjects.CategoryDAO;

/**
 * This class contains details about products.
 */
@Entity
@Table(name = "product")
public class Product implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private double price;
	
	private CategoryDAO categoryDAO;

	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "details")
	private String details;

	public Product() {
		this.categoryDAO = new CategoryDAO();
	}
	
	public Product(ProductDTO dto) {
		this.categoryDAO = new CategoryDAO();
		this.name = dto.getName();
		this.price = dto.getPrice();
		this.details = dto.getDetails();
		this.id = dto.getId();
		if (dto.getCategory() > 0) {
			this.category = categoryDAO.getObjectById((int) dto.getCategory());
		} else if (dto.getCategoryId() > 0) {
			this.category = categoryDAO.getObjectById((int) dto.getCategoryId());
		}
	}

	public Product(String name, double price, Category category) {
		this.categoryDAO = new CategoryDAO();
		this.name = name;
		this.price = price;
		this.category = category;
	}

	public Product(String name, double price, Category category, String details) {
		this.categoryDAO = new CategoryDAO();
		this.name = name;
		this.price = price;
		this.category = category;
		this.details = details;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
