package seedu.address.model.notification;

//@@Snookerballs
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a tip that will be displayed as a notification.
 */
public class Tip {
    private String header = "";
    private String body = "";

    public Tip() {

    }

    public Tip(String header, String body) {
        requireAllNonNull(header, body);
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return header + " " + body;
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, body);
    }

}
