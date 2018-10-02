package seedu.address.logic;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.Person;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException, NoUserSelectedException,
            UserAlreadyExistsException, NonExistentUserException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = addressBookParser.parseCommand(commandText);
            return command.execute(model, history);
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() throws NoUserSelectedException {
        return model.getFilteredPersonList();
    }

    //@@author jonathantjm
    public LinkedHashMap<String, Double> getExpenseStats() throws NoUserSelectedException {
        ObservableList<Person> expenseList = model.getExpenseStats();
        LinkedHashMap<String, Double> stats = new LinkedHashMap<>();
        for (Person e : expenseList) {
            if (stats.containsKey(e.getDate().toString())) {
                stats.put(
                    e.getDate().toString(),
                    stats.get(e.getDate().toString()) + Double.parseDouble(e.getCost().value)
                );
            } else {
                stats.put(e.getDate().toString(), Double.parseDouble(e.getCost().value));
            }
        }
        return stats;
    }

    //@@author
    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
