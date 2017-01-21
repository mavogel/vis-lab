package hska.iwi.eShopMaster.model.businessLogic.manager;

import com.gitlab.mavogel.vislab.dtos.product.ProductDto;

import java.util.List;

public interface ProductManager {
    List<ProductDto> getProducts();

    ProductDto getProductById(int id);

    int addProduct(String name, double price, int categoryId, String details);

    List<ProductDto> getProductsForSearchValues(String searchValue, Double searchMinPrice, Double searchMaxPrice);

    void deleteProductById(int id);
}
