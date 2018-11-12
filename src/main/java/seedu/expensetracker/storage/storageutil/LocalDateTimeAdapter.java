package seedu.expensetracker.storage.storageutil;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XmlAdapter that adapts a string to LocalDateTime and back.
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v);
    }
    @Override
    public String marshal (LocalDateTime v) throws Exception {
        return v.toString();
    }
}

