package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalExpenses.BOOKS;
import static seedu.address.testutil.TypicalExpenses.CLOTHES;
import static seedu.address.testutil.TypicalExpenses.ICECREAM;
import static seedu.address.testutil.TypicalExpenses.KEYWORD_MATCHING_BUY;
import static seedu.address.testutil.TypicalExpenses.KEYWORD_MATCHING_FOOD;
import static seedu.address.testutil.TypicalExpenses.KEYWORD_MATCHING_LUNCH;
import static seedu.address.testutil.TypicalExpenses.LUNCH;
import static seedu.address.testutil.TypicalExpenses.TOY;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.Name;
import seedu.address.model.tag.Tag;

public class FindCommandSystemTest extends ExpenseTrackerSystemTest {

    @Test
    public void find() throws NoUserSelectedException, IllegalValueException {
        showAllExpenses();
        /* Case: find multiple expenses in expense tracker, command with leading spaces and trailing spaces
         * -> 2 expenses found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_BUY + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, TOY, BOOKS);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where expense list is displaying the expenses we are finding
         * -> 2 expenses found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_BUY;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find one expense in expense tracker with multiple keywords
         * -> 1 expenses found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_LUNCH + " " + KEYWORD_MATCHING_FOOD;
        ModelHelper.setFilteredList(expectedModel, LUNCH);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense where expense list is not displaying the expense we are finding -> 1 expense found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " Toy";
        ModelHelper.setFilteredList(expectedModel, TOY);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in expense tracker, 2 keywords -> 2 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " ice clothes";
        ModelHelper.setFilteredList(expectedModel, ICECREAM, CLOTHES);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in expense tracker, 2 keywords in reversed order -> 2 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " clothes ice";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in expense tracker, 2 keywords with 1 repeat -> 2 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " clothes clothes ice";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in expense tracker, 2 matching keywords and 1 non-matching keyword
         * -> 2 expenses found
         */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " ice clothes NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same expenses in expense tracker after deleting 1 of them -> 1 expense found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getExpenseTracker().getExpenseList().contains(ICECREAM));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_LUNCH;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, LUNCH);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense in expense tracker, keyword is same as name but of different case -> 1 expense found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " have";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense in expense tracker, keyword is substring of name -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " fo";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense in expense tracker, name is substring of keyword -> 1 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " ha";
        ModelHelper.setFilteredList(expectedModel, LUNCH);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense not in expense tracker -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " gamble";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find category name of expense in expense tracker -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + CLOTHES.getCategory().categoryName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of expense in expense tracker -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + CLOTHES.getCost().value;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, Name.MESSAGE_NAME_CONSTRAINTS));
        assertSelectedCardUnchanged();

        /* Case: find tags of expense in expense tracker -> 0 expenses found */
        List<Tag> tags = new ArrayList<>(CLOTHES.getTags());
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a expense is selected -> selected card deselected */
        showAllExpenses();
        selectExpense(Index.fromOneBased(1));
        assertFalse(getExpenseListPanel().getHandleToSelectedCard().getName().equals(CLOTHES.getName().expenseName));
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " clothes";
        ModelHelper.setFilteredList(expectedModel, CLOTHES);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find expense in empty expense tracker -> 0 expenses found */
        deleteAllExpenses();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_BUY;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, CLOTHES);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd n/Tax";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);

        /* Case: no prefix before keywords -> rejected */
        command = FindCommand.COMMAND_WORD + " Tax";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));


    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_EXPENSES_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) throws NoUserSelectedException,
            IllegalValueException {
        String expectedResultMessage = String.format(
                MESSAGE_EXPENSES_LISTED_OVERVIEW, expectedModel.getFilteredExpenseList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) throws NoUserSelectedException,
            IllegalValueException {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
