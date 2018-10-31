package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.commands.NotificationCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.commands.SignUpCommand;
import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.expense.EditExpenseDescriptor;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;
import seedu.address.testutil.EditExpenseDescriptorBuilder;
import seedu.address.testutil.ExpenseBuilder;
import seedu.address.testutil.ExpenseUtil;

public class ExpenseTrackerParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ExpenseTrackerParser parser = new ExpenseTrackerParser();

    @Test
    public void parseCommand_add() throws Exception {
        Expense expense = new ExpenseBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(ExpenseUtil.getAddCommand(expense));
        assertEquals(new AddCommand(expense), command);
        command = (AddCommand) parser.parseCommand(ExpenseUtil.getAddCommandAlias(expense));
        assertEquals(new AddCommand(expense), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_EXPENSE), command);
        command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + INDEX_FIRST_EXPENSE.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_EXPENSE), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Expense expense = new ExpenseBuilder().build();
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(expense).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_EXPENSE.getOneBased() + " " + ExpenseUtil.getEditExpenseDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_EXPENSE, descriptor), command);
        command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_EXPENSE.getOneBased() + " " + ExpenseUtil.getEditExpenseDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_EXPENSE, descriptor), command);
    }
    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_ALIAS) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_ALIAS + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        ArgumentMultimap keywordsMap = ArgumentTokenizer.tokenize(" n/foo bar baz", PREFIX_NAME);
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + PREFIX_NAME + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new ExpenseContainsKeywordsPredicate(keywordsMap)), command);
        command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + " " + PREFIX_NAME + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new ExpenseContainsKeywordsPredicate(keywordsMap)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_EXPENSE), command);
        command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_EXPENSE.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_EXPENSE), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS + " 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS + " 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_login() throws Exception {
        assertTrue(parser.parseCommand(LoginCommand.COMMAND_WORD + " u/userna p/assadd") instanceof LoginCommand);
        assertTrue(parser.parseCommand(LoginCommand.COMMAND_ALIAS + " u/userna p/assadd") instanceof LoginCommand);
    }

    @Test
    public void parseCommand_signUpCommand() throws Exception {
        assertTrue(parser.parseCommand(SignUpCommand.COMMAND_WORD + " ssssss") instanceof SignUpCommand);
        assertTrue(parser.parseCommand(SignUpCommand.COMMAND_ALIAS + " ssssss") instanceof SignUpCommand);
    }

    @Test
    public void parseCommand_setPassword() throws Exception {
        assertTrue(parser.parseCommand(SetPasswordCommand.COMMAND_WORD + " o/dsdfsdf7 n/sdfsdf")
                instanceof SetPasswordCommand);
        assertTrue(parser.parseCommand(SetPasswordCommand.COMMAND_ALIAS + " o/dsdfsdf7 n/sdfsdf")
                instanceof SetPasswordCommand);
    }

    @Test
    public void parseCommand_stats() throws Exception {
        assertTrue(parser.parseCommand(StatsCommand.COMMAND_WORD) instanceof StatsCommand);
        assertTrue(parser.parseCommand(StatsCommand.COMMAND_WORD + " n/7 p/m m/t") instanceof StatsCommand);
        assertTrue(parser.parseCommand(StatsCommand.COMMAND_ALIAS) instanceof StatsCommand);
        assertTrue(parser.parseCommand(StatsCommand.COMMAND_ALIAS + " n/7 p/m m/t") instanceof StatsCommand);
    }

    @Test
    public void parseCommand_notification() throws Exception {
        assertTrue(parser.parseCommand(NotificationCommand.COMMAND_WORD + " t/off")
                instanceof NotificationCommand);
        assertTrue(parser.parseCommand(NotificationCommand.COMMAND_WORD + " n/warning t/on")
                instanceof NotificationCommand);
        assertTrue(parser.parseCommand(NotificationCommand.COMMAND_ALIAS + " t/off")
                instanceof NotificationCommand);
        assertTrue(parser.parseCommand(NotificationCommand.COMMAND_ALIAS + " n/tip t/off")
                instanceof NotificationCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
