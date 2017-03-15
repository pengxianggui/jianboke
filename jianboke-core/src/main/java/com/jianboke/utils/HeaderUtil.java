package com.jianboke.utils;

import org.springframework.http.HttpHeaders;

/**
 * Created by pengxg on 2017/3/15.
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String params) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-App-Alert", message);
        headers.add("X-App-Params", params);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String params) {
        return createAlert("app." + entityName + ".created", params);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String params) {
        return createAlert("app." + entityName + ".updated", params);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String params) {
        return createAlert("app." + entityName + ".deleted", params);
    }

}
