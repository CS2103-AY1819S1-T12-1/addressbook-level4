package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_1990;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_2018;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FOOD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.expense.Expense;
import seedu.address.model.user.Username;



/**
 * A utility class containing a list of {@code Expense} objects to be used in tests.
 */
public class TypicalExpenses {
    public static final Username SAMPLE_USERNAME = new Username("sampleData");

    public static final Expense ALICE = new ExpenseBuilder().withName("Alice Pauline")
            .withCost("3.00")
            .withCategory("School")
            .withDate(VALID_DATE_2018)
            .withTags("friends").build();
    public static final Expense BENSON = new ExpenseBuilder().withName("Benson Meier")
            .withCost("2.00")
            .withCategory("Food")
            .withDate(VALID_DATE_2018)
            .withTags("owesMoney", "friends").build();
    public static final Expense CARL = new ExpenseBuilder().withName("Carl Kurz")
            .withCategory("Entertainment")
            .withDate(VALID_DATE_2018)
            .withCost("1.00").build();
    public static final Expense DANIEL = new ExpenseBuilder()
            .withName("Daniel Meier")
            .withCategory("Shopping")
            .withCost("2.00")
            .withDate(VALID_DATE_2018)
            .withTags("friends").build();
    public static final Expense ELLE = new ExpenseBuilder()
            .withName("Elle Meyer")
            .withDate(VALID_DATE_2018)
            .withCategory("Tax")
            .withCost("5.00").build();
    public static final Expense FIONA = new ExpenseBuilder()
            .withName("Fiona Kunz")
            .withDate(VALID_DATE_2018)
            .withCategory("Book")
            .withCost("6.00").build();
    public static final Expense GEORGE = new ExpenseBuilder()
            .withName("George Best")
            .withCategory("Fine")
            .withDate(VALID_DATE_2018)
            .withCost("7.00").build();

    // Manually added
    public static final Expense HOON = new ExpenseBuilder()
            .withName("Hoon Meier")
            .withCategory("Stock")
            .withDate(VALID_DATE_2018)
            .withCost("1.00").build();
    public static final Expense IDA = new ExpenseBuilder()
            .withName("Ida Mueller")
            .withDate(VALID_DATE_2018)
            .withCategory("Gamble")
            .withCost("2.00").build();

    // Manually added - Expense's details found in {@code CommandTestUtil}
    public static final Expense AMY = new ExpenseBuilder().withName(VALID_NAME_AMY).withCategory(VALID_CATEGORY_AMY)
            .withCost(VALID_COST_AMY)
            .withDate(VALID_DATE_1990)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Expense BOB = new ExpenseBuilder().withName(VALID_NAME_BOB).withCategory(VALID_CATEGORY_BOB)
            .withCost(VALID_COST_BOB)
            .withDate(VALID_DATE_2018)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();
    public static final Expense KFC = new ExpenseBuilder().withName(VALID_NAME_KFC).withCategory(VALID_CATEGORY_KFC)
            .withCost(VALID_COST_KFC)
            .withCategory(VALID_CATEGORY_KFC)
            .withTags(VALID_TAG_FOOD).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalExpenses() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical expenses.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook(SAMPLE_USERNAME);
        for (Expense expense : getTypicalExpenses()) {
            ab.addExpense(expense);
        }
        return ab;
    }

    public static List<Expense> getTypicalExpenses() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
