package seedu.expensetracker.logic.parser;
//@@author winsonhys

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.commons.exceptions.NegativeValueParseException.NEGATIVE_EXCEPTION_MESSAGE;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.expensetracker.model.budget.TotalBudgetTest.INVALID_BUDGET;
import static seedu.expensetracker.model.budget.TotalBudgetTest.NEGATIVE_BUDGET;
import static seedu.expensetracker.model.budget.TotalBudgetTest.VALID_BUDGET;

import org.junit.Test;

import seedu.expensetracker.logic.commands.SetBudgetCommand;
import seedu.expensetracker.model.budget.TotalBudget;



public class SetBudgetCommandParserTest {
    private SetBudgetCommandParser parser = new SetBudgetCommandParser();

    @Test
    public void parse_validBudget_returnsSetBudgetCommand() {
        assertParseSuccess(parser, " " + VALID_BUDGET, new SetBudgetCommand(new TotalBudget(VALID_BUDGET)));
    }

    @Test
    public void parse_invalidBudget_throwsParseException() {
        assertParseFailure(parser, " " + INVALID_BUDGET, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            SetBudgetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidBudget_throwNegativeValueParseException() {
        assertParseFailure(parser, " " + NEGATIVE_BUDGET, NEGATIVE_EXCEPTION_MESSAGE);
    }

}
