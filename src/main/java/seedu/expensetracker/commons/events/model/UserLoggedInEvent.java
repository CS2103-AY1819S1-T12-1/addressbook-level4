package seedu.expensetracker.commons.events.model;

import seedu.expensetracker.commons.events.BaseEvent;
import seedu.expensetracker.model.user.Username;

//@@author JasonChong96
/** Indicates the a user has logged in */
public class UserLoggedInEvent extends BaseEvent {
    private final Username username;

    public UserLoggedInEvent(Username username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Logged in to user: " + username.toString();
    }
}
