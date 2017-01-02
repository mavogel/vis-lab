package hska.iwi.eShopMaster.model.database.dataobjects;

public class ProductDTO {
	
	private long category;
	
	private long categoryId;

	private String name;
	
	private double price;
	
	private String details;
	
	private int id;

	public ProductDTO(Product p) {
		this.category = p.getCategory().getId();
		this.name = p.getName();
		this.price = p.getPrice();
		this.details = p.getDetails();
	}
	
	public ProductDTO() {}

	public ProductDTO(int id, long category, long categoryId, String name, double price, String details) {
		super();
		this.category = category;
		this.name = name;
		this.price = price;
		this.details = details;
		this.categoryId = categoryId;
		this.id = id;
	}

	public long getCategory() {
		return category;
	}

	public void setCategory(long category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
