package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.encryption.EncryptedCategory;
import seedu.address.model.encryption.EncryptedCost;
import seedu.address.model.encryption.EncryptedDate;
import seedu.address.model.encryption.EncryptedExpense;
import seedu.address.model.encryption.EncryptedName;
import seedu.address.model.encryption.EncryptedTag;
import seedu.address.model.expense.Category;
import seedu.address.model.expense.Cost;
import seedu.address.model.expense.Date;
import seedu.address.model.expense.Name;

/**
 * JAXB-friendly version of the Expense.
 */
public class XmlAdaptedExpense {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Expense's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String category;
    @XmlElement(required = true)
    private String cost;
    @XmlElement(required = true)
    private String date;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedExpense.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedExpense() {}

    /**
     * Constructs an {@code XmlAdaptedExpense} with the given expense details.
     */
    public XmlAdaptedExpense(String name, String category, String cost, String date, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.date = date;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Expense into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedExpense
     */
    public XmlAdaptedExpense(EncryptedExpense source) {
        name = source.getName().getEncryptedString();
        category = source.getCategory().getEncryptedString();
        cost = source.getCost().getEncryptedString();
        date = source.getDate().getEncryptedString();
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted expense object into the model's Expense object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted expense
     */
    public EncryptedExpense toModelType() throws IllegalValueException {
        final List<EncryptedTag> expenseTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            expenseTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (category == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Category.class.getSimpleName()));
        }
        if (cost == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Cost.class.getSimpleName()));
        }
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }
        final Set<EncryptedTag> modelTags = new HashSet<>(expenseTags);

        return new EncryptedExpense(new EncryptedName(name), new EncryptedCategory(category),
                new EncryptedCost(cost), new EncryptedDate(date), modelTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedExpense)) {
            return false;
        }

        XmlAdaptedExpense otherExpense = (XmlAdaptedExpense) other;
        return Objects.equals(name, otherExpense.name)
                && Objects.equals(category, otherExpense.category)
                && Objects.equals(cost, otherExpense.cost)
                && Objects.equals(date, otherExpense.date)
                && tagged.equals(otherExpense.tagged);
    }
}
