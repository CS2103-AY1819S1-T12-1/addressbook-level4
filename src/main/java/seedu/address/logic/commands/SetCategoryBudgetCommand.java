package seedu.address.logic.commands;
//@@author winsonhys

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.budget.CategoryBudget;
import seedu.address.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.address.model.exceptions.NoUserSelectedException;

/**
 * Adds a Category TotalBudget into the expense tracker.
 */

public class SetCategoryBudgetCommand extends Command {
    public static final String COMMAND_WORD = "setCategoryBudget";
    public static final String COMMAND_ALIAS = "scb";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a Category Budget cap for one of the "
        + "Categories in the expense tracker "
        + "Parameters: " + PREFIX_CATEGORY + "CATEGORY " + PREFIX_BUDGET + "MONEY (Must be a positive float with 2 "
        + "decimal places)\n"
        + "Example " + COMMAND_WORD + " " + PREFIX_CATEGORY + "School " + PREFIX_BUDGET + "130.00";

    public static final String MESSAGE_SUCCESS = "%s TotalBudget set to %1$s";

    public static final String EXCEED_MESSAGE = "The sum of your category budgets cannot exceed your total budget";

    private CategoryBudget toSet;

    public SetCategoryBudgetCommand(CategoryBudget budget) {
        requireNonNull(budget);
        this.toSet = budget;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws NoUserSelectedException, CommandException {
        try {
            requireNonNull(model);
            model.setCategoryBudget(this.toSet);
            model.commitExpenseTracker();
            return new CommandResult(String.format(MESSAGE_SUCCESS, this.toSet.getCategory(), this.toSet));
        } catch (CategoryBudgetExceedTotalBudgetException catBudExc) {
            throw new CommandException(EXCEED_MESSAGE);
        }
    }

    @Override

    public boolean equals (Object budget) {
        SetCategoryBudgetCommand cBudgetCommand = (SetCategoryBudgetCommand) budget;
        return this.toSet.equals(cBudgetCommand.toSet);
    }


}
