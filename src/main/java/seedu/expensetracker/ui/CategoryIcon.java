package seedu.expensetracker.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import seedu.expensetracker.model.budget.CategoryBudget;

/**
 * A UI Component that displays information of a {@code Category}
 */

//@@author snookerballs
public class CategoryIcon extends UiPart<Region> {
    private static final String FXML = "CategoryIcon.fxml";
    private static final String IMAGE_PATH = "/images/categoryIcons/categoryIcon.png";
    private static final String SUFFIX = "%";

    @FXML
    private Label categoryName;

    @FXML
    private ImageView categoryIcon;

    @FXML
    private Label categoryPercentage;

    public CategoryIcon(CategoryBudget budget) {
        super(FXML);
        categoryPercentage.setText(String.format("%.2f", budget.getBudgetRatio() * 100) + SUFFIX);
        categoryName.setText(budget.toString());
        Image image = new Image(IMAGE_PATH);
        categoryIcon.setImage(image);
    }

}
