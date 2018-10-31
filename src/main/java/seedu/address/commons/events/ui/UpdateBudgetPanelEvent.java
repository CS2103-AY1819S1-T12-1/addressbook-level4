package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.budget.TotalBudget;

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
