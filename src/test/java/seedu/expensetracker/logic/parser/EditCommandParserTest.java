package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_CATEGORY_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_COST_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_THIRD_EXPENSE;

import org.junit.Test;

import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.logic.commands.EditCommand;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.EditExpenseDescriptor;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;
import seedu.expensetracker.testutil.EditExpenseDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_GAME, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_GAME, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_GAME, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS); // invalid name
        assertParseFailure(parser,
                "1" + INVALID_CATEGORY_DESC, Category.MESSAGE_CATEGORY_CONSTRAINTS); // invalid category
        assertParseFailure(parser, "1" + INVALID_COST_DESC, Cost.MESSAGE_COST_CONSTRAINTS); // invalid expensetracker
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag

        // invalid category
        assertParseFailure(parser, "1" + INVALID_CATEGORY_DESC, Category.MESSAGE_CATEGORY_CONSTRAINTS);

        // valid category followed by invalid category.
        // The test case for invalid category followed by valid category
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser,
                "1" + CATEGORY_DESC_IPHONE + INVALID_CATEGORY_DESC, Category.MESSAGE_CATEGORY_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Expense} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + VALID_COST_GAME + VALID_CATEGORY_GAME,
                Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_EXPENSE;
        String userInput = targetIndex.getOneBased() + CATEGORY_DESC_IPHONE + TAG_DESC_HUSBAND
                + COST_DESC_GAME + NAME_DESC_GAME + TAG_DESC_FRIEND;

        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder().withName(VALID_NAME_GAME)
                .withCategory(VALID_CATEGORY_IPHONE).withCost(VALID_COST_GAME)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_EXPENSE;
        String userInput = targetIndex.getOneBased() + CATEGORY_DESC_IPHONE;

        EditExpenseDescriptor descriptor =
                new EditExpenseDescriptorBuilder().withCategory(VALID_CATEGORY_IPHONE)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_EXPENSE;
        String userInput = targetIndex.getOneBased() + NAME_DESC_GAME;
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder().withName(VALID_NAME_GAME).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // category
        userInput = targetIndex.getOneBased() + CATEGORY_DESC_GAME;
        descriptor = new EditExpenseDescriptorBuilder().withCategory(VALID_CATEGORY_GAME).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // cost
        userInput = targetIndex.getOneBased() + COST_DESC_GAME;
        descriptor = new EditExpenseDescriptorBuilder().withCost(VALID_COST_GAME).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditExpenseDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_EXPENSE;
        String userInput = targetIndex.getOneBased() + CATEGORY_DESC_GAME + COST_DESC_GAME
                + TAG_DESC_FRIEND + CATEGORY_DESC_GAME + COST_DESC_GAME + TAG_DESC_FRIEND
                + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE + TAG_DESC_HUSBAND;

        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withCategory(VALID_CATEGORY_IPHONE)
                .withCost(VALID_COST_IPHONE).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_EXPENSE;
        String userInput = targetIndex.getOneBased() + INVALID_CATEGORY_DESC + CATEGORY_DESC_IPHONE;
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withCategory(VALID_CATEGORY_IPHONE).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + INVALID_CATEGORY_DESC + COST_DESC_IPHONE
                + CATEGORY_DESC_IPHONE;
        descriptor = new EditExpenseDescriptorBuilder().withCategory(VALID_CATEGORY_IPHONE)
                .withCost(VALID_COST_IPHONE).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_EXPENSE;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
