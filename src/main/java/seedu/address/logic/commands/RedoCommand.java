package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EXPENSES;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;

/**
 * Reverts the {@code model}'s expense tracker to its previously undone state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String COMMAND_ALIAS = "r";
    public static final String MESSAGE_SUCCESS = "Redo success!";
    public static final String MESSAGE_FAILURE = "No more commands to redo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException, NoUserSelectedException {
        requireNonNull(model);

        if (!model.canRedoExpenseTracker()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redoExpenseTracker();
        model.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
        EventsCenter.getInstance().post(new UpdateBudgetPanelEvent(model.getMaximumBudget()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
