package seedu.expensetracker.commons.events.ui;

import seedu.expensetracker.commons.events.BaseEvent;
import seedu.expensetracker.model.budget.TotalBudget;

//@@author snookerballs
/**
 * An event requesting an update in totalBudget display.
 */
public class UpdateBudgetPanelEvent extends BaseEvent {

    public final TotalBudget totalBudget;

    public UpdateBudgetPanelEvent(TotalBudget totalBudget) {
        this.totalBudget = totalBudget;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
