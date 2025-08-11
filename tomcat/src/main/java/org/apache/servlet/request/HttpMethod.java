package org.apache.servlet.request;

import java.util.Arrays;

public enum HttpMethod {


    GET,
    POST;

    HttpMethod() {
    }

    public static HttpMethod of(String methodInput) {

        if (methodInput == null) {
            throw new IllegalArgumentException();
        }

        HttpMethod[] methods = HttpMethod.values();

        return Arrays.stream(methods)
                     .filter(method -> methodInput.equals(method.toString()))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);


    }
}
