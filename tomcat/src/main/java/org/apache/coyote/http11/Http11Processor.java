package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import nextstep.custom.request.HttpRequest;
import nextstep.custom.request.HttpRequestLine;
import nextstep.custom.request.HttpRequestParser;
import nextstep.custom.response.HttpResponse;
import nextstep.custom.response.HttpResponseHeader;
import nextstep.custom.response.HttpResponseLine;
import nextstep.custom.response.HttpStatusCode;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    //
    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            HttpRequest request = HttpRequestParser.parse(bufferedReader);

            HttpRequestLine requestLine = request.getRequestLine();

            String response = createResponse(requestLine);

            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();


            log.info(response);

        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String createResponse(HttpRequestLine requestLine) throws IOException {

        HttpResponseLine httpResponseLine = createHttpResponseLine(requestLine);
        String responseBody = getResponseBody(requestLine.getUri());
        String contentType = decideContentType(requestLine.getUri());

        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.put("Content-Length", String.valueOf(responseBody.getBytes().length));
        httpResponseHeader.put("Content-Type", contentType +";charset=utf-8");

        HttpResponse httpResponse = new HttpResponse(httpResponseLine,
                responseBody,
                httpResponseHeader);

        return httpResponse.toString();
    }


    private HttpResponseLine createHttpResponseLine(HttpRequestLine requestLine) {
        String httpVersion = requestLine.getHttpVersion();
        HttpStatusCode httpStatusCode = HttpStatusCode.OK;

        return new HttpResponseLine(httpVersion, httpStatusCode);

    }


    private String getResponseBody(String uri) throws IOException {

        if (uri.equals("/")) {
            return "Hello World!";
        }

        URL resource = getClass().
                getClassLoader()
                .getResource("static" + uri);

        log.info("response body uri: {}", uri);

        File file = new File(resource.getFile());
        return new String(Files.readAllBytes(file.toPath()));
    }

    private String decideContentType(String uri) {

        return "text/html";


    }

}
