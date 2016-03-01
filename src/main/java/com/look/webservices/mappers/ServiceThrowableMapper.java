package com.look.webservices.mappers;

import com.look.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServiceThrowableMapper implements ExceptionMapper<Throwable> {

    final static Logger LOG = LoggerFactory.getLogger(ServiceThrowableMapper.class);

    @Inject
    private ResponseUtils responseUtils;

    @Override
    public Response toResponse(Throwable th) {
        LOG.error(th.getMessage(), th);
        if (NotFoundException.class.equals(th.getClass()))
            return responseUtils.toResponse(Response.Status.NOT_FOUND, th, "Resource not found");
        if (WebApplicationException.class.equals(th.getClass()))
            return responseUtils.toResponse(Response.Status.BAD_REQUEST, th, "Request format error");
        return responseUtils.toResponse(Response.Status.INTERNAL_SERVER_ERROR, th, "Server Error");
    }

    public ResponseUtils getResponseUtils() {
        return responseUtils;
    }

    public void setResponseUtils(ResponseUtils responseUtils) {
        this.responseUtils = responseUtils;
    }
}
