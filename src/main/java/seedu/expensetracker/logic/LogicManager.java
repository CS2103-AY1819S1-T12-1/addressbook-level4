package seedu.expensetracker.logic;

import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.expensetracker.commons.core.ComponentManager;
import seedu.expensetracker.commons.core.LogsCenter;
import seedu.expensetracker.logic.commands.Command;
import seedu.expensetracker.logic.commands.CommandResult;
import seedu.expensetracker.logic.commands.StatsCommand.StatsMode;
import seedu.expensetracker.logic.commands.StatsCommand.StatsPeriod;
import seedu.expensetracker.logic.commands.exceptions.CommandException;
import seedu.expensetracker.logic.parser.ExpenseTrackerParser;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.Notification;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final ExpenseTrackerParser expenseTrackerParser;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        expenseTrackerParser = new ExpenseTrackerParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException, NoUserSelectedException,
            UserAlreadyExistsException, NonExistentUserException, InvalidDataException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = expenseTrackerParser.parseCommand(commandText);
            return command.execute(model, history);
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException {
        return model.getFilteredExpenseList();
    }

    //@@author winsonhys
    @Override
    public TotalBudget getMaximumBudget() throws NoUserSelectedException {
        return model.getMaximumBudget();
    }

    //@@author jonathantjm

    /**
     * Returns a map of expenses with key and value pair representing data for the statistics chart.
     * The method will get the expense stats from {@code Model} then, depending on {@code statsMode},
     * the method will call helper functions which will return the relevant map data.
     *
     * @return LinkedHashMap of String key and Double value
     * @throws NoUserSelectedException
     */
    public LinkedHashMap<String, Double> getExpenseStats() throws NoUserSelectedException {
        ObservableList<Expense> expenseList = model.getExpenseStats();
        StatsPeriod statsPeriod = model.getStatsPeriod();
        StatsMode statsMode = model.getStatsMode();

        if (statsMode == statsMode.TIME) {
            return getTimeBasedStats(expenseList, statsPeriod);
        }
        return getCategoryBasedStats(expenseList);
    }

    /**
     * Returns a map of expenses with key and value pair representing data for the statistics chart.
     * If {@code statsPeriod} is set to month, the key will be the different months in the period. Otherwise,
     * the key will be the different days in the period.
     * The value represents the cumulative cost for that day or month.
     *
     * @param expenseList a list of expenses
     * @param statsPeriod a {@code StatsPeriod} enum representing the period
     * @return the map of data used for the statistics
     */
    private LinkedHashMap<String, Double> getTimeBasedStats(
        ObservableList<Expense> expenseList,
        StatsPeriod statsPeriod
    ) {
        LinkedHashMap<String, Double> stats = new LinkedHashMap<>();
        for (Expense e : expenseList) {
            String period;
            if (statsPeriod == statsPeriod.DAY) {
                period = e.getDate().toString();
            } else {
                period = e.getDate().getFullDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }

            if (stats.containsKey(period)) {
                stats.put(
                        period,
                        stats.get(period) + e.getCost().getCostValue()
                );
            } else {
                stats.put(period, e.getCost().getCostValue());
            }
        }
        return stats;
    }

    /**
     * Returns a map of expenses with key and value pair representing data for the statistics chart.
     * The key represents the different categories.
     * The value represents the cumulative cost for that day or month.
     *
     * @param expenseList a list of expenses
     * @return the map of data used for the statistics
     */
    private LinkedHashMap<String, Double> getCategoryBasedStats(ObservableList<Expense> expenseList) {
        LinkedHashMap<String, Double> stats = new LinkedHashMap<>();
        for (Expense e : expenseList) {
            String category;
            category = e.getCategory().categoryName;

            if (stats.containsKey(category)) {
                stats.put(
                        category,
                        stats.get(category) + e.getCost().getCostValue()
                );
            } else {
                stats.put(category, e.getCost().getCostValue());
            }
        }
        return stats;
    }

    public StatsPeriod getStatsPeriod() {
        return model.getStatsPeriod();
    }

    public StatsMode getStatsMode() {
        return model.getStatsMode();
    }

    public int getPeriodAmount() {
        return model.getPeriodAmount();
    }

    /*public Iterator getCategoryList() {
        return model.getCategoryList();
    }*/

    //@@author
    public ObservableList<Notification> getNotificationList() throws NoUserSelectedException {
        return model.getNotificationList();
    }

    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

    public Iterator<CategoryBudget> getCategoryBudgets() throws NoUserSelectedException {
        return model.getCategoryBudgets().iterator();
    }

}
