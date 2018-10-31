package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.StatsCommand;

public class StatsCommandParserTest {
    private StatsCommandParser parser = new StatsCommandParser();

    @Test
    public void parseAllFieldsPresentSuccess() {
        assertParseSuccess(parser, " n/7 p/m m/t", new StatsCommand(7, "m", "t"));
        assertParseSuccess(parser, " n/6 p/d m/c", new StatsCommand(6, "d", "c"));
    }

    @Test
    public void parseNoParamsSuccess() {
        assertParseSuccess(parser, "", new StatsCommand(7, "d", "t"));
    }

    @Test
    public void parseFieldMissingFailure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatsCommand.MESSAGE_USAGE);

        // missing number
        assertParseFailure(parser, " p/m m/c", expectedMessage);

        // missing period
        assertParseFailure(parser, " n/7 m/t", expectedMessage);

        // missing mode
        assertParseFailure(parser, " n/7 p/m", expectedMessage);

        // missing number + period
        assertParseFailure(parser, "m/t", expectedMessage);

        // missing number + mode
        assertParseFailure(parser, " n/7 p/m", expectedMessage);

        // missing mode + period
        assertParseFailure(parser, "n/7", expectedMessage);
    }

    @Test
    public void parseInvalidValueFailure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatsCommand.MESSAGE_USAGE);

        // number less than 0
        assertParseFailure(parser, " n/0 p/m m/t", expectedMessage);

        // period not d or m
        assertParseFailure(parser, " n/1 p/p m/t", expectedMessage);

        // mode not c or t
        assertParseFailure(parser, " n/1 p/m m/m", expectedMessage);

        // all params invalid
        assertParseFailure(parser, " n/0 p/p m/m", expectedMessage);
    }
}
