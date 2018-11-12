package seedu.expensetracker.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.logic.parser.ParserUtil.ensureKeywordsAreValid;

import seedu.expensetracker.logic.commands.MassEditCommand;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.expense.EditExpenseDescriptor;
import seedu.expensetracker.model.expense.ExpenseContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new MassEditCommand
 * */
//@@author jcjxwy
public class MassEditCommandParser implements Parser<MassEditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MassEditCommand
     * and returns a MassEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     * */
    public MassEditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String[] inputList = args.trim().split("->");

        //Check whether the user follows the pattern
        if (inputList.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassEditCommand.MESSAGE_USAGE));
        }

        String keywords = inputList[0].trim();
        String editedKeywords = inputList[1].trim();
        //Check whether the user enters keywords
        if (keywords.equals("") || editedKeywords.equals("")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassEditCommand.MESSAGE_USAGE));
        }

        //Check whether the user enters at least one prefix
        if (!keywords.contains("/") || !editedKeywords.contains("/")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassEditCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap keywordsMap =
                ArgumentTokenizer.tokenize(" " + keywords,
                        PREFIX_NAME, PREFIX_CATEGORY, PREFIX_COST, PREFIX_DATE, PREFIX_TAG);
        ArgumentMultimap editedKeywordsMap =
                ArgumentTokenizer.tokenize(" " + editedKeywords,
                        PREFIX_NAME, PREFIX_CATEGORY, PREFIX_COST, PREFIX_DATE, PREFIX_TAG);
        ensureKeywordsAreValid(keywordsMap);
        ensureKeywordsAreValid(editedKeywordsMap);

        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        EditExpenseDescriptor editExpenseDescriptor =
                EditExpenseDescriptor.createEditExpenseDescriptor(editedKeywordsMap);

        return new MassEditCommand(predicate, editExpenseDescriptor);
    }

}
