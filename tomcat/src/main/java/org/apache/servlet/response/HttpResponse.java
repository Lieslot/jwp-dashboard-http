package org.apache.servlet.response;

import java.util.List;

import org.apache.servlet.request.HttpRequestLine;

public class HttpResponse {


    public HttpResponse() {

    }


    private String httpVersion;
    private HttpStatusCode httpStatusCode;


    private String httpResponseBody;
    private HttpResponseHeader httpResponseHeader = new HttpResponseHeader();


    @Override
    public String toString() {

        return getResponseLineValue() + " \r\n" + httpResponseHeader.parse() + "\r\n" + httpResponseBody;
    }

    private String getResponseLineValue() {
        return String.join(" ",
                List.of(httpVersion, httpStatusCode.getStatusCode(), httpStatusCode.toString()));
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setHttpStatusCode(HttpStatusCode statusCode) {
        this.httpStatusCode = statusCode;
    }


    public void addHeaderParameter(String key, String value) {
        httpResponseHeader.put(key, value);
    }

    public void setHttpResponseBody(String httpResponseBody) {
        this.httpResponseHeader.put("Content-Length", String.valueOf(httpResponseBody.getBytes().length));
        this.httpResponseBody = httpResponseBody;
    }


    private static HttpResponseLine createHttpResponseLine(HttpRequestLine requestLine, HttpStatusCode statusCode) {
        String httpVersion = requestLine.getHttpVersion();

        return new HttpResponseLine(httpVersion, statusCode);

    }


}
