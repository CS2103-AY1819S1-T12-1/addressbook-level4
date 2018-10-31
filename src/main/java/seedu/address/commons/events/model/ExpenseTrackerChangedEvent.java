package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.encryption.EncryptedExpenseTracker;

/** Indicates the ExpenseTracker in the model has changed*/
public class ExpenseTrackerChangedEvent extends BaseEvent {

    public final EncryptedExpenseTracker data;

    public ExpenseTrackerChangedEvent(EncryptedExpenseTracker data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of expenses " + data.getEncryptedExpenses().size();
    }
}
