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
import seedu.address.model.expense.Person;
import seedu.address.model.user.Username;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private Predicate<Person> expenseStatPredicate;
    private VersionedAddressBook versionedAddressBook;
    private FilteredList<Person> filteredPersons;
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
        this.filteredPersons = null;
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
        this.filteredPersons = null;
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
    public boolean hasPerson(Person person) throws NoUserSelectedException {
        requireNonNull(person);
        if (versionedAddressBook == null) {
            throw new NoUserSelectedException();
        }
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) throws NoUserSelectedException {
        versionedAddressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addPerson(Person person) throws NoUserSelectedException {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) throws NoUserSelectedException {
        requireAllNonNull(target, editedPerson);

        versionedAddressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() throws NoUserSelectedException {
        if (filteredPersons == null) {
            throw new NoUserSelectedException();
        }
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) throws NoUserSelectedException {
        requireNonNull(predicate);
        if (filteredPersons == null) {
            throw new NoUserSelectedException();
        }
        filteredPersons.setPredicate(predicate);
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
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getExpenseStats() throws NoUserSelectedException {
        if (filteredPersons == null) {
            throw new NoUserSelectedException();
        }
        FilteredList<Person> temp = new FilteredList<>(versionedAddressBook.getPersonList());
        temp.setPredicate(expenseStatPredicate);
        return FXCollections.unmodifiableObservableList(temp);
    }

    @Override
    public void updateExpenseStats(Predicate<Person> predicate) throws NoUserSelectedException {
        if (filteredPersons == null) {
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
        this.filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
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
        this.filteredPersons = null;
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
        copy.filteredPersons = new FilteredList<>(copy.versionedAddressBook.getPersonList());
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
        return versionedAddressBook != null && filteredPersons != null && username != null;
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
                && filteredPersons.equals(other.filteredPersons);
    }

}
