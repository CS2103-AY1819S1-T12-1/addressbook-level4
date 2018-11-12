package seedu.expensetracker.model.expense;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;

import org.junit.Test;

import seedu.expensetracker.logic.parser.ArgumentMultimap;
import seedu.expensetracker.logic.parser.ArgumentTokenizer;
import seedu.expensetracker.testutil.ExpenseBuilder;

//@@author jcjxwy
public class ExpenseContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        ArgumentMultimap firstPredicateKeywordMap = prepareKeywords("n/first");
        ArgumentMultimap secondPredicateKeywordMap = prepareKeywords("n/second");

        ExpenseContainsKeywordsPredicate firstPredicate =
                new ExpenseContainsKeywordsPredicate(firstPredicateKeywordMap);
        ExpenseContainsKeywordsPredicate secondPredicate =
                new ExpenseContainsKeywordsPredicate(secondPredicateKeywordMap);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ExpenseContainsKeywordsPredicate firstPredicateCopy =
                new ExpenseContainsKeywordsPredicate(firstPredicateKeywordMap);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different expense -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        ArgumentMultimap keywordsMap = prepareKeywords("n/Alice");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        keywordsMap = prepareKeywords("n/Alice Bob");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withName("Alice Bob").build()));

        // Only one matching name keyword
        keywordsMap = prepareKeywords("n/Alice Carol");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        keywordsMap = prepareKeywords("n/ALicE BOb");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        ArgumentMultimap keywordsMap = prepareKeywords("n/");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withName("Alice").build()));

        // Non-matching keyword
        keywordsMap = prepareKeywords("n/Have Lunch");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withName("Drink").build()));

        // Name Keywords match category and expensetracker, but does not match name
        keywordsMap = prepareKeywords("n/12345 2.00");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withName("Alice").withCategory("12345")
                .withCost("2.00").build()));
    }

    @Test
    public void test_tagContainKeywords_returnTrue() {
        // One tag keyword, one tag
        ArgumentMultimap keywordsMap = prepareKeywords("t/Lunch");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withTags("Lunch").build()));

        // One tag keywords, multiple tags
        assertTrue(predicate.test(new ExpenseBuilder().withTags("Lunch", "Cheap").build()));

        // Multiple tag keywords, multiple tags, all match
        keywordsMap = prepareKeywords("t/Lunch t/Cheap");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withTags("Cheap", "Lunch").build()));

        // Multiple tag keywords, multiple tags, one match
        assertTrue(predicate.test(new ExpenseBuilder().withTags("Nice", "Lunch").build()));

    }

    @Test
    public void test_tagDoesNotContainKeywords_returnFalse() {
        // One tag keyword, one tag
        ArgumentMultimap keywordsMap = prepareKeywords("t/Dinner");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withTags("Lunch").build()));

        // One tag keywords, multiple tags
        assertFalse(predicate.test(new ExpenseBuilder().withTags("Lunch", "Cheap").build()));

        // Multiple tag keywords, multiple tags, all don't match
        keywordsMap = prepareKeywords("t/Dinner t/Expensive");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withTags("Cheap", "Lunch").build()));

    }

    @Test
    public void test_categoryContainKeywords_returnTrue() {
        // One keyword
        ArgumentMultimap keywordsMap = prepareKeywords("c/School");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withCategory("School").build()));

        // Multiple keywords
        keywordsMap = prepareKeywords("c/Have fun");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withCategory("Have fun").build()));

        keywordsMap = ArgumentTokenizer.tokenize(" c/Play with friends", PREFIX_CATEGORY);
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withCategory("Play with friends").build()));

        // Only one matching name keyword
        keywordsMap = prepareKeywords("c/Buy books");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withCategory("Buy lunch").build()));

        // Mixed-case keywords
        keywordsMap = prepareKeywords("c/FOod");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withCategory("food").build()));
    }

    @Test
    public void test_categoryDoesNotContainKeywords_returnFalse() {
        // One keyword
        ArgumentMultimap keywordsMap = prepareKeywords("c/Food");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withCategory("School").build()));

        // Multiple keywords
        keywordsMap = prepareKeywords("c/Have fun");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withCategory("Buy books").build()));

        keywordsMap = prepareKeywords("c/Play with friends");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withCategory("Buy books").build()));

    }

    @Test
    public void test_dateWithinRange_returnTrue() {
        // One keyword
        ArgumentMultimap keywordsMap = prepareKeywords("d/01-10-2018");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withDate("01-10-2018").build()));

        // Multiple keywords
        keywordsMap = prepareKeywords("d/01-10-2018:07-10-2018");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withDate("05-10-2018").build()));

        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withDate("07-10-2018").build()));

    }

    @Test
    public void test_dateOutsideRange_returnTrue() {
        // One keyword
        ArgumentMultimap keywordsMap = prepareKeywords("d/02-10-2018");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withDate("01-10-2018").build()));

        // Multiple keywords
        keywordsMap = prepareKeywords("d/01-10-2018:07-10-2018");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withDate("08-10-2018").build()));

    }

    @Test
    public void test_costWithinRange_returnTrue() {
        // One keyword
        ArgumentMultimap keywordsMap = prepareKeywords("$/2.00");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withCost("2.00").build()));

        // Multiple keywords
        keywordsMap = prepareKeywords("$/1.00:5.00");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withCost("3.00").build()));
        assertTrue(predicate.test(new ExpenseBuilder().withCost("5.00").build()));

    }

    @Test
    public void test_costOutsideRange_returnTrue() {
        // One keyword
        ArgumentMultimap keywordsMap = prepareKeywords("$/3.00");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withCost("2.00").build()));

        // Multiple keywords
        keywordsMap = prepareKeywords("$/1.00:5.00");
        predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withCost("6.00").build()));
        assertFalse(predicate.test(new ExpenseBuilder().withCost("0.50").build()));

    }

    @Test
    public void test_expenseMatchesMultipleKeywords_returnTrue() {
        //Multiple keywords, all match
        ArgumentMultimap keywordsMap = prepareKeywords("n/Have Lunch c/Food d/01-02-2018:28-02-2018");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertTrue(predicate.test(new ExpenseBuilder().withName("Have Lunch").withCategory("Food")
                .withDate("02-02-2018").build()));
        assertTrue(predicate.test(new ExpenseBuilder().withName("Have Lunch").withCategory("Food")
                .withDate("20-02-2018").withCost("123.00").build()));
    }

    @Test
    public void test_expenseDoesNotMatchAllKeywords_returnFalse() {
        //Only one keyword matches
        ArgumentMultimap keywordsMap = prepareKeywords("n/Have Lunch c/Food d/01-02-2018:28-02-2018");
        ExpenseContainsKeywordsPredicate predicate =
                new ExpenseContainsKeywordsPredicate(keywordsMap);
        assertFalse(predicate.test(new ExpenseBuilder().withName("Drink").withCategory("Food")
                .withDate("02-01-2018").build()));

        //Some keywords match but not all
        assertFalse(predicate.test(new ExpenseBuilder().withName("Drink").withCategory("Food")
                .withDate("20-02-2018").build()));

    }


    /**
     * Returns an {@code ArgumentMultiMap} which tokenize the {@code arg} based on prefixes.
     * */
    public ArgumentMultimap prepareKeywords(String arg) {
        return ArgumentTokenizer.tokenize(" " + arg,
                PREFIX_NAME, PREFIX_CATEGORY, PREFIX_COST, PREFIX_TAG, PREFIX_DATE);
    }

}
