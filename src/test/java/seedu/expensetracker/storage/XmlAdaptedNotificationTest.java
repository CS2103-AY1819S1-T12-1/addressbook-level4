package seedu.expensetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.expensetracker.storage.XmlAdaptedNotification.MISSING_FIELD_MESSAGE_FORMAT;

import org.junit.Test;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.notification.Notification.NotificationType;
import seedu.expensetracker.model.notification.NotificationTest;
import seedu.expensetracker.testutil.Assert;

//@@author Snookerballs

public class XmlAdaptedNotificationTest {
    private static final String MISMATCHED_HEADER = "Header";
    private static final String MISMATCHED_BODY = "Header";
    private static final NotificationType MISMATCHED_TYPE = NotificationType.WARNING;

    @Test
    public void testEquals() {
        XmlAdaptedNotification validXmlAdaptedTipNotification = new XmlAdaptedNotification(
                NotificationTest.VALID_HEADER,
                NotificationTest.VALID_BODY,
                NotificationTest.VALID_TIP_TYPE);
        XmlAdaptedNotification validXmlAdaptedWarningNotification = new XmlAdaptedNotification(
                NotificationTest.VALID_HEADER,
                NotificationTest.VALID_BODY,
                NotificationTest.VALID_WARNING_TYPE);
        assertNotEquals(validXmlAdaptedTipNotification, new XmlAdaptedNotification(MISMATCHED_HEADER,
                MISMATCHED_BODY, MISMATCHED_TYPE));
        assertNotEquals(validXmlAdaptedTipNotification, new XmlAdaptedNotification(NotificationTest.VALID_HEADER,
                MISMATCHED_BODY, MISMATCHED_TYPE));
        assertNotEquals(validXmlAdaptedTipNotification, new XmlAdaptedNotification(NotificationTest.VALID_HEADER,
                NotificationTest.VALID_BODY, MISMATCHED_TYPE));
        assertEquals(validXmlAdaptedWarningNotification, new XmlAdaptedNotification(NotificationTest.VALID_HEADER,
                NotificationTest.VALID_BODY, NotificationTest.VALID_WARNING_TYPE));
        assertEquals(validXmlAdaptedWarningNotification.getHeader(), NotificationTest.VALID_HEADER);
        assertEquals(validXmlAdaptedWarningNotification.getBody(), NotificationTest.VALID_BODY);
        assertNotEquals(validXmlAdaptedWarningNotification, 0);
        assertNotEquals(validXmlAdaptedWarningNotification, null);
    }

    @Test
    public void testToModelType_nullHeader_throwsIllegalValueException() {
        XmlAdaptedNotification invalidXmlAdaptedNotification = new XmlAdaptedNotification(null,
                NotificationTest.VALID_BODY,
                NotificationTest.VALID_TIP_TYPE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "header");
        Assert.assertThrows(IllegalValueException.class, expectedMessage, invalidXmlAdaptedNotification::toModelType);
    }

    @Test
    public void testToModelType_nullBody_throwsIllegalValueException() {
        XmlAdaptedNotification invalidXmlAdaptedNotification = new XmlAdaptedNotification(NotificationTest.VALID_HEADER,
                null, NotificationTest.VALID_TIP_TYPE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "body");
        Assert.assertThrows(IllegalValueException.class, expectedMessage, invalidXmlAdaptedNotification::toModelType);
    }

    @Test
    public void testToModelType_nullType_throwsIllegalValueException() {
        XmlAdaptedNotification invalidXmlAdaptedNotification = new XmlAdaptedNotification(NotificationTest.VALID_HEADER,
                NotificationTest.VALID_BODY,
                null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "type");
        Assert.assertThrows(IllegalValueException.class, expectedMessage, invalidXmlAdaptedNotification::toModelType);
    }

}
