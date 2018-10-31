package seedu.address.storage;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import seedu.address.model.notification.NotificationHandler;
import seedu.address.storage.storageutil.LocalDateTimeAdapter;

//@@Snookerballs

/**
 * JAXB-friendly adapted version of NotificationHandler.
 */
public class XmlAdaptedNotificationHandler {

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
     *
     * @param source source budget
     */
    public XmlAdaptedNotificationHandler(NotificationHandler source) {
        lastTipSentOn = source.getLastTipSentOn();
        isTipEnabled = source.isTipEnabled();
        isWarningEnabled = source.isWarningEnabled();
    }

    /**
     * Converts this jaxb-friendly adapted expense object into the model's NotificationHandler object.
     */
    public NotificationHandler toModelType() {
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

}
