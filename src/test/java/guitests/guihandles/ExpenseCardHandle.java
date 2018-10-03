package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.expense.Expense;

/**
 * Provides a handle to a expense card in the expense list panel.
 */
public class ExpenseCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#cost";
    private static final String CATEGORY_FIELD_ID = "#category";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label costLabel;
    private final Label categoryLabel;
    private final List<Label> tagLabels;

    public ExpenseCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        nameLabel = getChildNode(NAME_FIELD_ID);
        costLabel = getChildNode(ADDRESS_FIELD_ID);
        categoryLabel = getChildNode(CATEGORY_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getCost() {
        return costLabel.getText();
    }

    public String getCategory() {
        return categoryLabel.getText();
    }


    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code expense}.
     */
    public boolean equals(Expense expense) {
        return getName().equals(expense.getName().expenseName)
                && getCost().equals(expense.getCost().value)
                && getCategory().equals(expense.getCategory().getName())
                && ImmutableMultiset.copyOf(getTags()).equals(ImmutableMultiset.copyOf(expense.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.toList())));
    }
}
