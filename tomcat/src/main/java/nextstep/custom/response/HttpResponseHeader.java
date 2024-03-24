package nextstep.custom.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseHeader {


    private final Map<String, String> properties = new HashMap<>();


    public void put(String key, String value) {
        properties.putIfAbsent(key, value);
    }

    public String parse() {
        StringBuilder value = new StringBuilder();
        for (Map.Entry<String, String> property : properties.entrySet()) {

            if (property.getValue() == null) {
                continue;
            }

            value.append(String.join(": ", List.of(property.getKey(), property.getValue())))
                    .append(" ")
                 .append("\r\n");
        }


        return value.toString();
    }


}
