package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.UserLoggedInEvent;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.commons.events.ui.ShowStatsRequestEvent;
import seedu.address.commons.events.ui.SwapLeftPanelEvent;
import seedu.address.logic.Logic;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.NoUserSelectedException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private ExpenseListPanel expenseListPanel;
    private Config config;
    private UserPrefs prefs;
    private HelpWindow helpWindow;
    private StatsWindow statsWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private SplitPane splitPane;

    @FXML
    private StackPane titlePlaceholder;

    @FXML
    private StackPane budgetPanelPlaceholder;

    @FXML
    private StackPane notificationPanelPlaceholder;

    @FXML
    private StackPane leftPanelPlaceholder;

    @FXML
    private SplitPane statisticsSplitPane;


    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setWindowDefaultSize(prefs);

        setAccelerators();
        registerAsAnEventHandler(this);

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        hideLoggedInUi();
    }

    /**
     * Swaps the panel from statistics to list
     */
    public void swapToList() {
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(expenseListPanel.getRoot());
    }

    /**
     * Swaps the panel from list to statistics
     */
    public void swapToStat() {
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(statisticsSplitPane);
    }

    /**
     * Initialize UI after login
     */
    private void initializeAfterLogin() {
        try {
            expenseListPanel = new ExpenseListPanel(logic.getFilteredExpenseList());
        } catch (NoUserSelectedException e) {
            throw new IllegalStateException(e.getMessage());
        }

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getAddressBookDirPath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
        Title title = new Title();
        titlePlaceholder.getChildren().add(title.getRoot());

        BudgetPanel budgetPanel = new BudgetPanel();
        budgetPanelPlaceholder.getChildren().add(budgetPanel.getRoot());

        NotificationPanel notificationPanel = new NotificationPanel();
        notificationPanelPlaceholder.getChildren().add(notificationPanel.getRoot());

        StatisticPanel statisticPanel = new StatisticPanel();
        CategoriesPanel categoriesPanel = new CategoriesPanel();

        statisticsSplitPane = new SplitPane();
        statisticsSplitPane.setOrientation(Orientation.VERTICAL);
        statisticsSplitPane.getItems().add(statisticPanel.getRoot());
        statisticsSplitPane.getItems().add(categoriesPanel.getRoot());

        leftPanelPlaceholder.getChildren().add(expenseListPanel.getRoot());

        swapToList();
    }

    /**
     * Hides the bottom part of the UI which shows entries in the AddressBook and sync information.
     */
    private void hideLoggedInUi() {
        splitPane.setManaged(false);
        splitPane.setVisible(false);
        getPrimaryStage().setHeight(225);
        getPrimaryStage().setMaxHeight(225);
        getPrimaryStage().setMinHeight(225);
        statusbarPlaceholder.setManaged(false);
    }

    /**
     * Shows the bottom part of the UI which shows entries in the AddressBook and sync information.
     */
    private void showLoggedInUi() {
        splitPane.setManaged(true);
        splitPane.setVisible(true);
        getPrimaryStage().setMaxHeight(Integer.MAX_VALUE);
        getPrimaryStage().setMinHeight(600);
        setWindowDefaultSize(prefs);
        statusbarPlaceholder.setManaged(true);
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    /**
     * Opens the stats window or focuses on it if it's already opened.
     */
    @FXML
    public void handleStats() {
        if (statsWindow.isShowing()) {
            statsWindow.close();
        }
        statsWindow.show();
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public ExpenseListPanel getExpenseListPanel() {
        return expenseListPanel;
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }

    @Subscribe
    private void handleShowStatsEvent(ShowStatsRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        try {
            statsWindow = new StatsWindow(logic.getExpenseStats());
        } catch (NoUserSelectedException e) {
            throw new IllegalStateException(e.getMessage());
        }
        handleStats();
    }

    @Subscribe
    public void handleLoggedInEvent(UserLoggedInEvent event) {
        initializeAfterLogin();
        showLoggedInUi();
    }

    @Subscribe
    public void handleSwapLeftPanelEvent(SwapLeftPanelEvent event) {
        switch(event.getPanelType()) {
        case LIST:
            swapToList();
            break;
        case STATISTIC:
            swapToStat();
            break;
        default:
        }
    }
}
