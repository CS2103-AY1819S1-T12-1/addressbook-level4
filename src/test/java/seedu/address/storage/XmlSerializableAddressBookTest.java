package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.TypicalExpenses;

public class XmlSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlSerializableAddressBookTest");
    private static final Path TYPICAL_EXPENSES_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.xml");
    private static final Path INVALID_EXPENSE_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.xml");
    private static final Path DUPLICATE_EXPENSE_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalExpensesFile_success() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(TYPICAL_EXPENSES_FILE,
                XmlSerializableAddressBook.class);
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalExpensesAddressBook = TypicalExpenses.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalExpensesAddressBook);
    }

    @Test
    public void toModelType_invalidExpenseFile_throwsIllegalValueException() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(INVALID_EXPENSE_FILE,
                XmlSerializableAddressBook.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateExpenses_throwsIllegalValueException() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(DUPLICATE_EXPENSE_FILE,
                XmlSerializableAddressBook.class);
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(XmlSerializableAddressBook.MESSAGE_DUPLICATE_EXPENSE);
        dataFromFile.toModelType();
    }

}
