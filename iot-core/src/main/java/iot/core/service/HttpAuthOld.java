package iot.core.service;

import iot.core.security.UserPrincipal;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Rafael
 */
public class HttpAuthOld implements ClientRequestFilter {

    private final String user;
    private final String password;

    public HttpAuthOld(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public HttpAuthOld(UserPrincipal userPrincipal) {
        this.user = userPrincipal.getName();
        this.password = userPrincipal.getPassword();
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        final String basicAuthentication = getBasicAuthentication();
        headers.add("Authorization", basicAuthentication);
    }

    private String getBasicAuthentication() {
        String token = this.user + ":" + this.password;
        try {
            return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Cannot encode with UTF-8", ex);
        }
    }
}
