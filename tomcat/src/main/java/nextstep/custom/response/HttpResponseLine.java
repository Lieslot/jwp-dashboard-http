package nextstep.custom.response;


import java.util.List;


public class HttpResponseLine {

    private String value;

    public String getValue() {
        return value;
    }

    public HttpResponseLine(String httpVersion, HttpStatusCode httpStatusCode) {
        this.value = String.join(" ",
                List.of(httpVersion, httpStatusCode.getStatusCode(), httpStatusCode.toString()));
    }
}
