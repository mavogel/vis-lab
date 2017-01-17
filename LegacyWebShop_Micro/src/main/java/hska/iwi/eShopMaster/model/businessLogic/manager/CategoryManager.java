package hska.iwi.eShopMaster.model.businessLogic.manager;

import hska.iwi.eShopMaster.model.database.dataobjects.Category;

import java.util.List;

public interface CategoryManager {
	List<Category> getCategories();
	Category getCategory(int id);
	Category getCategoryByName(String name);
	void addCategory(String name);
	void delCategory(Category cat);
	void delCategoryById(int id);
}
