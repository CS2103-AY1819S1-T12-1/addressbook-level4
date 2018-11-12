package seedu.expensetracker.model.notification;

//@@author winsonhys

/**
 * Represents a notification to display general notifications
 */
public class GeneralNotification extends Notification {
    public GeneralNotification (String header, String body) {
        super(header, body);
        type = NotificationType.WARNING;
    }


    public GeneralNotification(GeneralNotification notification) {
        super(notification);
    }
}
