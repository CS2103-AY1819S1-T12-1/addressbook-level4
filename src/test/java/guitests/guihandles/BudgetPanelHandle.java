package guitests.guihandles;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

//@@author snookerballs
/**
 * A handle to the {@code BudgetPanel} in the GUI.
 */
public class BudgetPanelHandle extends NodeHandle<Region> {
    public static final String BUDGET_PANEL_ID = "#budgetPanel";
    public static final String BUDGET_BAR_ID = "#budgetBar";
    private static final String PERCENTAGE_DISPLAY_ID = "#percentageDisplay";

    private static final String BUDGET_DISPLAY_PREFIX = "/ $";
    private static final String EXPENSE_DISPLAY_PREFIX = "$";

    private static final String RED_HEXCODE = "#ae3b3b";
    private static final String GREEN_HEXCODE = "#61a15a";

    private static final int BUDGET_DISPLAY_INDEX = 1;
    private static final int EXPENSE_DISPLAY_INDEX = 0;

    private final Text budgetDisplay;
    private final Text expenseDisplay;
    private final ProgressBar budgetBar;

    public BudgetPanelHandle(Region budgetPanelNode) {
        super(budgetPanelNode);

        budgetBar = getChildNode(BUDGET_BAR_ID);
        TextFlow percentageDisplay = getChildNode(PERCENTAGE_DISPLAY_ID);
        expenseDisplay = (Text) percentageDisplay.getChildren().get(EXPENSE_DISPLAY_INDEX);
        budgetDisplay = (Text) percentageDisplay.getChildren().get(BUDGET_DISPLAY_INDEX);
    }

    public Text getBudgetDisplay() {
        return budgetDisplay;
    }

    public Text getExpenseDisplay() {
        return expenseDisplay;
    }

    public ProgressBar getBudgetBar() {
        return budgetBar;
    }

    /**
     * Checks if the expenseDisplay displays the correct text
     * @param expense to check against
     * @return true if expenseDisplay portrays the correct text, false otherwise.
     */
    public boolean isExpenseCorrect(String expense) {
        return expenseDisplay.getText().equals(EXPENSE_DISPLAY_PREFIX + expense);
    }

    /**
     * Check if the budgetDisplay displays the correct text
     * @param budget to check against
     * @return true if budgetDisplay portrays the correct text, false otherwise.
     */
    public boolean isBudgetCorrect(String budget) {
        return budgetDisplay.getText().equals(BUDGET_DISPLAY_PREFIX + budget);
    }

    /**
     * Check if the budgetBar is at the correct progress percentage
     * @param percentage to check against
     * @return true if the progress percentage is correct, false otherwise.
     */
    public boolean isBudgetBarProgressAccurate(double percentage) {
        if (percentage >= 1.0) {
            return budgetBar.getProgress() == 1.0;
        }
        return budgetBar.getProgress() == percentage;
    }

    /**
     * Checks if the expenseText and budgetBar turns red when over totalBudget.
     * @return true if both UI elements are red, false otherwise.
     */
    public boolean isColorRed() {
        return expenseDisplay.getFill().equals(Color.web(RED_HEXCODE))
                && budgetBar.getStyle().equals("-fx-accent: derive(#ae3b3b, 20%);");
    }

    /**
     * Checks if the expenseText and budgetBar is green when under totalBudget.
     * @return true if both UI elements are green, false otherwise.
     */
    public boolean isColorGreen() {
        return expenseDisplay.getFill().equals(Color.web(GREEN_HEXCODE))
                && budgetBar.getStyle().equals("-fx-accent: derive(#61a15a, 20%);");
    }
}
