package seedu.expensetracker.logic.parser;
//@@author winsonhys

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.expensetracker.model.budget.TotalBudgetTest.INVALID_BUDGET;
import static seedu.expensetracker.model.budget.TotalBudgetTest.VALID_BUDGET;
import static seedu.expensetracker.model.expense.CategoryTest.INVALID_CATEGORY;
import static seedu.expensetracker.model.expense.CategoryTest.VALID_CATEGORY;

import org.junit.Test;

import seedu.expensetracker.logic.commands.SetCategoryBudgetCommand;
import seedu.expensetracker.model.budget.CategoryBudget;


public class SetCategoryBudgetParserTest {
    private SetCategoryBudgetCommandParser parser = new SetCategoryBudgetCommandParser();

    @Test
    public void parse_validCategoryBudget_returnsAddCategoryBudgetCommand() {
        assertParseSuccess(parser, " " + PREFIX_CATEGORY + VALID_CATEGORY
            + " " + PREFIX_BUDGET + VALID_BUDGET, new SetCategoryBudgetCommand(new CategoryBudget(VALID_CATEGORY,
            VALID_BUDGET)));
    }

    @Test
    public void parse_invalidCategoryValidBudget_throwsParseError() {
        assertParseFailure(parser, " " + PREFIX_CATEGORY + INVALID_CATEGORY
            + " " + PREFIX_BUDGET + VALID_BUDGET , String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            SetCategoryBudgetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validCategoryInvalidBudget_throwsParseError() {
        assertParseFailure(parser, " " + PREFIX_CATEGORY + VALID_CATEGORY
            + " " + PREFIX_BUDGET + INVALID_BUDGET, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            SetCategoryBudgetCommand.MESSAGE_USAGE));
    }
}
