package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * A UI Component that displays information of a {@code Category}
 */

//@@author snookerballs
public class CategoryIcon extends UiPart<Region> {
    private static final String FXML = "CategoryIcon.fxml";

    @FXML
    private Text categoryName;

    @FXML
    private ImageView categoryIcon;

    public CategoryIcon(String text, String iconRoute) {
        super(FXML);

        categoryName.setText(text);
        Image image = new Image(iconRoute);
        categoryIcon.setImage(image);
    }
}
