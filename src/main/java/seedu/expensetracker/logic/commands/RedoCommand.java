package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.model.Model.PREDICATE_SHOW_ALL_EXPENSES;

import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.expensetracker.commons.events.ui.UpdateCategoriesPanelEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.commands.exceptions.CommandException;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

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
        EventsCenter.getInstance().post(new UpdateCategoriesPanelEvent(model.getCategoryBudgets().iterator()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
