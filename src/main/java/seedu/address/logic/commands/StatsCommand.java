package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD_AMOUNT;

import java.util.Calendar;
import java.util.function.Predicate;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowStatsRequestEvent;
import seedu.address.commons.events.ui.SwapLeftPanelEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.Expense;

//@@author jonathantjm
/**
 * Opens up the stats window for the user.
 */
public class StatsCommand extends Command {

    public static final String COMMAND_WORD = "stats";
    public static final String COMMAND_ALIAS = "st";


    public static final String MESSAGE_SUCCESS = "updated the stats panel";

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

    private int periodAmount;
    private StatsPeriod period;
    private StatsMode mode;

    public StatsCommand(int periodAmount, String period, String mode) {
        requireNonNull(periodAmount, mode);
        checkArgument(isValidMode(mode), MESSAGE_PARAMETERS_FORMAT);
        checkArgument(isValidPeriod(period), MESSAGE_PARAMETERS_FORMAT);
        checkArgument(isValidNumber(periodAmount), MESSAGE_PARAMETERS_FORMAT);
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

    public StatsCommand() {
        this(7, "d", "t");
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        model.updateExpenseStatsPredicate(getStatsPredicate());
        model.updateStatsMode(this.mode);
        model.updateStatsPeriod(this.period);
        model.updatePeriodAmount(this.periodAmount);
        EventsCenter.getInstance().post(new SwapLeftPanelEvent(SwapLeftPanelEvent.PanelType.STATISTIC));
        EventsCenter.getInstance().post(new ShowStatsRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Returns Predicate used for FilteredList based on the current statsMode
     * @return Predicate
     */
    private Predicate<Expense> getStatsPredicate() {
        Calendar now = Calendar.getInstance();
        if (this.period == StatsPeriod.DAY) {
            now.add(Calendar.DAY_OF_MONTH, this.periodAmount * -1);
        } else {
            now.add(Calendar.MONTH, this.periodAmount * -1);
        }
        return e -> e.getDate().fullDate.after(now);
    }

    private boolean isValidMode(String mode) {
        return "t".equals(mode) || "c".equals(mode);
    }

    private boolean isValidPeriod(String period) {
        return "d".equals(period) || "m".equals(period);
    }

    private boolean isValidNumber(int num) {
        return num > 0;
    }

    /**
     * Checks equality of current object to another object
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
