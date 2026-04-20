package samuelvalentini.u5d11ex.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorPayload {
    private final List<String> messages;
    private String message;
    private LocalDateTime timeStamp;

    public ErrorPayload(String message, LocalDateTime timeStamp) {
        this.message = message;
        this.messages = List.of(message);
        this.timeStamp = timeStamp;
    }

    public ErrorPayload(List<String> messages, LocalDateTime timeStamp) {
        this.message = String.join(". ", messages);
        this.messages = messages;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
