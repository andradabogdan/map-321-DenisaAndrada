package ro.ubbcluj.map.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Message extends Entity<Long> {
    private Long from_user;
    private Long to_user;
    private String message;
    private LocalDate data;
    private Long reply;

    public Message() {
        this.id = 0L;
    }

    public Long getReply() {
        return this.reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public Long getFrom() {
        return this.from_user;
    }

    public Long getTo() {
        return this.to_user;
    }

    public void setFrom(Long from) {
        this.from_user = from;
    }

    public void setTo(Long to) {
        this.to_user = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Message(Long from, Long to, String message, LocalDate data, Long reply) {
        this.from_user = from;
        this.to_user = to;
        this.message = message;
        this.data = data;
        this.reply = reply;
    }

    public String toString() {
        return "Message{id=" + this.id + ", from=" + this.from_user + ", to=" + this.to_user + ", message='" + this.message + "', data=" + this.data + ", reply=" + this.reply + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Message message1 = (Message)o;
            return Objects.equals(this.id, message1.id) && Objects.equals(this.from_user, message1.from_user) && Objects.equals(this.to_user, message1.to_user) && Objects.equals(this.message, message1.message) && Objects.equals(this.data, message1.data);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.from_user, this.to_user, this.message, this.data});
    }
}
