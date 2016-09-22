package iot.service.security;

import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import iot.core.security.SecurityService;
import iot.core.security.UserPrincipal;
import iot.domain.entity.User;
import iot.domain.service.UserService;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class AuthSecurityService implements SecurityService {

    private static final long serialVersionUID = 3060578476342703986L;

    @Inject
    private UserService userService;

    @Override
    public boolean isAuthenticatedUser(UserPrincipal userPrincipal) {
        try {
            if (userPrincipal.getName() != null
                && userPrincipal.getPassword() != null) {
                User user = userService.findByUsername(userPrincipal.getName());
                if (user != null && userPrincipal.getPassword().equalsIgnoreCase(user.getPassword())) {
                    userPrincipal.setId(user.getId());
                    user.getRoles().stream().forEach((role) -> {
                        userPrincipal.getRoles().add(role.getName());
                    });
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean isAuthorizedUser(UserPrincipal userPrincipal, Set<String> roles) {
        return roles.stream().anyMatch((role) -> (userPrincipal.getRoles().contains(role)));
    }
}
