package com.look.utils;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Map;

public class ResponseUtils {

    public Response toResponse(Status status, Throwable throwable, String defaultValue) {
        return toResponse(status, MediaType.TEXT_PLAIN, throwable, defaultValue);
    }

    public Response toResponse(Status status, Throwable throwable, String value, String defaultValue) {
        return toResponse(status, MediaType.TEXT_PLAIN, throwable, value, defaultValue);
    }

    public Response toResponse(Status status, String type, Throwable throwable, String defaultValue) {
        return toResponse(status, type, throwable, throwable.getMessage(), defaultValue);
    }

    public Response toResponse(Status status, String type, Throwable throwable, String value, String defaultValue) {
        return toResponse(status, type, JsonUtils.getJsonFormatError(value, defaultValue, getStackTrace(isDebug(), throwable)));
    }

    public Response toResponse(Status status, Map<String, Object> entityMap, Throwable throwable, String defaultValue) {
        return toResponse(status, MediaType.TEXT_PLAIN, entityMap, throwable, defaultValue);
    }

    public Response toResponse(Status status, String type, Map<String, Object> entityMap, Throwable throwable,
            String defaultValue) {
        return toResponse(status, type,
                JsonUtils.getJsonStackTraceFormat(entityMap, getStackTrace(isDebug(), throwable)));
    }

    public static Response toSuccessResponse(Status status, String type, String entityBody) {
        return toResponse(status, type, JsonUtils.getJsonFormatSuccess(entityBody));
    }

    public static Response toResponse(Status status, String type, String entityBody) {
        return Response.status(status).type(type).entity(entityBody).build();
    }

    private String getStackTrace(Boolean debug, Throwable throwable) {
        if (debug == null || !debug)
            return null;
        return throwable == null ? "" : org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(throwable);

    }

    private boolean isDebug() {
        return false;
    }

}
