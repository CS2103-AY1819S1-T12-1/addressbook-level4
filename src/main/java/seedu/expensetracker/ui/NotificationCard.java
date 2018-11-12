package seedu.expensetracker.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.Notification.NotificationType;

//@@author Snookerballs
/**
 * An UI component that displays information of a {@code Expense}.
 */
public class NotificationCard extends UiPart<Region> {

    private static final String FXML = "NotificationCard.fxml";
    private static final String WARNING_IMAGE_LOCATION = "/images/notificationIcons/warning.png";
    private static final String TIP_IMAGE_LOCATION = "/images/notificationIcons/tip.png";

    public final Notification notification;

    @FXML
    private Label header;
    @FXML
    private Label body;
    @FXML
    private ImageView notificationImage;

    public NotificationCard(Notification notification) {
        super(FXML);
        this.notification = notification;
        header.setText(notification.getHeader());
        body.setText(notification.getBody());
        setImage(notification.getNotificationType());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NotificationCard)) {
            return false;
        }

        // state check
        NotificationCard card = (NotificationCard) other;
        return header.getText().equals(card.header.getText())
                && body.getText().equals(card.body.getText());
    }


    private void setImage(NotificationType type) {
        if (type.equals(NotificationType.TIP)) {
            Image image = new Image(TIP_IMAGE_LOCATION);
            notificationImage.setImage(image);
        } else {
            Image image = new Image(WARNING_IMAGE_LOCATION);
            notificationImage.setImage(image);
        }
    }

}
