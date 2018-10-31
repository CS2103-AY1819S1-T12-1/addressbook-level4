package seedu.address.logic.commands;
//@@author winsonhys

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.expense.CategoryTest.VALID_CATEGORY;
import static seedu.address.testutil.ModelUtil.getTypicalModel;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.budget.CategoryBudget;
import seedu.address.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.address.model.exceptions.NoUserSelectedException;


public class AddCategoryBudgetCommandTest {
    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        this.model = getTypicalModel();
        this.commandHistory = new CommandHistory();
    }

    @Test
    public void execute_addCategoryBudget_successful() throws NoUserSelectedException,
        CategoryBudgetExceedTotalBudgetException {
        ModelManager expectedModel = getTypicalModel();
        String validBudget = String.format("%.2f", expectedModel.getMaximumBudget().getBudgetCap() - 1);
        CategoryBudget toAdd = new CategoryBudget(VALID_CATEGORY, validBudget);
        expectedModel.addCategoryBudget(toAdd);
        expectedModel.commitExpenseTracker();
        AddCategoryBudgetCommand addCategoryBudgetCommand = new AddCategoryBudgetCommand(toAdd);
        String expectedMessage = String.format(AddCategoryBudgetCommand.MESSAGE_SUCCESS,
            toAdd.getCategory(), toAdd);

        assertCommandSuccess(addCategoryBudgetCommand, model, commandHistory, expectedMessage, expectedModel);

    }

    @Test
    public void execute_addCategoryBudget_unsuccessful() throws NoUserSelectedException {
        String validExcessBudget = String.format("%.2f", this.model.getMaximumBudget().getBudgetCap() + 1);
        CategoryBudget toAdd = new CategoryBudget(VALID_CATEGORY, validExcessBudget);
        AddCategoryBudgetCommand addCategoryBudgetCommand = new AddCategoryBudgetCommand(toAdd);
        String expectedMessage = AddCategoryBudgetCommand.EXCEED_MESSAGE;
        assertCommandFailure(addCategoryBudgetCommand, model, commandHistory, expectedMessage);

    }

}
