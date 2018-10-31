package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.address.testutil.TypicalExpenses.SCHOOLFEE;
import static seedu.address.testutil.TypicalExpenses.getTypicalExpenseTracker;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.exceptions.DuplicateExpenseException;
import seedu.address.model.notification.Notification;
import seedu.address.model.notification.NotificationHandler;
import seedu.address.model.user.Password;
import seedu.address.model.user.Username;
import seedu.address.testutil.ExpenseBuilder;
import seedu.address.testutil.ModelUtil;

public class ExpenseTrackerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ExpenseTracker expenseTracker = new ExpenseTracker(ModelUtil.TEST_USERNAME, null,
            DEFAULT_ENCRYPTION_KEY);

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), expenseTracker.getExpenseList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        this.expenseTracker.resetData(null);
        assertTrue(this.expenseTracker.getMaximumTotalBudget().getBudgetCap() == 0);
    }

    @Test
    public void resetData_withValidReadOnlyExpenseTracker_replacesData() {
        ExpenseTracker newData = getTypicalExpenseTracker();
        expenseTracker.resetData(newData);
        newData.setUsername(ModelUtil.TEST_USERNAME);
        assertEquals(newData, expenseTracker);
        assertEquals(newData.getMaximumTotalBudget(), expenseTracker.getMaximumTotalBudget());
    }

    @Test
    public void resetData_withDuplicateExpenses_throwsDuplicateExpenseException() {
        // Two expenses with the same identity fields
        Expense editedAlice = new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Expense> newExpenses = Arrays.asList(SCHOOLFEE, editedAlice);
        ExpenseTrackerStub newData = new ExpenseTrackerStub(newExpenses);

        thrown.expect(DuplicateExpenseException.class);
        expenseTracker.resetData(newData);
    }

    @Test
    public void hasExpense_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseTracker.hasExpense(null);
    }

    @Test
    public void hasExpense_expenseNotInExpenseTracker_returnsFalse() {
        assertFalse(expenseTracker.hasExpense(SCHOOLFEE));
    }

    @Test
    public void hasExpense_expenseInExpenseTracker_returnsTrue() {
        expenseTracker.addExpense(SCHOOLFEE);
        assertTrue(expenseTracker.hasExpense(SCHOOLFEE));
    }

    @Test
    public void hasExpense_expenseWithSameIdentityFieldsInExpenseTracker_returnsTrue() {
        expenseTracker.addExpense(SCHOOLFEE);
        Expense editedAlice = new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(expenseTracker.hasExpense(editedAlice));
    }

    @Test
    public void getExpenseList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        expenseTracker.getExpenseList().remove(0);
    }

    @Test
    public void setNotifications_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseTracker.setNotifications(null);
    }

    /**
     * A stub ReadOnlyExpenseTracker whose expenses list can violate interface constraints.
     */
    private static class ExpenseTrackerStub implements ReadOnlyExpenseTracker {
        private final ObservableList<Expense> expenses = FXCollections.observableArrayList();
        private final ObservableList<Notification> notifications = FXCollections.observableArrayList();

        ExpenseTrackerStub(Collection<Expense> expenses) {
            this.expenses.setAll(expenses);
        }

        @Override
        public ObservableList<Expense> getExpenseList() {
            return expenses;
        }

        @Override
        public TotalBudget getMaximumTotalBudget() {
            return new TotalBudget("0.00");
        }

        @Override
        public Username getUsername() {
            return ModelUtil.TEST_USERNAME;
        }

        @Override
        public ObservableList<Notification> getNotificationList() {
            return notifications;
        }

        @Override
        public NotificationHandler getNotificationHandler() {
            return new NotificationHandler();
        }

        @Override
        public Optional<Password> getPassword() {
            return Optional.empty();
        }

        @Override
        public boolean isMatchPassword(Password password) {
            return true;
        }

        @Override
        public String getEncryptionKey() {
            return "";
        }
    }
}
