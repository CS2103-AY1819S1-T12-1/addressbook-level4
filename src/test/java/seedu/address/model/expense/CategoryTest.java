package seedu.address.model.expense;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class CategoryTest {
    public static final String INVALID_CATEGORY = "";
    public static final String VALID_CATEGORY = "Test";

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Category(null));
    }

    @Test
    public void constructor_invalidCategory_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Category(INVALID_CATEGORY));
    }

    @Test
    public void isValidCategory() {
        // null category
        Assert.assertThrows(NullPointerException.class, () -> Category.isValidCategory(null));

        // invalid category
        assertFalse(Category.isValidCategory("")); // empty string
        assertFalse(Category.isValidCategory(" ")); // spaces only


        // valid category
        assertTrue(Category.isValidCategory("School"));
        assertTrue(Category.isValidCategory("Oct1"));
        assertTrue(Category.isValidCategory("124293842033123")); // long numbers
    }
}
