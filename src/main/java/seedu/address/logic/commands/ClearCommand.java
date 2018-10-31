package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.ExpenseTracker;
import seedu.address.model.Model;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.exceptions.NoUserSelectedException;

/**
 * Clears the expense tracker.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_ALIAS = "c";
    public static final String MESSAGE_SUCCESS = "Expense tracker has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        // TODO: Move code to Model class for better adherence to OOP principles
        ExpenseTracker newExpenseTracker =
                new ExpenseTracker(model.getExpenseTracker().getUsername(),
                        model.getExpenseTracker().getPassword().orElse(null),
                        model.getExpenseTracker().getEncryptionKey());
        TotalBudget clearedSpendingTotalBudget = model.getMaximumBudget();
        model.resetData(newExpenseTracker);
        clearedSpendingTotalBudget.clearSpending();
        model.modifyMaximumBudget(clearedSpendingTotalBudget);
        model.commitExpenseTracker();
        EventsCenter.getInstance().post(new UpdateBudgetPanelEvent(model.getMaximumBudget()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
