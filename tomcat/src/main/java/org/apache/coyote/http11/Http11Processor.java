package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.coyote.Processor;
import org.apache.servlet.request.HttpRequest;
import org.apache.servlet.request.HttpRequestParser;
import org.apache.servlet.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextstep.jwp.MainController;
import nextstep.jwp.exception.UncheckedServletException;

public class Http11Processor implements Runnable, Processor {

	private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
	private static final String SESSION_ID = "JSESSIONID";
	private static final MainController mainController = new MainController();

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

			String response = mainController.resolve(httpRequest, httpResponse);

			outputStream.write(response.getBytes(StandardCharsets.UTF_8));
			outputStream.flush();

			log.info(response);

		} catch (IOException | UncheckedServletException e) {
			log.error(e.getMessage(), e);
		}
	}

}
