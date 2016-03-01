package com.look.webservices.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Author: ivan
 * Date: 11.09.14
 * Time: 20:49
 */

@Path("/")
public class IndexResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("<html><head><title>Hello, world!</title></head>")
                .append("<body><h1>Hello, world!</h1></body></html>")
                .append("\n");
        return builder.toString();
    }
}
