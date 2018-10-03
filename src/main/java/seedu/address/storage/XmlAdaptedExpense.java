package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expense.Category;
import seedu.address.model.expense.Cost;
import seedu.address.model.expense.Date;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Name;
import seedu.address.model.tag.Tag;

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
    public XmlAdaptedExpense(Expense source) {
        name = source.getName().expenseName;
        category = source.getCategory().getName();
        cost = source.getCost().value;
        date = source.getDate().toString();
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted expense object into the model's Expense object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted expense
     */
    public Expense toModelType() throws IllegalValueException {
        final List<Tag> expenseTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            expenseTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (category == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Category.class.getSimpleName()));
        }
        if (!Category.isValidCategory(category)) {
            throw new IllegalValueException(Category.MESSAGE_CATEGORY_CONSTRAINTS);
        }
        final Category modelCategory = new Category(category);

        if (cost == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Cost.class.getSimpleName()));
        }
        if (!Cost.isValidCost(cost)) {
            throw new IllegalValueException(Cost.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Cost modelCost = new Cost(cost);

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.DATE_FORMAT_CONSTRAINTS);
        }
        final Date modelDate = new Date(date);

        final Set<Tag> modelTags = new HashSet<>(expenseTags);

        return new Expense(modelName, modelCategory, modelCost, modelDate, modelTags);
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
