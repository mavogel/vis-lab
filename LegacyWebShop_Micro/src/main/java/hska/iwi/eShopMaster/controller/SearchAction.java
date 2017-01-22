package hska.iwi.eShopMaster.controller;

import com.gitlab.mavogel.vislab.dtos.category.CategoryDto;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import com.gitlab.mavogel.vislab.dtos.user.UserDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.CategoryManagerImpl;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.ProductManagerImpl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchAction extends ActionSupport {

    /**
     *
     */
    private static final long serialVersionUID = -6565401833074694229L;


    private String searchDescription = null;
    private String searchMinPrice;
    private String searchMaxPrice;

    private Double sMinPrice = null;
    private Double sMaxPrice = null;

    private UserDto user;
    private List<ProductDto> products;
    private List<CategoryDto> categories;


    public String execute() throws Exception {

        String result = "input";

        // Get user:
        Map<String, Object> session = ActionContext.getContext().getSession();
        user = (UserDto) session.get("webshop_user");
        ActionContext.getContext().setLocale(Locale.US);

        if (user != null) {
            // Search products and show results:
            ProductManager productManager = new ProductManagerImpl();
//			this.products = productManager.getProductsForSearchValues(this.searchDescription, this.searchMinPrice, this.searchMaxPrice);
            if (!searchMinPrice.isEmpty()) {
                sMinPrice = Double.parseDouble(this.searchMinPrice);
            } else {
                sMinPrice = Double.MIN_VALUE;
            }
            if (!searchMaxPrice.isEmpty()) {
                sMaxPrice = Double.parseDouble(this.searchMaxPrice);
            } else {
                sMaxPrice = Double.MAX_VALUE;
            }
            this.products = productManager.getProductsForSearchValues(this.searchDescription, sMinPrice, sMaxPrice);

            // Show all categories:
            CategoryManager categoryManager = new CategoryManagerImpl();
            this.categories = categoryManager.getCategories();
            result = "success";
        }

        return result;
    }


    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }


    public String getSearchValue() {
        return searchDescription;
    }


    public void setSearchValue(String searchValue) {
        this.searchDescription = searchValue;
    }


    public String getSearchMinPrice() {
        return searchMinPrice;
    }


    public void setSearchMinPrice(String searchMinPrice) {
        this.searchMinPrice = searchMinPrice;
    }


    public String getSearchMaxPrice() {
        return searchMaxPrice;
    }


    public void setSearchMaxPrice(String searchMaxPrice) {
        this.searchMaxPrice = searchMaxPrice;
    }


//	public Double getSearchMinPrice() {
//		return searchMinPrice;
//	}
//
//
//	public void setSearchMinPrice(Double searchMinPrice) {
//		this.searchMinPrice = searchMinPrice;
//	}
//
//
//	public Double getSearchMaxPrice() {
//		return searchMaxPrice;
//	}
//
//
//	public void setSearchMaxPrice(Double searchMaxPrice) {
//		this.searchMaxPrice = searchMaxPrice;
//	}
}
