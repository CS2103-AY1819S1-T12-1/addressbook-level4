package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
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
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withCost("2.00")
            .withCategory("Food")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withCategory("Entertainment")
            .withCost("1.00").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withCategory("Shopping")
            .withCost("2.00").withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withCategory("Tax")
            .withCost("5.00").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withCategory("Book")
            .withCost("6.00").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withCategory("Fine")
            .withCost("7.00").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withCategory("Stock")
            .withCost("1.00").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withCategory("Gamble")
            .withCost("2.00").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withCategory(VALID_PHONE_AMY)
        .withCost(VALID_ADDRESS_AMY)
        .withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withCategory(VALID_PHONE_BOB)
        .withCost(VALID_ADDRESS_BOB)
        .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
        .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

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
