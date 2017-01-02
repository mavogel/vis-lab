package hska.iwi.eShopMaster.model.database.dataAccessObjects;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hska.iwi.eShopMaster.model.database.GenericHibernateDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;

public class RoleDAO extends GenericHibernateDAO<Role, Integer> {
	
	private String baseUrl = "http://localhost:8088/user/level/";
	private Client client;
	
	public RoleDAO() {
		client = ClientBuilder.newClient();
	}
	
	public Role getRoleByLevel(int roleLevel) {
		WebTarget webTarget = client.target(baseUrl).path(String.valueOf(roleLevel));
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		Role r = response.readEntity(Role.class);
		System.out.println("Getting role " + response.getStatus());
		return r;
	}
	


}
