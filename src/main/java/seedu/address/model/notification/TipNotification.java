package seedu.address.model.notification;

/**
 * Represents a notification that displays a tip.
 */
//@@Snookerballs
public class TipNotification extends Notification {
    private Tip tip;

    public TipNotification (Tips tips) {
        tip = tips.getRandomTip();
        header = tip.getHeader();
        body = tip.getBody();
        type = NotificationType.TIP;
    }

    public TipNotification(String header, String body) {
        super(header, body);
        type = NotificationType.TIP;
    }

    public TipNotification(TipNotification notification) {
        super(notification);
    }

}
