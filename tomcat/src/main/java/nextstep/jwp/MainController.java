package nextstep.jwp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

import org.apache.coyote.http11.Http11Processor;
import org.apache.servlet.request.HttpMethod;
import org.apache.servlet.request.HttpRequest;
import org.apache.servlet.response.HttpResponse;
import org.apache.servlet.response.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextstep.jwp.db.InMemorySessionRepository;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

public class MainController {

	private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

	public String resolve(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

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
		// 메인 컨트롤러
		httpResponse.addHeaderParameter("Set-Cookie",
			"JSESSIONID=" + sessionID + "; HttpOnly; Path=/; Max-Age=3600" + "; SameSite=Lax");

		httpResponse.setHttpStatusCode(HttpStatusCode.FOUND);

		return httpResponse.toString();
	}

	private String processGetLogin(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		String uri = httpRequest.getRequestLine()
			.getUri();

		if (httpRequest.checkSessionIDExists()) {
			return response401Page(httpResponse);
		}

		String httpResponseBody = resolveResponseBody(httpResponse, uri + ".html");

		httpResponse.addHeaderParameter("Content-Type", "text/html");
		httpResponse.setHttpResponseBody(httpResponseBody);
		httpResponse.setHttpStatusCode(HttpStatusCode.OK);

		return httpResponse.toString();
	}

	private String response401Page(HttpResponse httpResponse) throws IOException {
		String httpResponseBody = resolveResponseBody(httpResponse, "/401.html");
		httpResponse.addHeaderParameter("Content-Type", "text/html");
		httpResponse.setHttpResponseBody(httpResponseBody);
		httpResponse.setHttpStatusCode(HttpStatusCode.OK);

		return httpResponse.toString();
	}

	private String response404Page(HttpResponse httpResponse) throws IOException {
		String httpResponseBody = resolveResponseBody(httpResponse, "/404.html");
		httpResponse.addHeaderParameter("Content-Type", "text/html");
		httpResponse.setHttpResponseBody(httpResponseBody);
		httpResponse.setHttpStatusCode(HttpStatusCode.NOT_FOUND);

		return httpResponse.toString();
	}

	private String resolveResponseBody(HttpResponse response, String uri) throws IOException {

		if (uri.equals("/")) {
			return "Hello World!";
		}

		URL resource = getClass().
			getClassLoader()
			.getResource("static" + uri);

		if (resource == null) {
			return response404Page(response);
		}

		File file = new File(resource.getFile());
		return new String(Files.readAllBytes(file.toPath()));
	}

	private String processStatic(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		String url = httpRequest.getRequestLine()
			.getUri();

		String httpResponseBody = resolveResponseBody(httpResponse, url);
		String contentType = resolveContentType(url);

		httpResponse.addHeaderParameter("Content-Type", contentType);
		httpResponse.setHttpStatusCode(HttpStatusCode.OK);
		httpResponse.setHttpResponseBody(httpResponseBody);
		return httpResponse.toString();
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
