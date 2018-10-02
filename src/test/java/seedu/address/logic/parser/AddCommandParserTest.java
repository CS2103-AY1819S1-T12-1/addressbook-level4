package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CATEGORY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.CATEGORY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.COST_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.COST_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_1990;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_2018;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CATEGORY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_1990;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.expense.Category;
import seedu.address.model.expense.Cost;
import seedu.address.model.expense.Date;
import seedu.address.model.expense.Name;
import seedu.address.model.expense.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();
        Person expectedPersonWithDate = new PersonBuilder(BOB)
                .withTags(VALID_TAG_FRIEND)
                .withDate(VALID_DATE_1990).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + CATEGORY_DESC_BOB + DATE_DESC_2018
                + COST_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + CATEGORY_DESC_BOB + DATE_DESC_2018
                + COST_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple category - last category accepted
        assertParseSuccess(parser, NAME_DESC_BOB + CATEGORY_DESC_AMY + CATEGORY_DESC_BOB + DATE_DESC_2018
                + COST_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + CATEGORY_DESC_BOB + COST_DESC_AMY + DATE_DESC_2018
                + COST_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // add date
        assertParseSuccess(parser, NAME_DESC_BOB + CATEGORY_DESC_BOB + COST_DESC_AMY
                + COST_DESC_BOB + TAG_DESC_FRIEND + DATE_DESC_1990, new AddCommand(expectedPersonWithDate));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + CATEGORY_DESC_BOB + COST_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + DATE_DESC_2018, new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();

        assertParseSuccess(parser, NAME_DESC_AMY + CATEGORY_DESC_AMY + COST_DESC_AMY + DATE_DESC_1990,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + CATEGORY_DESC_BOB + COST_DESC_BOB,
                expectedMessage);

        // missing category prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_CATEGORY_BOB + COST_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + CATEGORY_DESC_BOB + VALID_COST_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_CATEGORY_BOB + VALID_COST_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + CATEGORY_DESC_BOB + COST_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid category
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_CATEGORY_DESC + COST_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Category.MESSAGE_CATEGORY_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + CATEGORY_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Cost.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + CATEGORY_DESC_BOB + COST_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser, NAME_DESC_BOB + CATEGORY_DESC_BOB + COST_DESC_BOB
                + INVALID_DATE_DESC + VALID_TAG_FRIEND, Date.DATE_FORMAT_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + CATEGORY_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + CATEGORY_DESC_BOB
                        + COST_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
