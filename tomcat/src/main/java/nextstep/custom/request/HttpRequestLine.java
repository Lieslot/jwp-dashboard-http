package nextstep.custom.request;

import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpRequestLine {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestLine.class);

    private String uri;
    private HttpMethod method;
    private String httpVersion;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpRequestLine(String informs) {

        log.info(informs);

        StringTokenizer stringTokenizer = new StringTokenizer(informs, " ");

        validateTokenSize(stringTokenizer.countTokens());

        method = HttpMethod.of(stringTokenizer.nextToken());
        uri = stringTokenizer.nextToken();
        httpVersion = stringTokenizer.nextToken();

    }

    private void validateTokenSize(int count) {
        if (count != 3) {
            throw new IllegalArgumentException();
        }
    }


}
