package seedu.expensetracker.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.ui.testutil.GuiTestAssert.assertCardDisplaysNotification;

import org.junit.Test;

import guitests.guihandles.NotificationCardHandle;
import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.TipNotification;
import seedu.expensetracker.model.notification.WarningNotification;

//@@author Snookerballs
public class NotificationCardTest extends GuiUnitTest {
    private static final String VALID_HEADER = "header";
    private static final String VALID_BODY = "body";

    @Test
    public void display() {
        //Warning notification
        Notification warningNotification = new WarningNotification(VALID_HEADER, VALID_BODY);
        Notification tipNotification = new TipNotification(VALID_HEADER, VALID_BODY);

        NotificationCard notificationCard = new NotificationCard(tipNotification);
        uiPartRule.setUiPart(notificationCard);
        assertCardDisplay(notificationCard, tipNotification);

        // Tip Notification
        notificationCard = new NotificationCard(warningNotification);
        uiPartRule.setUiPart(notificationCard);
        assertCardDisplay(notificationCard, warningNotification);
    }

    @Test
    public void equals() {
        Notification notification = new WarningNotification(VALID_HEADER, VALID_BODY);
        NotificationCard notificationCard = new NotificationCard(notification);

        // same expense, same index -> returns true
        NotificationCard copy = new NotificationCard(notification);
        assertTrue(notificationCard.equals(copy));

        // same object -> returns true
        assertTrue(notificationCard.equals(notificationCard));

        // null -> returns false
        assertFalse(notificationCard == null);

        // different types -> returns false
        assertFalse(notificationCard.equals(0));

        // different expense, same index -> returns false
        Notification differentNotification = new WarningNotification("DIFFERENT", "DIFFERENT");
        assertFalse(notificationCard.equals(new NotificationCard(differentNotification)));
    }

    /**
     * Asserts that {@code notificationCard} displays the details of {@code notification} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(NotificationCard notificationCard, Notification notification) {
        guiRobot.pauseForHuman();

        NotificationCardHandle notificationCardHandle = new NotificationCardHandle(notificationCard.getRoot());

        // verify expense details are displayed correctly
        assertCardDisplaysNotification(notification, notificationCardHandle);
    }
}
