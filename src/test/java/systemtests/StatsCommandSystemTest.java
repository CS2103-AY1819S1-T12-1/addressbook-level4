package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import org.junit.Test;

import guitests.GuiRobot;
import guitests.guihandles.StatsWindowHandle;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.StatsCommand;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.ui.StatusBarFooter;

/**
 * A system test class for the help window, which contains interaction with other UI components.
 */
public class StatsCommandSystemTest extends AddressBookSystemTest {
    private static final String ERROR_MESSAGE = "ATTENTION!!!! : On some computers, this test may fail when run on "
            + "non-headless mode as FxRobot#clickOn(Node, MouseButton...) clicks on the wrong location. We suspect "
            + "that this is a bug with TestFX library that we are using. If this test fails, you have to run your "
            + "tests on headless mode. See UsingGradle.adoc on how to do so.";

    private final GuiRobot guiRobot = new GuiRobot();

    @Test
    public void openStatsWindow() throws NoUserSelectedException {
        //use command box
        executeCommand(StatsCommand.COMMAND_WORD);
        assertStatsWindowOpen();

        // open stats window and give it focus
        executeCommand(StatsCommand.COMMAND_WORD);
        getMainWindowHandle().focus();

        // assert that while the stats window is open the UI updates correctly for a command execution
        executeCommand(SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased());
        assertEquals("", getCommandBox().getInput());
        assertCommandBoxShowsDefaultStyle();
        assertNotEquals(StatsCommand.MESSAGE_SUCCESS, getResultDisplay().getText());
        assertListMatching(getExpenseListPanel(), getModel().getFilteredExpenseList());

        // assert that the status bar too is updated correctly while the help window is open
        // note: the select command tested above does not update the status bar
        executeCommand(DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased());
        assertNotEquals(StatusBarFooter.SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Asserts that the help window is open, and closes it after checking.
     */
    private void assertStatsWindowOpen() {
        assertTrue(ERROR_MESSAGE, StatsWindowHandle.isWindowPresent());
        guiRobot.pauseForHuman();

        new StatsWindowHandle(guiRobot.getStage(StatsWindowHandle.STATS_WINDOW_TITLE)).close();
        getMainWindowHandle().focus();
    }

    /**
     * Asserts that the help window isn't open.
     */
    private void assertStatsWindowNotOpen() {
        assertFalse(ERROR_MESSAGE, StatsWindowHandle.isWindowPresent());
    }

}
