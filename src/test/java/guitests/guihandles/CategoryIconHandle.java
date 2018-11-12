package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.Label;
import seedu.expensetracker.model.budget.CategoryBudget;

//@@author Snookerballs
/**
 * Provides a handle to a category icon in the categories panel.
 */
public class CategoryIconHandle extends NodeHandle<Node> {
    public static final String CATEGORIES_ICON_ID = "#categoriesIcon";
    private static final String CATEGORY_NAME_FIELD_ID = "#categoryName";
    private static final String CATEGORY_BUDGET_CAP_ID = "#categoryPercentage";
    private static final String SUFFIX = "%";

    private final Label categoryName;
    private final Label categoryPercentage;


    public CategoryIconHandle(Node node) {
        super(node);

        categoryPercentage = getChildNode(CATEGORY_BUDGET_CAP_ID);
        categoryName = getChildNode(CATEGORY_NAME_FIELD_ID);
    }

    public String getCategoryName() {
        return categoryName.getText();
    }

    public double getCategoryPercentage() {
        return Double.parseDouble(categoryPercentage.getText().substring(0,
                categoryPercentage.getText().length() - 1)) / 100;
    }
    /**
     * Returns true if the information in this object matches the information in the {@code budget} object
     * @param budget to compare against.
     * @return
     */
    public boolean equals(CategoryBudget budget) {
        return categoryName.getText().equals(budget.toString())
                && categoryPercentage.getText().equals(String.format("%.2f", budget.getBudgetRatio()) + SUFFIX);
    }

}
