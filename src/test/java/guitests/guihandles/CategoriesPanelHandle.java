package guitests.guihandles;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.expensetracker.model.budget.CategoryBudget;

//@@author Snookerballs
/**
 * Provides a handle to the {@code categoriesPanel} in the GUI.
 */
public class CategoriesPanelHandle extends NodeHandle<Region> {
    public static final String CATEGORIES_PANEL_ID = "#categoriesPanel";
    private static final String CATEGORIES_GRID_ID = "#categoriesGrid";

    @FXML
    private GridPane categoriesGrid;

    public CategoriesPanelHandle(Region categoriesPanel) {
        super(categoriesPanel);
        categoriesGrid = getChildNode(CATEGORIES_GRID_ID);
    }

    private ObservableList<Node> getAllIconNodes() {
        return categoriesGrid.getChildren();
    }

    public CategoryIconHandle getCategoryIconHandle(CategoryBudget categoryBudget) {
        return getAllIconNodes().stream()
                .map(CategoryIconHandle::new)
                .filter(handle -> handle.equals(categoryBudget))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public int size() {
        return categoriesGrid.getChildren().size();
    }
}
