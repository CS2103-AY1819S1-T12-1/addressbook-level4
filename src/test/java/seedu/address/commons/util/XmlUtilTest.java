package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.AddressBook;
import seedu.address.model.user.Username;
import seedu.address.storage.XmlAdaptedExpense;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlSerializableAddressBook;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.ExpenseBuilder;
import seedu.address.testutil.TestUtil;

public class XmlUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validAddressBook.xml");
    private static final Path MISSING_EXPENSE_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingExpenseField.xml");
    private static final Path INVALID_EXPENSE_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidExpenseField.xml");
    private static final Path VALID_EXPENSE_FILE = TEST_DATA_FOLDER.resolve("validExpense.xml");
    private static final Path TEMP_FILE = TestUtil.getFilePathInSandboxFolder("tempAddressBook.xml");

    private static final String INVALID_CATEGORY = " ";

    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_CATEGORY = "School";
    private static final String VALID_ADDRESS = "1.00";
    private static final String VALID_DATE = "01-10-2018";
    private static final String VALID_ADDRESS_BOOK_USERNAME = "validAddressBook";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("friends"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, AddressBook.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        AddressBook dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableAddressBook.class).toModelType();
        assertEquals(9, dataFromFile.getExpenseList().size());
        assertEquals(dataFromFile.getUsername(), new Username(VALID_ADDRESS_BOOK_USERNAME));
    }

    @Test
    public void xmlAdaptedExpenseFromFile_fileWithMissingExpenseField_validResult() throws Exception {
        XmlAdaptedExpense actualExpense = XmlUtil.getDataFromFile(
                MISSING_EXPENSE_FIELD_FILE, XmlAdaptedExpenseWithRootElement.class);
        XmlAdaptedExpense expectedExpense = new XmlAdaptedExpense(
                null, VALID_CATEGORY, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        assertEquals(expectedExpense, actualExpense);
    }

    @Test
    public void xmlAdaptedExpenseFromFile_fileWithInvalidExpenseField_validResult() throws Exception {
        XmlAdaptedExpense actualExpense = XmlUtil.getDataFromFile(
                INVALID_EXPENSE_FIELD_FILE, XmlAdaptedExpenseWithRootElement.class);
        XmlAdaptedExpense expectedExpense = new XmlAdaptedExpense(
                VALID_NAME, INVALID_CATEGORY, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        assertEquals(expectedExpense, actualExpense);
    }

    @Test
    public void xmlAdaptedExpenseFromFile_fileWithValidExpense_validResult() throws Exception {
        XmlAdaptedExpense actualExpense = XmlUtil.getDataFromFile(
                VALID_EXPENSE_FILE, XmlAdaptedExpenseWithRootElement.class);
        XmlAdaptedExpense expectedExpense = new XmlAdaptedExpense(
                VALID_NAME, VALID_CATEGORY, VALID_ADDRESS, VALID_DATE, VALID_TAGS);
        assertEquals(expectedExpense, actualExpense);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new AddressBook(new Username("aaa")));
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new AddressBook(new Username("aaa")));
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableAddressBook dataToWrite = new XmlSerializableAddressBook(new AddressBook(new Username("AA")));
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);

        AddressBookBuilder builder = new AddressBookBuilder(new AddressBook(new Username("AAA")));
        dataToWrite = new XmlSerializableAddressBook(
                builder.withExpense(new ExpenseBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedExpense}
     * objects.
     */
    @XmlRootElement(name = "expense")
    private static class XmlAdaptedExpenseWithRootElement extends XmlAdaptedExpense {}
}
