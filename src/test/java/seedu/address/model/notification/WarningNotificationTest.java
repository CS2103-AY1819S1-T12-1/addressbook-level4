package seedu.address.model.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.model.budget.Budget;
import seedu.address.testutil.Assert;

//@@Snookerballs
public class WarningNotificationTest extends NotificationTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new WarningNotification((Budget) null));
        Assert.assertThrows(NullPointerException.class, () -> new WarningNotification((WarningNotification) null));
    }

    @Test
    public void constructor_nullStrings_throwsIllegalArgumentException() {

        Assert.assertThrows(NullPointerException.class, () -> new WarningNotification(null, null));
    }


    @Test
    public void getNotificationType() {
        assertEquals(new WarningNotification(VALID_HEADER, VALID_BODY).type, Notification.NotificationType.WARNING);
    }

    @Test
    public void isSameNotification() {

        assertTrue(new WarningNotification(VALID_HEADER, VALID_BODY).isSameNotification(
                new WarningNotification("aa", "bb")));
        assertFalse(new WarningNotification(VALID_HEADER, VALID_BODY).isSameNotification(
                new WarningNotification("bb", "bb")));
    }
}
