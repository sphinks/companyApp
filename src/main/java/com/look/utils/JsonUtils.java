package com.look.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.rightPad;

public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    public static final String ERROR_FIELD = "error";
    private static final String SUCCESS_FIELD = "success";
    private static final String STACK_TRACE_FIELD = "stackTrace";

    public static String getJsonFormatError(String value, String defaultValue) {
        return getJsonFormat(ERROR_FIELD, value, defaultValue);
    }

    public static String getJsonFormatError(String value, String defaultValue, String stackTrace) {
        return getJsonStackTraceFormat(ERROR_FIELD, value, defaultValue, stackTrace);
    }

    public static String getJsonFormatSuccess(String value) {
        return getJsonFormat(SUCCESS_FIELD, value, value);
    }

    public static String getJsonFormat(String field, Object value) {
        return getJsonFormat(field, value, value);
    }

    public static String getJsonFormat(String field, Object value, Object defaultValue) {
        return getJsonStackTraceFormat(field, value, defaultValue, null);
    }

    private static String getJsonStackTraceFormat(String field, Object value, Object defaultValue, String stackTrace) {
        Map<String, Object> errorData = new HashMap<String, Object>();
        errorData.put(field, value == null ? defaultValue : value);
        return getJsonStackTraceFormat(errorData, stackTrace);
    }
    
    public static String getJsonStackTraceFormat(Map<String, Object> errorData, String stackTrace) {
        ObjectMapper mapper = new ObjectMapper();
        if (!isBlank(stackTrace)) {
            errorData.put(STACK_TRACE_FIELD, stackTrace);
        }
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorData);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return jsonString;
    }

}
