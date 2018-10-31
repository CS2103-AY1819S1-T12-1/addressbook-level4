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
 * Reverts the {@code model}'s expense tracker to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String COMMAND_ALIAS = "u";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException, NoUserSelectedException {
        requireNonNull(model);

        if (!model.canUndoExpenseTracker()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoExpenseTracker();
        model.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
        EventsCenter.getInstance().post(new UpdateBudgetPanelEvent(model.getMaximumBudget()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
