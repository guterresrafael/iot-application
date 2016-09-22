package iot.core.security;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Rafael Guterres
 */
public interface SecurityService extends Serializable {

    boolean isAuthenticatedUser(UserPrincipal userPrincipal);

    boolean isAuthorizedUser(UserPrincipal userPrincipal, Set<String> roles);
}
