package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import com.gitlab.mavogel.vislab.dtos.user.NewUserDto;
import com.gitlab.mavogel.vislab.dtos.user.RoleDto;
import com.gitlab.mavogel.vislab.dtos.user.UserDto;
import hska.iwi.eShopMaster.config.TemplateFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author mavogel
 */

public class UserManagerImpl implements UserManager {
    private static final Logger LOG = LogManager.getLogger(UserManager.class);

    public UserManagerImpl() {
    }

    public boolean registerUser(String username, String firstname, String lastname, String password) {
        final NewUserDto newUserDto = new NewUserDto(username, firstname, lastname, password);
        ResponseEntity<UserDto> createdUser = null;
        try {
            createdUser = TemplateFactory.getRestTemplate()
                    .postForEntity(TemplateFactory.API_GATEWAY + "/user/register", newUserDto, UserDto.class);
        } catch (Exception e) {
            LOG.error("Registering user ' " + username + "'" + createdUser.getStatusCode());
            return false;
        }
        return createdUser == null ? false : HttpStatus.CREATED.equals(createdUser.getStatusCode());
    }

    // TODO delete
    public UserDto getUserByUsername(String username) {
        if (username == null || username.equals("")) {
            return null;
        }

        ResponseEntity<UserDto> userDto = null;
        try {
            userDto = TemplateFactory.getOAuth2RestTemplate()
                    .getForEntity(TemplateFactory.API_GATEWAY + "/user/" + username, UserDto.class);
        } catch (Exception e) {
            LOG.error("Failed to get user by username: '" + username + "'");
            return null;
        }
        return userDto.getBody();
    }

    public UserDto loginAndGetUser(String username, String password) {
        ResponseEntity<UserDto> userDto = null;
        try {
            userDto = TemplateFactory.createAndGetOAuth2RestTemplate(username, password)
                    .getForEntity(TemplateFactory.API_GATEWAY + "/user/" + username, UserDto.class);
        } catch (Exception e) {
            LOG.error("Failed to log in for user: '" + username + "'");
            return null;
        }
        return userDto.getBody();
    }

    public boolean deleteUserById(int id) {
        try {
            TemplateFactory.getOAuth2RestTemplate().delete(TemplateFactory.API_GATEWAY + "/user/" + id);
            return true;
        } catch (Exception e) {
            LOG.error("Failed to delete user with id: '" + id + "'");
            return false;
        }
    }

    public RoleDto getRoleByLevel(int level) {
        try {
            ResponseEntity<RoleDto> roleDto = TemplateFactory.getOAuth2RestTemplate()
                    .getForEntity(TemplateFactory.API_GATEWAY + "/user/level/" + level, RoleDto.class);
            return HttpStatus.OK.equals(roleDto.getStatusCode()) ? roleDto.getBody() : null;
        } catch (Exception e) {
            LOG.error("Failed to get role for level: '" + level + "'");
            return null;
        }
    }
}
