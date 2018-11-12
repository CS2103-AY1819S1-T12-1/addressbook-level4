package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.core.Messages;
import seedu.expensetracker.commons.events.ui.SwapLeftPanelEvent;
import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.ExpenseContainsKeywordsPredicate;

/**
 * Finds and lists all expenses in expense tracker whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "f";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all expenses contain "
            + "the specified keywords (case-in"
            + "sensitive) and displays them as a list with index numbers. "
            + "The keywords do not need to be in order.\n"
            + "Parameters: [n/NAME] [c/CATEGORY] [$/COST] [t/TAG] [d/DATE:DATE]...\n"
            + "Example: " + COMMAND_WORD + " n/Have KFC c/Food t/lunch $/2.00:10.00 d/01-01-2018:10-01-2018";

    public static final String MESSAGE_INVALID_RANGE =
            "The lower bound should not be larger than higher bound.";
    public static final String MESSAGE_INVALID_DATE_KEYWORDS_FORMAT =
            "The correct format is d/DATE:DATE or d/DATE. Example: d/01-01-2018:10-01-2018";
    public static final String MESSAGE_INVALID_COST_KEYWORDS_FORMAT =
            "The correct format is $/COST:COST or $/COST. Example: $/1.00:10.00";
    public static final String MESSAGE_MULTIPLE_KEYWORDS =
            "The command should not contain more than one name/category/date/cost keyword.";

    private final ExpenseContainsKeywordsPredicate predicate;

    public FindCommand(ExpenseContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException {
        requireNonNull(model);
        EventsCenter.getInstance().post(new SwapLeftPanelEvent(SwapLeftPanelEvent.PanelType.LIST));
        model.updateFilteredExpenseList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW, model.getFilteredExpenseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
