package seedu.expensetracker.model.exceptions;
//@@author winsonhys

import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;

/**
 * Exception when trying to add category totalBudget that exceeds the current total totalBudget.
 */
public class CategoryBudgetExceedTotalBudgetException extends Exception {
    public CategoryBudgetExceedTotalBudgetException (CategoryBudget categoryBudget, TotalBudget totalBudget) {
        super(String.format("The category totalBudget of %.2f exceeds the total totalBudget of %.2f ",
            categoryBudget.getBudgetCap(),
            totalBudget.getBudgetCap()));
    }
}
