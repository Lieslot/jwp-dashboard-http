package nextstep.custom.request;

import org.apache.servlet.request.HttpRequestHeader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpHeaderTest {


    @Test
    void addMethodTest() {

        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();

        httpRequestHeader.add("key:value");

        Assertions.assertThat(httpRequestHeader.get("key"))
                  .isEqualTo("value");

    }


}
