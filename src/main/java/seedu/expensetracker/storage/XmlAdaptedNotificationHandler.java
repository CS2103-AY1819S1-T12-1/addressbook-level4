package seedu.expensetracker.storage;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.storage.storageutil.LocalDateTimeAdapter;

//@@Snookerballs

/**
 * JAXB-friendly adapted version of NotificationHandler.
 */
public class XmlAdaptedNotificationHandler {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "NotificationHandler's %s field is missing!";

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime lastTipSentOn;
    @XmlElement
    private boolean isTipEnabled;
    @XmlElement
    private boolean isWarningEnabled;

    /**
     * Constructs an XmlAdaptedNotificationHandler.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedNotificationHandler() {

    }

    /**
     * Converts a given NotificationHandler into this class for JAXB use.
     * @param source source budget
     */
    public XmlAdaptedNotificationHandler(NotificationHandler source) {
        this(source.getLastTipSentOn(), source.isTipEnabled(), source.isWarningEnabled());
    }

    /**
     * Creates a {@code XmlAdaptedNotificationHandler} based on the given parameters for JAXB use.
     * @param date
     * @param isTipEnabled
     * @param isWarningEnabled
     */
    public XmlAdaptedNotificationHandler(LocalDateTime date, boolean isTipEnabled, boolean isWarningEnabled) {
        this.lastTipSentOn = date;
        this.isTipEnabled = isTipEnabled;
        this.isWarningEnabled = isWarningEnabled;
    }

    /**
     * Converts this jaxb-friendly adapted expense object into the model's NotificationHandler object.
     */
    public NotificationHandler toModelType() throws IllegalValueException {
        if (lastTipSentOn == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "lastTipSentOn"));
        }

        return new NotificationHandler(lastTipSentOn, isTipEnabled, isWarningEnabled);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedNotificationHandler)) {
            return false;
        }
        return this.isWarningEnabled == ((XmlAdaptedNotificationHandler) other).isWarningEnabled
                && this.isTipEnabled == ((XmlAdaptedNotificationHandler) other).isTipEnabled
                && this.lastTipSentOn.getDayOfMonth() == ((XmlAdaptedNotificationHandler) other)
                .lastTipSentOn.getDayOfMonth()
                && this.lastTipSentOn.getMonth().equals(((XmlAdaptedNotificationHandler) other)
                .lastTipSentOn.getMonth())
                && this.lastTipSentOn.getYear() == ((XmlAdaptedNotificationHandler) other).lastTipSentOn.getYear();
    }

    public LocalDateTime getLastTipSentOn() {
        return lastTipSentOn;
    }

    public boolean isTipEnabled() {
        return isTipEnabled;
    }

    public boolean isWarningEnabled() {
        return isWarningEnabled;
    }

}
