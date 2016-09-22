package iot.core.resource;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.json.JsonStructure;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.DatatypeConverter;
import iot.core.entity.BaseEntity;
import iot.core.security.UserPrincipal;

/**
 *
 * @author Rafael
 * @param <T>
 * @param <I>
 */
public abstract class JsonResource<T extends BaseEntity, I extends Serializable> {

    private static final long serialVersionUID = -813905661837996046L;

    protected abstract SecurityContext getContext();

    protected abstract String getTarget();

    protected abstract String getPath();

    public JsonStructure getJsonEntities() {
        return getBuilder().get(JsonStructure.class);
    }

    public JsonStructure getJsonEntity(I id) {
        return getBuilder(id).get(JsonStructure.class);
    }

    public Response getJsonEntity(T entity) {
        return getBuilder().post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
    }

    public Response postEntity(T entity) {
        return getBuilder().post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
    }

    public Response putEntity(I id, T entity) {
        return getBuilder(id).put(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
    }

    public Response deleteEntity(I id) {
        return getBuilder(id).delete();
    }

    private Invocation.Builder getBuilder() {
        return getBuilder(null);
    }

    private Invocation.Builder getBuilder(I id) {
        Invocation.Builder builder = null;
        try {
            String path = getPath();
            if (id != null) {
                path = path.concat("/").concat(id.toString());
            }
            UserPrincipal userPrincipal = (UserPrincipal) getContext().getUserPrincipal();
            Client client = ClientBuilder.newClient().register(new HttpAuth(userPrincipal));
            WebTarget webTarget = client.target(getTarget()).path(path);
            builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }

    public class HttpAuth implements ClientRequestFilter {

        private final String user;
        private final String password;

        public HttpAuth(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public HttpAuth(UserPrincipal userPrincipal) {
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
}
