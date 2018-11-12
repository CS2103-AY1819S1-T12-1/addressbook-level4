package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.expensetracker.logic.commands.MassEditCommand;
import seedu.expensetracker.model.expense.EditExpenseDescriptor;
import seedu.expensetracker.model.expense.ExpenseContainsKeywordsPredicate;
import seedu.expensetracker.testutil.EditExpenseDescriptorBuilder;

//@@author jcjxwy
public class MassEditCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, MassEditCommand.MESSAGE_USAGE);
    private MassEditCommandParser parser = new MassEditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        //empty parameter
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);

        //missing keywords
        assertParseFailure(parser, "-> n/test", MESSAGE_INVALID_FORMAT);

        //missing edited keywords
        assertParseFailure(parser, "n/test -> ", MESSAGE_INVALID_FORMAT);

        //missing arrow symbol
        assertParseFailure(parser, "n/test n/test2", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidFormat_failure() {
        //wrong symbol
        assertParseFailure(parser, "n/test => c/test", MESSAGE_INVALID_FORMAT);

        //multiple symbols
        assertParseFailure(parser, "n/test -> c/test -> n/test", MESSAGE_INVALID_FORMAT);

        //wrong prefix format
        assertParseFailure(parser, "n test -> c/test", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "n/test -> c test", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecify_success() {
        ArgumentMultimap keywords = prepareMap("n/test c/test");
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywords);
        EditExpenseDescriptor editExpenseDescriptor =
                new EditExpenseDescriptorBuilder()
                .withName("test")
                .withCategory("equal")
                .withTags("same")
                .withCost("1.00")
                .withDate("01-01-2018")
                .build();
        MassEditCommand expectedCommand = new MassEditCommand(predicate, editExpenseDescriptor);
        assertParseSuccess(parser,
                "n/test c/test -> n/test c/equal t/same d/01-01-2018 $/1.00", expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecify_success() {
        //name
        ArgumentMultimap keywords = prepareMap("n/test");
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywords);
        EditExpenseDescriptor editExpenseDescriptor =
                new EditExpenseDescriptorBuilder().withName("equal").build();
        MassEditCommand expectedCommand = new MassEditCommand(predicate, editExpenseDescriptor);
        assertParseSuccess(parser,
                "n/test -> n/equal", expectedCommand);

        //category
        keywords = prepareMap("n/test");
        predicate = new ExpenseContainsKeywordsPredicate(keywords);
        editExpenseDescriptor =
                new EditExpenseDescriptorBuilder().withCategory("equal").build();
        expectedCommand = new MassEditCommand(predicate, editExpenseDescriptor);
        assertParseSuccess(parser,
                "n/test -> c/equal", expectedCommand);

        //tags
        keywords = prepareMap("n/test");
        predicate = new ExpenseContainsKeywordsPredicate(keywords);
        editExpenseDescriptor =
                new EditExpenseDescriptorBuilder().withTags("test", "equal").build();
        expectedCommand = new MassEditCommand(predicate, editExpenseDescriptor);
        assertParseSuccess(parser,
                "n/test -> t/test t/equal", expectedCommand);

        //cost
        keywords = prepareMap("n/test");
        predicate = new ExpenseContainsKeywordsPredicate(keywords);
        editExpenseDescriptor =
                new EditExpenseDescriptorBuilder().withCost("2.00").build();
        expectedCommand = new MassEditCommand(predicate, editExpenseDescriptor);
        assertParseSuccess(parser,
                "n/test -> $/2.00", expectedCommand);

        //date
        keywords = prepareMap("n/test");
        predicate = new ExpenseContainsKeywordsPredicate(keywords);
        editExpenseDescriptor =
                new EditExpenseDescriptorBuilder().withDate("01-01-2018").build();
        expectedCommand = new MassEditCommand(predicate, editExpenseDescriptor);
        assertParseSuccess(parser,
                "n/test -> d/01-01-2018", expectedCommand);

    }

    /**
     * Parses the input and stores the keywords in {@code ArgumentMultimap}
     * */
    private ArgumentMultimap prepareMap(String input) {
        String preparedInput = " " + input.trim();
        ArgumentMultimap map = ArgumentTokenizer.tokenize(preparedInput,
                PREFIX_NAME, PREFIX_CATEGORY, PREFIX_COST, PREFIX_DATE, PREFIX_TAG);
        return map;
    }

}
