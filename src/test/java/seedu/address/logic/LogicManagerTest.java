package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.commands.StatsCommand.StatsMode;
import seedu.address.logic.commands.StatsCommand.StatsPeriod;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.InvalidDataException;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.Expense;
import seedu.address.testutil.ExpenseBuilder;
import seedu.address.testutil.ModelUtil;

public class LogicManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = ModelUtil.modelWithTestUser();

    private Logic logic = new LogicManager(model);

    public LogicManagerTest() throws UserAlreadyExistsException, NonExistentUserException, NoUserSelectedException,
            InvalidDataException, ParseException {
    }

    @BeforeEach
    public void clearModel() throws UserAlreadyExistsException, NonExistentUserException, NoUserSelectedException,
            InvalidDataException, ParseException {
        model = ModelUtil.modelWithTestUser();
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() throws NoUserSelectedException {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
        assertHistoryCorrect(invalidCommand);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() throws NoUserSelectedException {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
        assertHistoryCorrect(deleteCommand);
    }

    @Test
    public void execute_validCommand_success() {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
        assertHistoryCorrect(listCommand);
    }

    @Test
    public void getFilteredExpenseList_modifyList_throwsUnsupportedOperationException() throws NoUserSelectedException {
        thrown.expect(UnsupportedOperationException.class);
        logic.getFilteredExpenseList().remove(0);
    }

    @Test
    public void getExpenseStats_returnsEmptyMapWhenNoEntries() throws NoUserSelectedException {
        assertTrue(logic.getExpenseStats().size() == 0);
    }

    @Test
    public void getExpenseStatsReturnsMapWithCorrectSingleEntries() throws NoUserSelectedException {
        Expense validExpense = new ExpenseBuilder().build();
        model.addExpense(validExpense);
        model.updateStatsMode(StatsMode.TIME);
        model.updateStatsPeriod(StatsPeriod.DAY);

        //Check
        LinkedHashMap<String, Double> map = logic.getExpenseStats();
        assertTrue(map.size() > 0);
        assertTrue(map.containsKey(validExpense.getDate().toString()));
        assertTrue(map.get(validExpense.getDate().toString()) == validExpense.getCost().getCostValue());

        model.updateStatsPeriod(StatsCommand.StatsPeriod.MONTH);
        map = logic.getExpenseStats();
        String month = new SimpleDateFormat("MMM-YYYY").format(validExpense.getDate().fullDate.getTime());
        assertTrue(map.size() > 0);
        assertTrue(map.containsKey(month));
        assertTrue(map.get(month) == validExpense.getCost().getCostValue());

        model.updateStatsMode(StatsMode.CATEGORY);
        map = logic.getExpenseStats();
        assertTrue(map.size() > 0);
        assertTrue(map.containsKey(validExpense.getCategory().categoryName));
        assertTrue(map.get(validExpense.getCategory().categoryName) == validExpense.getCost().getCostValue());
    }

    @Test
    public void getExpenseStatsReturnsMapWithCorrectMultipleEntries() throws NoUserSelectedException {
        Expense validExpense = new ExpenseBuilder().build();
        Expense validExpenseTwo = new ExpenseBuilder().withName("Food").build();
        model.addExpense(validExpense);
        model.addExpense(validExpenseTwo);
        model.updateStatsMode(StatsMode.TIME);
        model.updateStatsPeriod(StatsPeriod.DAY);

        LinkedHashMap<String, Double> map = logic.getExpenseStats();
        assertTrue(map.size() > 0);
        assertTrue(map.containsKey(validExpense.getDate().toString()));
        assertTrue(map.get(validExpense.getDate().toString()) == (validExpense.getCost().getCostValue() * 2));

        model.updateStatsPeriod(StatsCommand.StatsPeriod.MONTH);
        map = logic.getExpenseStats();
        String month = new SimpleDateFormat("MMM-YYYY").format(validExpense.getDate().fullDate.getTime());
        assertTrue(map.size() > 0);
        assertTrue(map.containsKey(month));
        assertTrue(map.get(month) == (validExpense.getCost().getCostValue() * 2));

        model.updateStatsMode(StatsMode.CATEGORY);
        map = logic.getExpenseStats();
        assertTrue(map.size() > 0);
        assertTrue(map.containsKey(validExpense.getCategory().categoryName));
        assertTrue(map.get(validExpense.getCategory().categoryName) == (validExpense.getCost().getCostValue() * 2));
    }

    @Test
    public void getStatsPeriodReturnsCorrectStatsMode() throws NoUserSelectedException {
        model.updateStatsPeriod(StatsCommand.StatsPeriod.DAY);
        assertTrue(logic.getStatsPeriod() == StatsPeriod.DAY);
        model.updateStatsPeriod(StatsCommand.StatsPeriod.MONTH);
        assertTrue(logic.getStatsPeriod() == StatsCommand.StatsPeriod.MONTH);
    }

    @Test
    public void getStatsModeReturnsCorrectStatsMode() throws NoUserSelectedException {
        model.updateStatsMode(StatsMode.TIME);
        assertTrue(logic.getStatsMode() == StatsMode.TIME);
        model.updateStatsMode(StatsMode.CATEGORY);
        assertTrue(logic.getStatsMode() == StatsMode.CATEGORY);
    }

    @Test
    public void getPeriodAmountReturnsCorrectPeriodAmount() throws NoUserSelectedException {
        model.updatePeriodAmount(7);
        assertTrue(logic.getPeriodAmount() == 7);
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the result message is correct.
     * Also confirms that {@code expectedModel} is as specified.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) throws NoUserSelectedException {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) throws NoUserSelectedException {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<?> expectedException, String expectedMessage)
            throws NoUserSelectedException {
        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        assertCommandBehavior(expectedException, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and that the expected exception is thrown,
     * and also confirms that the following two parts of the LogicManager object's state are as expected:<br>
     * - the internal model manager data are same as those in the {@code expectedModel} <br>
     * - {@code expectedModel}'s expense tracker was saved to the storage file.
     */
    private void assertCommandBehavior(Class<?> expectedException, String inputCommand,
                                       String expectedMessage, Model expectedModel) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertNull(expectedException);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException | ParseException | NoUserSelectedException | NonExistentUserException
                | UserAlreadyExistsException | InvalidDataException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }
        assertEquals(expectedModel, model);
    }

    /**
     * Asserts that the result display shows all the {@code expectedCommands} upon the execution of
     * {@code HistoryCommand}.
     */
    private void assertHistoryCorrect(String... expectedCommands) {
        try {
            CommandResult result = logic.execute(HistoryCommand.COMMAND_WORD);
            String expectedMessage = String.format(
                    HistoryCommand.MESSAGE_SUCCESS, String.join("\n", expectedCommands));
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (ParseException | CommandException | NoUserSelectedException | NonExistentUserException
                | UserAlreadyExistsException | InvalidDataException e) {
            throw new AssertionError("Parsing and execution of HistoryCommand.COMMAND_WORD should succeed.", e);
        }
    }
}
