package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.collections.ObservableList;

import seedu.address.model.budget.CategoryBudget;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.exceptions.CategoryBudgetDoesNotExist;
import seedu.address.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.UniqueExpenseList;
import seedu.address.model.notification.Notification;
import seedu.address.model.notification.NotificationHandler;
import seedu.address.model.user.Password;
import seedu.address.model.user.Username;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameExpense comparison)
 */
public class ExpenseTracker implements ReadOnlyExpenseTracker {

    protected Username username;
    protected Password password;
    private String encryptionKey;
    private final UniqueExpenseList expenses;
    private TotalBudget maximumTotalBudget;

    private NotificationHandler notificationHandler;

    /**
     * Creates an empty ExpenseTracker with the given username.
     * @param username the username of the ExpenseTracker
     */
    public ExpenseTracker(Username username, Password password, String encryptionKey) {
        this.username = username;
        this.password = password;
        this.encryptionKey = encryptionKey;
        this.expenses = new UniqueExpenseList();
        this.notificationHandler = new NotificationHandler();
        this.maximumTotalBudget = new TotalBudget("28.00");
    }

    /**
     * Creates an ExpenseTracker using the Expenses in the {@code toBeCopied}
     */
    public ExpenseTracker(ReadOnlyExpenseTracker toBeCopied) {
        this(toBeCopied.getUsername(), toBeCopied.getPassword().orElse(null), toBeCopied.getEncryptionKey());
        this.notificationHandler = toBeCopied.getNotificationHandler();
        this.maximumTotalBudget = toBeCopied.getMaximumTotalBudget();
        resetData(toBeCopied);
    }
    //// totalBudget operations


    /**
     * Modifies the maximum totalBudget for the current expense tracker
     * @param totalBudget a valid TotalBudget
     */
    public void modifyMaximumBudget(TotalBudget totalBudget) {
        double previousSpending = this.maximumTotalBudget.getCurrentExpenses();
        this.maximumTotalBudget = totalBudget;
        this.maximumTotalBudget.modifyExpenses(previousSpending);

    }

    /**
     * Adds a new category totalBudget to the expense tracker.
     * @param budget
     * @throws CategoryBudgetExceedTotalBudgetException Throws this if adding a category totalBudget will result in the
     * sum of category budgets exceeding the total TotalBudget
     */
    public void addCategoryBudget(CategoryBudget budget) throws CategoryBudgetExceedTotalBudgetException {

        this.maximumTotalBudget.addCategoryBudget(budget);
        System.out.println("Expense tracker class");
        System.out.println(this.maximumTotalBudget.getCategoryBudgets());
    }

    /**
     * Modifies an existing category totalBudget.
     * @param budget
     * @throws CategoryBudgetDoesNotExist Throws this if category totalBudget does not exist
     */
    public void modifyCategoryBudget(CategoryBudget budget) throws CategoryBudgetDoesNotExist {
        this.maximumTotalBudget.modifyCategoryBudget(budget);
    }

    /**
     * Sets the recurrence frequency for resetting the totalBudget and spending.
     * @param seconds
     */
    public void setRecurrenceFrequency(long seconds) {
        this.maximumTotalBudget.setRecurrenceFrequency(seconds);
    }



