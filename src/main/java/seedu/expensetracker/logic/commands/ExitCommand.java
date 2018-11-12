package seedu.expensetracker.logic.commands;

import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.events.ui.ExitAppRequestEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";
    public static final String COMMAND_ALIAS = "x";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Expense Tracker as requested ...";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        EventsCenter.getInstance().post(new ExitAppRequestEvent());
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

}
