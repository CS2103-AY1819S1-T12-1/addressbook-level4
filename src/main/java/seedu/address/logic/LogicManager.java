package seedu.address.logic;

import java.text.SimpleDateFormat;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.StatsCommand.StatsMode;
import seedu.address.logic.commands.StatsCommand.StatsPeriod;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ExpenseTrackerParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.exceptions.InvalidDataException;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.Expense;
import seedu.address.model.notification.Notification;

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
     * Returns a LinkedHashMap containing data for the bar chart, where the key is the x-axis and the value is the
     * y-axis.
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
                period = new SimpleDateFormat("MMM-YYYY").format(e.getDate().fullDate.getTime());
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

    /*public Iterator getCategoryList() {
        return model.getCategoryList();
    }*/

    public ObservableList<Notification> getNotificationList() throws NoUserSelectedException {
        return model.getNotificationList();
    }

    public int getPeriodAmount() {
        return model.getPeriodAmount();
    }

    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

}
