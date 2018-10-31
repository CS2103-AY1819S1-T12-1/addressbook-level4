package seedu.address.model.notification;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a notification in the expense tracker
 */
//@@Snookerballs
public abstract class Notification {

    protected String header;

    protected String body;

    protected NotificationType type;

    /**
     * The different types of notifications
     */
    public enum NotificationType {
        WARNING, TIP
    }

    public Notification() {

    }

    public Notification(String header, String body) {
        requireAllNonNull(header, body);
        this.header = header;
        this.body = body;
    }

    public Notification(Notification notification) {
        requireAllNonNull(notification);
        this.header = notification.header;
        this.body = notification.body;
        this.type = notification.type;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public boolean isSameNotification(Notification otherNotification) {
        return (otherNotification.header.equals(header)) && (otherNotification.body.equals(body));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getHeader())
                .append(getBody());
        return builder.toString();
    }

    public NotificationType getNotificationType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Notification)) {
            return false;
        }
        Notification notification = (Notification) obj;
        return this.header.equals(notification.header)
                && this.body.equals(notification.body)
                && this.type.equals(notification.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, body, type);
    }

}
