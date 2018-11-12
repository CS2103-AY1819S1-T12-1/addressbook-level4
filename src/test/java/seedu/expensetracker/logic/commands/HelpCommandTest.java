package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.Rule;
import org.junit.Test;

import seedu.expensetracker.commons.events.ui.ShowHelpRequestEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.testutil.ModelUtil;
import seedu.expensetracker.ui.testutil.EventsCollectorRule;

public class HelpCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = ModelUtil.modelWithTestUser();
    private Model expectedModel = ModelUtil.modelWithTestUser();
    private CommandHistory commandHistory = new CommandHistory();

    public HelpCommandTest() throws UserAlreadyExistsException, NonExistentUserException, NoUserSelectedException,
            InvalidDataException, ParseException {
    }

    @Test
    public void execute_help_success() {
        assertCommandSuccess(new HelpCommand(), model, commandHistory, SHOWING_HELP_MESSAGE, expectedModel);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ShowHelpRequestEvent);
        assertEquals(15, eventsCollectorRule.eventsCollector.getSize());
    }
}
