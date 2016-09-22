package iot.core.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.util.Base64;

/**
 *
 * @author Rafael Guterres
 */
public final class AuthorizationBasic implements Serializable {

    private static final long serialVersionUID = 5670883358002212562L;

    public static final String HEADER = "Authorization";
    public static final String SCHEME = "Basic";
    public static final String REGEXP = SCHEME + " ";
    public static final String REPLACEMENT = "";

    private AuthorizationBasic() {
    }

    public static UserPrincipal getUserPrincipal(ContainerRequestContext requestContext) {
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();
        final List<String> authorization = headers.get(HEADER);
        if (authorization == null || authorization.isEmpty()) {
            return null;
        }
        final String encodedUsernameAndPassword = authorization.get(0).replaceFirst(REGEXP, REPLACEMENT);
        String usernameAndPassword;
        try {
            usernameAndPassword = new String(Base64.decode(encodedUsernameAndPassword));
        } catch (IOException e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
            return null;
        }
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        Set<String> roles = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        return new UserPrincipal(username, password, roles);
    }
}
