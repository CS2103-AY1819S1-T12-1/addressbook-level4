package seedu.expensetracker.storage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import seedu.expensetracker.model.notification.Tip;

/**
 * Represents a storage for {@link seedu.expensetracker.model.notification.Tips}.
 */
public interface TipsStorage {
    /**
     * Returns the list of tips from storage
     * Returns {@code Optional.empty} if storage is not found.
     */
    Optional<List<Tip>> readTips() throws IOException;
}
