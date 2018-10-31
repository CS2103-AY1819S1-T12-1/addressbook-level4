package seedu.address.logic.parser;
//@@authoer winsonhys

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.logic.commands.AddCategoryBudgetCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.budget.CategoryBudget;

/**
 * Parses input arguments and creates a new AddCategoryBudgetCommandParser object
 */
public class AddCategoryBudgetCommandParser implements Parser<AddCategoryBudgetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCategoryBudgetCommand parse(String args) throws ParseException {
        try {
            ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CATEGORY, PREFIX_BUDGET);

            if (!arePrefixesPresent(argMultimap, PREFIX_CATEGORY, PREFIX_BUDGET)
                || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddCategoryBudgetCommand.MESSAGE_USAGE));
            }

            String categoryName = argMultimap.getValue(PREFIX_CATEGORY).get();
            String budget = argMultimap.getValue(PREFIX_BUDGET).get();

            return new AddCategoryBudgetCommand(new CategoryBudget(categoryName, budget));
        } catch (IllegalArgumentException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddCategoryBudgetCommand.MESSAGE_USAGE));
        }
    }
}
