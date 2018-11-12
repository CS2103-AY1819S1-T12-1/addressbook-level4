package seedu.expensetracker.testutil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.model.notification.TipNotification;
import seedu.expensetracker.model.notification.WarningNotification;

//@@author Snookerballs

/**
 * A utility class containing a list of {@code Notification} objects to be used in tests.
 */
public class TypicalNotifications {

    public static final WarningNotification OVERBUDGET_WARNING =
            new WarningNotification("WARNING", "You are Overbudget!");
    public static final WarningNotification ALMOST_OVERBUDGET_WARNING =
            new WarningNotification("WARNING", "You are about to be overbudget!");
    public static final TipNotification TIP =
            new TipNotification("Save Money", "Save money by saving some money");
    public static final String DATE_FORMAT = "2018-10-31T01:42:04.021756";

    private TypicalNotifications(){}

    /**
     * Returns the list of {@code Notification} objects as an Arraylist
     * @return an arraylist of Notification Objects
     */
    public static ArrayList<Notification> getTypicalNotifications() {
        return new ArrayList<>(Arrays.asList(OVERBUDGET_WARNING, ALMOST_OVERBUDGET_WARNING, TIP));
    }

    /**
     * Return the list of {@code Notification} objects as a NotificationHandler
     * @return a NotificationHandler
     */
    public static NotificationHandler asNotificationHandler() {
        NotificationHandler handler = new NotificationHandler(LocalDateTime.parse(DATE_FORMAT),
                true, true);
        handler.add(OVERBUDGET_WARNING);
        handler.add(ALMOST_OVERBUDGET_WARNING);
        handler.add(TIP);
        return handler;
    }
}

