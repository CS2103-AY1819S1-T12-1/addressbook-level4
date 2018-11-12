package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_HOURS;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_MINUTES;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NOTIFICATION_TYPE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_SECONDS;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TOGGLE;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.commands.exceptions.CommandException;
import seedu.expensetracker.logic.parser.ArgumentMultimap;
import seedu.expensetracker.logic.parser.ArgumentTokenizer;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.expense.EditExpenseDescriptor;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.expense.ExpenseContainsKeywordsPredicate;
import seedu.expensetracker.testutil.EditExpenseDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_GAME = "Spend on video game";
    public static final String VALID_NAME_IPHONE = "IPone";
    public static final String VALID_NAME_KFC = "Have KFC";
    public static final String VALID_CATEGORY_GAME = "Game";
    public static final String VALID_CATEGORY_IPHONE = "Phone";
    public static final String VALID_CATEGORY_KFC = "Food";

    public static final String VALID_COST_GAME = "123.00";
    public static final String VALID_COST_IPHONE = "722.00";
    public static final String VALID_COST_KFC = "10.00";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_TAG_FOOD = "Lunch";
    public static final String VALID_DATE_1990 = "01-01-1990";
    public static final String VALID_DATE_2018 = "02-10-2018";

    public static final String VALID_NOTIFICATION_TOGGLE_ON = "on";
    public static final String VALID_NOTIFICATION_TOGGLE_OFF = "off";
    public static final String VALID_NOTIFICATION_TYPE_WARNING = "warning";
    public static final String VALID_NOTIFICATION_TYPE_TIP = "tip";
    public static final String VALID_NOTIFICATION_ON = PREFIX_TOGGLE
            + VALID_NOTIFICATION_TOGGLE_ON;
    public static final String VALID_NOTIFICATION_OFF = PREFIX_TOGGLE
            + VALID_NOTIFICATION_TOGGLE_OFF;
    public static final String VALID_NOTIFICATION_WARNING_ON = PREFIX_NOTIFICATION_TYPE
            + VALID_NOTIFICATION_TYPE_WARNING + " "
            + VALID_NOTIFICATION_ON;
    public static final String VALID_NOTIFICATION_WARNING_OFF = PREFIX_NOTIFICATION_TYPE
            + VALID_NOTIFICATION_TYPE_WARNING + " "
            + VALID_NOTIFICATION_OFF;
    public static final String VALID_NOTIFICATION_TIP_ON = PREFIX_NOTIFICATION_TYPE
            + VALID_NOTIFICATION_TYPE_TIP + " "
            + VALID_NOTIFICATION_ON;
    public static final String VALID_NOTIFICATION_TIP_OFF = PREFIX_NOTIFICATION_TYPE
            + VALID_NOTIFICATION_TYPE_TIP + " "
            + VALID_NOTIFICATION_OFF;
    public static final String INVALID_NOTIFICATION_WARNING_ONLY = PREFIX_NOTIFICATION_TYPE
            + VALID_NOTIFICATION_TYPE_WARNING;
    public static final String INVALID_NOTIFICATION_TIP_ONLY = PREFIX_NOTIFICATION_TYPE
            + VALID_NOTIFICATION_TYPE_TIP;
    public static final String INVALID_NOTIFICATION_TYPE = PREFIX_NOTIFICATION_TYPE + "invalid"
            + " " + VALID_NOTIFICATION_ON;
    public static final String INVALID_TOGGLE_TYPE = PREFIX_TOGGLE + "invalid";


    public static final String VALID_TIME_ONE = "1";
    public static final String VALID_TIME_ONE_SECOND = " " + PREFIX_SECONDS + VALID_TIME_ONE;
    public static final String VALID_TIME_ONE_MINUTE = " " + PREFIX_MINUTES + VALID_TIME_ONE;
    public static final String VALID_TIME_ONE_HOUR = " " + PREFIX_HOURS + VALID_TIME_ONE;
    public static final String NAME_DESC_GAME = " " + PREFIX_NAME + VALID_NAME_GAME;
    public static final String NAME_DESC_IPHONE = " " + PREFIX_NAME + VALID_NAME_IPHONE;
    public static final String NAME_DESC_KFC = " " + PREFIX_NAME + VALID_NAME_KFC;
    public static final String CATEGORY_DESC_GAME = " " + PREFIX_CATEGORY + VALID_CATEGORY_GAME;
    public static final String CATEGORY_DESC_IPHONE = " " + PREFIX_CATEGORY + VALID_CATEGORY_IPHONE;
    public static final String CATEGORY_DESC_KFC = " " + PREFIX_CATEGORY + VALID_CATEGORY_KFC;
    public static final String COST_DESC_GAME = " " + PREFIX_COST + VALID_COST_GAME;
    public static final String COST_DESC_IPHONE = " " + PREFIX_COST + VALID_COST_IPHONE;
    public static final String COST_DESC_KFC = " " + PREFIX_COST + VALID_COST_KFC;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String DATE_DESC_1990 = " " + PREFIX_DATE + VALID_DATE_1990;
    public static final String DATE_DESC_2018 = " " + PREFIX_DATE + VALID_DATE_2018;
    public static final String TAG_DESC_FOOD = " " + PREFIX_TAG + VALID_TAG_FOOD;
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_CATEGORY_DESC = " " + PREFIX_CATEGORY + " "; // empty entry is not allowed
    public static final String INVALID_COST_DESC = " " + PREFIX_COST; // empty string not allowed for addresses
    public static final String INVALID_COST_DESC_NEGATIVE = " " + PREFIX_COST + "0.00";
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "-01-1990"; // no day

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditExpenseDescriptor DESC_GAME;
    public static final EditExpenseDescriptor DESC_IPHONE;

    static {
        DESC_GAME = new EditExpenseDescriptorBuilder().withName(VALID_NAME_GAME)
                .withCategory(VALID_CATEGORY_GAME).withCost(VALID_COST_GAME)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_IPHONE = new EditExpenseDescriptorBuilder().withName(VALID_NAME_IPHONE)
                .withCategory(VALID_CATEGORY_IPHONE).withCost(VALID_COST_IPHONE)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel.getMaximumBudget(), actualModel.getMaximumBudget());
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (Exception ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the expense tracker and the filtered expense list in the {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        try {
            ExpenseTracker expectedExpenseTracker = new ExpenseTracker(actualModel.getExpenseTracker());
            List<Expense> expectedFilteredList = new ArrayList<>(actualModel.getFilteredExpenseList());

            CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

            try {
                command.execute(actualModel, actualCommandHistory);
                throw new AssertionError("The expected CommandException was not thrown.");
            } catch (CommandException e) {
                assertEquals(expectedMessage, e.getMessage());
                assertEquals(expectedExpenseTracker, actualModel.getExpenseTracker());
                assertEquals(expectedFilteredList, actualModel.getFilteredExpenseList());
                assertEquals(expectedCommandHistory, actualCommandHistory);
            }
        } catch (NoUserSelectedException | NonExistentUserException | UserAlreadyExistsException
                | InvalidDataException e) {
            Assert.fail("Command threw error : " + e.getMessage());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the expense at the given {@code targetIndex} in the
     * {@code model}'s expense tracker.
     */
    public static void showExpenseAtIndex(Model model, Index targetIndex) throws NoUserSelectedException {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredExpenseList().size());

        Expense expense = model.getFilteredExpenseList().get(targetIndex.getZeroBased());
        String[] splitName = expense.getName().expenseName.split("\\s+");
        final ArgumentMultimap keywordsMap = ArgumentTokenizer.tokenize(" n/"
                + splitName[0], PREFIX_NAME);
        model.updateFilteredExpenseList(new ExpenseContainsKeywordsPredicate(keywordsMap));

        assertEquals(1, model.getFilteredExpenseList().size());
    }

    /**
     * Deletes the first expense in {@code model}'s filtered list from {@code model}'s expense tracker.
     */
    public static void deleteFirstExpense(Model model) throws NoUserSelectedException {
        Expense firstExpense = model.getFilteredExpenseList().get(0);
        model.deleteExpense(firstExpense);
        model.commitExpenseTracker();
    }

}
