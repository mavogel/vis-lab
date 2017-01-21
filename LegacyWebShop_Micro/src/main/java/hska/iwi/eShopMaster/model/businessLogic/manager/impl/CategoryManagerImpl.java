package hska.iwi.eShopMaster.model.businessLogic.manager.impl;


import com.gitlab.mavogel.vislab.dtos.category.CategoryDto;
import com.gitlab.mavogel.vislab.dtos.category.NewCategoryDto;
import hska.iwi.eShopMaster.config.TemplateFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author mavogel
 */
public class CategoryManagerImpl implements CategoryManager {

    private static final Logger LOG = LogManager.getLogger(CategoryManager.class);

    public CategoryManagerImpl() {
    }

    public List<CategoryDto> getCategories() {
        ResponseEntity<List<CategoryDto>> categories;
        try {
            // see: https://stackoverflow.com/a/31947188
            categories = TemplateFactory.getOAuth2RestTemplate()
                    .exchange(TemplateFactory.API_GATEWAY + "/category",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<CategoryDto>>() {
                            });
        } catch (Exception e) {
            LOG.error("Failed to get categories!", e.getMessage());
            return Collections.emptyList();
        }
        return categories.getBody();
    }

    public CategoryDto getCategory(int id) {
        ResponseEntity<CategoryDto> category = null;
        try {
            category = TemplateFactory.getOAuth2RestTemplate()
                    .getForEntity(TemplateFactory.API_GATEWAY + "/category/" + id, CategoryDto.class);
        } catch (Exception e) {
            LOG.error("Failed to get category with id '" + id + "'", e.getMessage());
            return null;
        }
        return category.getBody();
    }

    public void addCategory(String name) {
        NewCategoryDto newCategory = new NewCategoryDto(name);
        try {
            ResponseEntity<Void> voidResponseEntity = TemplateFactory.getOAuth2RestTemplate()
                    .postForEntity(TemplateFactory.API_GATEWAY + "/category", newCategory, Void.class);
            if (!HttpStatus.CREATED.equals(voidResponseEntity.getStatusCode())) {
                LOG.error("Failed to add category with name '" + name + "'");
            }
        } catch (Exception e) {
            LOG.error("Failed to add category with name '" + name + "' with error message:", e.getMessage());
        }
    }

    public void delCategoryById(int id) {
        try {
            TemplateFactory.getOAuth2RestTemplate()
                    .delete(TemplateFactory.API_GATEWAY + "/category/" + id);
        } catch (Exception e) {
            LOG.error("Failed to delete category with id '" + id + "' with error message:", e.getMessage());
        }
    }
}
