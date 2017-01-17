package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import com.gitlab.mavogel.vislab.dtos.user.NewUserDto;
import hska.iwi.eShopMaster.config.TemplateFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.RoleDAO;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.UserDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author knad0001
 */

public class UserManagerImpl implements UserManager {
    //	UserDAO helper;
    public UserManagerImpl() {
//		helper = new UserDAO();
    }

    public boolean registerUser(String username, String firstname, String lastname, String password, Role role) {
        final NewUserDto newUserDto = new NewUserDto(username,firstname,lastname, password);
        ResponseEntity<NewUserDto> createdUser = TemplateFactory.getRestTemplate()
                .postForEntity(TemplateFactory.API_GATEWAY + "/user/register", newUserDto, NewUserDto.class);
        return createdUser.getStatusCode().equals(HttpStatus.CREATED);
    }

    public User getUserByUsername(String username) {
        if (username == null || username.equals("")) {
            return null;
        }
        return null;
//        return helper.getUserByUsername(username);
    }

    public boolean deleteUserById(int id) {
//        User user = new User();
//        user.setId(id);
//        helper.deleteObject(user);
        return true;
    }

    public Role getRoleByLevel(int level) {
//        RoleDAO roleHelper = new RoleDAO();
//        return roleHelper.getRoleByLevel(level);
        return null;
    }

    public boolean validate(User user) {
        if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null || user.getLastname() == null || user.getUsername() == null) {
            return false;
        }

        return true;
    }

}
