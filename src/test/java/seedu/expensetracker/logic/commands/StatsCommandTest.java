package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.logic.commands.StatsCommand.MESSAGE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.expensetracker.commons.events.ui.ShowStatsRequestEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.testutil.Assert;
import seedu.expensetracker.testutil.ModelUtil;
import seedu.expensetracker.ui.testutil.EventsCollectorRule;

public class StatsCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = ModelUtil.modelWithTestUser();
    private Model expectedModel = ModelUtil.modelWithTestUser();
    private CommandHistory commandHistory = new CommandHistory();

    public StatsCommandTest() throws UserAlreadyExistsException, NonExistentUserException, NoUserSelectedException,
            InvalidDataException, ParseException {
    }

    @Test
    public void constructorValidParameters() {
        StatsCommand statsCommand = new StatsCommand(6, "d", "c");
        assertTrue(statsCommand.equals(new StatsCommand(6, "d", "c")));

        statsCommand = new StatsCommand();
        assertTrue(statsCommand.equals(new StatsCommand(7, "d", "t")));

        statsCommand = new StatsCommand(8, "m", "t");
        assertTrue(statsCommand.equals(new StatsCommand(8, "m", "t")));
    }

    @Test
    public void constructorInvalidParameters() {
        Assert.assertThrows(NullPointerException.class, () -> new StatsCommand(1, null, "t"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(1, "asd", "t"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(0, "d", "t"));
        Assert.assertThrows(NullPointerException.class, () -> new StatsCommand(1, "d", null));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(1, "d", "asd"));
    }

    @Test
    public void executeStatsSuccess() throws NoUserSelectedException {
        StatsCommand statsCommand = new StatsCommand();
        statsCommand.execute(model, commandHistory);

        assertTrue(model.getStatsPeriod() == StatsCommand.StatsPeriod.DAY);

        assertCommandSuccess(new StatsCommand(), model, commandHistory, MESSAGE_SUCCESS, expectedModel);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ShowStatsRequestEvent);
        assertEquals(18, eventsCollectorRule.eventsCollector.getSize());
    }
}
