package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_1990;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_2018;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.expense.Person;
import seedu.address.model.user.Username;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {
    public static final Username SAMPLE_USERNAME = new Username("sampleData");

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withCost("3.00")
            .withCategory("School")
            .withDate(VALID_DATE_2018)
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withCost("2.00")
            .withCategory("Food")
            .withDate(VALID_DATE_2018)
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
            .withCategory("Entertainment")
            .withDate(VALID_DATE_2018)
            .withCost("1.00").build();
    public static final Person DANIEL = new PersonBuilder()
            .withName("Daniel Meier")
            .withCategory("Shopping")
            .withCost("2.00")
            .withDate(VALID_DATE_2018)
            .withTags("friends").build();
    public static final Person ELLE = new PersonBuilder()
            .withName("Elle Meyer")
            .withDate(VALID_DATE_2018)
            .withCategory("Tax")
            .withCost("5.00").build();
    public static final Person FIONA = new PersonBuilder()
            .withName("Fiona Kunz")
            .withDate(VALID_DATE_2018)
            .withCategory("Book")
            .withCost("6.00").build();
    public static final Person GEORGE = new PersonBuilder()
            .withName("George Best")
            .withCategory("Fine")
            .withDate(VALID_DATE_2018)
            .withCost("7.00").build();

    // Manually added
    public static final Person HOON = new PersonBuilder()
            .withName("Hoon Meier")
            .withCategory("Stock")
            .withDate(VALID_DATE_2018)
            .withCost("1.00").build();
    public static final Person IDA = new PersonBuilder()
            .withName("Ida Mueller")
            .withDate(VALID_DATE_2018)
            .withCategory("Gamble")
            .withCost("2.00").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withCategory(VALID_CATEGORY_AMY)
            .withCost(VALID_COST_AMY)
            .withDate(VALID_DATE_1990)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withCategory(VALID_CATEGORY_BOB)
            .withCost(VALID_COST_BOB)
            .withDate(VALID_DATE_2018)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook(SAMPLE_USERNAME);
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
