package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

//@@author JasonChong96
/**
 * Encrypts a given String using the current user's encryption key.
 */
public class EncryptCommand extends Command {

    public static final String COMMAND_WORD = "encrypt";
    public static final String COMMAND_ALIAS = "en";
    public static final String MESSAGE_SUCCESS = "Encrypted string:\n%1$s";

    private final String toEncrypt;

    public EncryptCommand(String toEncrypt) {
        requireNonNull(toEncrypt);
        this.toEncrypt = toEncrypt;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        String encryptedString = model.encryptString(toEncrypt);
        return new CommandResult(String.format(MESSAGE_SUCCESS, encryptedString));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof EncryptCommand && this.toEncrypt.equals(((EncryptCommand) other).toEncrypt);
    }

    @Override
    public int hashCode() {
        return toEncrypt.hashCode();
    }
}
