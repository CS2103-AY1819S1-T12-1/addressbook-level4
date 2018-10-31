package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.notification.Tip;

/**
 * A class to access the list of tips stored in the hard disk as a json file
 */
//@@Snookerballs
public class JsonTipsStorage implements TipsStorage {

    private Path filePath;

    public JsonTipsStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getTipsFilePath() {
        return filePath;
    }

    @Override
    public Optional<List<Tip>> readTips() throws IOException {
        return readTips(filePath);
    }

    /**
     * Similar to
     * @param prefsFilePath location of the data. Cannot be null.
     * @throws DataConversionException if the file format is not as expected.
     */
    public Optional<List<Tip>> readTips(Path prefsFilePath) throws IOException {
        return JsonUtil.fromJsonToArray(prefsFilePath, Tip.class);
    }
}
