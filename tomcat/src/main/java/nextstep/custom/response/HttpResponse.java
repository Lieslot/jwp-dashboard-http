package nextstep.custom.response;


import java.util.List;


public class HttpResponse {


    public HttpResponse(HttpResponseLine responseLine, String httpResponseBody, HttpResponseHeader httpResponseHeader) {
        this.responseLine = responseLine;
        this.httpResponseBody = httpResponseBody;
        this.httpResponseHeader = httpResponseHeader;
    }

    private HttpResponseLine responseLine;
    private String httpResponseBody;
    private HttpResponseHeader httpResponseHeader;


    @Override
    public String toString() {

          return responseLine.getValue() + " \r\n" + httpResponseHeader.parse() + "\r\n" + httpResponseBody;
    }

}
