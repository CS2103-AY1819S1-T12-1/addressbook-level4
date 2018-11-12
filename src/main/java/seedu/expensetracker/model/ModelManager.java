package seedu.expensetracker.model;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.expensetracker.model.budget.TotalBudget.NOT_SET;
import static seedu.expensetracker.model.budget.TotalBudget.SPENDING_RESET;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.model.encryption.EncryptionUtil.createEncryptionKey;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.expensetracker.commons.core.ComponentManager;
import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.core.LogsCenter;
import seedu.expensetracker.commons.events.model.ExpenseTrackerChangedEvent;
import seedu.expensetracker.commons.events.model.UserLoggedInEvent;
import seedu.expensetracker.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.logic.commands.StatsCommand.StatsMode;
import seedu.expensetracker.logic.commands.StatsCommand.StatsPeriod;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.encryption.EncryptedExpenseTracker;
import seedu.expensetracker.model.encryption.EncryptionUtil;
import seedu.expensetracker.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.expense.Date;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.GeneralNotification;
import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.notification.NotificationHandler;
import seedu.expensetracker.model.notification.TipNotification;
import seedu.expensetracker.model.notification.Tips;
import seedu.expensetracker.model.notification.WarningNotification;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;

/**
 * Represents the in-memory model of the expense tracker data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger LOGGER = LogsCenter.getLogger(ModelManager.class);
    private static Tips tips;

    private VersionedExpenseTracker versionedExpenseTracker;
    private FilteredList<Expense> filteredExpenses;


    //Stats related variables
    private StatsPeriod statsPeriod;
    private StatsMode statsMode;
    private Predicate<Expense> expenseStatPredicate;
    private int periodAmount;
    private final Map<Username, EncryptedExpenseTracker> expenseTrackers;

    /**
     * Initializes a ModelManager with the given expenseTrackers and userPrefs.
     */
    public ModelManager(Map<Username, EncryptedExpenseTracker> expenseTrackers, UserPrefs userPrefs, Tips tips) {
        super();
        requireAllNonNull(expenseTrackers, userPrefs);
        this.expenseTrackers = expenseTrackers;
        LOGGER.fine("Initializing with expense tracker: " + expenseTrackers + " and user prefs " + userPrefs);
        this.versionedExpenseTracker = null;
        this.filteredExpenses = null;
        this.tips = tips;
        this.statsPeriod = defaultStatsPeriod();
        this.statsMode = defaultStatsMode();
        this.expenseStatPredicate = defaultExpensePredicate();
        this.periodAmount = defaultPeriodAmount();
    }

    /**
     * Initializes a ModelManager with an input ExpenseTracker and UserPrefs. The ModelManager will be logged into
     * the input ExpenseTracker. Used for testing purposes.
     * @param expenseTracker the ExpenseTracker to be used
     * @param userPrefs the UserPrefs to be used
     */
    public ModelManager(ReadOnlyExpenseTracker expenseTracker, UserPrefs userPrefs, String password) {
        super();
        requireAllNonNull(expenseTracker, userPrefs);
        Map<Username, EncryptedExpenseTracker> expenseTrackers = new TreeMap<>();
        LOGGER.fine("Initializing with expense tracker: " + expenseTrackers + " and user prefs " + userPrefs);
        this.expenseTrackers = expenseTrackers;

        try {
            this.expenseTrackers.put(expenseTracker.getUsername(),
                    EncryptionUtil.encryptTracker(expenseTracker));
            this.versionedExpenseTracker = null;
            this.filteredExpenses = null;
            this.statsPeriod = defaultStatsPeriod();
            this.statsMode = defaultStatsMode();
            this.expenseStatPredicate = defaultExpensePredicate();
            this.tips = new Tips();
            LoginCredentials loginCredentials = new LoginCredentials(expenseTracker.getUsername(), password);
            loadUserData(loginCredentials);
        } catch (NonExistentUserException | IllegalValueException | InvalidDataException e) {
            // The ExpenseTracker data is guaranteed to be valid and added to the ModelManager.
            throw new IllegalStateException();
        }
    }

    public ModelManager() {
        this(new HashMap<>(), new UserPrefs(), new Tips());
    }

    @Override
    public void resetData(ReadOnlyExpenseTracker newData) throws NoUserSelectedException {
        versionedExpenseTracker.resetData(newData);
        try {
            expenseTrackers.replace(this.versionedExpenseTracker.getUsername(),
                    EncryptionUtil.encryptTracker(this.versionedExpenseTracker));
        } catch (IllegalValueException e) {
            throw new IllegalStateException("Illegal values in reset Expense Tracker");
        }
        indicateExpenseTrackerChanged();
    }

    @Override
    public ReadOnlyExpenseTracker getExpenseTracker() throws NoUserSelectedException {
        requireUserSelected();
        return this.versionedExpenseTracker;
    }

    /** Raises an event to indicate the model has changed */
    protected void indicateExpenseTrackerChanged() throws NoUserSelectedException {
        requireUserSelected();
        try {
            raise(new ExpenseTrackerChangedEvent(EncryptionUtil.encryptTracker(versionedExpenseTracker)));
        } catch (IllegalValueException e) {
            throw new IllegalStateException("Illegal value in expense tracker");
        }
    }

    @Override
    public boolean hasExpense(Expense expense) throws NoUserSelectedException {
        requireNonNull(expense);
        requireUserSelected();
        return versionedExpenseTracker.hasExpense(expense);
    }

    @Override
    public void deleteExpense(Expense target) throws NoUserSelectedException {
        versionedExpenseTracker.removeExpense(target);
        indicateExpenseTrackerChanged();
    }

    @Override
    public boolean addExpense(Expense expense) throws NoUserSelectedException {
        requireUserSelected();
        boolean budgetNotExceeded = versionedExpenseTracker.addExpense(expense);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
        indicateExpenseTrackerChanged();
        return budgetNotExceeded;
    }

    @Override
    public void updateExpense(Expense target, Expense editedExpense) throws NoUserSelectedException {
        requireAllNonNull(target, editedExpense);

        versionedExpenseTracker.updateExpense(target, editedExpense);
        indicateExpenseTrackerChanged();
    }

    //=========== Filtered Expense List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of
     * {@code versionedExpenseTracker}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException {
        requireUserSelected();
        return FXCollections.unmodifiableObservableList(filteredExpenses);
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Expense> predicate) throws NoUserSelectedException {
        requireNonNull(predicate);
        requireUserSelected();
        filteredExpenses.setPredicate(predicate);
    }

    //@@author Snookerballs
    //=========== Notification =================================================================================
    @Override
    public boolean addWarningNotification() throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }
        boolean isNotificationAdded = this.versionedExpenseTracker.checkIfAddWarningNotification(getMaximumBudget());
        if (isNotificationAdded) {
            LOGGER.info("Warning Notification will be sent.");
            this.versionedExpenseTracker.addNotificationToTop(new WarningNotification(getMaximumBudget()));
            indicateExpenseTrackerChanged();
        }
        return isNotificationAdded;
    }

    @Override
    public boolean addTipNotification() throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }

        boolean isNotificationAdded = this.versionedExpenseTracker.checkIfAddTipNotification();
        if (isNotificationAdded) {
            LOGGER.info("Tip Notification will be sent.");
            this.versionedExpenseTracker.addNotificationToTop(new TipNotification(tips));
            indicateExpenseTrackerChanged();
        }
        return isNotificationAdded;
    }

    @Override
    public void addGeneralNotification(Notification notif) throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }
        this.versionedExpenseTracker.addNotificationToTop(notif);
        indicateExpenseTrackerChanged();
    }

    @Override
    public ObservableList<Notification> getNotificationList() throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }

        return this.versionedExpenseTracker.getNotificationList();
    }

    @Override
    public void toggleTipNotification(boolean toggleOption) throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }
        versionedExpenseTracker.toggleTipNotification(toggleOption);
        indicateExpenseTrackerChanged();
    }

    @Override
    public void toggleWarningNotification(boolean toggleOption) throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }
        versionedExpenseTracker.toggleWarningNotification(toggleOption);
        indicateExpenseTrackerChanged();
    }

    @Override
    public void toggleBothNotification(boolean toggleOption) throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }

        versionedExpenseTracker.toggleTipNotification(toggleOption);
        versionedExpenseTracker.toggleWarningNotification(toggleOption);
        indicateExpenseTrackerChanged();
    }

    @Override
    public NotificationHandler getNotificationHandler() throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }

        return versionedExpenseTracker.getNotificationHandler();
    }

    @Override
    public void modifyNotificationHandler(LocalDateTime time, boolean isTipEnabled, boolean isWarningEnabled)
            throws NoUserSelectedException {
        if (versionedExpenseTracker == null) {
            throw new NoUserSelectedException();
        }
        this.versionedExpenseTracker.modifyNotificationHandler(time, isTipEnabled, isWarningEnabled);
    }

    @Override
    public void clearNotifications() throws NoUserSelectedException {
        if (!hasSelectedUser()) {
            throw new NoUserSelectedException();
        }

        versionedExpenseTracker.clearNotifications();
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoExpenseTracker() throws NoUserSelectedException {
        requireUserSelected();
        return versionedExpenseTracker.canUndo();
    }

    @Override
    public boolean canRedoExpenseTracker() {
        return versionedExpenseTracker.canRedo();
    }

    @Override
    public void undoExpenseTracker() throws NoUserSelectedException {
        versionedExpenseTracker.undo();
        indicateExpenseTrackerChanged();
    }

    @Override
    public void redoExpenseTracker() throws NoUserSelectedException {
        versionedExpenseTracker.redo();
        indicateExpenseTrackerChanged();
    }

    @Override
    public void commitExpenseTracker() throws NoUserSelectedException {
        requireUserSelected();
        versionedExpenseTracker.commit();
    }

    //@author winsonhys
    //========== TotalBudget ====================================================================

    @Override
    public void modifyMaximumBudget(TotalBudget totalBudget) throws NoUserSelectedException {
        requireUserSelected();
        this.versionedExpenseTracker.modifyMaximumBudget(totalBudget);
        indicateExpenseTrackerChanged();
    }

    @Override
    public void setRecurrenceFrequency(long seconds) throws NoUserSelectedException {
        requireUserSelected();
        this.versionedExpenseTracker.setRecurrenceFrequency(seconds);
        indicateExpenseTrackerChanged();
    }

    @Override
    public void setCategoryBudget(CategoryBudget budget) throws CategoryBudgetExceedTotalBudgetException,
        NoUserSelectedException {
        requireUserSelected();
        this.versionedExpenseTracker.setCategoryBudget(budget);
        indicateExpenseTrackerChanged();
    }


    @Override
    public TotalBudget getMaximumBudget() throws NoUserSelectedException {
        requireUserSelected();
        return this.versionedExpenseTracker.getMaximumTotalBudget();
    }

    @Override
    public HashSet<CategoryBudget> getCategoryBudgets() throws NoUserSelectedException {
        requireUserSelected();
        return this.versionedExpenseTracker.getCategoryBudgets();
    }

    //@@author jonathantjm
    //=========== Stats =================================================================================
    /**
     * Returns an unmodifiable view of expenses, filtered by {@code expenseStatPredicate} and sorted by date.
     *
     * @return {@code ObservableList<Expense>} of expenses which fulfill statistics filter
     * @throws NoUserSelectedException
     */
    @Override
    public ObservableList<Expense> getExpenseStats() throws NoUserSelectedException {
        requireUserSelected();
        FilteredList<Expense> filteredList = new FilteredList<>(versionedExpenseTracker.getExpenseList());
        filteredList.setPredicate(expenseStatPredicate);

        SortedList<Expense> sortedList = new SortedList<>(filteredList);
        Comparator<Expense> byDate = (Expense a, Expense b) -> (-1 * Date.compare(a.getDate(), b.getDate()));
        sortedList.setComparator(byDate);

        return FXCollections.unmodifiableObservableList(sortedList);
    }

    @Override
    public void updateExpenseStatsPredicate (Predicate<Expense> predicate) throws NoUserSelectedException {
        requireUserSelected();
        expenseStatPredicate = predicate;
    }

    @Override
    public void updateStatsPeriod(StatsPeriod period) {
        this.statsPeriod = period;
    }

    @Override
    public StatsPeriod getStatsPeriod() {
        return this.statsPeriod;
    }

    @Override
    public void updateStatsMode(StatsMode mode) {
        this.statsMode = mode;
    }

    @Override
    public StatsMode getStatsMode() {
        return this.statsMode;
    }

    @Override
    public void updatePeriodAmount(int periodAmount) {
        this.periodAmount = periodAmount;
    }

    @Override
    public int getPeriodAmount() {
        return this.periodAmount;
    }

    private StatsPeriod defaultStatsPeriod() {
        return StatsPeriod.DAY;
    }

    private StatsMode defaultStatsMode() {
        return StatsMode.TIME;
    }

    private int defaultPeriodAmount() {
        return 7;
    }

    private Predicate <Expense> defaultExpensePredicate() {
        LocalDateTime now = LocalDateTime.now().minusDays(7);
        return e -> e.getDate().getFullDate().isAfter(now);
    }

    //@@author JasonChong96
    //=========== Login =================================================================================
    @Override
    public boolean loadUserData(LoginCredentials loginCredentials)
            throws NonExistentUserException, InvalidDataException {
        requireAllNonNull(loginCredentials);
        LOGGER.fine("Attempting to load user data...");
        Username username = loginCredentials.getUsername();
        Optional<Password> password = loginCredentials.getPassword();
        Optional<String> plainPassword = loginCredentials.getPlainPassword();
        EncryptedExpenseTracker encryptedTracker = expenseTrackers.get(username);
        if (!isUserExists(username)) {
            LOGGER.fine("Username \"" + username + "\" does not exist.");
            throw new NonExistentUserException(username, expenseTrackers.size());
        }
        if (!encryptedTracker.isMatchPassword(password.orElse(null))) {
            LOGGER.fine("Failed to load user data as user has typed in an incorrect password");
            return false;
        }

        saveCurrentUserToMapIfExists();
        String encryptionKey = getEncryptionKeyForData(encryptedTracker, plainPassword.orElse(null));
        ExpenseTracker decryptedTrackerData = getDecryptedTrackerData(username, encryptionKey);
        this.versionedExpenseTracker = new VersionedExpenseTracker(decryptedTrackerData);
        this.filteredExpenses = new FilteredList<>(versionedExpenseTracker.getExpenseList());

        try {
            indicateUserLoggedIn();
            indicateExpenseTrackerChanged();
            String budgetStatus = checkBudgetRestart();
            if (budgetStatus.equals(NOT_SET)) {
                addGeneralNotification(new GeneralNotification("Recurrence",
                    "Recurrence "
                        + "time has not been set!"));
            } else if (budgetStatus.equals(SPENDING_RESET)) {
                addGeneralNotification(new GeneralNotification("Now you have money!",
                    "Spending has been reset!"));
            }
            addTipNotification();
        } catch (NoUserSelectedException nuse) {
            LOGGER.severe("NoUserSelectedException thrown after loading user data");
            throw new IllegalStateException("NoUserSelectedException thrown after loading user data");
        }
        LOGGER.fine("User data loaded: " + username);
        return true;
    }

    /**
     * Saves the data of the currently logged in user to the expense trackers map. Does nothing if no user is
     * currently logged in.
     */
    private void saveCurrentUserToMapIfExists() {
        if (!hasSelectedUser()) {
            return;
        }
        try {
            expenseTrackers.replace(versionedExpenseTracker.getUsername(),
                    EncryptionUtil.encryptTracker(this.versionedExpenseTracker));
        } catch (IllegalValueException e) {
            throw new IllegalStateException("Illegal value in old expense tracker.");
        }
    }

    private void requireUserSelected() throws NoUserSelectedException {
        if (!hasSelectedUser()) {
            throw new NoUserSelectedException();
        }
    }

    /**
     * Gets the decrypted expense tracker data of the user with the input username using the given encryption key.
     * @param username the username of the user
     * @param encryptionKey the key to use
     * @return the decrypted expense tracker data of the user as an ExpenseTracker object
     * @throws InvalidDataException if the expense tracker data of the user contains invalid fields or the
     * encryption key is invalid
     */
    private ExpenseTracker getDecryptedTrackerData(Username username, String encryptionKey)
            throws InvalidDataException {
        EncryptedExpenseTracker encryptedTracker = expenseTrackers.get(username);
        ExpenseTracker decryptedTracker;
        try {
            decryptedTracker = encryptedTracker.decryptTracker(encryptionKey);
        } catch (IllegalValueException e) {
            // Invalid expense tracker data associated with the user. Cannot be a key issue as the password has been
            // previously verified.
            throw new InvalidDataException();
        }
        return decryptedTracker;
    }

    /**
     * Generates the encryption key for a given encrypted expense tracker data and its password in plain text form.
     * The key generated can be used to decrypt the data if and only if password given is accurate.
     * @param tracker the data to decrypt
     * @param plainPassword the password in plain text form
     * @return the encryption key
     */
    private String getEncryptionKeyForData(EncryptedExpenseTracker tracker, String plainPassword) {
        requireNonNull(tracker);
        if (!tracker.getPassword().isPresent()) {
            return DEFAULT_ENCRYPTION_KEY;
        } else {
            requireNonNull(plainPassword);
            return createEncryptionKey(plainPassword);
        }
    }

    @Override
    public void unloadUserData() {
        this.versionedExpenseTracker = null;
        this.filteredExpenses = null;
    }

    @Override
    public boolean isUserExists(Username toCheck) {
        return expenseTrackers.containsKey(toCheck);
    }

    @Override
    public boolean isMatchPassword(Password toCheck) throws NoUserSelectedException {
        requireUserSelected();
        return versionedExpenseTracker.isMatchPassword(toCheck);
    }

    @Override
    public String encryptString(String toEncrypt) throws NoUserSelectedException {
        requireUserSelected();
        try {
            return EncryptionUtil.encryptString(toEncrypt, versionedExpenseTracker.getEncryptionKey());
        } catch (IllegalValueException e) {
            LOGGER.severe("User has invalid encryption key after loading user data");
            throw new IllegalStateException("User's encryption key is invalid.");
        }
    }

    @Override
    public String decryptString(String toDecrypt) throws NoUserSelectedException, IllegalValueException {
        requireUserSelected();
        return EncryptionUtil.decryptString(toDecrypt, versionedExpenseTracker.getEncryptionKey());
    }

    /** Raises an event to indicate the user has logged in and has been processed by the model*/
    protected void indicateUserLoggedIn() throws NoUserSelectedException {
        requireUserSelected();
        LOGGER.fine("Raising UserLoggedInEvent");
        raise(new UserLoggedInEvent(this.versionedExpenseTracker.getUsername()));
    }

    /**
     * Checks if totalBudget is required to restart due to recurrence
     */
    protected String checkBudgetRestart() throws NoUserSelectedException {
        String response = this.versionedExpenseTracker.checkBudgetRestart();
        EventsCenter.getInstance().post(new UpdateBudgetPanelEvent(this.getMaximumBudget()));
        indicateExpenseTrackerChanged();
        return response;
    }


    @Override
    public Model copy(UserPrefs userPrefs) throws NoUserSelectedException {

        ModelManager copy = new ModelManager(expenseTrackers, userPrefs, tips);
        copy.versionedExpenseTracker = new VersionedExpenseTracker(this.getExpenseTracker());
        copy.filteredExpenses = new FilteredList<>(copy.versionedExpenseTracker.getExpenseList());
        return copy;
    }

    @Override
    public void addUser(Username newUsername) throws UserAlreadyExistsException {
        if (expenseTrackers.putIfAbsent(newUsername,
                new EncryptedExpenseTracker(newUsername, null)) != null) {
            throw new UserAlreadyExistsException(newUsername);
        }
    }

    @Override
    public boolean hasSelectedUser() {
        return versionedExpenseTracker != null && filteredExpenses != null;
    }

    @Override
    public void setPassword(Password password, String plainPassword) throws NoUserSelectedException {
        requireUserSelected();
        versionedExpenseTracker.setPassword(password);
        versionedExpenseTracker.setEncryptionKey(createEncryptionKey(plainPassword));
        indicateExpenseTrackerChanged();
        try {
            expenseTrackers.replace(this.versionedExpenseTracker.getUsername(),
                    EncryptionUtil.encryptTracker(this.versionedExpenseTracker));
        } catch (IllegalValueException e) {
            LOGGER.severe("Illegal key created for current expense tracker.");
            throw new IllegalStateException("Illegal key created for current expense tracker.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedExpenseTracker.equals(other.versionedExpenseTracker)
                && filteredExpenses.equals(other.filteredExpenses);
    }

}
