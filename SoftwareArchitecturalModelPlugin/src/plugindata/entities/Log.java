package plugindata.entities;

import java.util.HashSet;
import java.util.Set;

public class Log {

    Set<LogEvent> events;

    public Log() {
        events = new HashSet<>();
    }

    public void addEvent(LogEvent event) {
        events.add(event);
    }
}
