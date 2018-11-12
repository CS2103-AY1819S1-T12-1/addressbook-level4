package seedu.expensetracker.testutil;

import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_KFC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_KFC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_DATE_1990;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_DATE_2018;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_GAME;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_FOOD;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.model.user.Username;

/**
 * A utility class containing a list of {@code Expense} objects to be used in tests.
 */
public class TypicalExpenses {
    public static final Username SAMPLE_USERNAME = new Username("sampleData");
    public static final double INTIIAL_EXPENSES = 26.00;
    public static final double INTIIAL_BUDGET = 28.00;

    public static final Expense SCHOOLFEE = new ExpenseBuilder().withName("School fee")
            .withCost("3.00")
            .withCategory("School")
            .withDate(VALID_DATE_2018)
            .withTags("friends").build();
    public static final Expense ICECREAM = new ExpenseBuilder().withName("Eat ice cream")
            .withCost("2.00")
            .withCategory("Food")
            .withDate("02-10-2018")
            .withTags("owesMoney", "friends").build();
    public static final Expense TOY = new ExpenseBuilder().withName("Buy toy")
            .withCategory("Entertainment")
            .withDate("03-10-2018")
            .withCost("1.00").build();
    public static final Expense CLOTHES = new ExpenseBuilder()
            .withName("New clothes")
            .withCategory("Shopping")
            .withCost("2.00")
            .withDate("04-10-2018")
            .withTags("friends").build();
    public static final Expense TAX = new ExpenseBuilder()
            .withName("Pay tax")
            .withDate("05-10-2018")
            .withCategory("Tax")
            .withCost("5.00").build();
    public static final Expense BOOKS = new ExpenseBuilder()
            .withName("Buy books")
            .withDate("06-10-2018")
            .withCategory("Book")
            .withCost("6.00").build();
    public static final Expense LUNCH = new ExpenseBuilder()
            .withName("Have lunch")
            .withCategory("Food")
            .withDate(VALID_DATE_2018)
            .withCost("7.00").build();

    // Manually added
    public static final Expense STOCK = new ExpenseBuilder()
            .withName("Buy stock")
            .withCategory("Stock")
            .withDate(VALID_DATE_2018)
            .withCost("1.00").build();
    public static final Expense GAMBLE = new ExpenseBuilder()
            .withName("Try my luck")
            .withDate(VALID_DATE_2018)
            .withCategory("Gamble")
            .withCost("2.00").build();

    // Manually added - Expense's details found in {@code CommandTestUtil}
    public static final Expense GAME = new ExpenseBuilder().withName(VALID_NAME_GAME).withCategory(VALID_CATEGORY_GAME)
            .withCost(VALID_COST_GAME)
            .withDate(VALID_DATE_1990)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Expense IPHONE =
            new ExpenseBuilder().withName(VALID_NAME_IPHONE).withCategory(VALID_CATEGORY_IPHONE)
            .withCost(VALID_COST_IPHONE)
            .withDate(VALID_DATE_2018)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();
    public static final Expense KFC = new ExpenseBuilder().withName(VALID_NAME_KFC).withCategory(VALID_CATEGORY_KFC)
            .withCost(VALID_COST_KFC)
            .withCategory(VALID_CATEGORY_KFC)
            .withTags(VALID_TAG_FOOD).build();

    public static final String KEYWORD_MATCHING_BUY = "n/Buy"; // A keyword that matches Buy
    public static final String KEYWORD_MATCHING_FOOD = "c/Food"; //A keyword that matches Food category
    public static final String KEYWORD_MATCHING_LUNCH = "n/Lunch"; //A keyword that matches Lunch

    private TypicalExpenses() {
    } // prevents instantiation

    /**
     * Returns an {@code ExpenseTracker} with all the typical expenses
     * and its maximmum totalBudget equal to the sum of all expenses.
     */
    public static ExpenseTracker getTypicalExpenseTracker() {
        double expense = 0;
        ExpenseTracker ab = new ExpenseTracker(SAMPLE_USERNAME, null, DEFAULT_ENCRYPTION_KEY);
        for (Expense e : getTypicalExpenses()) {
            ab.addExpense(e);
            expense += e.getCost().getCostValue();
        }
        ab.setNotificationHandler(new NotificationHandler(LocalDateTime.parse("2018-10-01T17:26:18.868275"),
                true, true));
        ab.modifyMaximumBudget(new TotalBudget(expense + 2, expense, LocalDateTime.parse("2017-08-04T10:11:30"),
            50000));

        return ab;
    }

    public static List<Expense> getTypicalExpenses() {
        return new ArrayList<>(Arrays.asList(SCHOOLFEE, ICECREAM, TOY, CLOTHES, TAX, BOOKS, LUNCH));
    }
}
