package seedu.address.model.expense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_GAME;
import static seedu.address.logic.commands.CommandTestUtil.DESC_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditExpenseDescriptorBuilder;
public class EditExpenseDescriptorTest {

    @Test
    public void test_equals() {
        // same values -> returns true
        EditExpenseDescriptor descriptorWithSameValues = new EditExpenseDescriptor(DESC_GAME);
        assertTrue(DESC_GAME.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_GAME.equals(DESC_GAME));

        // null -> returns false
        assertFalse(DESC_GAME.equals(null));

        // different types -> returns false
        assertFalse(DESC_GAME.equals(5));

        // different values -> returns false
        assertFalse(DESC_GAME.equals(DESC_IPHONE));

        // different name -> returns false
        EditExpenseDescriptor editedAmy =
                new EditExpenseDescriptorBuilder(DESC_GAME).withName(VALID_NAME_IPHONE).build();
        assertFalse(DESC_GAME.equals(editedAmy));

        // different category -> returns false
        editedAmy = new EditExpenseDescriptorBuilder(DESC_GAME).withCategory(VALID_CATEGORY_IPHONE).build();
        assertFalse(DESC_GAME.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditExpenseDescriptorBuilder(DESC_GAME).withCost(VALID_COST_IPHONE).build();
        assertFalse(DESC_GAME.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditExpenseDescriptorBuilder(DESC_GAME).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_GAME.equals(editedAmy));
    }

    //@@author jcjxwy
    @Test
    public void test_createEditExpenseDescriptor_successful() {
        ArgumentMultimap testMap = prepareMap("n/test");
        try {
            EditExpenseDescriptor test = EditExpenseDescriptor.createEditExpenseDescriptor(testMap);
            assertTrue(test.getName().get().equals(new Name("test")));
        } catch (ParseException pe) {
            pe.printStackTrace();
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }

        testMap = prepareMap("c/test $/1.00");
        try {
            EditExpenseDescriptor test = EditExpenseDescriptor.createEditExpenseDescriptor(testMap);
            assertTrue(test.getCategory().get().equals(new Category("test")));
            assertTrue(test.getCost().get().equals(new Cost("1.00")));
        } catch (ParseException pe) {
            pe.printStackTrace();
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }

        testMap = prepareMap("n/same c/test $/1.00 t/equal d/01-01-2018");
        try {
            EditExpenseDescriptor test = EditExpenseDescriptor.createEditExpenseDescriptor(testMap);
            assertTrue(test.getCategory().get().equals(new Category("test")));
            assertTrue(test.getName().get().equals(new Name("same")));
            assertTrue(test.getCost().get().equals(new Cost("1.00")));
            assertTrue(test.getDate().get().equals(new Date("01-01-2018")));
            Set<Tag> tags = new HashSet<>();
            tags.add(new Tag("equal"));
            assertTrue(test.getTags().get().equals(tags));
        } catch (ParseException pe) {
            pe.printStackTrace();
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }
    }

    @Test
    public void test_createEditExpenseDescriptor_fail() {
        ArgumentMultimap testMap = prepareMap("");
        try {
            EditExpenseDescriptor.createEditExpenseDescriptor(testMap);
        } catch (ParseException pe) {
            assertEquals(pe.getMessage(), EditCommand.MESSAGE_NOT_EDITED);
        }
    }

    /**
     * Parses the input and stores the keywords in {@code ArgumentMultimap}
     * */
    private ArgumentMultimap prepareMap(String input) {
        String preparedInput = " " + input.trim();
        ArgumentMultimap map = ArgumentTokenizer.tokenize(preparedInput,
                PREFIX_NAME, PREFIX_CATEGORY, PREFIX_COST, PREFIX_DATE, PREFIX_TAG);
        return map;
    }
}
