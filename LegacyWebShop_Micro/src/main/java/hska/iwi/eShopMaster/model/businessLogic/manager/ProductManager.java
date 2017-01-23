package hska.iwi.eShopMaster.model.businessLogic.manager;

import com.gitlab.mavogel.vislab.dtos.product.FullProductDto;

import java.util.List;

public interface ProductManager {
    List<FullProductDto> getProducts();

    FullProductDto getProductById(int id);

    List<FullProductDto> getProductsForSearchValues(String searchValue, Double searchMinPrice, Double searchMaxPrice);

    int addProduct(String name, double price, int categoryId, String details);

    void deleteProductById(int id);
}
