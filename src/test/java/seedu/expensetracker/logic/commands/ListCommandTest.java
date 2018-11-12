package seedu.expensetracker.logic.commands;

import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.logic.commands.CommandTestUtil.showExpenseAtIndex;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;

import org.junit.Before;
import org.junit.Test;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.ModelManager;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() throws NoUserSelectedException {
        model = getTypicalModel();
        expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() throws NoUserSelectedException {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
