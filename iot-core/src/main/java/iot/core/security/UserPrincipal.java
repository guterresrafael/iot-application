package iot.core.security;

import java.security.Principal;
import java.util.Set;

/**
 *
 * @author Rafael Guterres
 */
public class UserPrincipal implements Principal {

    private Long id;
    private String username;
    private String password;
    private Set<String> roles;

    public UserPrincipal(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserPrincipal(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public UserPrincipal(String username) {
        this.username = username;
    }

    public UserPrincipal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
