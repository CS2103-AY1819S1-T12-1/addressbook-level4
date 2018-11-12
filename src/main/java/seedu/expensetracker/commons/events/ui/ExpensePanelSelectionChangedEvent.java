package seedu.expensetracker.commons.events.ui;

import seedu.expensetracker.commons.events.BaseEvent;
import seedu.expensetracker.model.expense.Expense;

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
