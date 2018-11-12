package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.expensetracker.commons.events.ui.UpdateCategoriesPanelEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

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
        ExpenseTracker newExpenseTracker =
                new ExpenseTracker(model.getExpenseTracker().getUsername(),
                        model.getExpenseTracker().getPassword().orElse(null),
                        model.getExpenseTracker().getEncryptionKey());
        TotalBudget clearedSpendingTotalBudget = model.getMaximumBudget();
        model.resetData(newExpenseTracker);
        clearedSpendingTotalBudget.clearSpending();
        model.modifyMaximumBudget(clearedSpendingTotalBudget);
        model.commitExpenseTracker();
        EventsCenter.getInstance().post(new UpdateCategoriesPanelEvent(model.getCategoryBudgets().iterator()));
        EventsCenter.getInstance().post(new UpdateBudgetPanelEvent(model.getMaximumBudget()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
