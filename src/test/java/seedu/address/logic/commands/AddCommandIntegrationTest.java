package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.ModelUtil.getTypicalModel;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.budget.CategoryBudget;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.Expense;
import seedu.address.testutil.ExpenseBuilder;

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
    public void execute_newExpense_budgetExceed() throws NoUserSelectedException {
        Expense validExpense = new ExpenseBuilder().withCost("9999.99").build();
        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        expectedModel.addExpense(validExpense);
        expectedModel.commitExpenseTracker();
        assertCommandSuccess(new AddCommand(validExpense), model, commandHistory,
            AddCommand.MESSAGE_BUDGET_EXCEED_WARNING, expectedModel);
    }

    @Test
    public void execute_newExpense_categoryBudgetExceed() throws NoUserSelectedException,
        CategoryBudgetExceedTotalBudgetException {
        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        this.model.modifyMaximumBudget(new TotalBudget("200.00"));
        this.model.setCategoryBudget(new CategoryBudget("Test", "1.00"));
        expectedModel.modifyMaximumBudget(new TotalBudget("200.00"));
        expectedModel.setCategoryBudget(new CategoryBudget("Test", "1.00"));
        Expense validExpense = new ExpenseBuilder().withCost("2.00").withCategory("Test").build();
        expectedModel.addExpense(validExpense);
        expectedModel.commitExpenseTracker();
        assertCommandSuccess(new AddCommand(validExpense), model, commandHistory,
            AddCommand.MESSAGE_BUDGET_EXCEED_WARNING, expectedModel);

    }

    @Test
    public void execute_duplicateExpense_throwsCommandException() throws NoUserSelectedException {
        Expense expenseInList = model.getExpenseTracker().getExpenseList().get(0);
        assertCommandFailure(new AddCommand(expenseInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_EXPENSE);
    }

}
