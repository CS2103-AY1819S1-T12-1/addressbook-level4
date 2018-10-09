package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalExpenses.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.Expense;
import seedu.address.testutil.ExpenseBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newExpense_withinBudget() throws NoUserSelectedException {
        Expense validExpense = new ExpenseBuilder().withCost("1.00").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addExpense(validExpense);
        expectedModel.commitAddressBook();


        assertCommandSuccess(new AddCommand(validExpense), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validExpense), expectedModel);
    }

    @Test
    public void execute_newExpense_budgetExceed() throws NoUserSelectedException {
        Expense validExpense = new ExpenseBuilder().withCost("9999.99").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addExpense(validExpense);
        expectedModel.commitAddressBook();

        assertCommandSuccess(new AddCommand(validExpense), model, commandHistory,
            AddCommand.MESSAGE_BUDGET_EXCEED_WARNING, expectedModel);
    }

    @Test
    public void execute_duplicateExpense_throwsCommandException() throws NoUserSelectedException {
        Expense expenseInList = model.getAddressBook().getExpenseList().get(0);
        assertCommandFailure(new AddCommand(expenseInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_EXPENSE);
    }

}
