package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.util.AppUtil.checkArgument;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_PERIOD_AMOUNT;

import java.time.LocalDateTime;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.events.ui.ShowStatsRequestEvent;
import seedu.expensetracker.commons.events.ui.SwapLeftPanelEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Expense;

//@@author jonathantjm
/**
 * This class consists of methods and properties related to the handling of the 'stats' command
 */
public class StatsCommand extends Command {

    /**
     * This is the command word and alias which a user needs to type to activate the statistics command
     */
    public static final String COMMAND_WORD = "stats";
    public static final String COMMAND_ALIAS = "st";

    /**
     * These are messages displayed to the user depending on whether their input is successful or not
     */
    public static final String MESSAGE_SUCCESS = "updated the stats panel";
    public static final String MESSAGE_PERIOD_AMOUNT_ERROR = "PERIOD_AMOUNT needs to be a positive integer";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Swap to or update the stats panel. "
            + "Parameters: "
            + PREFIX_PERIOD_AMOUNT + "PERIOD_AMOUNT\n "
            + PREFIX_PERIOD + "PERIOD (either 'm' or 'd')\n"
            + PREFIX_MODE + "MODE (either 'c' or 't')\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PERIOD_AMOUNT + "7 "
            + PREFIX_PERIOD + "d "
            + PREFIX_MODE + "t";

    public static final String MESSAGE_PARAMETERS_FORMAT = "Command should be in format: \n"
            + COMMAND_WORD + " "
            + PREFIX_PERIOD_AMOUNT + "PERIOD_AMOUNT\n "
            + PREFIX_PERIOD + "PERIOD (either 'm' or 'd')\n"
            + PREFIX_MODE + "MODE (either 'c' or 't')\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PERIOD_AMOUNT + "7 "
            + PREFIX_PERIOD + "d "
            + PREFIX_MODE + "t";

    /**
     *  Enum for StatsPeriod. StatsPeriod can either be MONTH or DAY
     */
    public enum StatsPeriod {
        MONTH, DAY
    }

    /**
     *  Enum for StatsMode. StatsMode can either be TIME or CATEGORY
     */
    public enum StatsMode {
        TIME, CATEGORY
    }

    /**
     * These are the parameters which the user can set based on what they want to see
     * in the chart
     */
    private final Logger logger = Logger.getLogger("Statistics Logger");
    private int periodAmount;
    private StatsPeriod period;
    private StatsMode mode;

    /**
     * Constructs a {@code StatsCommand} object with the set of default parameters.
     */
    public StatsCommand() {
        this(7, "d", "t");
    }

    /**
     * Constructs a {@code StatsCommand} object with parameters after checking whether parameters are valid.
     * The method checks whether {@code periodAmount} is a positive integer, {@code period} is either "m" or "d"
     * and whether {@code mode} is either "c" or "t"
     *
     * @param periodAmount an int for period amount
     * @param period a string for period
     * @param mode a string for mode
     */
    public StatsCommand(int periodAmount, String period, String mode) {
        requireNonNull(period);
        requireNonNull(mode);
        checkArgument(isValidMode(mode), MESSAGE_PARAMETERS_FORMAT);
        checkArgument(isValidPeriod(period), MESSAGE_PARAMETERS_FORMAT);
        checkArgument(isValidPeriodAmount(periodAmount), MESSAGE_PERIOD_AMOUNT_ERROR);
        this.periodAmount = periodAmount;
        if ("d".equals(period)) {
            this.period = StatsPeriod.DAY;
        } else {
            this.period = StatsPeriod.MONTH;
        }

        if ("t".equals(mode)) {
            this.mode = StatsMode.TIME;
        } else {
            this.mode = StatsMode.CATEGORY;
        }
    }

    /**
     * Executes the relevant steps to display statistics, which can be broken down into two steps.
     * The first step is to call the {@code Model} API to update properties related to statistics like
     * the mode, period and periodAmount.
     * The second step is to post the events in order to show statistics.
     *
     * @param model {@code Model} which the command should operate on.
     * @param history {@code CommandHistory} which the command should operate on.
     * @return {@code CommandResult} with the success message
     * @throws NoUserSelectedException
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        model.updateExpenseStatsPredicate(getStatsPredicate());
        model.updateStatsMode(mode);
        model.updateStatsPeriod(period);
        model.updatePeriodAmount(periodAmount);
        logger.log(Level.INFO,
                "Showing statistics with periodAmount of " + periodAmount
                + " in a period of " + period
                + " in " + mode + " mode.");
        EventsCenter.getInstance().post(new SwapLeftPanelEvent(SwapLeftPanelEvent.PanelType.STATISTIC));
        EventsCenter.getInstance().post(new ShowStatsRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Returns Predicate used for FilteredList based on the current {@code period} and {@code periodAmount}
     * @return Predicate with correct predicate properties
     */
    private Predicate<Expense> getStatsPredicate() {
        LocalDateTime date;
        if (period == StatsPeriod.DAY) {
            date = LocalDateTime.now().minusDays((long) periodAmount);
        } else {
            date = LocalDateTime.now().minusDays((long) periodAmount);
        }
        return e -> e.getDate().getFullDate().isAfter(date);
    }

    private boolean isValidMode(String mode) {
        return "t".equals(mode) || "c".equals(mode);
    }

    private boolean isValidPeriod(String period) {
        return "d".equals(period) || "m".equals(period);
    }

    private boolean isValidPeriodAmount(int periodAmount) {
        return periodAmount > 0;
    }

    /**
     * Checks equality of current {@code StatsCommand} object to another object based on their {@code periodAmount},
     * {@code period} and {@code mode}.
     *
     * @param other Object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StatsCommand // instanceof handles nulls
                && periodAmount == (((StatsCommand) other).periodAmount)
                && period == (((StatsCommand) other).period)
                && mode.equals(((StatsCommand) other).mode));
    }
}
