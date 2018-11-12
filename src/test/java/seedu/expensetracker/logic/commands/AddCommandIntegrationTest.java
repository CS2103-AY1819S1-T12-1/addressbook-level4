package seedu.expensetracker.logic.commands;

import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.ModelManager;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.testutil.ExpenseBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        model = getTypicalModel();
        commandHistory = new CommandHistory();
    }

    @Test
    public void execute_newExpense_withinBudget() throws NoUserSelectedException {
        Expense validExpense = new ExpenseBuilder().withCost("1.00").build();
        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        expectedModel.addExpense(validExpense);
        expectedModel.commitExpenseTracker();


        assertCommandSuccess(new AddCommand(validExpense), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validExpense), expectedModel);
    }

    @Test
    public void execute_newExpense_withinCategoryBudget() throws NoUserSelectedException,
        CategoryBudgetExceedTotalBudgetException {
        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        this.model.modifyMaximumBudget(new TotalBudget("200.00"));
        this.model.setCategoryBudget(new CategoryBudget("Test", "3.00"));
        expectedModel.modifyMaximumBudget(new TotalBudget("200.00"));
        expectedModel.setCategoryBudget(new CategoryBudget("Test", "3.00"));
        Expense validExpense = new ExpenseBuilder().withCost("1.00").withCategory("Test").build();
        expectedModel.addExpense(validExpense);
        expectedModel.commitExpenseTracker();
        assertCommandSuccess(new AddCommand(validExpense), model, commandHistory,
            String.format(AddCommand.MESSAGE_SUCCESS, validExpense), expectedModel);
    }


    @Test
    public void execute_duplicateExpense_budgetExceed() throws NoUserSelectedException {
        Expense expenseInList = model.getExpenseTracker().getExpenseList().get(0);
        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        expectedModel.addExpense(expenseInList);
        expectedModel.commitExpenseTracker();
        assertCommandSuccess(new AddCommand(expenseInList), model, commandHistory,
            String.format(AddCommand.MESSAGE_SUCCESS, expenseInList), expectedModel);
    }

}
