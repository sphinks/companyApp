package com.look.webservices.resources;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.look.webservices.config.LookApplication;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

/**
 * Created by dev on 09.10.14.
 */
public class AbstractIntegrationTest extends JerseyTest {
    public static final String ACCESS_TOKEN = "fake-token";

    @Override
    protected void configureClient(ClientConfig config) {
        super.configureClient(config);
        config.register(MultiPartFeature.class);
    }

    @Override
    protected Application configure() {
        ResourceConfig config = new LookApplication();
        config.property("contextConfigLocation", "classpath:/spring/applicationContext.xml");
        return config;
    }

    protected Invocation.Builder authorizedRequest(String path) {
        return target(path)
                .request(MediaType.APPLICATION_JSON)
                .header("access-token", ACCESS_TOKEN);
    }

    protected Invocation.Builder requestWithAppJson(String path) {
        return target(path)
                .request(MediaType.APPLICATION_JSON);
    }
}
