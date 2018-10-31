package seedu.address.model.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.testutil.TypicalNotifications.ALMOST_OVERBUDGET_WARNING;
import static seedu.address.testutil.TypicalNotifications.TIP;

import org.junit.Test;

//@@author Snookerballs
public class NotificationTest {
    public static final String VALID_HEADER = "aa";
    public static final String VALID_BODY = "bb";
    public static final Notification.NotificationType VALID_TIP_TYPE = Notification.NotificationType.TIP;
    public static final Notification.NotificationType VALID_WARNING_TYPE = Notification.NotificationType.WARNING;

    @Test
    public void equals() {
        Notification copy = new WarningNotification(ALMOST_OVERBUDGET_WARNING);
        assertNotEquals(copy, TIP);
        assertEquals(copy, ALMOST_OVERBUDGET_WARNING);
    }
}
