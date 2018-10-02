package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author jonathantjm
/**
 * An event requesting to view the help page.
 */
public class ShowStatsRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
