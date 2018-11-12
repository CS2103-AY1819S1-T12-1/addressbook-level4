package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.util.CollectionUtil.requireAllNonNull;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.user.Password;

//@@author JasonChong96
/**
 * Sets the password of the current user.
 */
public class SetPasswordCommand extends Command {
    public static final String COMMAND_WORD = "setPassword";
    public static final String COMMAND_ALIAS = "sp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the password of the currently logged in user.\n"
            + "Parameters: [o/OLD_PASSWORD] n/NEW_PASSWORD\n"
            + Password.MESSAGE_PASSWORD_CONSTRAINTS + "\n"
            + "If password has not been previously set, the OLD_PASSWORD field may be omitted"
            + "Example: " + COMMAND_WORD + " n/password123\n"
            + COMMAND_WORD + " o/oldpass123 n/newpass123";

    public static final String MESSAGE_SET_PASSWORD_SUCCESS = "Your password has been changed.";
    public static final String MESSAGE_INCORRECT_PASSWORD = "The old password is incorrect.";

    private final Password oldPassword;
    private final Password newPassword;
    private String newPasswordPlain;

    public SetPasswordCommand(Password oldPassword, Password newPassword, String newPasswordPlain) {
        requireAllNonNull(newPassword, newPasswordPlain);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordPlain = newPasswordPlain;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        if (!model.isMatchPassword(oldPassword)) {
            return new CommandResult(MESSAGE_INCORRECT_PASSWORD);
        }
        model.setPassword(newPassword, newPasswordPlain);
        return new CommandResult(MESSAGE_SET_PASSWORD_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetPasswordCommand // instanceof handles nulls
                && oldPassword.equals(((SetPasswordCommand) other).oldPassword)
                && newPassword.equals(((SetPasswordCommand) other).newPassword)); // state check
    }
}
