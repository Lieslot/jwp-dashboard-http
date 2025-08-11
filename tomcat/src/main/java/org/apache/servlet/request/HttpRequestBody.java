package org.apache.servlet.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBody {

    private Map<String, String> properties;

    public HttpRequestBody() {
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

        properties.putIfAbsent(key, value);
    }


}
