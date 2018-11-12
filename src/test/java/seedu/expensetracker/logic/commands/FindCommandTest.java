package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.commons.core.Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW;
import static seedu.expensetracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;
import static seedu.expensetracker.testutil.TypicalExpenses.BOOKS;
import static seedu.expensetracker.testutil.TypicalExpenses.CLOTHES;
import static seedu.expensetracker.testutil.TypicalExpenses.ICECREAM;
import static seedu.expensetracker.testutil.TypicalExpenses.LUNCH;
import static seedu.expensetracker.testutil.TypicalExpenses.SCHOOLFEE;
import static seedu.expensetracker.testutil.TypicalExpenses.TAX;
import static seedu.expensetracker.testutil.TypicalExpenses.TOY;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.parser.ArgumentMultimap;
import seedu.expensetracker.logic.parser.ArgumentTokenizer;
import seedu.expensetracker.logic.parser.Prefix;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.expense.ExpenseContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = getTypicalModel();
    private Model expectedModel = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        ArgumentMultimap firstMap = ArgumentTokenizer.tokenize(" n/first", PREFIX_NAME);
        ArgumentMultimap secondMap = ArgumentTokenizer.tokenize(" n/second", PREFIX_NAME);
        ExpenseContainsKeywordsPredicate firstPredicate =
                new ExpenseContainsKeywordsPredicate(firstMap);
        ExpenseContainsKeywordsPredicate secondPredicate =
                new ExpenseContainsKeywordsPredicate(secondMap);

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different expense -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noExpenseFound() throws NoUserSelectedException {
        testFindCommand(" n/ ", 0,
                Collections.emptyList(), PREFIX_NAME);
    }

    @Test
    public void execute_multipleNameKeywords_multipleExpensesFound() throws NoUserSelectedException {
        testFindCommand(" n/toy tax books", 3,
                Arrays.asList(TOY, TAX, BOOKS), PREFIX_NAME);
    }

    @Test
    public void execute_oneCategoryKeyword_multipleExpensesFound() throws NoUserSelectedException {
        testFindCommand(" c/Food", 2,
                Arrays.asList(ICECREAM, LUNCH), PREFIX_CATEGORY);
    }

    @Test
    public void execute_multipleTagKeyword_multipleExpensesFound() throws NoUserSelectedException {
        testFindCommand(" t/Friends", 3,
                Arrays.asList(SCHOOLFEE, ICECREAM, CLOTHES), PREFIX_TAG);
    }

    @Test
    public void execute_oneDateKeyword_oneExpensesFound() throws NoUserSelectedException {
        testFindCommand(" d/03-10-2018", 1,
                Arrays.asList(TOY), PREFIX_DATE);
    }

    @Test
    public void execute_multipleDateKeyword_multipleExpensesFound() throws NoUserSelectedException {
        testFindCommand(" d/02-10-2018:04-10-2018", 5,
                Arrays.asList(SCHOOLFEE, ICECREAM, TOY, CLOTHES, LUNCH), PREFIX_DATE);
    }

    @Test
    public void execute_oneCostKeyword_oneExpensesFound() throws NoUserSelectedException {
        testFindCommand(" $/1.00", 1,
                Arrays.asList(TOY), PREFIX_COST);
    }

    @Test
    public void execute_multipleCostKeyword_multipleExpensesFound() throws NoUserSelectedException {
        testFindCommand(" $/1.00:3.00", 4,
                Arrays.asList(SCHOOLFEE, ICECREAM, TOY, CLOTHES), PREFIX_COST);
    }

    @Test
    public void execute_multipleKeyword_oneExpensesFound() throws NoUserSelectedException {
        testFindCommand(" t/Friends c/School", 1,
                Arrays.asList(SCHOOLFEE), PREFIX_TAG, PREFIX_CATEGORY);
    }

    @Test
    public void execute_multipleKeyword_multipleExpensesFound() throws NoUserSelectedException {
        testFindCommand(" t/Friends $/2.00:3.00", 3,
                Arrays.asList(SCHOOLFEE, ICECREAM, CLOTHES), PREFIX_TAG, PREFIX_COST);
    }

    /**
     * Test whether the find command is executed successfully
     * */
    private void testFindCommand(String input, int expectedNumber, List<Expense> expenses, Prefix... prefixes)
            throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, expectedNumber);
        ArgumentMultimap keywordsMap =
                ArgumentTokenizer.tokenize(input, prefixes);
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(expenses, model.getFilteredExpenseList());
    }

}
