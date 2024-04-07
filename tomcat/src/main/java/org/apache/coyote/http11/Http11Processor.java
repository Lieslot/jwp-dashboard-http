package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import nextstep.custom.request.HttpMethod;
import nextstep.custom.request.HttpRequest;
import nextstep.custom.request.HttpRequestParser;
import nextstep.custom.response.HttpResponse;
import nextstep.custom.response.HttpStatusCode;
import nextstep.jwp.db.InMemorySessionRepository;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.model.User;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
    private static final String SESSION_ID = "JSESSIONID";

    private final Socket connection;


    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }


    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            HttpRequest httpRequest = HttpRequestParser.parse(bufferedReader);
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setHttpVersion(httpRequest.getRequestLine()
                                                   .getHttpVersion());

            String response = createResponse(httpRequest, httpResponse);

            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            log.info(response);

        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String processPostLogin(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        String body = httpRequest.getBody();

        String[] values = body.split("&");

        String account = values[0].split("=")[1];
        String password = values[1].split("=")[1];

        Optional<User> searchResult = InMemoryUserRepository.findByAccount(account);


        if (searchResult.isEmpty()) {
            return processGetLogin(httpRequest, httpResponse);
        }

        User user = searchResult.get();

        if (!user.checkPassword(password)) {
            log.info("password: {}", password);
            return processGetLogin(httpRequest, httpResponse);
        }

        String sessionID = InMemorySessionRepository.create();

        httpResponse.addHeaderParameter("Location", "/index.html");
        httpResponse.addHeaderParameter("Content-Type", "text/html");
        httpResponse.addHeaderParameter("Set-Cookie", "JSESSIONID=" + sessionID);
        httpResponse.setHttpStatusCode(HttpStatusCode.FOUND);

        return httpResponse.toString();
    }


//    private String getNotFoundPage(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
//        String uri = httpRequest.getRequestLine()
//                                .getUri();
//
//        String httpResponseBody = getResponseBody(uri+".html");
//
//
//        httpResponse.addHeaderParameter("Location", "/index.html");
//        httpResponse.addHeaderParameter("Content-Type", "text/html");
//        httpResponse.setHttpResponseBody(httpResponseBody);
//        httpResponse.setHttpStatusCode(HttpStatusCode.FOUND);
//    }


    private String processGetLogin(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String uri = httpRequest.getRequestLine()
                                .getUri();

        if (httpRequest.checkSessionIDExists()) {
            return response401Page(httpResponse);
        }

        String httpResponseBody = getResponseBody(uri + ".html");

        httpResponse.addHeaderParameter("Content-Type", "text/html");
        httpResponse.setHttpResponseBody(httpResponseBody);
        httpResponse.setHttpStatusCode(HttpStatusCode.OK);

        return httpResponse.toString();
    }

    private String response401Page(HttpResponse httpResponse) throws IOException {
        String httpResponseBody = getResponseBody("/404.html");
        httpResponse.addHeaderParameter("Content-Type", "text/html");
        httpResponse.setHttpResponseBody(httpResponseBody);
        httpResponse.setHttpStatusCode(HttpStatusCode.OK);

        return httpResponse.toString();
    }


    private String createResponse(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        String url = httpRequest.getRequestLine()
                                .getUri();
        HttpMethod httpMethod = httpRequest.getRequestLine()
                                           .getMethod();

        if (httpMethod == HttpMethod.POST) {

            if (url.equals("/login")) {
                return processPostLogin(httpRequest, httpResponse);
            }

        }

        if (httpMethod == HttpMethod.GET) {

            if (url.equals("/login")) {
                return processGetLogin(httpRequest, httpResponse);
            }

        }

        return processStatic(httpRequest, httpResponse);
    }

    private String processStatic(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String url = httpRequest.getRequestLine()
                                .getUri();

        String httpResponseBody = getResponseBody(url);
        String contentType = resolveContentType(url);

        httpResponse.addHeaderParameter("Content-Type", contentType);
        httpResponse.setHttpStatusCode(HttpStatusCode.OK);
        httpResponse.setHttpResponseBody(httpResponseBody);
        return httpResponse.toString();
    }


    private String getResponseBody(String uri) throws IOException {

        if (uri.equals("/")) {
            return "Hello World!";
        }

        URL resource = getClass().
                getClassLoader()
                .getResource("static" + uri);

        File file = new File(resource.getFile());
        return new String(Files.readAllBytes(file.toPath()));
    }


    private String resolveContentType(String uri) {

        if (uri.endsWith(".html")) {
            return "text/html";
        }

        if (uri.endsWith(".css")) {
            return "text/css";
        }

        if (uri.endsWith(".js")) {
            return "text/javascript";
        }

        return "text/plain";

    }


}
