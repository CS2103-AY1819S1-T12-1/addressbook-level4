package seedu.expensetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.expensetracker.storage.XmlAdaptedNotificationHandler.MISSING_FIELD_MESSAGE_FORMAT;

import org.junit.Test;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.testutil.Assert;
import seedu.expensetracker.testutil.TypicalNotifications;

//@@author Snookerballs

public class XmlAdaptedNotificationHandlerTest {

    @Test
    public void testEquals() {
        NotificationHandler handler = TypicalNotifications.asNotificationHandler();
        XmlAdaptedNotificationHandler validXmlAdaptedNotificationHandler = new XmlAdaptedNotificationHandler(handler);
        assertNotEquals(validXmlAdaptedNotificationHandler, new XmlAdaptedNotificationHandler(
                new NotificationHandler()));
        assertNotEquals(validXmlAdaptedNotificationHandler, new XmlAdaptedNotificationHandler(
                handler.getLastTipSentOn(), !handler.isTipEnabled(), handler.isWarningEnabled()));
        assertNotEquals(validXmlAdaptedNotificationHandler, new XmlAdaptedNotificationHandler(
                handler.getLastTipSentOn(), handler.isTipEnabled(), !handler.isWarningEnabled()));
        assertNotEquals(validXmlAdaptedNotificationHandler, 0);
        assertEquals(validXmlAdaptedNotificationHandler.getLastTipSentOn(),
                handler.getLastTipSentOn());
        assertEquals(validXmlAdaptedNotificationHandler.isTipEnabled(),
                handler.isTipEnabled());
        assertEquals(validXmlAdaptedNotificationHandler.isWarningEnabled(),
                handler.isWarningEnabled());
    }

    @Test
    public void testToModelType_nullLastTipSentOn_throwsIllegalValueException() {
        XmlAdaptedNotificationHandler invalidXmlAdaptedNotificationHandler = new XmlAdaptedNotificationHandler(
                null, true, true);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "lastTipSentOn");
        Assert.assertThrows(IllegalValueException.class, expectedMessage,
                invalidXmlAdaptedNotificationHandler::toModelType);
    }

}
