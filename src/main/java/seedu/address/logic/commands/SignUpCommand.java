package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.user.Username;

//@@author JasonChong96
/**
 * Adds a user to the expense tracker.
 */
public class SignUpCommand extends Command {
    public static final String COMMAND_WORD = "signup";
    public static final String COMMAND_ALIAS = "su";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates a user with the given USERNAME.\n"
            + "Parameters: USERNAME\n"
            + "USERNAME cannot contain any of these characters: \" > < : \\ / | ? *"
            + "Example: " + COMMAND_WORD + " examplename";

    public static final String MESSAGE_SIGN_UP_SUCCESS = "User \"%1$s\" has successfully been created.";

    private final Username username;

    public SignUpCommand(Username username) {
        requireNonNull(username);
        this.username = username;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws UserAlreadyExistsException {
        requireNonNull(model);
        model.addUser(this.username);
        return new CommandResult(String.format(MESSAGE_SIGN_UP_SUCCESS, this.username.toString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SignUpCommand // instanceof handles nulls
                && username.equals(((SignUpCommand) other).username)); // state check
    }
}
