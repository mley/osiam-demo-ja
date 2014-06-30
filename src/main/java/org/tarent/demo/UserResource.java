package org.tarent.demo;

import org.osiam.client.oauth.AccessToken;
import org.osiam.resources.scim.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.tarent.demo.OAuthResource.getOsiamConnector;

@Path("/user/")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public List<User> listUser(@Context HttpServletRequest req) {
        AccessToken accessToken = (AccessToken) req.getSession().getAttribute("accessToken");

        return getOsiamConnector().getAllUsers(accessToken);

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("create")
    public User createUser(@Context HttpServletRequest req, @FormParam("name") String name) {
        AccessToken accessToken = (AccessToken) req.getSession().getAttribute("accessToken");

        return getOsiamConnector().createUser(new User.Builder(name).build(), accessToken);

    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("create")
    public String createUserForm() {
        return "<html><body><form action='/create' method='post' ><input name='name' type='text'><input type='submit'></form></body></html>";
    }
}
