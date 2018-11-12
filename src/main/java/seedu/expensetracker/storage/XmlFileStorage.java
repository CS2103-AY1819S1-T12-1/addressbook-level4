package seedu.expensetracker.storage;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

import seedu.expensetracker.commons.exceptions.DataConversionException;
import seedu.expensetracker.commons.util.XmlUtil;

/**
 * Stores expensetracker data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given expensetracker data to the specified file.
     */
    public static void saveDataToFile(Path file, XmlSerializableExpenseTracker expenseTracker)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, expenseTracker);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Returns expense tracker in the file or an empty expense tracker
     */
    public static XmlSerializableExpenseTracker loadDataFromSaveFile(Path file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {

            return XmlUtil.getDataFromFile(file, XmlSerializableExpenseTracker.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
