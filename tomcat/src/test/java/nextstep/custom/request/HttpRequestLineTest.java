package nextstep.custom.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.coyote.http11.Http11Processor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import support.StubSocket;

public class HttpRequestLineTest {



    @Test
    void 제대로_파싱되는지_테스트() throws IOException {
        final String httpRequest = String.join("\r\n",
                "GET / HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String firstLine = bufferedReader.readLine();

        List<String> readLines = bufferedReader.lines()
                                               .collect(Collectors.toList());

        HttpRequestLine httpRequestLine = new HttpRequestLine(firstLine);
        String httpVersion = httpRequestLine.getHttpVersion();
        HttpMethod method = httpRequestLine.getMethod();
        String uri = httpRequestLine.getUri();

        Assertions.assertThat(httpVersion).isEqualTo("HTTP/1.1");
        Assertions.assertThat(method).isEqualTo(HttpMethod.GET);
        Assertions.assertThat(uri).isEqualTo("/");

    }
}
