package hska.iwi.eShopMaster.model.businessLogic.manager;

import com.gitlab.mavogel.vislab.dtos.user.RoleDto;
import com.gitlab.mavogel.vislab.dtos.user.UserDto;


public interface UserManager {
    boolean registerUser(String username, String firstname, String lastname, String password);

    UserDto loginAndGetUser(String username, String password);

    UserDto getUserByUsername(String username);

    boolean deleteUserById(int id);

    RoleDto getRoleByLevel(int level);
}
