package seedu.address.ui;

import javafx.scene.layout.Region;

//@@author snookerballs
/**
 * Panel containing the budget information.
 */
public class BudgetPanel extends UiPart<Region> {
    private static final String FXML = "BudgetPanel.fxml";

    public BudgetPanel () {
        super(FXML);
    }
}