package seedu.address.model.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalNotifications.ALMOST_OVERBUDGET_WARNING;
import static seedu.address.testutil.TypicalNotifications.DATE_FORMAT;
import static seedu.address.testutil.TypicalNotifications.OVERBUDGET_WARNING;
import static seedu.address.testutil.TypicalNotifications.TIP;
import static seedu.address.testutil.TypicalNotifications.asNotificationHandler;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//@@author Snookerballs
public class NotificationHandlerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private NotificationHandler handler = new NotificationHandler(LocalDateTime.parse(DATE_FORMAT),
            true , true);

    private ObservableList<Notification> list = FXCollections.observableArrayList(
            new TipNotification(NotificationTest.VALID_HEADER,
                    NotificationTest.VALID_BODY));

    @BeforeEach
    public void initializeList() {
        handler = new NotificationHandler();
    }

    @Test
    public void add_nullObject_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        handler.add(null);
    }

    @Test
    public void constructor_validArgs() {
        handler = new NotificationHandler(list);
        NotificationHandler defaultHandler = new NotificationHandler();
        assertEquals(handler.getInternalList(), list);
        assertEquals(handler.getInternalList().size(), list.size());
        assertEquals(handler.getLastTipSentOn(), defaultHandler.getLastTipSentOn());
        assertEquals(handler.isWarningEnabled(), defaultHandler.isWarningEnabled());
        assertEquals(handler.isTipEnabled(), defaultHandler.isTipEnabled());
    }

    @Test
    public void isEmpty() {
        assertTrue(handler.isEmpty());

        handler.add(OVERBUDGET_WARNING);
        assertFalse(handler.isEmpty());
    }

    @Test
    public void assertSetNotifications() {
        handler.setNotifications(list);
        assertEquals(handler.getInternalList(), list);
    }

    @Test
    public void equals() {
        //Differing objects in list
        handler.add(OVERBUDGET_WARNING);
        handler.add(ALMOST_OVERBUDGET_WARNING);
        assertNotEquals(handler, asNotificationHandler());

        //Valid
        handler.add(TIP);
        assertEquals(handler, asNotificationHandler());

        //Valid - Same Object
        assertEquals(handler, handler);

        //Invalid - null
        assertNotEquals(handler, null);

        //Invalid - not a NotificationHandler
        assertNotEquals(handler, 0);

        //Differing  Date
        handler = new NotificationHandler();
        assertNotEquals(handler, asNotificationHandler());

        //Differing isWarningEnabled
        handler = new NotificationHandler(LocalDateTime.parse(DATE_FORMAT),
                true , false);
        assertNotEquals(handler, asNotificationHandler());

        //Differing isTipEnabled
        handler = new NotificationHandler(LocalDateTime.parse(DATE_FORMAT),
                false , true);
        assertNotEquals(handler, asNotificationHandler());
    }
}
