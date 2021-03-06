package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import com.gitlab.mavogel.vislab.dtos.category.CategoryDto;
import com.gitlab.mavogel.vislab.dtos.product.FullProductDto;
import com.gitlab.mavogel.vislab.dtos.product.NewProductDto;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import com.gitlab.mavogel.vislab.dtos.product.SearchDto;
import hska.iwi.eShopMaster.config.TemplateFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * Handles requests for products.
 *
 * @author mavogel
 */
public class ProductManagerImpl implements ProductManager {

    private static final Logger LOG = LogManager.getLogger(ProductManager.class);

    public ProductManagerImpl() {
    }

    public List<FullProductDto> getProducts() {
        ResponseEntity<List<FullProductDto>> products;
        try {
            // see: https://stackoverflow.com/a/31947188
            products = TemplateFactory.getOAuth2RestTemplate()
                    .exchange(TemplateFactory.API_GATEWAY + "/product",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<FullProductDto>>() {
                            });
        } catch (Exception e) {
            LOG.error("Failed to get products!", e.getMessage());
            return Collections.emptyList();
        }
        return products.getBody();
    }

    public List<FullProductDto> getProductsForSearchValues(String searchDescription,
                                                    Double searchMinPrice, Double searchMaxPrice) {
        ResponseEntity<List<FullProductDto>> products;
        try {
            // see: https://stackoverflow.com/a/31947188
            final SearchDto searchDto = new SearchDto(searchDescription, searchMinPrice, searchMaxPrice);
            final HttpEntity<SearchDto> httpSearchEntity = new HttpEntity<SearchDto>(searchDto);
            products = TemplateFactory.getOAuth2RestTemplate()
                    .exchange(TemplateFactory.API_GATEWAY + "/product/search",
                            HttpMethod.POST, httpSearchEntity, new ParameterizedTypeReference<List<FullProductDto>>() {
                            });
        } catch (Exception e) {
            LOG.error("Failed to get products: " + e.getMessage(), e);
            return Collections.emptyList();
        }
        return products.getBody();
    }

    public FullProductDto getProductById(int id) {
        ResponseEntity<FullProductDto> product = null;
        try {
            product = TemplateFactory.getOAuth2RestTemplate()
                    .getForEntity(TemplateFactory.API_GATEWAY + "/product/" + id, FullProductDto.class);
        } catch (Exception e) {
            LOG.error("Failed to get product with id '" + id + "'", e.getMessage());
            return null;
        }
        return product.getBody();
    }

    public int addProduct(String name, double price, int categoryId, String details) {
        int productId = -1;

        CategoryManager categoryManager = new CategoryManagerImpl();
        CategoryDto category = categoryManager.getCategory(categoryId);

        if (category != null) {
            NewProductDto newProduct;
            if (details == null) {
                newProduct = new NewProductDto(name, price, category.getId(), "");
            } else {
                newProduct = new NewProductDto(name, price, category.getId(), details);
            }

            ResponseEntity<ProductDto> createdProduct = null;
            try {
                createdProduct = TemplateFactory.getOAuth2RestTemplate()
                        .postForEntity(TemplateFactory.API_GATEWAY + "/product", newProduct, ProductDto.class);
            } catch (Exception e) {
                LOG.error("Failed to add product with name ' " + name+ "' with message: " + e.getMessage());
                return productId;
            }
            productId = (int) createdProduct.getBody().getId();
        }

        return productId;
    }

    public void deleteProductById(int id) {
        try {
            TemplateFactory.getOAuth2RestTemplate()
                    .delete(TemplateFactory.API_GATEWAY + "/product/" + id);
        } catch (Exception e) {
            LOG.error("Failed to delete product with id '" + id + "' with error message:", e.getMessage());
        }
    }
}
