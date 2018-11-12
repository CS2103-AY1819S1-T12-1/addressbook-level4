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

public class UndoCommandTest {

    private final Model model = getTypicalModel();
    private final Model expectedModel = getTypicalModel();
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() throws NoUserSelectedException {
        // set up of models' undo/redo history
        deleteFirstExpense(model);
        deleteFirstExpense(model);

        deleteFirstExpense(expectedModel);
        deleteFirstExpense(expectedModel);
    }

    @Test
    public void execute() throws NoUserSelectedException {
        // multiple undoable states in model
        expectedModel.undoExpenseTracker();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // single undoable state in model
        expectedModel.undoExpenseTracker();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // no undoable states in model
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
    }
}
