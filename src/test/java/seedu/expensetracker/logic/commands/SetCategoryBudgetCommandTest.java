package seedu.expensetracker.logic.commands;
//@@author winsonhys

import static junit.framework.TestCase.assertTrue;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.model.expense.CategoryTest.VALID_CATEGORY;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.ModelManager;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;


public class SetCategoryBudgetCommandTest {
    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        this.model = getTypicalModel();
        this.commandHistory = new CommandHistory();
    }

    @Test
    public void execute_setCategoryBudget_successful() throws NoUserSelectedException,
        CategoryBudgetExceedTotalBudgetException {
        ModelManager expectedModel = getTypicalModel();
        String validBudget = String.format("%.2f", expectedModel.getMaximumBudget().getBudgetCap() - 1);
        CategoryBudget toAdd = new CategoryBudget(VALID_CATEGORY, validBudget);
        expectedModel.setCategoryBudget(toAdd);
        expectedModel.commitExpenseTracker();
        SetCategoryBudgetCommand setCategoryBudgetCommand = new SetCategoryBudgetCommand(toAdd);
        String expectedMessage = String.format(SetCategoryBudgetCommand.MESSAGE_SUCCESS,
            toAdd.getCategory(), toAdd.getBudgetCap());

        assertCommandSuccess(setCategoryBudgetCommand, model, commandHistory, expectedMessage, expectedModel);
        assertTrue(expectedModel.getMaximumBudget().getCategoryBudgets().toArray().length == 1);

    }

    @Test
    public void execute_setCategoryBudget_editSuccessful() throws NoUserSelectedException,
        CategoryBudgetExceedTotalBudgetException {
        ModelManager expectedModel = new ModelManager(this.model.getExpenseTracker(), new UserPrefs(), null);
        String validBudget = String.format("%.2f", expectedModel.getMaximumBudget().getBudgetCap() - 1);
        this.model.setCategoryBudget(new CategoryBudget(VALID_CATEGORY, validBudget));
        expectedModel.setCategoryBudget(new CategoryBudget(VALID_CATEGORY, validBudget));

        CategoryBudget toAdd = new CategoryBudget(VALID_CATEGORY, "1.00");
        expectedModel.setCategoryBudget(toAdd);
        expectedModel.commitExpenseTracker();
        SetCategoryBudgetCommand setCategoryBudgetCommand = new SetCategoryBudgetCommand(toAdd);
        String expectedMessage = String.format(SetCategoryBudgetCommand.MESSAGE_SUCCESS,
            toAdd.getCategory(), toAdd.getBudgetCap());

        assertCommandSuccess(setCategoryBudgetCommand, model, commandHistory, expectedMessage, expectedModel);
        assertTrue(expectedModel.getMaximumBudget()
            .getCategoryBudgets().toArray(new CategoryBudget[1])[0].getBudgetCap() == 1);
    }

    @Test
    public void execute_setCategoryBudget_unsuccessful() throws NoUserSelectedException {
        String validExcessBudget = String.format("%.2f", this.model.getMaximumBudget().getBudgetCap() + 1);
        CategoryBudget toAdd = new CategoryBudget(VALID_CATEGORY, validExcessBudget);
        SetCategoryBudgetCommand setCategoryBudgetCommand = new SetCategoryBudgetCommand(toAdd);
        String expectedMessage = SetCategoryBudgetCommand.EXCEED_MESSAGE;
        assertCommandFailure(setCategoryBudgetCommand, model, commandHistory, expectedMessage);

    }

}
