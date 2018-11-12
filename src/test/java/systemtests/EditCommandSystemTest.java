package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.DATE_DESC_1990;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_CATEGORY_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_COST_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.model.Model.PREDICATE_SHOW_ALL_EXPENSES;
import static seedu.expensetracker.testutil.TypicalExpenses.GAME;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.KEYWORD_MATCHING_BUY;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;

import org.junit.Test;

import seedu.expensetracker.commons.core.Messages;
import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.commands.EditCommand;
import seedu.expensetracker.logic.commands.RedoCommand;
import seedu.expensetracker.logic.commands.UndoCommand;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;
import seedu.expensetracker.testutil.ExpenseBuilder;

public class EditCommandSystemTest extends ExpenseTrackerSystemTest {

    @Test
    public void edit() throws NoUserSelectedException, IllegalValueException {
        Model model = getModel();
        showAllExpenses();
        /* -------------- Performing edit operation while an unfiltered list is being shown ------------------- */
        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each
         * field -> edited
         */
        Index index = INDEX_FIRST_EXPENSE;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_IPHONE + "  "
                + CATEGORY_DESC_IPHONE + " " + COST_DESC_IPHONE + " " + TAG_DESC_HUSBAND + " ";
        Expense editedExpense = new ExpenseBuilder(IPHONE).withTags(VALID_TAG_HUSBAND).build();
        assertCommandSuccess(command, index, editedExpense);
        /* Case: undo editing the last expense in the list -> last expense restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);
        /* Case: redo editing the last expense in the list -> last expense edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateExpense(
                getModel().getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased()), editedExpense);
        assertCommandSuccess(command, model, expectedResultMessage);
        /* Case: edit a expense with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE
                + COST_DESC_IPHONE + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, index, IPHONE);
        /* Case: edit a expense with new values same as another expense's values but with different name -> edited */
        assertTrue(getModel().getExpenseTracker().getExpenseList().contains(IPHONE));
        index = INDEX_SECOND_EXPENSE;
        assertNotEquals(getModel().getFilteredExpenseList().get(index.getZeroBased()), IPHONE);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_GAME + CATEGORY_DESC_IPHONE
                + COST_DESC_IPHONE + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedExpense = new ExpenseBuilder(IPHONE).withName(VALID_NAME_GAME).build();
        assertCommandSuccess(command, index, editedExpense);

        /* Case: edit a expense with new values same as another expense's values -> edited */
        assertTrue(getModel().getExpenseTracker().getExpenseList().contains(IPHONE));
        index = INDEX_SECOND_EXPENSE;
        assertFalse(getModel().getFilteredExpenseList().get(index.getZeroBased()).equals(IPHONE));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE
                + COST_DESC_IPHONE + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, index, IPHONE);

        /* Case: edit a expense with new values same as another expense's values but with different tags -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE
                + COST_DESC_IPHONE + TAG_DESC_HUSBAND;
        editedExpense = new ExpenseBuilder(IPHONE).withTags(VALID_TAG_HUSBAND).build();
        assertCommandSuccess(command, index, editedExpense);

        /* Case: edit a expense with new values same as another expense's values but with different cost
        -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_IPHONE + CATEGORY_DESC_IPHONE
                + COST_DESC_GAME + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedExpense = new ExpenseBuilder(IPHONE).withCost(VALID_COST_GAME).build();
        assertCommandSuccess(command, index, editedExpense);

        /* Case: edit a expense with new values same as another expense's values but with different category
        -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_IPHONE + CATEGORY_DESC_GAME
                + COST_DESC_IPHONE + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedExpense = new ExpenseBuilder(IPHONE).withCategory(VALID_CATEGORY_GAME).build();
        assertCommandSuccess(command, index, editedExpense);

        /* Case: edit a expense with new values same as another expense's values but with different category and cost
         * -> edited
         */
        index = INDEX_SECOND_EXPENSE;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_IPHONE + CATEGORY_DESC_GAME
                + COST_DESC_GAME + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedExpense = new ExpenseBuilder(IPHONE).withCategory(VALID_CATEGORY_GAME).withCost(VALID_COST_GAME).build();
        assertCommandSuccess(command, index, editedExpense);
        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_EXPENSE;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Expense expenseToEdit = getModel().getFilteredExpenseList().get(index.getZeroBased());
        editedExpense = new ExpenseBuilder(expenseToEdit).withTags().build();
        assertCommandSuccess(command, index, editedExpense);
        /* --------------- Performing edit operation while a filtered list is being shown --------------------- */
        /* Case: filtered expense list, edit index within bounds of expense tracker and expense list -> edited */
        showExpensesWithName(KEYWORD_MATCHING_BUY);
        index = INDEX_FIRST_EXPENSE;
        assertTrue(index.getZeroBased() < getModel().getFilteredExpenseList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_IPHONE;
        expenseToEdit = getModel().getFilteredExpenseList().get(index.getZeroBased());
        editedExpense = new ExpenseBuilder(expenseToEdit).withName(VALID_NAME_IPHONE).build();
        assertCommandSuccess(command, index, editedExpense);
        /* Case: filtered expense list, edit index within bounds of expense tracker but out of bounds of expense list
         * -> rejected
         */
        showExpensesWithName(KEYWORD_MATCHING_BUY);
        int invalidIndex = getModel().getExpenseTracker().getExpenseList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_IPHONE,
                Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
        /* ------------------ Performing edit operation while a expense card is selected ----------------------- */
        /* Case: selects first card in the expense list, edit a expense -> edited, card selection remains unchanged
         * but browser url changes
         */
        showAllExpenses();
        index = INDEX_FIRST_EXPENSE;
        selectExpense(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_GAME + CATEGORY_DESC_GAME
                + COST_DESC_GAME + TAG_DESC_FRIEND + DATE_DESC_1990;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new expense's name
        assertCommandSuccess(command, index, GAME, index);
        /* ------------------------------ Performing invalid edit operation ----------------------------------- */
        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_IPHONE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_IPHONE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredExpenseList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_IPHONE,
                Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_IPHONE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);
        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased()
                + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS);
        /* Case: invalid category -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased() + INVALID_CATEGORY_DESC,
                Category.MESSAGE_CATEGORY_CONSTRAINTS);

        /* Case: invalid expensetracker -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased()
                + INVALID_COST_DESC, Cost.MESSAGE_COST_CONSTRAINTS);
        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased()
                + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);


    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Expense, Index)} except that
     * the browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Expense, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Expense editedExpense) throws
            NoUserSelectedException, IllegalValueException {
        assertCommandSuccess(command, toEdit, editedExpense, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the expense at index {@code toEdit} being
     * updated to values specified {@code editedExpense}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Expense editedExpense,
                                      Index expectedSelectedCardIndex)
            throws NoUserSelectedException, IllegalValueException {
        Model expectedModel = getModel();
        expectedModel.updateExpense(expectedModel.getFilteredExpenseList().get(toEdit.getZeroBased()), editedExpense);
        expectedModel.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) throws
            NoUserSelectedException, IllegalValueException {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see ExpenseTrackerSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex)
            throws NoUserSelectedException, IllegalValueException {
        executeCommand(command);
        expectedModel.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
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
     *
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
