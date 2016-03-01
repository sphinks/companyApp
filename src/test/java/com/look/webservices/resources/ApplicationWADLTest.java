package com.look.webservices.resources;

import com.look.webservices.config.LookApplication;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/spring/applicationContext.xml"
})
public class ApplicationWADLTest extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig config = new LookApplication();
        config.property("contextConfigLocation", "classpath:/spring/applicationContext.xml");
        return config;
    }

    @Test
    public void testSystemInfoShouldAllowUnauthorizedAccess() {
        // we do not provide access token here
        Response response = target("/application.wadl").request().get();

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

}