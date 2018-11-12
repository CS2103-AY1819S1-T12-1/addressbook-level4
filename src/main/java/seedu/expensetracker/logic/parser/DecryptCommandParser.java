package seedu.expensetracker.logic.parser;

import seedu.expensetracker.logic.commands.DecryptCommand;

//@@author JasonChong96
/**
 * Parses input arguments and creates a new DecryptCommand object
 */
public class DecryptCommandParser implements Parser<DecryptCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DecryptCommand
     * and returns an DecryptCommand object for execution.
     */
    @Override
    public DecryptCommand parse(String userInput) {
        return new DecryptCommand(userInput.trim());
    }
}
