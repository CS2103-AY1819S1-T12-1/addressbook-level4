package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showExpenseAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;
import static seedu.address.testutil.TypicalExpenses.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.EditCommand.EditExpenseDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.Expense;
import seedu.address.testutil.EditExpenseDescriptorBuilder;
import seedu.address.testutil.ExpenseBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws NoUserSelectedException {
        Expense editedExpense = new ExpenseBuilder().build();
        EditCommand.EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateExpense(model.getFilteredExpenseList().get(0), editedExpense);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws NoUserSelectedException {
        Index indexLastExpense = Index.fromOneBased(model.getFilteredExpenseList().size());
        Expense lastExpense = model.getFilteredExpenseList().get(indexLastExpense.getZeroBased());

        ExpenseBuilder expenseInList = new ExpenseBuilder(lastExpense);
        Expense editedExpense = expenseInList.withName(VALID_NAME_BOB).withCategory(VALID_CATEGORY_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditCommand.EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder().withName(VALID_NAME_BOB)
                .withCategory(VALID_CATEGORY_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastExpense, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateExpense(lastExpense, editedExpense);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws NoUserSelectedException {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE, new EditExpenseDescriptor());
        Expense editedExpense = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);

        Expense expenseInFilteredList = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        Expense editedExpense = new ExpenseBuilder(expenseInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE,
                new EditExpenseDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateExpense(model.getFilteredExpenseList().get(0), editedExpense);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateExpenseUnfilteredList_failure() throws NoUserSelectedException {
        Expense firstExpense = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(firstExpense).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_EXPENSE, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_EXPENSES);
    }

    @Test
    public void execute_duplicateExpenseFilteredList_failure() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        // edit expense in filtered list into a duplicate in address book
        Expense expenseInList = model.getAddressBook().getExpenseList().get(INDEX_SECOND_EXPENSE.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE,
                new EditExpenseDescriptorBuilder(expenseInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_EXPENSES);
    }

    @Test
    public void execute_invalidExpenseIndexUnfilteredList_failure() throws NoUserSelectedException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredExpenseList().size() + 1);
        EditCommand.EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidExpenseIndexFilteredList_failure() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        Index outOfBoundIndex = INDEX_SECOND_EXPENSE;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getExpenseList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditExpenseDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Expense editedExpense = new ExpenseBuilder().build();
        Expense expenseToEdit = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_EXPENSE, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateExpense(expenseToEdit, editedExpense);
        expectedModel.commitAddressBook();

        // edit -> first expense edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered expense list to show all expenses
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first expense edited again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() throws NoUserSelectedException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredExpenseList().size() + 1);
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> address book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
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
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showExpenseAtIndex(model, INDEX_SECOND_EXPENSE);
        Expense expenseToEdit = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());
        expectedModel.updateExpense(expenseToEdit, editedExpense);
        expectedModel.commitAddressBook();

        // edit -> edits second expense in unfiltered expense list / first expense in filtered expense list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered expense list to show all expenses
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased()), expenseToEdit);
        // redo -> edits same second expense in unfiltered expense list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_EXPENSE, DESC_AMY);

        // same values -> returns true
        EditCommand.EditExpenseDescriptor copyDescriptor = new EditCommand.EditExpenseDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_EXPENSE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_EXPENSE, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_EXPENSE, DESC_BOB)));
    }

}
