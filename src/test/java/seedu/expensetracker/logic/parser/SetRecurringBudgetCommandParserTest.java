package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TIME_ONE_HOUR;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TIME_ONE_MINUTE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TIME_ONE_SECOND;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_SECONDS;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.expensetracker.logic.commands.SetRecurringBudgetCommand;

public class SetRecurringBudgetCommandParserTest {
    private SetRecurringBudgetCommandParser parser = new SetRecurringBudgetCommandParser();

    @Test
    public void parse_validSecondsArgs_returnsSetRecurringBudgetCommand() {
        assertParseSuccess(parser, VALID_TIME_ONE_SECOND, new SetRecurringBudgetCommand(1));
    }

    @Test
    public void parse_validMinutesArgs_returnsSetRecurringBudgetCommand() {
        assertParseSuccess(parser, VALID_TIME_ONE_MINUTE, new SetRecurringBudgetCommand(60));
    }

    @Test
    public void parse_validHoursArgs_returnsSetRecurringBudgetCommand() {
        assertParseSuccess(parser, VALID_TIME_ONE_HOUR,
            new SetRecurringBudgetCommand(60 * 60));
    }

    @Test
    public void parse_validMultipleArgs_returnsSetRecurringBudgetCommand() {
        assertParseSuccess(parser, VALID_TIME_ONE_MINUTE + VALID_TIME_ONE_SECOND,
            new SetRecurringBudgetCommand(61));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            SetRecurringBudgetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_negativeArgs_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_SECONDS + "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            SetRecurringBudgetCommand.MESSAGE_USAGE));
    }

}
