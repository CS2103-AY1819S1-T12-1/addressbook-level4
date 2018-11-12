package guitests.guihandles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import seedu.expensetracker.model.expense.Expense;

/**
 * Provides a handle for {@code ExpenseListPanel} containing the list of {@code ExpenseCard}.
 */
public class ExpenseListPanelHandle extends NodeHandle<ListView<Expense>> {
    public static final String EXPENSE_LIST_VIEW_ID = "#expenseListView";

    private static final String CARD_PANE_ID = "#cardPane";

    private Optional<Expense> lastRememberedSelectedExpenseCard;

    public ExpenseListPanelHandle(ListView<Expense> expenseListPanelNode) {
        super(expenseListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code ExpenseCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public ExpenseCardHandle getHandleToSelectedCard() {
        List<Expense> selectedExpenseList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedExpenseList.size() != 1) {
            throw new AssertionError("Expense list size expected 1.");
        }

        return getAllCardNodes().stream()
                .map(ExpenseCardHandle::new)
                .filter(handle -> handle.equals(selectedExpenseList.get(0)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<Expense> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display {@code expense}.
     */
    public void navigateToCard(Expense expense) {
        if (!getRootNode().getItems().contains(expense)) {
            throw new IllegalArgumentException("Expense does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(expense);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Navigates the listview to {@code index}.
     */
    public void navigateToCard(int index) {
        if (index < 0 || index >= getRootNode().getItems().size()) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(index);
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Selects the {@code ExpenseCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Returns the expense card handle of a expense associated with the {@code index} in the list.
     * @throws IllegalStateException if the selected card is currently not in the scene graph.
     */
    public ExpenseCardHandle getExpenseCardHandle(int index) {
        return getAllCardNodes().stream()
                .map(ExpenseCardHandle::new)
                .filter(handle -> handle.equals(getExpense(index)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private Expense getExpense(int index) {
        return getRootNode().getItems().get(index);
    }

    /**
     * Returns all card nodes in the scene graph.
     * Card nodes that are visible in the listview are definitely in the scene graph, while some nodes that are not
     * visible in the listview may also be in the scene graph.
     */
    private Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    /**
     * Remembers the selected {@code ExpenseCard} in the list.
     */
    public void rememberSelectedExpenseCard() {
        List<Expense> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedExpenseCard = Optional.empty();
        } else {
            lastRememberedSelectedExpenseCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code ExpenseCard} is different from the value remembered by the most recent
     * {@code rememberSelectedExpenseCard()} call.
     */
    public boolean isSelectedExpenseCardChanged() {
        List<Expense> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedExpenseCard.isPresent();
        } else {
            return !lastRememberedSelectedExpenseCard.isPresent()
                    || !lastRememberedSelectedExpenseCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
