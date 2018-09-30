package seedu.address.model.expense;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class CategoryTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Category(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Category(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        Assert.assertThrows(NullPointerException.class, () -> Category.isValidCategory(null));

        // invalid phone numbers
        assertFalse(Category.isValidCategory("")); // empty string
        assertFalse(Category.isValidCategory(" ")); // spaces only
        assertFalse(Category.isValidCategory("91")); // less than 3 numbers
        assertFalse(Category.isValidCategory("phone")); // non-numeric
        assertFalse(Category.isValidCategory("9011p041")); // alphabets within digits
        assertFalse(Category.isValidCategory("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(Category.isValidCategory("911")); // exactly 3 numbers
        assertTrue(Category.isValidCategory("93121534"));
        assertTrue(Category.isValidCategory("124293842033123")); // long phone numbers
    }
}
