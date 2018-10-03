package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedExpense.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalExpenses.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
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
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_CATEGORY = BENSON.getCategory().toString();
    private static final String VALID_ADDRESS = BENSON.getCost().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validExpenseDetails_returnsExpense() throws Exception {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(BENSON);
        assertEquals(BENSON, expense.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedExpense expense =
                new XmlAdaptedExpense(INVALID_NAME, VALID_CATEGORY, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(null, VALID_CATEGORY, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_invalidCategory_throwsIllegalValueException() {
        XmlAdaptedExpense expense =
                new XmlAdaptedExpense(VALID_NAME, INVALID_CATEGORY, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        String expectedMessage = Category.MESSAGE_CATEGORY_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullCategory_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(VALID_NAME, null, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Category.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }


    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedExpense expense =
                new XmlAdaptedExpense(VALID_NAME, VALID_CATEGORY, INVALID_ADDRESS, VALID_DATE, VALID_TAGS);
        String expectedMessage = Cost.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(VALID_NAME, VALID_CATEGORY, null, VALID_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Cost.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedExpense expense =
                new XmlAdaptedExpense(VALID_NAME, VALID_CATEGORY, VALID_ADDRESS, VALID_DATE, invalidTags);
        Assert.assertThrows(IllegalValueException.class, expense::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        XmlAdaptedExpense expense =
                new XmlAdaptedExpense(VALID_NAME, VALID_CATEGORY, VALID_ADDRESS, INVALID_DATE, VALID_TAGS);
        String expectedMessage = Date.DATE_FORMAT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        XmlAdaptedExpense expense = new XmlAdaptedExpense(VALID_NAME, VALID_CATEGORY, VALID_ADDRESS, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

}
