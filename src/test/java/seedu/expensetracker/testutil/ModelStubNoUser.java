package seedu.expensetracker.testutil;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.logic.commands.StatsCommand;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;

/**
 * A model stub that throws NoUserSelectedException for every method that requires a selected user.
 */
public class ModelStubNoUser extends ModelStub {
    @Override
    public boolean addExpense(Expense expense) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void setRecurrenceFrequency(long seconds) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void setCategoryBudget(CategoryBudget budget) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }
    @Override
    public void resetData(ReadOnlyExpenseTracker newData) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void modifyMaximumBudget(TotalBudget totalBudget) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public ReadOnlyExpenseTracker getExpenseTracker() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public boolean hasExpense(Expense expense) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void deleteExpense(Expense target) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void updateExpense(Expense target, Expense editedExpense) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Expense> predicate) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public ObservableList<Expense> getExpenseStats() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void updateExpenseStatsPredicate (Predicate<Expense> predicate) throws NoUserSelectedException {
        throw new NoUserSelectedException();
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
        throw new NoUserSelectedException();
    }

    @Override
    public boolean canRedoExpenseTracker() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void undoExpenseTracker() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void redoExpenseTracker() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void commitExpenseTracker() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public boolean loadUserData(LoginCredentials loginCredentials) {
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
    public void addUser(Username username) {
        throw new AssertionError("addUser method should not be called.");
    }

    @Override
    public boolean hasSelectedUser() {
        throw new AssertionError("hasSelectedUser method should not be called.");
    }

    @Override
    public String encryptString(String toEncrypt) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public String decryptString(String toDecrypt) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public Model copy(UserPrefs userPrefs) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void setPassword(Password newPassword, String plainPassword) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public boolean isMatchPassword(Password toCheck) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public boolean addWarningNotification() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public boolean addTipNotification() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public ObservableList<Notification> getNotificationList() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void toggleTipNotification(boolean toggleOption) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void toggleWarningNotification(boolean toggleOption) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void toggleBothNotification(boolean toggleOption) throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public NotificationHandler getNotificationHandler() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void modifyNotificationHandler(LocalDateTime time, boolean isTipEnabled, boolean isWarningEnabled)
            throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public void clearNotifications() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }

    @Override
    public TotalBudget getMaximumBudget() throws NoUserSelectedException {
        throw new NoUserSelectedException();
    }
}
