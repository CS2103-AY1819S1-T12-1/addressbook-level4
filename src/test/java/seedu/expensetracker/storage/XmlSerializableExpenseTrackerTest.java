package seedu.expensetracker.storage;

import static org.junit.Assert.assertEquals;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.commons.util.XmlUtil;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.testutil.TypicalExpenses;

public class XmlSerializableExpenseTrackerTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test",
        "data", "XmlSerializableExpenseTrackerTest");
    private static final Path TYPICAL_EXPENSES_FILE = TEST_DATA_FOLDER.resolve("typicalExpensesExpenseTracker.xml");
    private static final Path DUPLICATE_EXPENSE_FILE = TEST_DATA_FOLDER.resolve("duplicateExpenseExpenseTracker.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalExpensesFile_success() throws Exception {
        XmlSerializableExpenseTracker dataFromFile = XmlUtil.getDataFromFile(TYPICAL_EXPENSES_FILE,
                XmlSerializableExpenseTracker.class);
        ExpenseTracker expenseTrackerFromFile = dataFromFile.toModelType().decryptTracker(DEFAULT_ENCRYPTION_KEY);
        ExpenseTracker typicalExpensesExpenseTracker = TypicalExpenses.getTypicalExpenseTracker();
        typicalExpensesExpenseTracker.setUsername(new Username(TYPICAL_EXPENSES_FILE.getFileName().toString()
                .replace(".xml", "")));
        assertEquals(expenseTrackerFromFile, typicalExpensesExpenseTracker);
        assertEquals(expenseTrackerFromFile.getNotificationHandler(),
                typicalExpensesExpenseTracker.getNotificationHandler());
        assertEquals(expenseTrackerFromFile.getMaximumTotalBudget(),
            typicalExpensesExpenseTracker.getMaximumTotalBudget());
    }


}
