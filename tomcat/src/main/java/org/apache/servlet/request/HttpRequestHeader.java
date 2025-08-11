package org.apache.servlet.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.servlet.common.Cookie;

public class HttpRequestHeader {


    private Map<String, String> properties;
    private Cookie cookie;

    public HttpRequestHeader() {
        properties = new HashMap<>();
    }

    public void validateInput(String input) {

        if (input == null) {
            throw new IllegalArgumentException();
        }

        int index = input.indexOf(":");

        if (index == -1) {
            throw new IllegalArgumentException();
        }
    }


    public void add(String input) {
        validateInput(input);

        int splitIndex = input.indexOf(":");
        String key = input.substring(0, splitIndex)
                          .trim();
        String value = input.substring(splitIndex + 1)
                            .trim();

        if (key.equals("Cookie")) {
            this.cookie = Cookie.from(value);
        }

        properties.putIfAbsent(key, value);
    }

    public Optional<String> get(String key) {
        String value = properties.get(key);

        return Optional.ofNullable(value);
    }

    public Optional<String> getFromCookie(String key) {

        if (cookie == null) {
            return Optional.empty();
        }

        String value = cookie.get(key);

        return Optional.ofNullable(cookie.get(key));
    }

}
