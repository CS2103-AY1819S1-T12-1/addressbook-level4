package seedu.address.logic.commands;
//@@author winsonhys
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MINUTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SECONDS;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;


/**
 * Command that sets the recurrence time for the current {@code TotalBudget}
 */
public class SetRecurringBudgetCommand extends Command {
    public static final String COMMAND_WORD = "setRecurrenceFrequency";
    public static final String COMMAND_ALIAS = "srf";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a frequency for the budget to reset "
        + "Parameters: "
        + "[" + PREFIX_HOURS + "HOURS] "
        + "[" + PREFIX_MINUTES + "MINUTES]"
        + "[" + PREFIX_SECONDS + "SECONDS]\n"
        + "Example " + COMMAND_WORD + " " + PREFIX_HOURS + "3";

    public static final String MESSAGE_SUCCESS = "Budget recurrence is set at %s";

    private long toSet;

    /**
     * Creates an AddCommand to add the specified {@code TotalBudget}
     */

    public SetRecurringBudgetCommand(long recurrenceFrequency) {
        requireNonNull(recurrenceFrequency);
        this.toSet = recurrenceFrequency;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        model.setRecurrenceFrequency(this.toSet);
        model.commitExpenseTracker();
        return new CommandResult(String.format(MESSAGE_SUCCESS, this.toSet));
    }

    @Override
    public boolean equals(Object other) {
        SetRecurringBudgetCommand otherCommand = (SetRecurringBudgetCommand) other;
        return otherCommand.toSet == this.toSet;
    }

}
