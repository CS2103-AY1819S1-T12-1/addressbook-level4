package seedu.expensetracker.commons.events.ui;

import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of expenses
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
