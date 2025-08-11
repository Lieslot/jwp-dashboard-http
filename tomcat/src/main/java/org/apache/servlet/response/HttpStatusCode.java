package org.apache.servlet.response;


public enum HttpStatusCode {

    OK("200"),
    FOUND("302"),
    UNAUTHORIZED("401"),
    NOT_FOUND("404");


    public String getStatusCode() {
        return statusCode;
    }

    private final String statusCode;

    HttpStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


}
