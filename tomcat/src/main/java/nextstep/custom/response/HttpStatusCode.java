package nextstep.custom.response;


public enum HttpStatusCode {

    OK("200");


    public String getStatusCode() {
        return statusCode;
    }

    private final String statusCode;

    HttpStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


}
