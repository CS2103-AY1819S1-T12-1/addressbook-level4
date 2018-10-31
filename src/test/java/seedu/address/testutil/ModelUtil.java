package seedu.address.testutil;

import static seedu.address.testutil.TypicalExpenses.getTypicalExpenseTracker;

import java.time.LocalDateTime;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.budget.TotalBudget;
import seedu.address.model.exceptions.InvalidDataException;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.user.LoginInformation;
import seedu.address.model.user.Username;



//@@author JasonChong96
/**
 * Helper methods related to models.
 */
public class ModelUtil {
    public static final Username TEST_USERNAME = new Username("AAA");

    /**
     * An extension of the {@code ModelManager} where developers can pass in the time instead of checking against
     * system clock. Class of a model with a test user that is already logged in with username TEST_USERNAME.
     * The expense tracker is empty. TotalBudget cap is $28 with $10 current expenses
     */
    public static Model modelWithTestUser() throws NonExistentUserException, UserAlreadyExistsException,
            NoUserSelectedException, InvalidDataException, ParseException {
        Model model = new ModelManager();
        model.addUser(TypicalExpenses.SAMPLE_USERNAME);
        model.loadUserData(new LoginInformation(TypicalExpenses.SAMPLE_USERNAME, null));
        model.getNotificationHandler().clearList();
        model.modifyNotificationHandler(LocalDateTime.parse("2018-10-01T17:26:18.868275"), true,
                true);
        model.modifyMaximumBudget(new TotalBudget(28.00, 0.00, LocalDateTime.parse("2018-10-10T10:11:30"), 50000));
        return model;
    }

    /**
     * Creates a model with a Typical expense tracker that is logged in.
     * @return a ModelManager with the typical expense tracker loaded
     */
    public static ModelManager getTypicalModel() {
        return new ModelManager(getTypicalExpenseTracker(), new UserPrefs(), null);
    }
}


