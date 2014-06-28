package org.tarent.demo;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class OsiamDemo {

    public static void main(String[] args) throws Exception {

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(OAuthResource.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);

        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        Server server = new Server(5000);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        server.setHandler(context);

        server.start();
        server.join();
    }

}
