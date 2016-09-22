package iot.domain.filter;

import iot.core.repository.Filter;

/**
 *
 * @author Rafael Guterres
 */
public class UserFilter implements Filter {

    private static final long serialVersionUID = 1882905398878841103L;

    private String login;
    private String email;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
