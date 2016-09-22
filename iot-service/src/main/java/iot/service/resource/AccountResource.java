package iot.service.resource;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.links.AddLinks;
import iot.service.path.ApplicationPath;
import iot.core.role.ApplicationRole;
import iot.core.resource.BaseResource;
import iot.core.service.BaseService;
import iot.domain.entity.User;
import iot.domain.service.UserService;

/**
 *
 * @author Rafael Guterres
 */
@Path(ApplicationPath.ACCOUNT)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Stateless
public class AccountResource extends BaseResource<User, Long> {

    @Inject
    private UserService userService;

    @Override
    protected BaseService<User, Long> getService() {
        return userService;
    }

    @GET
    @Path("/")
    @AddLinks
    @RolesAllowed(ApplicationRole.ACCOUNT)
    public User getAccount() {
        return null; //super.getEntityByUsername(getUsername());
    }
}
