package nextstep.custom.request;


import java.util.Map;
import java.util.Optional;
import nextstep.custom.common.Cookie;
import nextstep.jwp.db.InMemorySessionRepository;

public class HttpRequest {

    private static final String SESSION_ID = "JSESSIONID";

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

    public Optional<String> getCookieValue(String key) {
        return header.getFromCookie(key);
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

    public boolean checkSessionIDExists() {
        Optional<String> sessionIDSearchResult = getCookieValue(SESSION_ID);
        if (sessionIDSearchResult.isEmpty()) {
            return false;
        }
        String sessionID = sessionIDSearchResult.get();
        return InMemorySessionRepository.exists(sessionID);
    }

}
