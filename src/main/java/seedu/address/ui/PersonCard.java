package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.expense.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String[] TAG_COLORS = {"teal", "red", "yellow", "blue", "orange", "brown", "green",
        "pink", "black", "grey"};
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;
    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label category;
    @FXML
    private Label cost;
    @FXML
    private Label date;
    @FXML
    private FlowPane tags;

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().expenseName);
        category.setText(person.getCategory().getName());
        cost.setText(person.getCost().value);
        date.setText(person.getDate().toString());
        person.getTags().forEach(tag -> {
            Label tempLabel = new Label(tag.tagName);
            tempLabel.setStyle("-fx-background-color: " + getColorStyleOfTag(tag.tagName));
            tags.getChildren().add(tempLabel);
        });

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }

    /**
     * Returns color style for a specific tag
     */
    public static String getColorStyleOfTag(String tagName) {
        return TAG_COLORS[Math.abs(tagName.hashCode() % TAG_COLORS.length)];
    }
}
