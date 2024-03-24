package nextstep.custom.request;

import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestParser {


    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);


    public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {

        log.info("http request parsing start");
        String firstLine = bufferedReader.readLine();
        HttpRequestLine httpRequestLine = new HttpRequestLine(firstLine);
        log.info("http request RequestLine parse");

        log.info("http request readline parse");
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();

        String line;
        while (!(line = bufferedReader.readLine()).isEmpty()) {
            httpRequestHeader.add(line);
        }


        int contentLength = Integer.parseInt(httpRequestHeader.get("Content-Length").orElse("0"));

        char[] content = new char[contentLength];

        bufferedReader.read(content);

        log.info("Http request parsing end");
        return new HttpRequest(httpRequestLine, httpRequestHeader, new String(content));


    }

    private static byte[] addEOF(byte[] bytes) {
        byte[] inputBytes = new byte[bytes.length + 1];

        System.arraycopy(bytes, 0, inputBytes, 0, bytes.length);
        inputBytes[bytes.length] = 0;
        return inputBytes;
    }


}
