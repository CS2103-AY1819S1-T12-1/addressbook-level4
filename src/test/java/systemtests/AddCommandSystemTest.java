package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.CATEGORY_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.COST_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.DATE_DESC_1990;
import static seedu.expensetracker.logic.commands.CommandTestUtil.DATE_DESC_2018;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_CATEGORY_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_COST_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.NAME_DESC_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.testutil.TypicalExpenses.GAMBLE;
import static seedu.expensetracker.testutil.TypicalExpenses.GAME;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.KEYWORD_MATCHING_BUY;
import static seedu.expensetracker.testutil.TypicalExpenses.SCHOOLFEE;
import static seedu.expensetracker.testutil.TypicalExpenses.STOCK;
import static seedu.expensetracker.testutil.TypicalExpenses.TOY;

import org.junit.Test;

import seedu.expensetracker.commons.core.Messages;
import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.commands.AddCommand;
import seedu.expensetracker.logic.commands.RedoCommand;
import seedu.expensetracker.logic.commands.UndoCommand;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;
import seedu.expensetracker.testutil.ExpenseBuilder;
import seedu.expensetracker.testutil.ExpenseUtil;

public class AddCommandSystemTest extends ExpenseTrackerSystemTest {

    @Test
    public void add() throws NoUserSelectedException, IllegalValueException {
        Model model = getModel();
        //Set totalBudget such that it never exceeds
        model.commitExpenseTracker();
        testApp.getActualModel().modifyMaximumBudget(new TotalBudget(String.format("%.2f", Double.MAX_VALUE)));
        testApp.getActualModel().commitExpenseTracker();

        model.modifyMaximumBudget(new TotalBudget(String.format("%.2f", Double.MAX_VALUE)));
        showAllExpenses();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a expense without tags to a non-empty expense tracker,
         * command with leading spaces and trailing spaces
         * -> added
         */
        Expense toAdd = GAME;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_GAME + "  " + CATEGORY_DESC_GAME + " "
                + "   " + COST_DESC_GAME + "   " + DATE_DESC_1990 + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addExpense(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a expense with all fields same as another expense in the expense tracker except name -> added */
        toAdd = new ExpenseBuilder(GAME).withName(VALID_NAME_IPHONE).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_IPHONE + CATEGORY_DESC_GAME + COST_DESC_GAME + DATE_DESC_1990
                + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a expense with all fields same as another expense in the expense tracker except category and cost
         * -> added
         */
        toAdd = new ExpenseBuilder(GAME).withCategory(VALID_CATEGORY_IPHONE).withCost(VALID_COST_IPHONE).build();
        command = ExpenseUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty expense tracker -> added */
        deleteAllExpenses();
        assertCommandSuccess(SCHOOLFEE);

        /* Case: add a expense with tags, command with parameters in random order -> added */
        toAdd = IPHONE;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE + NAME_DESC_IPHONE
                + TAG_DESC_HUSBAND + DATE_DESC_2018;
        assertCommandSuccess(command, toAdd);

        /* Case: add a expense, missing tags -> added */
        assertCommandSuccess(STOCK);

        /* Case: add a duplicate expense -> added */
        command = ExpenseUtil.getAddCommand(STOCK);
        toAdd = STOCK;
        assertCommandSuccess(command, toAdd);

        /* Case: add a duplicate expense except with different category -> added */
        toAdd = new ExpenseBuilder(STOCK).withCategory(VALID_CATEGORY_IPHONE).build();
        command = ExpenseUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add a duplicate expense except with different expensetracker -> added */
        toAdd = new ExpenseBuilder(STOCK).withCost(VALID_COST_IPHONE).build();
        command = ExpenseUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add a duplicate expense except with different tags -> added */
        toAdd = new ExpenseBuilder(STOCK).withTags(VALID_TAG_FRIEND).build();
        command = ExpenseUtil.getAddCommand(STOCK) + " " + PREFIX_TAG.getPrefix() + "friend";
        assertCommandSuccess(command, toAdd);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the expense list before adding -> added */
        showExpensesWithName(KEYWORD_MATCHING_BUY);
        assertCommandSuccess(GAMBLE);

        /* ------------------------ Perform add operation while a expense card is selected -------------------------- */

        /* Case: selects first card in the expense list, add a expense -> added, card selection remains unchanged */
        selectExpense(Index.fromOneBased(1));
        assertCommandSuccess(TOY);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + CATEGORY_DESC_GAME + COST_DESC_GAME + DATE_DESC_1990;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing category -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GAME + COST_DESC_GAME + DATE_DESC_1990;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing expensetracker -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GAME + CATEGORY_DESC_GAME + DATE_DESC_1990;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + ExpenseUtil.getExpenseDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + CATEGORY_DESC_GAME + COST_DESC_GAME + DATE_DESC_2018;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid category -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GAME + INVALID_CATEGORY_DESC + COST_DESC_GAME + DATE_DESC_1990;
        assertCommandFailure(command, Category.MESSAGE_CATEGORY_CONSTRAINTS);

        /* Case: invalid cost -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GAME + CATEGORY_DESC_GAME + INVALID_COST_DESC + DATE_DESC_1990;
        assertCommandFailure(command, Cost.MESSAGE_COST_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GAME + CATEGORY_DESC_GAME + COST_DESC_GAME + DATE_DESC_1990
                + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: add a expense when no user is logged in */
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + CATEGORY_DESC_IPHONE + COST_DESC_IPHONE + NAME_DESC_IPHONE
                + TAG_DESC_HUSBAND + DATE_DESC_2018;
        testApp.getActualModel().unloadUserData();
        executeCommand(command);
        assertTrue(getResultDisplay().getText().startsWith(new NoUserSelectedException().getMessage()));
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code ExpenseListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see ExpenseTrackerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Expense toAdd) throws NoUserSelectedException, IllegalValueException {
        assertCommandSuccess(ExpenseUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Expense)}. Executes {@code command}
     * instead.
     *
     * @see AddCommandSystemTest#assertCommandSuccess(Expense)
     */
    private void assertCommandSuccess(String command, Expense toAdd) throws NoUserSelectedException,
            IllegalValueException {
        Model expectedModel = getModel();
        expectedModel.addExpense(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Expense)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code ExpenseListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     *
     * @see AddCommandSystemTest#assertCommandSuccess(String, Expense)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) throws
            NoUserSelectedException, IllegalValueException {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code ExpenseListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
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
