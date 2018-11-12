package seedu.expensetracker.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.logic.commands.StatsCommand.StatsMode;
import seedu.expensetracker.logic.commands.StatsCommand.StatsPeriod;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Expense> PREDICATE_SHOW_ALL_EXPENSES = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyExpenseTracker newData) throws NoUserSelectedException;

    /** Returns the ExpenseTracker */
    ReadOnlyExpenseTracker getExpenseTracker() throws NoUserSelectedException;

    /**
     * Returns true if a expense with the same identity as {@code expense} exists in the expense tracker.
     */
    boolean hasExpense(Expense expense) throws NoUserSelectedException;

    /**
     * Deletes the given expense.
     * The expense must exist in the expense tracker.
     */
    void deleteExpense(Expense target) throws NoUserSelectedException;

    /**
     * Adds the given expense.
     * {@code expense} must not already exist in the expense tracker.
     * @return true if expense is added without warning, else false.
     */
    boolean addExpense(Expense expense) throws NoUserSelectedException;

    /**
     * Replaces the given expense {@code target} with {@code editedExpense}.
     * {@code target} must exist in the expense tracker.
     * The expense identity of {@code editedExpense}
     * must not be the same as another existing expense in the expense tracker.
     */
    void updateExpense(Expense target, Expense editedExpense) throws NoUserSelectedException;

    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException;

    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     * @throws NoUserSelectedException if there is no user selected in this Model
     */
    void updateFilteredExpenseList(Predicate<Expense> predicate) throws NoUserSelectedException;

    /**
     * Updates statsPeriod to the given {@code period}.
     */
    void updateStatsPeriod(StatsPeriod period);

    /**
     * Returns statsPeriod.
     */
    StatsPeriod getStatsPeriod();

    /**
     * Updates statsMode to the given {@code mode}.
     */
    void updateStatsMode(StatsMode mode);

    /**
     * Returns statsMode.
     */
    StatsMode getStatsMode();

    /**
     * Updates statsNoOfDays to the given {@code periodAmount}.
     */
    void updatePeriodAmount(int periodAmount);

    /**
     * Returns statsNoOfDaysOrMonths.
     */
    int getPeriodAmount();

    /**
     * Returns true if the model has previous expense tracker states to restore.
     */
    boolean canUndoExpenseTracker() throws NoUserSelectedException;

    /**
     * Returns true if the model has undone expense tracker states to restore.
     */
    boolean canRedoExpenseTracker() throws NoUserSelectedException;

    /**
     * Restores the model's expense tracker to its previous state.
     */
    void undoExpenseTracker() throws NoUserSelectedException;

    /**
     * Restores the model's expense tracker to its previously undone state.
     */
    void redoExpenseTracker() throws NoUserSelectedException;

    /**
     * Saves the current expense tracker state for undo/redo.
     */
    void commitExpenseTracker() throws NoUserSelectedException;

    //=========== Login =================================================================================

    /**
     * Selects the ExpenseTracker of the user matching the input LoginCredentials to be used.
     * @param loginCredentials the LoginCredentials used
     * @return  true if successful, false if the input password is incorrect.
     * @throws NonExistentUserException if no user matches the Username in the given LoginCredentials
     * @throws InvalidDataException if the stored user data contains invalid values
     */
    boolean loadUserData(LoginCredentials loginCredentials)
            throws NonExistentUserException, InvalidDataException;

    /**
     * Logs out the user in the model.
     */
    void unloadUserData();

    /**
     * Returns true if there is a user with the input username in memory.
     * @param username the Username of the desired user
     */
    boolean isUserExists(Username username);

    /**
     * Adds a user with the given username and gives him/her an empty ExpenseTracker.
     * @param username the Username of the new user
     * @throws UserAlreadyExistsException if a user with the given username already exists
     */
    void addUser(Username username) throws UserAlreadyExistsException;

    /**
     * Checks if there is a currently logged in user.
     * @return true if a user has been selected to be used. i.e Already logged in
     */
    boolean hasSelectedUser();

    /**
     * Sets the password of the currently logged in user as the new password given.
     * @param newPassword the new password to be set
     * @param plainPassword the string representation of the new password to be set
     */
    void setPassword(Password newPassword, String plainPassword) throws NoUserSelectedException;

    /**
     * Checks if the given password matches that of the currently logged in user. If the user does not have any password
     * set, then they are considered to be matching.
     * @param toCheck the password to check as an optional
     * @return true if the password to check matches that of the currently logged in user, false if it doesn't
     */
    boolean isMatchPassword(Password toCheck) throws NoUserSelectedException;

    /**
     * Encrypts the input String using the current user's encryption key.
     * @param toEncrypt the String to encrypt
     * @return the encrypted String
     * @throws NoUserSelectedException if there is no user selected in this Model
     */
    String encryptString(String toEncrypt) throws NoUserSelectedException;

    /**
     * Decrypts the input String using the current user's encryption key.
     * @param toDecrypt the encrypted String to decrypt
     * @return the decrypted String
     * @throws NoUserSelectedException if there is no user selected in this Model
     */
    String decryptString(String toDecrypt) throws NoUserSelectedException, IllegalValueException;

    /**
     * Returns an unmodifiable view of expenses which fulfill the statistics filter
     *
     * @return {@code ObservableList<Expense>} of expenses which fulfill statistics filter
     * @throws NoUserSelectedException
     */
    ObservableList<Expense> getExpenseStats() throws NoUserSelectedException;

    /**
     * Updates the predicate used for expense statistics
     * @throws NullPointerException if {@code predicate} is null.
     * @throws NoUserSelectedException if there is no user selected in this Model
     */
    void updateExpenseStatsPredicate (Predicate<Expense> predicate) throws NoUserSelectedException;

    /**
     * Modifies the existing maximum totalBudget for the current user
     */
    void modifyMaximumBudget(TotalBudget totalBudget) throws NoUserSelectedException;

    /**
     * Returns the existing maximum totalBudget for the current user
     */
    TotalBudget getMaximumBudget() throws NoUserSelectedException;

    /**
     * Adds Category totalBudget into the expense tracker
     * @param budget a valid {@code CategoryBudget}
     * @throws CategoryBudgetExceedTotalBudgetException Throws this if adding a
     * category totalBudget results in the sum of all category budgets exceeding the total totalBudget.
     */
    void setCategoryBudget(CategoryBudget budget) throws CategoryBudgetExceedTotalBudgetException,
            NoUserSelectedException;

    /**
     * Sets the totalBudget to reset and store spending data after a certain amount of time
     * @param seconds The recurrence frequency
     */
    void setRecurrenceFrequency(long seconds) throws NoUserSelectedException;

    /**
     * Returns a copy of this model.
     */
    Model copy(UserPrefs userPrefs) throws NonExistentUserException, NoUserSelectedException;

    /**
     * Adds a notification of type {@code WarningNotification}to the list of notification
     * @return true if successfully added
     * @throws NoUserSelectedException
     */
    boolean addWarningNotification() throws NoUserSelectedException;

    /**
     * Adds a notification of type {@code TipNotification}to the list of notification
     * @return true if successfully added.
     * @throws NoUserSelectedException
     */
    boolean addTipNotification() throws NoUserSelectedException;

    /**
     * Adds a notification of type {@code GeneralNotification}to the list of notification
     * @throws NoUserSelectedException
     */
    void addGeneralNotification(Notification notif) throws NoUserSelectedException;

    /**
     * Returns an {@code ObservableList} of current notifications
     * @return an {@code ObservableList} of current notifications
     * @throws NoUserSelectedException
     */
    ObservableList<Notification> getNotificationList() throws NoUserSelectedException;

    /**
     * Toggles the ability to add {@code TipNotifications}
     * @param toggleOption to toggle to
     * @throws NoUserSelectedException
     */
    void toggleTipNotification(boolean toggleOption) throws NoUserSelectedException;

    /**
     * Toggles the ability to add {@code WarningNotifications}
     * @param toggleOption to toggle to
     * @throws NoUserSelectedException
     */
    void toggleWarningNotification(boolean toggleOption) throws NoUserSelectedException;

    /**
     * Toggles the ability to add {@code WarningNotifications} and {@code TipNotification}
     * @param toggleOption to toggle to
     * @throws NoUserSelectedException
     */
    void toggleBothNotification(boolean toggleOption) throws NoUserSelectedException;

    /**
     * Returns notificationHandler.
     */
    NotificationHandler getNotificationHandler() throws NoUserSelectedException;

    /**
     * Modify NotificationHandler.
     */
    void modifyNotificationHandler(LocalDateTime time, boolean isTipEnabled, boolean isWarningEnabled)
            throws NoUserSelectedException;

    /**
     * Clears the list in notificationHandler
     */
    void clearNotifications() throws NoUserSelectedException;

    /**
     * Returns the set of category budgets
     */
    HashSet<CategoryBudget> getCategoryBudgets() throws NoUserSelectedException;
}
