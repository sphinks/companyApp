package com.look.webservices.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.assertThat;

public class IndexResourceIntegrationTest extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(IndexResource.class);
        return config;
    }

    @Test
    public void testGetIndex() {
        final String hello = target("/").request().get(String.class);
        assertThat(hello, Matchers.containsString("Hello, world!"));
    }
}