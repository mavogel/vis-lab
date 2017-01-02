package hska.iwi.eShopMaster.model.database.dataAccessObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import hska.iwi.eShopMaster.model.database.GenericHibernateDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.dataobjects.ProductDTO;
import hska.iwi.eShopMaster.model.database.dataobjects.Search;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.sessionFactory.util.HibernateUtil;

public class ProductDAO { //extends GenericHibernateDAO<Product, Integer> {

	private String baseUrl = "http://localhost:8088/product/";
	private Client client;
	
	public ProductDAO() {
		client = ClientBuilder.newClient();
	}
	
	public List<Product> getProductListByCriteria(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {
		//return getObjectList();
		
		Search search = new Search(searchDescription);
		WebTarget webTarget = client.target(baseUrl).path("search");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		// TODO: This can't be reached currently, it's a get on the server instead of a post route.
		Response response = invocationBuilder.post(Entity.entity(search, MediaType.APPLICATION_JSON));
		System.out.println("Getting products by search " + response.getStatus());
		GenericType<List<Product>> gtlp = new GenericType<List<Product>>() {};
		List<Product> products = response.readEntity(gtlp);
		
	    return products;
	}
	
	public List<Product> getObjectList() {
		WebTarget webTarget = client.target(baseUrl).path("");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		System.out.println("Getting all products " + response.getStatus());
		GenericType<List<ProductDTO>> gtlp = new GenericType<List<ProductDTO>>() {};
		List<ProductDTO> productDTOs = response.readEntity(gtlp);
		List<Product> products = new ArrayList<Product>();
		
		for (ProductDTO dto: productDTOs) {
			products.add(new Product(dto));
		}
				
		return products;
	}
	
	
	public Product getObjectById(int id) {
		WebTarget webTarget = client.target(baseUrl).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		System.out.println("Getting product by id " + response.getStatus());
		ProductDTO productDTO = response.readEntity(ProductDTO.class);
		System.out.println("Gotten product has cat id " + productDTO.getCategory());
		return new Product(productDTO);
	}
	
	public void deleteById(int id) {
		WebTarget webTarget = client.target(baseUrl).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.delete();
		System.out.println("Deleting product " + response.getStatus());
	}
	
	public void saveObject(Product product) {
		WebTarget webTarget = client.target(baseUrl).path("");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(new ProductDTO(product), MediaType.APPLICATION_JSON));
		System.out.println("Creating product status " + response.getStatus());
		if (response.getStatus() != 201) {
			System.out.println(response.readEntity(String.class));
		}
	}

	
}
