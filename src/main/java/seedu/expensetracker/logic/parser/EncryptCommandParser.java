package seedu.expensetracker.logic.parser;

import seedu.expensetracker.logic.commands.EncryptCommand;

//@@author JasonChong96
/**
 * Parses input arguments and creates a new EncryptCommand object
 */
public class EncryptCommandParser implements Parser<EncryptCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EncryptCommand
     * and returns an EncryptCommand object for execution.
     */
    @Override
    public EncryptCommand parse(String userInput) {
        return new EncryptCommand(userInput.trim());
    }
}
