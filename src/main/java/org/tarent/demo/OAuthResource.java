package org.tarent.demo;


import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.oauth.Scope;
import org.osiam.resources.scim.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/")
public class OAuthResource {

    private OsiamConnector getOsiamConnector() {
        return new OsiamConnector.Builder()
                .setEndpoint("http://localhost:8080")
                .setClientRedirectUri("http://localhost:5000/oauth2")
                .setClientId("example-client")
                .setClientSecret("secret")
                .build();
    }

    /**
     * Nach dem Login auf der OSIAM Login-Seite und der Freigabe des Zugriffs, leitet OSIAM auf diese Resource weiter.
     * @param req HttpServletRequest
     * @param authCode auth code. Damit kann ein AccessToken bezogen werden.
     * @return
     * @throws URISyntaxException
     */
    @GET
    @Path("oauth2")
    public Response oauth2(@Context HttpServletRequest req, @QueryParam("code") String authCode) throws URISyntaxException {
        OsiamConnector oc = getOsiamConnector();

        // wir holen uns das AccessToken
        AccessToken at = oc.retrieveAccessToken(authCode);

        // und schreiben es in die Session
        req.getSession().setAttribute("accessToken", at);

        // leiten dann wieder auf die landing resource
        return Response.temporaryRedirect(new URI("http://localhost:5000/")).build();

    }

    /**
     * Landing resource.
     * @param req HttpServletRequest
     * @return HTML
     * @throws URISyntaxException
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("")
    public String login(@Context HttpServletRequest req) throws URISyntaxException {

        AccessToken accessToken = (AccessToken) req.getSession().getAttribute("accessToken");
        if (accessToken == null) {
            // falls wir kein AccessToken haben, zeigen wir einen Link auf die Login-Seite.
            OsiamConnector oc = getOsiamConnector();
            URI uri = oc.getAuthorizationUri(Scope.ALL);
            return "<html><body><a href='" + uri.toString() + "'>Login</a></body></html>";

        } else {
            // mit AccessToken leiten wir auf die geschützte Resource weiter.
            return "<html><body><a href='/secured'>secured</a></body></html>";
        }
    }

    /**
     *
     * @param req HttpServletRequest
     * @return eingeloggter Benutzer
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("secured")
    public User viewSelf(@Context HttpServletRequest req) {

        AccessToken accessToken = (AccessToken) req.getSession().getAttribute("accessToken");

        // hier holen wir uns unseren eigenen Benutzer mit dem AccessToken
        User user = getOsiamConnector().getCurrentUser(accessToken);
        return user;
    }


}
