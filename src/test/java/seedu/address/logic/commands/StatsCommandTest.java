package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.StatsCommand.MESSAGE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.events.ui.ShowStatsRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.InvalidDataException;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.testutil.Assert;
import seedu.address.testutil.ModelUtil;
import seedu.address.ui.testutil.EventsCollectorRule;

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
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(1, null, "t"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(1, "asd", "t"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(0, "d", "t"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(1, "d", null));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StatsCommand(1, "d", "asd"));
    }

    @Test
    public void executeStatsSuccess() throws NoUserSelectedException {
        StatsCommand statsCommand = new StatsCommand();
        statsCommand.execute(model, commandHistory);

        assertTrue(model.getStatsPeriod() == StatsCommand.StatsPeriod.DAY);

        assertCommandSuccess(new StatsCommand(), model, commandHistory, MESSAGE_SUCCESS, expectedModel);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ShowStatsRequestEvent);
        assertEquals(12, eventsCollectorRule.eventsCollector.getSize());
    }
}
