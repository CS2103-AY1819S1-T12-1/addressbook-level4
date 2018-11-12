package seedu.expensetracker.model.exceptions;

import seedu.expensetracker.model.user.Username;

//@@author JasonChong96
/**
 * Represents an error where a user with the given username already exists in the model is encountered.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(Username username) {
        super("The user \"" + username + "\" already exists exist.");
    }
}
