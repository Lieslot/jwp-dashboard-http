package nextstep.custom.request;


import java.util.Map;

public class HttpRequest {

    private HttpRequestLine requestLine;
    private HttpRequestHeader header;
    private String body;
    private Map<String, String> parameter;

    public HttpRequest(HttpRequestLine requestLine, HttpRequestHeader header, String body,
                       Map<String, String> parameter) {
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
        this.parameter = parameter;
    }

    public void addParameter(String key, String value) {
        parameter.put(key, value);
    }

    public String getParameter(String key, String value) {
        return parameter.get(key);

    }

    public HttpRequestLine getRequestLine() {
        return requestLine;
    }

    public HttpRequestHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
