package seedu.expensetracker.logic.commands;

import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;

import org.junit.Test;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.testutil.ModelUtil;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyExpenseTracker_success() throws NoUserSelectedException, UserAlreadyExistsException,
            NonExistentUserException, InvalidDataException, ParseException {
        Model model = ModelUtil.modelWithTestUser();
        Model expectedModel = ModelUtil.modelWithTestUser();
        expectedModel.commitExpenseTracker();
        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS,
                expectedModel);
    }

    @Test
    public void execute_nonEmptyExpenseTracker_success() throws NoUserSelectedException {
        Model model = getTypicalModel();

        Model expectedModel = getTypicalModel();
        expectedModel.resetData(new ExpenseTracker(new Username("typicalExpenseTracker"), null,
                DEFAULT_ENCRYPTION_KEY));

        TotalBudget clearedTotalBudget = model.getMaximumBudget();
        clearedTotalBudget.clearSpending();
        expectedModel.modifyMaximumBudget(clearedTotalBudget);
        expectedModel.commitExpenseTracker();
        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
