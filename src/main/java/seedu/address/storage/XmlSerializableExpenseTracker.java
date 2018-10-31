package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.encryption.EncryptedExpense;
import seedu.address.model.encryption.EncryptedExpenseTracker;
import seedu.address.model.notification.Notification;
import seedu.address.model.user.Password;
import seedu.address.storage.budget.XmlAdaptedTotalBudget;

/**
 * An Immutable ExpenseTracker that is serializable to XML format
 */
@XmlRootElement(name = "expensetracker")
public class XmlSerializableExpenseTracker {

    public static final String MESSAGE_DUPLICATE_EXPENSE = "Expenses list contains duplicate expense(s).";

    @XmlElement
    private List<XmlAdaptedExpense> expenses;
    @XmlElement
    private XmlAdaptedUsername username;
    @XmlElement
    private XmlAdaptedTotalBudget totalBudget;
    @XmlElement
    private XmlAdaptedPassword password;
    @XmlElement
    private XmlAdaptedNotificationHandler notificationHandler;
    @XmlElement
    private List<XmlAdaptedNotification> notifications;

    /**
     * Creates an empty XmlSerializableExpenseTracker.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableExpenseTracker() {
        expenses = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableExpenseTracker(EncryptedExpenseTracker src) {
        this();
        this.username = new XmlAdaptedUsername(src.getUsername());
        this.password = src.getPassword().map(XmlAdaptedPassword::new).orElse(null);
        expenses.addAll(src.getEncryptedExpenses().stream().map(XmlAdaptedExpense::new).collect(Collectors.toList()));
        this.notificationHandler = new XmlAdaptedNotificationHandler(src.getNotificationHandler());
        this.notifications.addAll(src.getNotificationList().stream()
                .map(XmlAdaptedNotification::new).collect(Collectors.toList()));
        this.totalBudget = new XmlAdaptedTotalBudget(src.getMaximumTotalBudget());
    }

    /**
     * Converts this expensetracker into the model's {@code ExpenseTracker} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedExpense}.
     */
    public EncryptedExpenseTracker toModelType() throws IllegalValueException {
        Optional<Password> passwordOptional = Optional.ofNullable(password).map(XmlAdaptedPassword::toModelType);
        EncryptedExpenseTracker expenseTracker;
        if (totalBudget == null || notificationHandler == null) {
            expenseTracker = new EncryptedExpenseTracker(username.toModelType(), passwordOptional.orElse(null));
        } else {
            expenseTracker = new EncryptedExpenseTracker(username.toModelType(), passwordOptional.orElse(null),
                    totalBudget.toModelType(), notificationHandler.toModelType());
        }
        for (XmlAdaptedExpense p : expenses) {
            EncryptedExpense expense = p.toModelType();
            if (expenseTracker.hasExpense(expense)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EXPENSE);
            }
            expenseTracker.addExpense(expense);
        }

        for (XmlAdaptedNotification n : notifications) {
            Notification notification = n.toModelType();
            expenseTracker.addNotification(notification);
        }

        return expenseTracker;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableExpenseTracker)) {
            return false;
        }
        return expenses.equals(((XmlSerializableExpenseTracker) other).expenses)
                && notificationHandler.equals(((XmlSerializableExpenseTracker) other).notificationHandler);
    }
}
