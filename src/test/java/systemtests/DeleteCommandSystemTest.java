package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX;
import static seedu.expensetracker.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.expensetracker.logic.commands.DeleteCommand.MESSAGE_DELETE_EXPENSE_SUCCESS;
import static seedu.expensetracker.testutil.TestUtil.getExpense;
import static seedu.expensetracker.testutil.TestUtil.getLastIndex;
import static seedu.expensetracker.testutil.TestUtil.getMidIndex;
import static seedu.expensetracker.testutil.TypicalExpenses.KEYWORD_MATCHING_BUY;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;

import org.junit.Test;

import seedu.expensetracker.commons.core.Messages;
import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.commands.DeleteCommand;
import seedu.expensetracker.logic.commands.RedoCommand;
import seedu.expensetracker.logic.commands.UndoCommand;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Expense;

public class DeleteCommandSystemTest extends ExpenseTrackerSystemTest {

    private static final String MESSAGE_INVALID_DELETE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);

    @Test
    public void delete() throws NoUserSelectedException, IllegalValueException {
        showAllExpenses();
        /* -------------- Performing delete operation while an unfiltered list is being shown ----------------- */

        /* Case: delete the first expense in the list, command with leading spaces and trailing spaces -> deleted */
        Model expectedModel = getModel();
        String command = "     " + DeleteCommand.COMMAND_WORD + "      " + INDEX_FIRST_EXPENSE.getOneBased()
                + "       ";
        Expense deletedExpense = removeExpense(expectedModel, INDEX_FIRST_EXPENSE);
        String expectedResultMessage = String.format(MESSAGE_DELETE_EXPENSE_SUCCESS, deletedExpense);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete the last expense in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastExpenseIndex = getLastIndex(modelBeforeDeletingLast);
        assertCommandSuccess(lastExpenseIndex);

        /* Case: undo deleting the last expense in the list -> last expense restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting the last expense in the list -> last expense deleted again */
        command = RedoCommand.COMMAND_WORD;
        removeExpense(modelBeforeDeletingLast, lastExpenseIndex);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete the middle expense in the list -> deleted */
        Index middleExpenseIndex = getMidIndex(getModel());
        assertCommandSuccess(middleExpenseIndex);

        /* --------------- Performing delete operation while a filtered list is being shown ------------------- */

        /* Case: filtered expense list, delete index within bounds of expense tracker and expense list -> deleted */
        showExpensesWithName(KEYWORD_MATCHING_BUY);
        Index index = INDEX_FIRST_EXPENSE;
        assertTrue(index.getZeroBased() < getModel().getFilteredExpenseList().size());
        assertCommandSuccess(index);

        /* Case: filtered expense list, delete index within bounds of expense tracker but out of bounds of expense list
         * -> rejected
         */
        showExpensesWithName(KEYWORD_MATCHING_BUY);
        int invalidIndex = getModel().getExpenseTracker().getExpenseList().size();
        command = DeleteCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);

        /* ------------------ Performing delete operation while a expense card is selected --------------------- */

        /* Case: delete the selected expense -> expense list panel selects the expense before the deleted expense */
        showAllExpenses();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectExpense(selectedIndex);
        command = DeleteCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        deletedExpense = removeExpense(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_DELETE_EXPENSE_SUCCESS, deletedExpense);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* ------------------------------ Performing invalid delete operation --------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getExpenseTracker().getExpenseList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Expense} at the specified {@code index} in {@code model}'s expense tracker.
     * @return the removed expense
     */
    private Expense removeExpense(Model model, Index index) throws NoUserSelectedException {
        Expense targetExpense = getExpense(model, index);
        model.deleteExpense(targetExpense);
        return targetExpense;
    }

    /**
     * Deletes the expense at {@code toDelete} by creating a default {@code DeleteCommand} using {@code toDelete} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Index toDelete) throws NoUserSelectedException, IllegalValueException {
        Model expectedModel = getModel();
        Expense deletedExpense = removeExpense(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_EXPENSE_SUCCESS, deletedExpense);

        assertCommandSuccess(
                DeleteCommand.COMMAND_WORD + " " + toDelete.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card remains unchanged.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) throws
            NoUserSelectedException, IllegalValueException {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see ExpenseTrackerSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) throws NoUserSelectedException, IllegalValueException {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
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
