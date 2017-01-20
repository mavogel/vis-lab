package hska.iwi.eShopMaster.model.businessLogic.manager;

import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;

import java.util.List;

public interface ProductManager {
    List<ProductDto> getProducts();

    ProductDto getProductById(int id);

//    ProductDto getProductByName(String name);

    int addProduct(String name, double price, int categoryId, String details);

    List<ProductDto> getProductsForSearchValues(String searchValue, Double searchMinPrice, Double searchMaxPrice);

//    boolean deleteProductsByCategoryId(int categoryId);

    void deleteProductById(int id);


}
