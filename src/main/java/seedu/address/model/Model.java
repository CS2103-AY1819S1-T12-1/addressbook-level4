package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.Person;
import seedu.address.model.user.Username;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData) throws NoUserSelectedException;

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook() throws NoUserSelectedException;

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person) throws NoUserSelectedException;

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target) throws NoUserSelectedException;

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person) throws NoUserSelectedException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void updatePerson(Person target, Person editedPerson) throws NoUserSelectedException;

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList() throws NoUserSelectedException;

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate) throws NoUserSelectedException;

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoAddressBook() throws NoUserSelectedException;

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoAddressBook() throws NoUserSelectedException;

    /**
     * Restores the model's address book to its previous state.
     */
    void undoAddressBook() throws NoUserSelectedException;

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoAddressBook() throws NoUserSelectedException;

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitAddressBook() throws NoUserSelectedException;

    /**
     * Selects the AddressBook of the user with the input username to be used.
     */
    void loadUserData(Username username) throws NonExistentUserException;

    /**
     * Logs out the user in the model.
     */
    void unloadUserData();

    /**
     * Returns true if there is a user with the input username in memory.
     */
    boolean isUserExists(Username username);

    /**
     * Adds a user with the given username and gives him/her an empty AddressBook.
     * @throws UserAlreadyExistsException if a user with the given username already exists
     */
    void addUser(Username username) throws UserAlreadyExistsException;

    /**
     * Returns true if a user has been selected to be used. i.e Already logged in
     */
    boolean hasSelectedUser();

    /** Returns an unmodifiable view of the expense stats*/
    ObservableList<Person> getExpenseStats() throws NoUserSelectedException;

    /**
     * Updates the expense stats
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateExpenseStats(Predicate<Person> predicate) throws NoUserSelectedException;

    /**
     * Returns a copy of this model.
     */
    Model copy(UserPrefs userPrefs) throws NonExistentUserException, NoUserSelectedException;
}
