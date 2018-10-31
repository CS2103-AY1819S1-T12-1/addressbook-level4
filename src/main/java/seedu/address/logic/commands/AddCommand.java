package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.SwapLeftPanelEvent;
import seedu.address.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.Expense;

/**
 * Adds a expense to the expense tracker.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an expense to the expense tracker. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_CATEGORY + "CATEGORY "
            + PREFIX_COST + "ADDRESS "
            + "[" + PREFIX_DATE + "DATE]"
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Stationery "
            + PREFIX_CATEGORY + "School "
            + PREFIX_COST + "1.00 "
            + PREFIX_TAG + "bookhaven "
            + PREFIX_TAG + "nus";

    public static final String MESSAGE_SUCCESS = "New expense added: %1$s";
    public static final String MESSAGE_DUPLICATE_EXPENSE = "This expense already exists in the expense tracker";
    //TODO: Redirect this to notification center
    public static final String MESSAGE_BUDGET_EXCEED_WARNING = "WARNING: "
        + "Adding this expense will cause your totalBudget to exceed.";

    private final Expense toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Expense}
     */
    public AddCommand(Expense expense) {
        requireNonNull(expense);
        toAdd = expense;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException, NoUserSelectedException {
        requireNonNull(model);
        EventsCenter.getInstance().post(new SwapLeftPanelEvent(SwapLeftPanelEvent.PanelType.LIST));
        if (model.hasExpense(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_EXPENSE);
        }
        boolean withinBudget = model.addExpense(toAdd);
        model.addWarningNotification();
        model.commitExpenseTracker();
        EventsCenter.getInstance().post(new UpdateBudgetPanelEvent(model.getMaximumBudget()));
        if (withinBudget) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        }
        return new CommandResult(MESSAGE_BUDGET_EXCEED_WARNING);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
