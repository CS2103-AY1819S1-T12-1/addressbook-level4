package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.commons.exceptions.NegativeValueParseException.NEGATIVE_EXCEPTION_MESSAGE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_KFC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_KFC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.DATE_DESC_1990;
import static seedu.expensetracker.logic.commands.CommandTestUtil.DATE_DESC_2018;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_CATEGORY_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_COST_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_COST_DESC_NEGATIVE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_KFC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.expensetracker.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_FOOD;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_DATE_1990;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.expensetracker.testutil.TypicalExpenses.GAME;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.KFC;

import org.junit.Test;

import seedu.expensetracker.logic.commands.AddCommand;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.Date;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;
import seedu.expensetracker.testutil.ExpenseBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Expense expectedExpense = new ExpenseBuilder(IPHONE).withTags(VALID_TAG_FRIEND).build();
        Expense expectedExpenseWithDate = new ExpenseBuilder(IPHONE)
                .withTags(VALID_TAG_FRIEND)
                .withDate(VALID_DATE_1990).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + DATE_DESC_2018
                + COST_DESC_IPHONE + TAG_DESC_FRIEND, new AddCommand(expectedExpense));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_GAME + NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + DATE_DESC_2018
                + COST_DESC_IPHONE + TAG_DESC_FRIEND, new AddCommand(expectedExpense));

        // multiple category - last category accepted
        assertParseSuccess(parser, NAME_DESC_IPHONE + CATEGORY_DESC_GAME + CATEGORY_DESC_IPHONE + DATE_DESC_2018
                + COST_DESC_IPHONE + TAG_DESC_FRIEND, new AddCommand(expectedExpense));

        // multiple addresses - last expensetracker accepted
        assertParseSuccess(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + COST_DESC_GAME + DATE_DESC_2018
                + COST_DESC_IPHONE + TAG_DESC_FRIEND, new AddCommand(expectedExpense));

        // add date
        assertParseSuccess(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + COST_DESC_GAME
                + COST_DESC_IPHONE + TAG_DESC_FRIEND + DATE_DESC_1990, new AddCommand(expectedExpenseWithDate));

        // multiple tags - all accepted
        Expense expectedExpenseMultipleTags = new ExpenseBuilder(IPHONE).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + DATE_DESC_2018, new AddCommand(expectedExpenseMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Expense expectedExpense = new ExpenseBuilder(GAME).withTags().build();

        assertParseSuccess(parser, NAME_DESC_GAME + CATEGORY_DESC_GAME + COST_DESC_GAME + DATE_DESC_1990,
                new AddCommand(expectedExpense));

        // no date
        Expense expectedExpense2 = new ExpenseBuilder(KFC).withDate(new Date().toString()).build();

        assertParseSuccess(parser, NAME_DESC_KFC + CATEGORY_DESC_KFC + COST_DESC_KFC + TAG_DESC_FOOD,
                new AddCommand(expectedExpense2));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_IPHONE + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE,
                expectedMessage);

        // missing category prefix
        assertParseFailure(parser, NAME_DESC_IPHONE + VALID_CATEGORY_IPHONE + COST_DESC_IPHONE,
                expectedMessage);

        // missing expensetracker prefix
        assertParseFailure(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + VALID_COST_IPHONE,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_IPHONE + VALID_CATEGORY_IPHONE + VALID_COST_IPHONE,
                expectedMessage);
    }


    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid category
        assertParseFailure(parser, NAME_DESC_IPHONE + INVALID_CATEGORY_DESC + COST_DESC_IPHONE
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Category.MESSAGE_CATEGORY_CONSTRAINTS);

        // invalid expensetracker
        assertParseFailure(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + INVALID_COST_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Cost.MESSAGE_COST_CONSTRAINTS);

        // 0 cost
        assertParseFailure(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + INVALID_COST_DESC_NEGATIVE
            + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, NEGATIVE_EXCEPTION_MESSAGE);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser, NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE
                + INVALID_DATE_DESC + VALID_TAG_FRIEND, Date.DATE_FORMAT_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + CATEGORY_DESC_IPHONE + INVALID_COST_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE
                        + COST_DESC_IPHONE + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
