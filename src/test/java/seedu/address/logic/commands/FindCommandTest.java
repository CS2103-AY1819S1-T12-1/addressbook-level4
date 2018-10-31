package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.ModelUtil.getTypicalModel;
import static seedu.address.testutil.TypicalExpenses.BOOKS;
import static seedu.address.testutil.TypicalExpenses.CLOTHES;
import static seedu.address.testutil.TypicalExpenses.ICECREAM;
import static seedu.address.testutil.TypicalExpenses.LUNCH;
import static seedu.address.testutil.TypicalExpenses.SCHOOLFEE;
import static seedu.address.testutil.TypicalExpenses.TAX;
import static seedu.address.testutil.TypicalExpenses.TOY;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;

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
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 0);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("n/ ", PREFIX_NAME);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredExpenseList());
    }

    @Test
    public void execute_multipleNameKeywords_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 3);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("n/toy tax books", PREFIX_NAME);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(TOY, TAX, BOOKS), model.getFilteredExpenseList());
    }

    @Test
    public void execute_oneCategoryKeyword_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 2);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("c/Food", PREFIX_CATEGORY);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ICECREAM, LUNCH), model.getFilteredExpenseList());
    }

    @Test
    public void execute_multipleTagKeyword_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 3);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("t/Friends", PREFIX_TAG);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SCHOOLFEE, ICECREAM, CLOTHES), model.getFilteredExpenseList());
    }

    @Test
    public void execute_oneDateKeyword_oneExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 1);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("d/03-10-2018", PREFIX_DATE);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(TOY), model.getFilteredExpenseList());
    }

    @Test
    public void execute_multipleDateKeyword_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 5);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("d/02-10-2018:04-10-2018", PREFIX_DATE);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SCHOOLFEE, ICECREAM, TOY, CLOTHES, LUNCH), model.getFilteredExpenseList());
    }

    @Test
    public void execute_oneCostKeyword_oneExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 1);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("$/1.00", PREFIX_COST);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(TOY), model.getFilteredExpenseList());
    }

    @Test
    public void execute_multipleCostKeyword_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 4);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("$/1.00:3.00", PREFIX_COST);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SCHOOLFEE, ICECREAM, TOY, CLOTHES), model.getFilteredExpenseList());
    }

    @Test
    public void execute_multipleKeyword_oneExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 1);
        ArgumentMultimap keywordsMap =
                ArgumentTokenizer.tokenize(" t/Friends c/School", PREFIX_TAG, PREFIX_CATEGORY);
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SCHOOLFEE), model.getFilteredExpenseList());
    }

    @Test
    public void execute_multipleKeyword_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 3);
        ArgumentMultimap keywordsMap =
                ArgumentTokenizer.tokenize(" t/Friends $/2.00:3.00", PREFIX_TAG, PREFIX_COST);
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SCHOOLFEE, ICECREAM, CLOTHES), model.getFilteredExpenseList());
    }

    /**
     * Parses {@code userInput} into a {@code ExpenseContainsKeywordsPredicate}.
     */
    private ExpenseContainsKeywordsPredicate preparePredicate(String userInput, Prefix prefix) {
        ArgumentMultimap keywordsMap = ArgumentTokenizer.tokenize(" " + userInput, prefix);
        return new ExpenseContainsKeywordsPredicate(keywordsMap);
    }
}
