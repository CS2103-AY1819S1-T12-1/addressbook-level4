package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.logic.commands.CommandTestUtil.DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.logic.commands.CommandTestUtil.showExpenseAtIndex;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;

import org.junit.Test;

import seedu.expensetracker.commons.core.Messages;
import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.ModelManager;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.EditExpenseDescriptor;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.testutil.EditExpenseDescriptorBuilder;
import seedu.expensetracker.testutil.ExpenseBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws NoUserSelectedException {
        Expense editedExpense = new ExpenseBuilder().build();
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);
        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);
        expectedModel.updateExpense(model.getFilteredExpenseList().get(0), editedExpense);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws NoUserSelectedException {
        Index indexLastExpense = Index.fromOneBased(model.getFilteredExpenseList().size());
        Expense lastExpense = model.getFilteredExpenseList().get(indexLastExpense.getZeroBased());

        ExpenseBuilder expenseInList = new ExpenseBuilder(lastExpense);
        Expense editedExpense = expenseInList.withName(VALID_NAME_IPHONE).withCategory(VALID_CATEGORY_IPHONE)
                .withTags(VALID_TAG_HUSBAND).build();

        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder().withName(VALID_NAME_IPHONE)
                .withCategory(VALID_CATEGORY_IPHONE).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastExpense, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);
        expectedModel.updateExpense(lastExpense, editedExpense);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws NoUserSelectedException {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE, new EditExpenseDescriptor());
        Expense editedExpense = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);

        Expense expenseInFilteredList = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        Expense editedExpense = new ExpenseBuilder(expenseInFilteredList).withName(VALID_NAME_IPHONE).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE,
                new EditExpenseDescriptorBuilder().withName(VALID_NAME_IPHONE).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);
        expectedModel.updateExpense(model.getFilteredExpenseList().get(0), editedExpense);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateExpenseUnfilteredList_success() throws NoUserSelectedException {
        Expense firstExpense = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(firstExpense).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_EXPENSE, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, firstExpense);

        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);
        expectedModel.updateExpense(model.getFilteredExpenseList().get(1), firstExpense);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateExpenseFilteredList_success() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        // edit expense in filtered list into a duplicate in expense tracker
        Expense expenseInList = model.getExpenseTracker().getExpenseList().get(INDEX_SECOND_EXPENSE.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE,
                new EditExpenseDescriptorBuilder(expenseInList).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, expenseInList);

        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);
        expectedModel.updateExpense(model.getFilteredExpenseList().get(0), expenseInList);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidExpenseIndexUnfilteredList_failure() throws NoUserSelectedException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredExpenseList().size() + 1);
        EditExpenseDescriptor descriptor =
                new EditExpenseDescriptorBuilder().withName(VALID_NAME_IPHONE).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of expense tracker
     */
    @Test
    public void execute_invalidExpenseIndexFilteredList_failure() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        Index outOfBoundIndex = INDEX_SECOND_EXPENSE;
        // ensures that outOfBoundIndex is still in bounds of expense tracker list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getExpenseTracker().getExpenseList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditExpenseDescriptorBuilder().withName(VALID_NAME_IPHONE).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Expense editedExpense = new ExpenseBuilder().build();
        Expense expenseToEdit = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE, descriptor);
        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);
        expectedModel.updateExpense(expenseToEdit, editedExpense);
        expectedModel.commitExpenseTracker();

        // edit -> first expense edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts expensetracker back to previous state and filtered expense list to show all expenses
        expectedModel.undoExpenseTracker();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first expense edited again
        expectedModel.redoExpenseTracker();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() throws NoUserSelectedException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredExpenseList().size() + 1);
        EditExpenseDescriptor descriptor =
                new EditExpenseDescriptorBuilder().withName(VALID_NAME_IPHONE).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> expense tracker state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);

        // single expense tracker state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Expense} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited expense in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the expense object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameExpenseEdited() throws Exception {
        Expense editedExpense = new ExpenseBuilder().build();
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE, descriptor);
        Model expectedModel = new ModelManager(new ExpenseTracker(model.getExpenseTracker()), new UserPrefs(), null);

        showExpenseAtIndex(model, INDEX_SECOND_EXPENSE);
        Expense expenseToEdit = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        expectedModel.updateExpense(expenseToEdit, editedExpense);
        expectedModel.commitExpenseTracker();

        // edit -> edits second expense in unfiltered expense list / first expense in filtered expense list
        editCommand.execute(model, commandHistory);

        // undo -> reverts expensetracker back to previous state and filtered expense list to show all expenses
        expectedModel.undoExpenseTracker();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased()), expenseToEdit);
        // redo -> edits same second expense in unfiltered expense list
        expectedModel.redoExpenseTracker();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_EXPENSE, DESC_GAME);

        // same values -> returns true
        EditExpenseDescriptor copyDescriptor = new EditExpenseDescriptor(DESC_GAME);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_EXPENSE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_EXPENSE, DESC_GAME)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_EXPENSE, DESC_IPHONE)));
    }

}
