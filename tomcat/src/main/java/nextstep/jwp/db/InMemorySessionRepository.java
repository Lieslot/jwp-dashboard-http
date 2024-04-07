package nextstep.jwp.db;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import nextstep.jwp.model.Session;

public class InMemorySessionRepository {


    private static final Map<String, Session> SESSION = new ConcurrentHashMap<>();


    public static String create() {
        String sessionID = UUID.randomUUID()
                               .toString();

        Session session = new Session(sessionID);

        SESSION.put(sessionID, session);

        return sessionID;
    }

    public static boolean exists(String id) {

        return SESSION.containsKey(id);
    }







}
