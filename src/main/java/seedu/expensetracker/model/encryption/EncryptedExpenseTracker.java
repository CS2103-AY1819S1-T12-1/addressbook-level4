package seedu.expensetracker.model.encryption;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;

//@@author JasonChong96
/**
 * Represents a user's Expense Tracker data in it's encrypted form.
 */
public class EncryptedExpenseTracker {
    private Username username;
    private final Password password;
    private final EncryptedExpenseList expenses;
    private final TotalBudget maximumTotalBudget;
    private final NotificationHandler notificationHandler;

    /**
     * Creates an empty EncryptedExpenseTracker with the given username.
     * @param username the username associated to the ExpenseTracker
     */
    public EncryptedExpenseTracker(Username username, Password password) {
        this(username, password, new TotalBudget("28.00"), new NotificationHandler());
    }

    public EncryptedExpenseTracker(Username username, Password password, TotalBudget budget,
                                   NotificationHandler notificationHandler) {
        this.username = username;
        this.password = password;
        this.expenses = new EncryptedExpenseList();
        this.maximumTotalBudget = budget;
        this.notificationHandler = notificationHandler;
    }

    /**
     * Decrypts the Expense Tracker represented in this instance and returns its decrypted form as a
     * ExpenseTracker instance.
     *
     * @param key the encryption key needed to decrypt this Expense Tracker data
     * @return a decrypted ExpenseTracker
     * @throws IllegalValueException if the key is invalid or an illegal field value is detected in the data
     */
    public ExpenseTracker decryptTracker(String key) throws IllegalValueException {
        ExpenseTracker result = new ExpenseTracker(username, password, key);
        for (EncryptedExpense expense : expenses) {
            result.addExpense(expense.getDecryptedExpense(key));
        }
        result.modifyMaximumBudget(maximumTotalBudget);
        result.setNotificationHandler(notificationHandler);
        return result;
    }

    /**
     * Checks if the input password matches the password of the current user. If the user has no password, then true
     * is returned.
     * @param toCheck the password to check
     * @return true if the user has no password or if the input password matches his/her password, or else false
     */
    public boolean isMatchPassword(Password toCheck) {
        return this.password == null || this.password.equals(toCheck);
    }

    /**
     * Returns true if a expense with the same identity as {@code expense} exists in the tracker.
     */
    public boolean hasExpense(EncryptedExpense expense) {
        requireNonNull(expense);
        return this.expenses.contains(expense);
    }

    /**
     * Adds a expense into the tracker
     */
    public void addExpense(EncryptedExpense p) {
        this.expenses.add(p);
    }

    public Optional<Password> getPassword() {
        return Optional.ofNullable(password);
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    /**
     * Returns a copy of the TotalBudget of the ExpenseTracker.
     * @return a copy of the TotalBudget of the ExpenseTracker
     */
    public TotalBudget getMaximumTotalBudget() {
        return new TotalBudget(this.maximumTotalBudget.getBudgetCap(), this.maximumTotalBudget.getCurrentExpenses(),
                this.maximumTotalBudget.getNextRecurrence(), this.maximumTotalBudget.getNumberOfSecondsToRecurAgain(),
                this.maximumTotalBudget.getCategoryBudgets());
    }

    public EncryptedExpenseList getEncryptedExpenses() {
        return expenses;
    }

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    /**
     * Adds a notification to the tracker
     */
    public void addNotification(Notification notification) {
        this.notificationHandler.add(notification);
    }

    /**
     * Returns an unmodifiable version of the list of Notificaitions
     * @return Returns an unmodifiable version of the list of Notificaitions
     */
    public ObservableList<Notification> getNotificationList() {
        return notificationHandler.asUnmodifiableObservableList();
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EncryptedExpenseTracker // instanceof handles nulls
                && expenses.equals(((EncryptedExpenseTracker) other).expenses))
                && this.maximumTotalBudget.equals(((EncryptedExpenseTracker) other).maximumTotalBudget)
                && this.username.equals(((EncryptedExpenseTracker) other).username)
                && Objects.equals(this.password, ((EncryptedExpenseTracker) other).password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenses, maximumTotalBudget, username, password);
    }
}
