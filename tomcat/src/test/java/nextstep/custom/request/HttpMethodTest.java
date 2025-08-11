package nextstep.custom.request;

import org.apache.servlet.request.HttpMethod;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpMethodTest {

    @Test
    void 문자열로_http_메소드를_얻을_수_있는지_확인() {

        HttpMethod get = HttpMethod.of("GET");
        HttpMethod post = HttpMethod.of("POST");

        Assertions.assertThat(get)
                  .isEqualTo(HttpMethod.GET);
        Assertions.assertThat(post)
                  .isEqualTo(HttpMethod.POST);
    }


}
