package seedu.expensetracker.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.ExpenseCardHandle;
import guitests.guihandles.ExpenseListPanelHandle;
import guitests.guihandles.NotificationCardHandle;
import guitests.guihandles.NotificationListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.Notification;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertExpenseCardEquals(ExpenseCardHandle expectedCard, ExpenseCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getCost(), actualCard.getCost());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getCategory(), actualCard.getCategory());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedExpense}.
     */
    public static void assertCardDisplaysExpense(Expense expectedExpense, ExpenseCardHandle actualCard) {
        assertEquals(expectedExpense.getName().expenseName, actualCard.getName());
        assertEquals(expectedExpense.getCategory().categoryName, actualCard.getCategory());
        assertEquals(expectedExpense.getCost().value, actualCard.getCost());
        assertEquals(expectedExpense.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
    }

    /**
     * Asserts that the list in {@code expenseListPanelHandle} displays the details of {@code expenses} correctly and
     * in the correct order.
     */
    public static void assertExpenseListMatching(ExpenseListPanelHandle expenseListPanelHandle, Expense... expenses) {
        for (int i = 0; i < expenses.length; i++) {
            expenseListPanelHandle.navigateToCard(i);
            assertCardDisplaysExpense(expenses[i], expenseListPanelHandle.getExpenseCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code expenseListPanelHandle} displays the details of {@code expenses} correctly and
     * in the correct order.
     */
    public static void assertExpenseListMatching(ExpenseListPanelHandle expenseListPanelHandle,
                                                 List<Expense> expenses) {
        assertExpenseListMatching(expenseListPanelHandle, expenses.toArray(new Expense[0]));
    }

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertNotificationCardEquals(NotificationCardHandle expectedCard,
                                                    NotificationCardHandle actualCard) {
        assertEquals(expectedCard.getHeader(), actualCard.getHeader());
        assertEquals(expectedCard.getBody(), actualCard.getBody());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedNotification}.
     */
    public static void assertCardDisplaysNotification(Notification expectedNotification,
                                                      NotificationCardHandle actualCard) {
        assertEquals(expectedNotification.getHeader(), actualCard.getHeader());
        assertEquals(expectedNotification.getBody(), actualCard.getBody());
    }

    /**
     * Asserts that the list in {@code notificationListPanelHandle} displays the details of {@code notification}
     * correctly and in the correct order.
     */
    public static void assertNotificationListMatching(NotificationListPanelHandle notificationsListPanelHandle,
                                                      Notification... notifications) {
        for (int i = 0; i < notifications.length; i++) {
            notificationsListPanelHandle.navigateToCard(i);
            assertCardDisplaysNotification(notifications[i], notificationsListPanelHandle.getNotificationCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code notificationListPanelHandle} displays the details of {@code notifications}
     * correctly and in the correct order.
     */
    public static void assertNotificationListMatching(NotificationListPanelHandle notificationListPanelHandle,
                                                      List<Notification> notifications) {
        assertNotificationListMatching(notificationListPanelHandle, notifications.toArray(new Notification[0]));
    }


    /**
     * Asserts the size of the list in {@code expenseListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(ExpenseListPanelHandle expenseListPanelHandle, int size) {
        int numberOfPeople = expenseListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
