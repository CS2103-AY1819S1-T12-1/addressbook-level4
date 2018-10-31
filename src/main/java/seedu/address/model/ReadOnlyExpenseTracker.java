package seedu.address.model;

import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.expense.Expense;
import seedu.address.model.notification.Notification;
import seedu.address.model.notification.NotificationHandler;
import seedu.address.model.user.Password;
import seedu.address.model.user.Username;

/**
 * Unmodifiable view of an expense tracker
 */
public interface ReadOnlyExpenseTracker {

    /**
     * Returns an unmodifiable view of the expenses list.
     * This list will not contain any duplicate expenses.
     */
    ObservableList<Expense> getExpenseList();
    TotalBudget getMaximumTotalBudget();
    Username getUsername();
    ObservableList<Notification> getNotificationList();
    NotificationHandler getNotificationHandler();
    Optional<Password> getPassword();

    /**
     * Checks if the input password matches the password of the current user. If the user has no password, then true
     * is returned.
     * @param password the password to check
     * @return true if the user has no password or if the input password matches his/her password, or else false
     */
    boolean isMatchPassword(Password password);

    String getEncryptionKey();
}
