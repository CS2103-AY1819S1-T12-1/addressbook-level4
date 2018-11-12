package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;

//@@author JasonChong96
/**
 * Logs in a user in the model
 */
public class LoginCommand extends Command {
    public static final String COMMAND_WORD = "login";
    public static final String COMMAND_ALIAS = "li";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Logs in to the user with the given USERNAME and PASSWORD. The password\n"
            + "Parameters: u/USERNAME [p/PASSWORD]\n"
            + "Example: " + COMMAND_WORD + " u/examplename\n"
            + COMMAND_WORD + "u/john p/password123";

    public static final String MESSAGE_LOGIN_SUCCESS = "Logged in as %1$s";
    public static final String MESSAGE_INCORRECT_PASSWORD = "Incorrect password";

    private final LoginCredentials loginCredentials;

    public LoginCommand(LoginCredentials loginCredentials) {
        requireNonNull(loginCredentials);
        this.loginCredentials = loginCredentials;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws
            NonExistentUserException, InvalidDataException {
        requireNonNull(model);
        if (model.loadUserData(loginCredentials)) {
            return new CommandResult(String.format(MESSAGE_LOGIN_SUCCESS, loginCredentials.getUsername()));
        } else {
            return new CommandResult(MESSAGE_INCORRECT_PASSWORD);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LoginCommand // instanceof handles nulls
                && loginCredentials.equals(((LoginCommand) other).loginCredentials)); // state check
    }

    @Override
    public int hashCode() {
        return loginCredentials.hashCode();
    }
}
