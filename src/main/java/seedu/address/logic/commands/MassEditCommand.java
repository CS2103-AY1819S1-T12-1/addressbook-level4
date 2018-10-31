package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.expense.EditExpenseDescriptor.createEditedExpense;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.SwapLeftPanelEvent;
import seedu.address.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.EditExpenseDescriptor;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;

//@@author jcjxwy
/**
 * Edit the details of multiple expenses in expense tracker
 * */
public class MassEditCommand extends Command {
    public static final String COMMAND_WORD = "massedit";
    public static final String COMMAND_ALIAS = "me";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the expense identified "
            + "by keywords entered by the user. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_CATEGORY + "CATEGORY] "
            + "[" + PREFIX_COST + "ADDRESS] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TAG + "TAG]..."
            + " -> "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_CATEGORY + "CATEGORY] "
            + "[" + PREFIX_COST + "ADDRESS] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "lunch -> "
            + PREFIX_CATEGORY + "Lunch ";

    public static final String MESSAGE_EDIT_MULTIPLE_EXPENSE_SUCCESS = "Edit expenses successfully.";
    public static final String MESSAGE_NO_EXPENSE_FOUND = "No expense is found by the keywords.";

    private final ExpenseContainsKeywordsPredicate predicate;
    private final EditExpenseDescriptor editExpenseDescriptor;

    /**
     * @param predicate predicate to filter out the intended expenses
     * @param editExpenseDescriptor details to edit the expense with
     */
    public MassEditCommand(ExpenseContainsKeywordsPredicate predicate, EditExpenseDescriptor editExpenseDescriptor) {
        requireNonNull(predicate);
        requireNonNull(editExpenseDescriptor);

        this.predicate = predicate;
        this.editExpenseDescriptor = editExpenseDescriptor;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException, NoUserSelectedException {
        requireNonNull(model);
        EventsCenter.getInstance().post(new SwapLeftPanelEvent(SwapLeftPanelEvent.PanelType.LIST));
        model.updateFilteredExpenseList(predicate);
        List<Expense> lastShownList = model.getFilteredExpenseList();

        //Throw exception if no expense was found by the keywords
        if (lastShownList.size() == 0) {
            throw new CommandException(MESSAGE_NO_EXPENSE_FOUND);
        }

        //Edit all the filtered expenses
        List<Expense> editedList = new ArrayList<>();
        for (int i = 0; i < lastShownList.size(); i++) {
            Expense expense = lastShownList.get(i);
            Expense editedExpense = createEditedExpense(expense, editExpenseDescriptor);
            model.updateExpense(expense, editedExpense);
            editedList.add(editedExpense);
        }
        //Show the edited expenses to the user
        Predicate<Expense> newPredicate = e -> editedList.stream().anyMatch(newExpense -> e == newExpense);
        model.updateFilteredExpenseList(newPredicate);

        model.commitExpenseTracker();
        EventsCenter.getInstance().post(new UpdateBudgetPanelEvent(model.getMaximumBudget()));

        return new CommandResult(MESSAGE_EDIT_MULTIPLE_EXPENSE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MassEditCommand)) {
            return false;
        }

        // state check
        MassEditCommand e = (MassEditCommand) other;
        return predicate.equals(e.predicate)
                && editExpenseDescriptor.equals(e.editExpenseDescriptor);
    }

}
