package nextstep.custom.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestParser {


    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);


    public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {

        String firstLine = bufferedReader.readLine();
        HttpRequestLine httpRequestLine = new HttpRequestLine(firstLine);

        Map<String, String> queryParams = new HashMap<>();
        String uri = httpRequestLine.getUri();
        String[] uriComponents = uri.split("\\?");

        if (hasQueryString(uri)) {
            queryParams = createQueryParams(uriComponents[1]);
        }
        httpRequestLine.setUri(uriComponents[0]);

        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();

        String line;
        while (!(line = bufferedReader.readLine()).isEmpty()) {
            httpRequestHeader.add(line);
        }

        int contentLength = Integer.parseInt(httpRequestHeader.get("Content-Length")
                                                              .orElse("0"));

        char[] content = new char[contentLength];
        bufferedReader.read(content);

        String body = new String(content);
        log.info("Http request parsing end");
        return new HttpRequest(httpRequestLine, httpRequestHeader, body, queryParams);


    }

    private static Map<String, String> createQueryParams(String queryString) {

        Map<String, String> results = new HashMap<>();

        String[] split = queryString.split("&");
        for (String query : split) {

            String[] keyValue = query.split("=");
            results.put(keyValue[0], keyValue[1]);


        }

        return results;

    }

    private static boolean hasQueryString(String url) {
        return url.contains("?");
    }


}
