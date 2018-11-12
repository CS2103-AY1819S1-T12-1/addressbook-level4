package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_NOTIFICATION_TIP_ONLY;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_NOTIFICATION_TYPE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_NOTIFICATION_WARNING_ONLY;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_TOGGLE_TYPE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NOTIFICATION_OFF;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NOTIFICATION_ON;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NOTIFICATION_TIP_OFF;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NOTIFICATION_TIP_ON;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NOTIFICATION_WARNING_OFF;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NOTIFICATION_WARNING_ON;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.expensetracker.logic.commands.NotificationCommand;
import seedu.expensetracker.logic.commands.NotificationCommand.NotificationCommandDescriptor;



//@@author Snookerballs

public class NotificationCommandParserTest {
    private NotificationCommandParser parser = new NotificationCommandParser();

    @Test
    public void parse_invalidSyntax_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NotificationCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNoSuffix_throwsParseException() {
        assertParseFailure(parser, NotificationCommand.COMMAND_WORD + " "
                + INVALID_NOTIFICATION_TYPE, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NotificationCommand.MESSAGE_USAGE));
        assertParseFailure(parser, NotificationCommand.COMMAND_WORD + " "
                + INVALID_TOGGLE_TYPE, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NotificationCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_invalidNoTogglePrefix_throwsParseException() {
        assertParseFailure(parser, NotificationCommand.COMMAND_WORD + " "
                + INVALID_NOTIFICATION_TIP_ONLY, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NotificationCommand.MESSAGE_USAGE));
        assertParseFailure(parser, NotificationCommand.COMMAND_WORD + " "
                + INVALID_NOTIFICATION_WARNING_ONLY, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NotificationCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_validNoNotificationTypePrefix() {
        NotificationCommandDescriptor descriptor = new NotificationCommandDescriptor();
        descriptor.setToggle(NotificationCommand.OPTION_ON);
        assertParseSuccess(parser, NotificationCommand.COMMAND_WORD + " " + VALID_NOTIFICATION_ON,
                new NotificationCommand(descriptor));
        descriptor.setToggle(NotificationCommand.OPTION_OFF);
        assertParseSuccess(parser, NotificationCommand.COMMAND_WORD + " " + VALID_NOTIFICATION_OFF,
                new NotificationCommand(descriptor));
    }

    @Test
    public void parse_validToggleWarning() {
        NotificationCommandDescriptor descriptor = new NotificationCommandDescriptor();
        descriptor.setNotificationType(NotificationCommand.OPTION_WARNING);
        descriptor.setToggle(NotificationCommand.OPTION_ON);
        assertParseSuccess(parser, NotificationCommand.COMMAND_WORD + " " + VALID_NOTIFICATION_WARNING_ON,
                new NotificationCommand(descriptor));
        descriptor.setToggle(NotificationCommand.OPTION_OFF);
        assertParseSuccess(parser, NotificationCommand.COMMAND_WORD + " " + VALID_NOTIFICATION_WARNING_OFF,
                new NotificationCommand(descriptor));
    }

    @Test
    public void parse_validToggleTip() {
        NotificationCommandDescriptor descriptor = new NotificationCommandDescriptor();
        descriptor.setNotificationType(NotificationCommand.OPTION_TIP);
        descriptor.setToggle(NotificationCommand.OPTION_ON);
        assertParseSuccess(parser, NotificationCommand.COMMAND_WORD + " " + VALID_NOTIFICATION_TIP_ON,
                new NotificationCommand(descriptor));
        descriptor.setToggle(NotificationCommand.OPTION_OFF);
        assertParseSuccess(parser, NotificationCommand.COMMAND_WORD + " " + VALID_NOTIFICATION_TIP_OFF,
                new NotificationCommand(descriptor));
    }


}
