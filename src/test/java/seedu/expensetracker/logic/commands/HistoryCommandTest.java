package seedu.expensetracker.logic.commands;

import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.Test;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.testutil.ModelUtil;

public class HistoryCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = ModelUtil.modelWithTestUser();
    private Model expectedModel = ModelUtil.modelWithTestUser();

    public HistoryCommandTest() throws UserAlreadyExistsException, NonExistentUserException, NoUserSelectedException,
            InvalidDataException, ParseException {
    }

    @Test
    public void execute() {
        assertCommandSuccess(new HistoryCommand(), model, history, HistoryCommand.MESSAGE_NO_HISTORY, expectedModel);

        String command1 = "clear";
        history.add(command1);
        assertCommandSuccess(new HistoryCommand(), model, history,
                String.format(HistoryCommand.MESSAGE_SUCCESS, command1), expectedModel);

        String command2 = "randomCommand";
        String command3 = "select 1";
        history.add(command2);
        history.add(command3);

        String expectedMessage = String.format(HistoryCommand.MESSAGE_SUCCESS,
                String.join("\n", command3, command2, command1));
        assertCommandSuccess(new HistoryCommand(), model, history, expectedMessage, expectedModel);
    }

}
