package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.expense.Expense;

/**
 * Represents a selection change in the Expense List Panel
 */
public class ExpensePanelSelectionChangedEvent extends BaseEvent {


    private final Expense newSelection;

    public ExpensePanelSelectionChangedEvent(Expense newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public Expense getNewSelection() {
        return newSelection;
    }
}
