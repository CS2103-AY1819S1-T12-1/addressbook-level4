package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

//@@author JasonChong96
/**
 * Decrypts a given String that has been encrypted using the current user's encryption key.
 */
public class DecryptCommand extends Command {

    public static final String COMMAND_WORD = "decrypt";
    public static final String COMMAND_ALIAS = "de";
    public static final String MESSAGE_SUCCESS = "Decrypted string:\n%1$s";
    public static final String MESSAGE_FAILURE = "The input String was not encrypted using your encryption key.\n";

    private final String toDecrypt;

    public DecryptCommand(String toDecrypt) {
        requireNonNull(toDecrypt);
        this.toDecrypt = toDecrypt;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        String decryptedString;
        try {
            decryptedString = model.decryptString(toDecrypt);
            return new CommandResult(String.format(MESSAGE_SUCCESS, decryptedString));
        } catch (IllegalValueException e) {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DecryptCommand && this.toDecrypt.equals(((DecryptCommand) other).toDecrypt);
    }

    @Override
    public int hashCode() {
        return toDecrypt.hashCode();
    }
}
