package com.look.webservices.config;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import java.util.logging.Logger;

public class LookApplication extends ResourceConfig {

    public LookApplication() {
        super();

        this.register(RequestContextFilter.class);
        //this.register(AuthorizationRequestFilter.class);

        // Register resources and providers using package-scanning.
        packages("com.look.webservices");

        // Register an instance of LoggingFilter.
        register(new LoggingFilter(Logger.getLogger(LookApplication.class.getName()), true));
        register(MultiPartFeature.class);

        // Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");

    }
}
