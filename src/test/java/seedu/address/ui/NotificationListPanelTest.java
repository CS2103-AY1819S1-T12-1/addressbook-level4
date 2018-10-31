package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;
import static seedu.address.testutil.TypicalNotifications.getTypicalNotifications;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysNotification;
import static seedu.address.ui.testutil.GuiTestAssert.assertNotificationCardEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import guitests.guihandles.NotificationCardHandle;
import guitests.guihandles.NotificationListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.notification.Notification;
import seedu.address.storage.XmlSerializableExpenseTracker;

//@@author Snookerballs
public class NotificationListPanelTest extends GuiUnitTest {
    private static final ObservableList<Notification> TYPICAL_NOTIFICATIONS =
            FXCollections.observableList(getTypicalNotifications());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_EXPENSE);

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private NotificationListPanelHandle notificationListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_NOTIFICATIONS);

        for (int i = 0; i < TYPICAL_NOTIFICATIONS.size(); i++) {
            notificationListPanelHandle.navigateToCard(TYPICAL_NOTIFICATIONS.get(i));
            Notification expectedNotification = TYPICAL_NOTIFICATIONS.get(i);
            NotificationCardHandle actualCard = notificationListPanelHandle.getNotificationCardHandle(
                    i);

            assertCardDisplaysNotification(expectedNotification, actualCard);
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        initUi(TYPICAL_NOTIFICATIONS);
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        NotificationCardHandle expectedNotification = notificationListPanelHandle.getNotificationCardHandle(
                INDEX_SECOND_EXPENSE.getZeroBased());
        NotificationCardHandle selectedNotification = notificationListPanelHandle.getHandleToSelectedCard();
        assertNotificationCardEquals(expectedNotification, selectedNotification);
    }

    /**
     * Verifies that creating and deleting large number of expenses in {@code ExpenseListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() throws Exception {
        ObservableList<Notification> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of expense cards exceeded time limit");
    }

    /**
     * Returns a list of expenses containing {@code expenseCount} expenses that is used to populate the
     * {@code ExpenseListPanel}.
     */
    private ObservableList<Notification> createBackingList(int notificationCount) throws Exception {
        Path xmlFile = createXmlFileWithExpenses(notificationCount);
        XmlSerializableExpenseTracker xmlExpenseTracker =
                XmlUtil.getDataFromFile(xmlFile, XmlSerializableExpenseTracker.class);
        return FXCollections.observableArrayList(xmlExpenseTracker.toModelType().getNotificationList());
    }

    /**
     * Returns a .xml file containing {@code expenseCount} expenses. This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithExpenses(int notificationCount) throws Exception {
        StringBuilder builder = new StringBuilder();


        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<expensetracker>\n");
        for (int i = 0; i < notificationCount; i++) {
            builder.append("<notifications>\n");
            builder.append("<header>").append(i).append("h</header>\n");
            builder.append("<body>").append(i).append("b</body>\n");
            builder.append("<type>WARNING</type>\n");
            builder.append("</notifications>\n");
        }
        builder.append("<notificationHandler>\n");
        builder.append("<lastTipSentOn>2018-10-31T01:42:04.021750</lastTipSentOn>\n");
        builder.append("<isTipEnabled>true</isTipEnabled>\n");
        builder.append("<isWarningEnabled>true</isWarningEnabled>\n");
        builder.append("</notificationHandler>\n");

        builder.append("<username>manyExpenses</username>\n");
        builder.append("<budget>\n");
        builder.append("<budgetCap>").append((double) notificationCount).append("</budgetCap>\n");
        builder.append("<currentExpenses>").append((double) notificationCount).append("</currentExpenses>\n");
        builder.append("</budget>\n");
        builder.append("</expensetracker>\n");

        Path manyExpensesFile = Paths.get(TEST_DATA_FOLDER + "manyExpenses.xml");
        FileUtil.createFile(manyExpensesFile);
        FileUtil.writeToFile(manyExpensesFile, builder.toString());
        manyExpensesFile.toFile().deleteOnExit();
        return manyExpensesFile;
    }

    /**
     * Initializes {@code expenseListPanelHandle} with a {@code ExpenseListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code ExpenseListPanel}.
     */
    private void initUi(ObservableList<Notification> backingList) {
        NotificationPanel notificationListPanel = new NotificationPanel(backingList);
        uiPartRule.setUiPart(notificationListPanel);

        notificationListPanelHandle = new NotificationListPanelHandle(getChildNode(notificationListPanel.getRoot(),
                NotificationListPanelHandle.NOTIFICATION_LIST_VIEW_ID));
    }
}
