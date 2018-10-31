package seedu.address.logic.parser;
//@@author winsonhys

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.budget.TotalBudgetTest.INVALID_BUDGET;
import static seedu.address.model.budget.TotalBudgetTest.VALID_BUDGET;
import static seedu.address.model.expense.CategoryTest.INVALID_CATEGORY;
import static seedu.address.model.expense.CategoryTest.VALID_CATEGORY;

import org.junit.Test;

import seedu.address.logic.commands.AddCategoryBudgetCommand;
import seedu.address.model.budget.CategoryBudget;


public class AddCategoryBudgetParserTest {
    private AddCategoryBudgetCommandParser parser = new AddCategoryBudgetCommandParser();

    @Test
    public void parse_validCategoryBudget_returnsAddCategoryBudgetCommand() {
        assertParseSuccess(parser, " " + PREFIX_CATEGORY + VALID_CATEGORY
            + " " + PREFIX_BUDGET + VALID_BUDGET, new AddCategoryBudgetCommand(new CategoryBudget(VALID_CATEGORY,
            VALID_BUDGET)));
    }

    @Test
    public void parse_invalidCategoryValidBudget_throwsParseError() {
        assertParseFailure(parser, " " + PREFIX_CATEGORY + INVALID_CATEGORY
            + " " + PREFIX_BUDGET + VALID_BUDGET , String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            AddCategoryBudgetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validCategoryInvalidBudget_throwsParseError() {
        assertParseFailure(parser, " " + PREFIX_CATEGORY + VALID_CATEGORY
            + " " + PREFIX_BUDGET + INVALID_BUDGET, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            AddCategoryBudgetCommand.MESSAGE_USAGE));
    }
}
