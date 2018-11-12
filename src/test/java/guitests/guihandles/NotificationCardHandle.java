package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.Label;
import seedu.expensetracker.model.notification.Notification;

/**
 * Provides a handle to anotification card in the expense list panel.
 */
public class NotificationCardHandle extends NodeHandle<Node> {
    private static final String HEADER_FIELD_ID = "#header";
    private static final String BODY_FIELD_ID = "#body";

    private final Label headerLabel;
    private final Label bodyLabel;

    public NotificationCardHandle(Node cardNode) {
        super(cardNode);

        headerLabel = getChildNode(HEADER_FIELD_ID);
        bodyLabel = getChildNode(BODY_FIELD_ID);

    }

    public String getHeader() {
        return headerLabel.getText();
    }

    public String getBody() {
        return bodyLabel.getText();
    }

    /**
     * Returns true if this handle contains {@code notification}.
     */
    public boolean equals(Notification notification) {
        return notification.getHeader().equals(getHeader())
                && notification.getBody().equals(getBody());
    }
}
