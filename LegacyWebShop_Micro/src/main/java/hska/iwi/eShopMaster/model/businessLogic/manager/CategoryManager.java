package hska.iwi.eShopMaster.model.businessLogic.manager;

import com.gitlab.mavogel.vislab.dtos.category.CategoryDto;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;

import java.util.List;

public interface CategoryManager {
    List<CategoryDto> getCategories();

    CategoryDto getCategory(int id);

    void addCategory(String name);

    void delCategoryById(int id);
}
