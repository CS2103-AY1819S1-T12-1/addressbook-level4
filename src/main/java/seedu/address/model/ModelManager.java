package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.UserLoggedInEvent;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.Expense;
import seedu.address.model.user.Username;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private Predicate<Expense> expenseStatPredicate;
    private VersionedAddressBook versionedAddressBook;
    private FilteredList<Expense> filteredExpenses;
    private Username username;
    private final Map<Username, ReadOnlyAddressBook> addressBooks;

    /**
     * Initializes a ModelManager with the given addressBooks and userPrefs.
     */
    public ModelManager(Map<Username, ReadOnlyAddressBook> addressBooks, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBooks, userPrefs);
        this.addressBooks = addressBooks;
        logger.fine("Initializing with address book: " + addressBooks + " and user prefs " + userPrefs);
        this.username = null;
        this.versionedAddressBook = null;
        this.filteredExpenses = null;
    }

    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);
        Map<Username, ReadOnlyAddressBook> addressBooks = new TreeMap<>();
        logger.fine("Initializing with address book: " + addressBooks + " and user prefs " + userPrefs);
        this.addressBooks = addressBooks;
        this.addressBooks.put(addressBook.getUsername(), addressBook);
        this.username = addressBook.getUsername();
        this.versionedAddressBook = null;
        this.filteredExpenses = null;
        try {
            loadUserData(addressBook.getUsername());
        } catch (NonExistentUserException e) {
            throw new IllegalStateException();
        }
    }

    public ModelManager() {
        this(new HashMap<>(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) throws NoUserSelectedException {
        versionedAddressBook.resetData(newData);
        addressBooks.replace(this.username, this.versionedAddressBook);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() throws NoUserSelectedException {
        if (versionedAddressBook == null) {
            throw new NoUserSelectedException();
        }
        return versionedAddressBook;
    }

    /** Raises an event to indicate the model has changed */
    protected void indicateAddressBookChanged() throws NoUserSelectedException {
        if (versionedAddressBook == null) {
            throw new NoUserSelectedException();
        }
        raise(new AddressBookChangedEvent(versionedAddressBook));
    }

    @Override
    public boolean hasExpense(Expense expense) throws NoUserSelectedException {
        requireNonNull(expense);
        if (versionedAddressBook == null) {
            throw new NoUserSelectedException();
        }
        return versionedAddressBook.hasExpense(expense);
    }

    @Override
    public void deleteExpense(Expense target) throws NoUserSelectedException {
        versionedAddressBook.removeExpense(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addExpense(Expense expense) throws NoUserSelectedException {
        versionedAddressBook.addExpense(expense);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
        indicateAddressBookChanged();
    }

    @Override
    public void updateExpense(Expense target, Expense editedExpense) throws NoUserSelectedException {
        requireAllNonNull(target, editedExpense);

        versionedAddressBook.updateExpense(target, editedExpense);
        indicateAddressBookChanged();
    }

    //=========== Filtered Expense List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException {
        if (filteredExpenses == null) {
            throw new NoUserSelectedException();
        }
        return FXCollections.unmodifiableObservableList(filteredExpenses);
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Expense> predicate) throws NoUserSelectedException {
        requireNonNull(predicate);
        if (filteredExpenses == null) {
            throw new NoUserSelectedException();
        }
        filteredExpenses.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() throws NoUserSelectedException {
        if (versionedAddressBook == null) {
            throw new NoUserSelectedException();
        }
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() throws NoUserSelectedException {
        versionedAddressBook.undo();
        indicateAddressBookChanged();
    }

    @Override
    public void redoAddressBook() throws NoUserSelectedException {
        versionedAddressBook.redo();
        indicateAddressBookChanged();
    }

    @Override
    public void commitAddressBook() throws NoUserSelectedException {
        if (versionedAddressBook == null) {
            throw new NoUserSelectedException();
        }
        versionedAddressBook.commit();
    }

    //@@author jonathantjm
    //=========== Stats =================================================================================
    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Expense> getExpenseStats() throws NoUserSelectedException {
        if (filteredExpenses == null) {
            throw new NoUserSelectedException();
        }
        FilteredList<Expense> temp = new FilteredList<>(versionedAddressBook.getExpenseList());
        temp.setPredicate(expenseStatPredicate);
        return FXCollections.unmodifiableObservableList(temp);
    }

    @Override
    public void updateExpenseStats(Predicate<Expense> predicate) throws NoUserSelectedException {
        if (filteredExpenses == null) {
            throw new NoUserSelectedException();
        }
        expenseStatPredicate = predicate;
    }

    //@@author
    //=========== Login =================================================================================
    @Override
    public void loadUserData(Username username) throws NonExistentUserException {
        if (!isUserExists(username)) {
            throw new NonExistentUserException(username, addressBooks.size());
        }
        this.versionedAddressBook = new VersionedAddressBook(addressBooks.get(username));
        this.filteredExpenses = new FilteredList<>(versionedAddressBook.getExpenseList());
        this.username = username;
        addressBooks.replace(this.username, this.versionedAddressBook);
        try {
            indicateUserLoggedIn();
            indicateAddressBookChanged();
        } catch (NoUserSelectedException nuse) {
            throw new IllegalStateException(nuse.getMessage());
        }
    }

    @Override
    public void unloadUserData() {
        this.versionedAddressBook = null;
        this.filteredExpenses = null;
        this.username = null;
    }

    @Override
    public boolean isUserExists(Username toCheck) {
        return addressBooks.containsKey(toCheck);
    }

    /** Raises an event to indicate the user has logged in and has been processed by the model*/
    protected void indicateUserLoggedIn() throws NoUserSelectedException {
        if (this.username == null) {
            throw new NoUserSelectedException();
        }
        raise(new UserLoggedInEvent(this.username));
    }

    @Override
    public Model copy(UserPrefs userPrefs) throws NoUserSelectedException {
        ModelManager copy = new ModelManager(addressBooks, userPrefs);
        copy.versionedAddressBook = new VersionedAddressBook(this.getAddressBook());
        copy.filteredExpenses = new FilteredList<>(copy.versionedAddressBook.getExpenseList());
        copy.username = this.username;
        return copy;
    }

    @Override
    public void addUser(Username newUsername) throws UserAlreadyExistsException {
        if (addressBooks.putIfAbsent(newUsername, new AddressBook(newUsername)) != null) {
            throw new UserAlreadyExistsException(newUsername);
        }
    }

    @Override
    public boolean hasSelectedUser() {
        return versionedAddressBook != null && filteredExpenses != null && username != null;
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
        return versionedAddressBook.equals(other.versionedAddressBook)
                && filteredExpenses.equals(other.filteredExpenses);
    }

}
