package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.model.notification.Tip;

/**
 * Represents a storage for {@link seedu.address.model.notification.Tips}.
 */
public interface TipsStorage {
    Optional<List<Tip>> readTips() throws IOException;

    /**
     * Returns the file path of the UserPrefs data file.
     */
    Path getTipsFilePath();
}
