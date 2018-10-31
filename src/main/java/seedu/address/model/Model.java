package seedu.address.model;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.StatsCommand.StatsMode;
import seedu.address.logic.commands.StatsCommand.StatsPeriod;
import seedu.address.model.budget.CategoryBudget;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.exceptions.CategoryBudgetDoesNotExist;
import seedu.address.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.address.model.exceptions.InvalidDataException;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.Expense;
import seedu.address.model.notification.Notification;
import seedu.address.model.notification.NotificationHandler;
import seedu.address.model.user.LoginInformation;
import seedu.address.model.user.Password;
import seedu.address.model.user.Username;

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
     * Updates statsNoOfDays to the given {@code noOfDays}.
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

    //@@author JasonChong96
    //=========== Login =================================================================================

    /**
     * Selects the ExpenseTracker of the user with the input username to be used.
     * Returns true if successful, false if the input password is incorrect.
     */
    boolean loadUserData(LoginInformation loginInformation)
            throws NonExistentUserException, InvalidDataException;

    /**
     * Logs out the user in the model.
     */
    void unloadUserData();

    /**
     * Returns true if there is a user with the input username in memory.
     */
    boolean isUserExists(Username username);

    /**
     * Adds a user with the given username and gives him/her an empty ExpenseTracker.
     * @throws UserAlreadyExistsException if a user with the given username already exists
     */
    void addUser(Username username) throws UserAlreadyExistsException;

    /**
     * Returns true if a user has been selected to be used. i.e Already logged in
     */
    boolean hasSelectedUser();

    /** Returns an unmodifiable view of the expense stats*/
    ObservableList<Expense> getExpenseStats() throws NoUserSelectedException;

    /**
     * Updates the expense stats
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
    void addCategoryBudget(CategoryBudget budget) throws CategoryBudgetExceedTotalBudgetException,
        NoUserSelectedException;

    /**
     * Modifies an existing Category TotalBudget in the expense tracker
     * @param budget a valid {@code CategoryBudget}
     * @throws CategoryBudgetDoesNotExist Throws this if attempting to modify a {@code CategoryBudget} that does not
     * exist
     */
    void modifyCategoryBudget(CategoryBudget budget) throws CategoryBudgetDoesNotExist,
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

}
