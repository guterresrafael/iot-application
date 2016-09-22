package iot.core.security;

import java.security.Principal;

/**
 *
 * @author Rafael Guterres
 */
public class SecurityContext implements javax.ws.rs.core.SecurityContext {

    private static final String AUTHENTICATION_SCHEME = "BASIC";
    private static final boolean IS_SECURE = true;

    private Principal userPrincipal;

    public SecurityContext(Principal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public SecurityContext() {
    }

    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public boolean isUserInRole(String role) {
        UserPrincipal user = (UserPrincipal) userPrincipal;
        return user.getRoles().contains(role);
    }

    @Override
    public boolean isSecure() {
        return IS_SECURE;
    }

    @Override
    public String getAuthenticationScheme() {
        return AUTHENTICATION_SCHEME;
    }
}
