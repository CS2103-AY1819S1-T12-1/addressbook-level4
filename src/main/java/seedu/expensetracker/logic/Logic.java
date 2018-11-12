package seedu.expensetracker.logic;

import java.util.Iterator;
import java.util.LinkedHashMap;

import javafx.collections.ObservableList;

import seedu.expensetracker.logic.commands.CommandResult;
import seedu.expensetracker.logic.commands.StatsCommand.StatsMode;
import seedu.expensetracker.logic.commands.StatsCommand.StatsPeriod;
import seedu.expensetracker.logic.commands.exceptions.CommandException;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.Notification;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException, NoUserSelectedException,
            UserAlreadyExistsException, NonExistentUserException, InvalidDataException;

    TotalBudget getMaximumBudget() throws NoUserSelectedException;

    /**
     * @return an unmodifiable view of the filtered list of expenses
     * @throws NoUserSelectedException
     */
    ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException;

    /**
     * Returns a map of expenses with key and value pair representing data for the statistics chart
     * @return a LinkedHashMap of expenses for the bar chart
     * @throws NoUserSelectedException
     */
    LinkedHashMap<String, Double> getExpenseStats() throws NoUserSelectedException;

    /**
     * @return a {@code StatsMode} representing the current mode of statistics
     * @throws NoUserSelectedException
     */
    StatsMode getStatsMode() throws NoUserSelectedException;

    /**
     * @return a {@code StatsPeriod} representing the current period of statistics
     * @throws NoUserSelectedException
     */
    StatsPeriod getStatsPeriod() throws NoUserSelectedException;

    /**
     * @return an int representing the user selected number of days or months
     * @throws NoUserSelectedException
     */
    int getPeriodAmount() throws NoUserSelectedException;

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();

    /** Returns an iterator the categories and their total expenses */
    Iterator<CategoryBudget> getCategoryBudgets() throws NoUserSelectedException;

    /**
     * Returns an unmodifiable list of Notifications
     */
    ObservableList<Notification> getNotificationList() throws NoUserSelectedException;

}
