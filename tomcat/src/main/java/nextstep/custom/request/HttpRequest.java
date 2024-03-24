package nextstep.custom.request;


public class HttpRequest {

    private HttpRequestLine requestLine;

    private HttpRequestHeader header;

    private String body;


    public HttpRequest(HttpRequestLine requestLine, HttpRequestHeader header, String body) {
        this.requestLine = requestLine;
        this.body = body;
        this.header = header;
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
