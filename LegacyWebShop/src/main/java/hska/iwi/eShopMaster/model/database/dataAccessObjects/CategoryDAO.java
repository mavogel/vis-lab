package hska.iwi.eShopMaster.model.database.dataAccessObjects;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hska.iwi.eShopMaster.model.database.GenericHibernateDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;

public class CategoryDAO {//extends GenericHibernateDAO<Category, Integer> {
	
	private String baseUrl="http://localhost:8088/category";
	private Client client;
	
	public CategoryDAO() {
		client = ClientBuilder.newClient();
	}

	public List<Category> getObjectList() {
		WebTarget webTarget = client.target(baseUrl).path("");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		System.out.println("Getting all categories " + response.getStatus());
		GenericType<List<Category>> gtlp = new GenericType<List<Category>>() {};
		List<Category> categories = response.readEntity(gtlp);
		return categories;
	}
	
	public Category getObjectById(int id) {
		WebTarget webTarget = client.target(baseUrl).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		System.out.println("Getting category by id " + response.getStatus());
		Category category = response.readEntity(Category.class);
		return category;	
	}
	
	public void saveObject(Category category) {
		WebTarget webTarget = client.target(baseUrl).path("");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(category, MediaType.APPLICATION_JSON));
		System.out.println("Creating category status " + response.getStatus());
	}
	
	public void deleteById(int id) {
		WebTarget webTarget = client.target(baseUrl).path(String.valueOf(id));
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.delete();
		System.out.println("Deleting category " + response.getStatus());
	}
	
}
