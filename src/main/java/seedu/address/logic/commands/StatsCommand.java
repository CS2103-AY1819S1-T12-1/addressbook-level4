package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Calendar;
import java.util.function.Predicate;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowStatsRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.Expense;

//@@author jonathantjm
/**
 * Lists all expenses in the address book to the user.
 */
public class StatsCommand extends Command {

    public static final String COMMAND_WORD = "stats";
    public static final String COMMAND_ALIAS = "st";

    public static final String MESSAGE_SUCCESS = "Opened the stats window";


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        model.updateExpenseStats(isPastSevenDays());
        EventsCenter.getInstance().post(new ShowStatsRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    private Predicate<Expense> isPastSevenDays() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -7);
        return e -> e.getDate().fullDate.after(now);
    }
}
