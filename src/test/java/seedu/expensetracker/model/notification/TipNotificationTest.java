package seedu.expensetracker.model.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.expensetracker.testutil.Assert;

//@@Snookerballs
public class TipNotificationTest extends NotificationTest {
    public static final String VALID_HEADER = "header";
    public static final String VALID_BODY = "body";
    public static final TipNotification VALID_TIP_NOTIFICATION = new TipNotification(VALID_HEADER, VALID_BODY);

    @Test
    public void constructor_nullBudget_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new TipNotification((Tips) null));
        Assert.assertThrows(NullPointerException.class, () -> new TipNotification((TipNotification) null));
    }

    @Test
    public void constructor_validArgs() {
        assertEquals(new TipNotification(VALID_HEADER, VALID_BODY), VALID_TIP_NOTIFICATION);
        assertEquals(new TipNotification(VALID_TIP_NOTIFICATION), VALID_TIP_NOTIFICATION);
        Tips tips = new Tips(TipsTest.VALID_TIP);
        assertEquals(new TipNotification(tips).getHeader(), tips.getRandomTip().getHeader());
        assertEquals(new TipNotification(tips).getBody(), tips.getRandomTip().getBody());
    }

    @Test
    public void constructor_nullStrings_throwsIllegalArgumentException() {
        Assert.assertThrows(NullPointerException.class, () -> new TipNotification(null, null));
    }

    @Test
    public void getNotificationType() {
        assertEquals(new TipNotification(VALID_HEADER, VALID_BODY).type, Notification.NotificationType.TIP);
    }

    @Test
    public void isSameNotification() {
        assertTrue(new TipNotification(VALID_HEADER, VALID_BODY)
                .isSameNotification(new TipNotification(VALID_HEADER, VALID_BODY)));
        assertFalse(new TipNotification(VALID_HEADER, VALID_BODY)
                .isSameNotification(new TipNotification("bb", "bb")));
    }
}
