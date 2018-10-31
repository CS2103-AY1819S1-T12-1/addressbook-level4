package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.address.storage.XmlAdaptedExpense.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalExpenses.ICECREAM;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.encryption.EncryptedTag;
import seedu.address.model.encryption.EncryptionUtil;
import seedu.address.model.expense.Category;
import seedu.address.model.expense.Cost;
import seedu.address.model.expense.Date;
import seedu.address.model.expense.Name;
import seedu.address.testutil.Assert;

public class XmlAdaptedExpenseTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_CATEGORY = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_DATE = "0-0-0";

    private static final String VALID_DATE = "01-10-2018";
    private static final String VALID_NAME = ICECREAM.getName().toString();
    private static final String VALID_CATEGORY = ICECREAM.getCategory().toString();
    private static final String VALID_ADDRESS = ICECREAM.getCost().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = ICECREAM.getTags().stream()
            .map(tag -> {
                try {
                    return new EncryptedTag(tag, DEFAULT_ENCRYPTION_KEY);
                } catch (IllegalValueException e) {
                    throw new IllegalStateException("Default key is invalid");
                }
            })
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validExpenseDetails_returnsExpense() throws Exception {
        XmlAdaptedExpense expense =
                new XmlAdaptedExpense(EncryptionUtil.encryptExpense(ICECREAM, DEFAULT_ENCRYPTION_KEY));
        assertEquals(ICECREAM, expense.toModelType().getDecryptedExpense(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(null, VALID_CATEGORY, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(VALID_NAME, null, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Category.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(VALID_NAME, VALID_CATEGORY, null, VALID_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Cost.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(VALID_NAME, VALID_CATEGORY, VALID_ADDRESS, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

}
