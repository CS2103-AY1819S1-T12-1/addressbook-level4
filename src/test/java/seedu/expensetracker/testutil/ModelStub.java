package seedu.expensetracker.testutil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.logic.commands.StatsCommand;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {
    @Override
    public boolean addExpense(Expense expense) throws NoUserSelectedException {
        throw new AssertionError("addExpense method should not be called.");
    }

    @Override
    public void addGeneralNotification(Notification notif) {
        throw new AssertionError("addGeneralNotification method should not be called.");
    }

    @Override
    public void setRecurrenceFrequency(long seconds) throws NoUserSelectedException {
        throw new AssertionError("setRecurrenceFrequency should not be called");
    }

    @Override
    public void setCategoryBudget(CategoryBudget budget) throws NoUserSelectedException {
        throw new AssertionError("setCategoryBudget should not be called");

    }
    @Override
    public void resetData(ReadOnlyExpenseTracker newData) throws NoUserSelectedException {
        throw new AssertionError("resetData method should not be called.");
    }

    @Override
    public void modifyMaximumBudget(TotalBudget totalBudget) throws NoUserSelectedException {
        throw new AssertionError("modifyMaximumBudget method should not be called.");
    }

    @Override
    public ReadOnlyExpenseTracker getExpenseTracker() throws NoUserSelectedException {
        throw new AssertionError("getExpenseTracker should not be called.");
    }

    @Override
    public boolean hasExpense(Expense expense) throws NoUserSelectedException {
        throw new AssertionError("hasExpense method should not be called.");
    }

    @Override
    public void deleteExpense(Expense target) throws NoUserSelectedException {
        throw new AssertionError("deleteExpense method should not be called.");
    }

    @Override
    public void updateExpense(Expense target, Expense editedExpense) throws NoUserSelectedException {
        throw new AssertionError("updateExpense method should not be called.");
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException {
        throw new AssertionError("getFilteredExpenseList method should not be called.");
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Expense> predicate) throws NoUserSelectedException {
        throw new AssertionError("updateFilteredExpenseList method should not be called.");
    }

    @Override
    public ObservableList<Expense> getExpenseStats() throws NoUserSelectedException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateExpenseStatsPredicate (Predicate<Expense> predicate) throws NoUserSelectedException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public StatsCommand.StatsMode getStatsMode() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateStatsMode(StatsCommand.StatsMode statsMode) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public StatsCommand.StatsPeriod getStatsPeriod() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateStatsPeriod(StatsCommand.StatsPeriod statsPeriod) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public int getPeriodAmount() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updatePeriodAmount(int x) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canUndoExpenseTracker() throws NoUserSelectedException {
        throw new AssertionError("canUndoExpenseTracker method should not be called.");
    }

    @Override
    public boolean canRedoExpenseTracker() throws NoUserSelectedException {
        throw new AssertionError("canRedoExpenseTracker method should not be called.");
    }

    @Override
    public void undoExpenseTracker() throws NoUserSelectedException {
        throw new AssertionError("undoExpenseTracker method should not be called.");
    }

    @Override
    public void redoExpenseTracker() throws NoUserSelectedException {
        throw new AssertionError("redoExpenseTracker method should not be called.");
    }

    @Override
    public void commitExpenseTracker() throws NoUserSelectedException {
        throw new AssertionError("commitExpenseTracker method should not be called.");
    }

    @Override
    public boolean loadUserData(LoginCredentials loginCredentials) throws NonExistentUserException {
        throw new AssertionError("loadUserData method should not be called.");
    }

    @Override
    public void unloadUserData() {
        throw new AssertionError("unloadUserData method should not be called.");
    }

    @Override
    public boolean isUserExists(Username username) {
        throw new AssertionError("isUserExists method should not be called.");
    }

    @Override
    public void addUser(Username username) throws UserAlreadyExistsException {
        throw new AssertionError("addUser method should not be called.");
    }

    @Override
    public boolean hasSelectedUser() {
        throw new AssertionError("hasSelectedUser method should not be called.");
    }

    @Override
    public String encryptString(String toEncrypt) throws NoUserSelectedException {
        throw new AssertionError("encryptString method should not be called.");
    }

    @Override
    public String decryptString(String toDecrypt) throws IllegalValueException, NoUserSelectedException {
        throw new AssertionError("decryptString method should not be called.");
    }

    @Override
    public Model copy(UserPrefs userPrefs) throws NoUserSelectedException {
        throw new AssertionError("copy method should not be called.");
    }

    @Override
    public void setPassword(Password newPassword, String plainPassword) throws NoUserSelectedException {
        throw new AssertionError("setPassword method should not be called.");
    }

    @Override
    public boolean isMatchPassword(Password toCheck) throws NoUserSelectedException {
        throw new AssertionError("isMatchPassword method should not be called.");
    }

    @Override
    public boolean addWarningNotification() throws NoUserSelectedException {
        throw new AssertionError("addWarningNotification should not be called.");
    }

    @Override
    public boolean addTipNotification() throws NoUserSelectedException {
        throw new AssertionError("addTipNotification should not be called.");
    }

    @Override
    public ObservableList<Notification> getNotificationList() throws NoUserSelectedException {
        throw new AssertionError("addNotificationList should not be called.");
    }

    @Override
    public void toggleTipNotification(boolean toggleOption) throws NoUserSelectedException {
        throw new AssertionError("toggleTipNotification should not be called.");
    }

    @Override
    public void toggleWarningNotification(boolean toggleOption) throws NoUserSelectedException {
        throw new AssertionError("toggleWarningNotification should not be called.");
    }

    @Override
    public void toggleBothNotification(boolean toggleOption) throws NoUserSelectedException {
        throw new AssertionError("toggleBothNotification should not be called.");
    }

    @Override
    public NotificationHandler getNotificationHandler() throws NoUserSelectedException {
        throw new AssertionError("getNotificationHandler should not be called.");
    }

    @Override
    public void modifyNotificationHandler(LocalDateTime time, boolean isTipEnabled, boolean isWarningEnabled)
            throws NoUserSelectedException {
        throw new AssertionError("modifyNotificationHandlerWithoutList should not be called.");
    }

    @Override
    public void clearNotifications() throws NoUserSelectedException {
        throw new AssertionError("clearNotifications method should not be called.");
    }

    @Override
    public HashSet<CategoryBudget> getCategoryBudgets() throws NoUserSelectedException {
        throw new AssertionError("getCategoryBudgets method should not be called.");
    }

    @Override
    public TotalBudget getMaximumBudget() throws NoUserSelectedException {
        throw new AssertionError("getMaximumTotalBudget method should not be called.");
    }

}
