package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;
import static seedu.address.testutil.TypicalExpenses.getTypicalExpenses;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysExpense;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import guitests.guihandles.ExpenseCardHandle;
import guitests.guihandles.ExpenseListPanelHandle;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.expense.Expense;
import seedu.address.storage.XmlSerializableAddressBook;

public class ExpenseListPanelTest extends GuiUnitTest {
    private static final ObservableList<Expense> TYPICAL_EXPENSES =
            FXCollections.observableList(getTypicalExpenses());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_EXPENSE);

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private ExpenseListPanelHandle expenseListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_EXPENSES);

        for (int i = 0; i < TYPICAL_EXPENSES.size(); i++) {
            expenseListPanelHandle.navigateToCard(TYPICAL_EXPENSES.get(i));
            Expense expectedExpense = TYPICAL_EXPENSES.get(i);
            ExpenseCardHandle actualCard = expenseListPanelHandle.getExpenseCardHandle(i);

            assertCardDisplaysExpense(expectedExpense, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        initUi(TYPICAL_EXPENSES);
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        ExpenseCardHandle expectedExpense = expenseListPanelHandle.getExpenseCardHandle(INDEX_SECOND_EXPENSE.getZeroBased());
        ExpenseCardHandle selectedExpense = expenseListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedExpense, selectedExpense);
    }

    /**
     * Verifies that creating and deleting large number of expenses in {@code ExpenseListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() throws Exception {
        ObservableList<Expense> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of expense cards exceeded time limit");
    }

    /**
     * Returns a list of expenses containing {@code expenseCount} expenses that is used to populate the
     * {@code ExpenseListPanel}.
     */
    private ObservableList<Expense> createBackingList(int expenseCount) throws Exception {
        Path xmlFile = createXmlFileWithExpenses(expenseCount);
        XmlSerializableAddressBook xmlAddressBook =
                XmlUtil.getDataFromFile(xmlFile, XmlSerializableAddressBook.class);
        return FXCollections.observableArrayList(xmlAddressBook.toModelType().getExpenseList());
    }

    /**
     * Returns a .xml file containing {@code expenseCount} expenses. This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithExpenses(int expenseCount) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<addressbook>\n");
        for (int i = 0; i < expenseCount; i++) {
            builder.append("<expenses>\n");
            builder.append("<name>").append(i).append("a</name>\n");
            builder.append("<category>School</category>\n");
            builder.append("<cost>1.00</cost>\n");
            builder.append("<date>01-10-2018</date>\n");
            builder.append("</expenses>\n");
        }
        builder.append("<username>manyExpenses</username>");
        builder.append("</addressbook>\n");

        Path manyExpensesFile = Paths.get(TEST_DATA_FOLDER + "manyPersons.xml");
        FileUtil.createFile(manyExpensesFile);
        FileUtil.writeToFile(manyExpensesFile, builder.toString());
        manyExpensesFile.toFile().deleteOnExit();
        return manyExpensesFile;
    }

    /**
     * Initializes {@code expenseListPanelHandle} with a {@code ExpenseListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code ExpenseListPanel}.
     */
    private void initUi(ObservableList<Expense> backingList) {
        ExpenseListPanel expenseListPanel = new ExpenseListPanel(backingList);
        uiPartRule.setUiPart(expenseListPanel);

        expenseListPanelHandle = new ExpenseListPanelHandle(getChildNode(expenseListPanel.getRoot(),
                ExpenseListPanelHandle.EXPENSE_LIST_VIEW_ID));
    }
}
