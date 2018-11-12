package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_INVALID_COST_KEYWORDS_FORMAT;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_INVALID_DATE_KEYWORDS_FORMAT;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_INVALID_RANGE;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_MULTIPLE_KEYWORDS;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.expensetracker.logic.commands.FindCommand;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.Date;
import seedu.expensetracker.model.expense.ExpenseContainsKeywordsPredicate;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;

//@@author jcjxwy
public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // one keyword
        ArgumentMultimap keywordsMap = prepareKeywords("n/Have Lunch");
        FindCommand expectedFindCommand =
                new FindCommand(new ExpenseContainsKeywordsPredicate(keywordsMap));
        assertParseSuccess(parser, " n/Have Lunch", expectedFindCommand);
        assertParseSuccess(parser, " n/   Have Lunch", expectedFindCommand);

        // multiple keywords
        keywordsMap = prepareKeywords("n/Buy books c/Book");
        expectedFindCommand = new FindCommand(new ExpenseContainsKeywordsPredicate(keywordsMap));
        assertParseSuccess(parser, " n/Buy books c/Book", expectedFindCommand);

        // all keywords
        keywordsMap = prepareKeywords("n/lunch c/food t/myself d/01-01-2018 $/5.00");
        expectedFindCommand = new FindCommand(new ExpenseContainsKeywordsPredicate(keywordsMap));
        assertParseSuccess(parser, " n/lunch c/food t/myself d/01-01-2018 $/5.00", expectedFindCommand);
    }

    @Test
    public void parse_invalidNameKeywords_parseFail() {
        //invalid name keywords
        assertParseFailure(parser, " n/Have Lunch@KFC ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Name.MESSAGE_NAME_CONSTRAINTS));

        //multiple name keywords
        assertParseFailure(parser, " n/school n/lunch",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_MULTIPLE_KEYWORDS));

        //empty name keyword
        assertParseFailure(parser, " n/ ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Name.MESSAGE_NAME_CONSTRAINTS));

    }

    @Test
    public void parse_invalidCategoryKeywords_parseFail() {
        //invalid category keywords
        assertParseFailure(parser, " c/Lunch@KFC  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Category.MESSAGE_CATEGORY_CONSTRAINTS));

        //multiple category keywords
        assertParseFailure(parser, " c/school c/lunch",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_MULTIPLE_KEYWORDS));

        //empty category keywords
        assertParseFailure(parser, " c/ ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Category.MESSAGE_CATEGORY_CONSTRAINTS));
    }

    @Test
    public void parse_invalidTagKeywords_parseFail() {
        //invalid tag keywords
        assertParseFailure(parser, " t/Lunch@KFC  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Tag.MESSAGE_TAG_CONSTRAINTS));

        //empty tag keyword
        assertParseFailure(parser, " t/ ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Tag.MESSAGE_TAG_CONSTRAINTS));
    }

    @Test
    public void parse_invalidCostKeywords_parseFail() {
        //invalid cost keywords
        assertParseFailure(parser, " $/2.320",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Cost.MESSAGE_COST_CONSTRAINTS + "\n" + MESSAGE_INVALID_COST_KEYWORDS_FORMAT));
        assertParseFailure(parser, " $/1.00:1.203",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Cost.MESSAGE_COST_CONSTRAINTS));

        //higher bound is smaller than lower bound
        assertParseFailure(parser, " $/5.00:1.00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_RANGE));

        //invalid format
        assertParseFailure(parser, " $/1.00:2.00:3.00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_COST_KEYWORDS_FORMAT));
        assertParseFailure(parser, " $/1.00 => 2.00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Cost.MESSAGE_COST_CONSTRAINTS + "\n" + MESSAGE_INVALID_COST_KEYWORDS_FORMAT));

        //multiple cost keywords
        assertParseFailure(parser, " $/1.00:2.00 $/3.00",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_MULTIPLE_KEYWORDS));

        //empty cost keyword
        assertParseFailure(parser, " $/ ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Cost.MESSAGE_COST_CONSTRAINTS + "\n" + MESSAGE_INVALID_COST_KEYWORDS_FORMAT));

    }

    @Test
    public void parse_invalidDateKeywords_parseFail() {
        //invalid date keywords
        assertParseFailure(parser, " d/60-02-2019",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Date.DATE_FORMAT_CONSTRAINTS + "\n" + MESSAGE_INVALID_DATE_KEYWORDS_FORMAT));
        assertParseFailure(parser, " d/01-02-2019:99-02-2019",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Date.DATE_FORMAT_CONSTRAINTS));
        assertParseFailure(parser, " d/99-02-2019:01-10-2019",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Date.DATE_FORMAT_CONSTRAINTS));

        //ending date is earlier than starting date
        assertParseFailure(parser, " d/10-01-2018:01-01-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_RANGE));

        //invalid format
        assertParseFailure(parser, " d/01-01-2018:02-02-2018:02-03-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_DATE_KEYWORDS_FORMAT));
        assertParseFailure(parser, " d/01-01-2018 -> 02-02-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Date.DATE_FORMAT_CONSTRAINTS + "\n" + MESSAGE_INVALID_DATE_KEYWORDS_FORMAT));

        //multiple date keywords
        assertParseFailure(parser, " d/01-01-2018:03-01-2018 d/01-05-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_MULTIPLE_KEYWORDS));

        //empty date keyword
        assertParseFailure(parser, " d/ ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Date.DATE_FORMAT_CONSTRAINTS + "\n" + MESSAGE_INVALID_DATE_KEYWORDS_FORMAT));
    }

    @Test
    public void parse_missingPrefix_parseFail() {
        assertParseFailure(parser, " /School ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "$ 1.00 ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    /**
     * Returns an {@code ArgumentMultiMap} which tokenize the {@code arg} based on prefixes.
     * */
    public ArgumentMultimap prepareKeywords(String arg) {
        return ArgumentTokenizer.tokenize(" " + arg,
                PREFIX_NAME, PREFIX_CATEGORY, PREFIX_COST, PREFIX_TAG, PREFIX_DATE);
    }



}
