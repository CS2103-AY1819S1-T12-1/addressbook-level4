package seedu.address.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.notification.Notification;
import seedu.address.model.notification.Notification.NotificationType;
import seedu.address.model.notification.TipNotification;
import seedu.address.model.notification.WarningNotification;

//@author Snookerballs

/**
 * JAXB-friendly adapted version of Notification.
 */
public class XmlAdaptedNotification {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Notification's %s field is missing!";

    @XmlElement
    private String header;
    @XmlElement
    private String body;
    @XmlElement
    private NotificationType type;

    /**
     * Constructs an XmlAdaptedNotification.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedNotification() {
    }

    /**
     * Converts a given Notification into this class for JAXB use.
     *
     * @param source source budget
     */
    public XmlAdaptedNotification(Notification source) {
        this.header = source.getHeader();
        this.body = source.getBody();
        this.type = source.getNotificationType();
    }

    /**
     * Converts a given Notification into this class for JAXB use.
     *
     * @param header of the notification
     * @param body of the notification
     */
    public XmlAdaptedNotification(String header, String body, NotificationType type) {
        this.header = header;
        this.body = body;
        this.type = type;
    }

    /**
     * Converts this jaxb-friendly adapted expense object into the model's Notification object.
     */
    public Notification toModelType() throws IllegalValueException {
        if (header == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "header"));
        }
        if (body == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "body"));
        }

        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "type"));
        }

        if (type.equals(NotificationType.TIP)) {
            return new TipNotification(header, body);
        }
        return new WarningNotification(header, body);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedNotification)) {
            return false;
        }

        XmlAdaptedNotification notification = (XmlAdaptedNotification) other;
        return this.header.equals(notification.header)
                && this.body.equals(notification.body)
                && this.type.equals(notification.type);
    }


    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
