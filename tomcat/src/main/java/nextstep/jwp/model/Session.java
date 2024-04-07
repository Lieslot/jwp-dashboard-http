package nextstep.jwp.model;

import java.util.HashMap;
import java.util.Map;

public class Session {


    private final String id;

    private final Map<String, String> properties = new HashMap<>();

    public Session(String id) {
        this.id = id;
    }
}
