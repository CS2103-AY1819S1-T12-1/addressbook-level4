package seedu.expensetracker.logic.commands;

import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.logic.commands.CommandTestUtil.deleteFirstExpense;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;

import org.junit.Before;
import org.junit.Test;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

public class RedoCommandTest {

    private final Model model = getTypicalModel();
    private final Model expectedModel = getTypicalModel();
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() throws NoUserSelectedException {
        // set up of both models' undo/redo history
        deleteFirstExpense(model);
        deleteFirstExpense(model);
        model.undoExpenseTracker();
        model.undoExpenseTracker();

        deleteFirstExpense(expectedModel);
        deleteFirstExpense(expectedModel);
        expectedModel.undoExpenseTracker();
        expectedModel.undoExpenseTracker();
    }

    @Test
    public void execute() throws NoUserSelectedException {
        // multiple redoable states in model
        expectedModel.redoExpenseTracker();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // single redoable state in model
        expectedModel.redoExpenseTracker();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // no redoable state in model
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }
}
