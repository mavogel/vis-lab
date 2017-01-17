package hska.iwi.eShopMaster.model.businessLogic.manager;

import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;


public interface UserManager {
    boolean registerUser(String username, String firstname, String lastname, String password, Role role);
    User getUserByUsername(String username);
    boolean deleteUserById(int id);
    Role getRoleByLevel(int level);
}