    //// list overwrite operaticons

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     * {@code expenses} must not contain duplicate expenses.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.setExpenses(expenses);
        expenses.forEach(expense -> this.maximumTotalBudget.addExpense(expense));
    }

    /**
     * Resets the existing data of this {@code ExpenseTracker} with {@code newData}.
     */
    public void resetData(ReadOnlyExpenseTracker newData) {
        requireNonNull(newData);
        this.setExpenses(newData.getExpenseList());
        this.maximumTotalBudget = newData.getMaximumTotalBudget();
        this.setNotificationHandler(newData.getNotificationHandler());
    }

    /// notification-level operations

    /**
     * Adds a {@code notification} to the {@code notificationHandler}
     * @param notification to add
     */
    public void addNotification(Notification notification) {
        this.notificationHandler.add(notification);
    }

    /**
     * Replaces the contents of the notification list with {@code expenses}.
     * {@code expenses} must not contain duplicate notification lists.
     */
    public void setNotifications(ObservableList<Notification> notifications) {
        this.notificationHandler.setNotifications(notifications);
    }

    public void setNotificationHandler(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    /**
     * Checks if a {@code WarningNotification} object should be added to the list.
     * @return true if {@code WarningNotification} should be added, false otherwise.
     */
    public boolean checkIfAddWarningNotification(TotalBudget budget) {
        return notificationHandler.isTimeToSendWarning(budget);
    }

    /**
     * Checks if a {@code TipNotification} object should be added to the list.
     * @return true if {@code TipNotification} should be added, false otherwise.
     */
    public boolean checkIfAddTipNotification() {
        return notificationHandler.isTimeToSendTip();
    }

    /**
     * Toggle the ability to send {@code TipNotification}
     * @param option to toggle to.
     */
    public void toggleTipNotification(boolean option) {
        notificationHandler.setTipEnabled(option);
    }

    /**
     * Toggle the ability to send {@code WarningNotification}
     * @param option to toggle to.
     */
    public void toggleWarningNotification(boolean option) {
        notificationHandler.setWarningEnabled(option);
    }

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    /**
     * Modify notificationHandler {@code WarningNotification}
     */
    public void modifyNotificationHandler(LocalDateTime date, boolean isTipEnabled, boolean isWarningEnabled) {
        notificationHandler.modifyNotificationHandler(date, isTipEnabled, isWarningEnabled);
    }

    //// expense-level operations

    /**
     * Returns true if a expense with the same identity as {@code expense} exists in the expense tracker.
     */
    public boolean hasExpense(Expense expense) {
        requireNonNull(expense);
        return this.expenses.contains(expense);
    }

    /**
     * Adds a expense into the expense tracker
     * @return true if expense is successfully added without exceeding totalBudget, else false
     */
    public boolean addExpense(Expense p) {
        this.expenses.add(p);
        return this.maximumTotalBudget.addExpense(p);
    }

    /**
     * Replaces the given expense {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the expense tracker.
     * The expense identity of {@code editedExpense}
     * must not be the same as another existing expense in the expense tracker.
     */
    public void updateExpense(Expense target, Expense editedExpense) {
        requireNonNull(editedExpense);

        this.expenses.setExpense(target, editedExpense);
        this.maximumTotalBudget.alterSpending(target, editedExpense);

    }

    /**
     * Removes {@code key} from this {@code ExpenseTracker}.
     * {@code key} must exist in the expense tracker.
     */
    public void removeExpense(Expense key) {
        expenses.remove(key);
        this.maximumTotalBudget.removeExpense(key);
    }

    public TotalBudget getMaximumTotalBudget() {
        return new TotalBudget(this.maximumTotalBudget.getBudgetCap(), this.maximumTotalBudget.getCurrentExpenses(),
            this.maximumTotalBudget.getNextRecurrence(), this.maximumTotalBudget.getNumberOfSecondsToRecurAgain(),
            this.maximumTotalBudget.getCategoryBudgets());
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String newKey) {
        this.encryptionKey = newKey;
    }

    @Override
    public Username getUsername() {
        return username;
    }

    @Override
    public Optional<Password> getPassword() {
        return Optional.ofNullable(password);
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    @Override
    public boolean isMatchPassword(Password toCheck) {
        return this.password == null || this.password.equals(toCheck);
    }

    public void setUsername(Username newUsername) {
        this.username = newUsername;
    }
    //// util methods

    @Override
    public String toString() {
        return expenses.asUnmodifiableObservableList().size() + " expenses";
        // TODO: refine later
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Notification> getNotificationList() {
        return notificationHandler.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpenseTracker // instanceof handles nulls
                && expenses.equals(((ExpenseTracker) other).expenses))
                && this.notificationHandler.equals(((ExpenseTracker) other).notificationHandler)
                && this.username.equals(((ExpenseTracker) other).username)
                && Objects.equals(this.password, ((ExpenseTracker) other).password)
                && this.encryptionKey.equals(((ExpenseTracker) other).encryptionKey)
                && this.maximumTotalBudget.equals(((ExpenseTracker) other).maximumTotalBudget);

    }

    @Override
    public int hashCode() {
        return Objects.hash(expenses, maximumTotalBudget, username, password, encryptionKey);
    }

}
