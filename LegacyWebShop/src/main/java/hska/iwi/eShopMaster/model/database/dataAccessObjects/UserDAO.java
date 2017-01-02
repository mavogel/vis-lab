package hska.iwi.eShopMaster.model.database.dataAccessObjects;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.sessionFactory.util.HibernateUtil;

public class UserDAO { 
	
	private String baseUrl = "http://localhost:8088/user/";
	private Client client;
	
	public UserDAO() {
		client = ClientBuilder.newClient();
	}

	public void saveObject(User user) {
		WebTarget webTarget = client.target(baseUrl).path("");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(user, MediaType.APPLICATION_JSON));
		System.out.println("Registering user status " + response.getStatus());
	}
	
	public User getUserByUsername(String name) {
		WebTarget webTarget = client.target(baseUrl).path(name);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		User u = response.readEntity(User.class);
		System.out.println("Getting user by name " + response.getStatus());
		return u;
	}



}
