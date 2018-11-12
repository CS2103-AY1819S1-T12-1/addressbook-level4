package seedu.expensetracker.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import seedu.expensetracker.commons.core.Config;
import seedu.expensetracker.commons.core.GuiSettings;
import seedu.expensetracker.commons.core.LogsCenter;
import seedu.expensetracker.commons.events.model.UserLoggedInEvent;
import seedu.expensetracker.commons.events.ui.ExitAppRequestEvent;
import seedu.expensetracker.commons.events.ui.ShowHelpRequestEvent;
import seedu.expensetracker.commons.events.ui.ShowStatsRequestEvent;
import seedu.expensetracker.commons.events.ui.SwapLeftPanelEvent;
import seedu.expensetracker.commons.events.ui.UpdateCategoriesPanelEvent;
import seedu.expensetracker.logic.Logic;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_WIDTH = 900;
    private static final int MIN_HEIGHT = 800;
    private static final double INITIALIZE_ANIMATION_TIME = 0.5;

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private ExpenseListPanel expenseListPanel;
    private Config config;
    private UserPrefs prefs;
    private HelpWindow helpWindow;
    private StatisticsPanel statisticsPanel;

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
    private AnchorPane statisticsPane;

    @FXML
    private CategoriesPanel categoriesPanel;

    @FXML
    private SplitPane rightSplitPane;


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
        Title title = new Title();
        titlePlaceholder.getChildren().add(title.getRoot());

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        hideLoggedInUi();
    }

    //@@author snookerballs
    /**
     * Swaps the panel from statistics to list
     */
    public void swapToList() {
        Timeline timeline = new Timeline();
        leftPanelPlaceholder.setOpacity(0.0);
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(expenseListPanel.getRoot());
        addFadeInAnimation(leftPanelPlaceholder, 0.0, timeline);
        timeline.playFromStart();
    }

    //@@author snookerballs
    /**
     * Swaps the panel from list to statistics
     */
    public void swapToStat() {
        Timeline timeline = new Timeline();
        leftPanelPlaceholder.setOpacity(0.0);
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(statisticsPane);
        addFadeInAnimation(leftPanelPlaceholder, 0.0, timeline);
        timeline.playFromStart();
    }

    //@@author snookerballs
    /**
     * Initialize UI after login
     */
    private void initializeAfterLogin() throws NoUserSelectedException {
        try {
            expenseListPanel = new ExpenseListPanel(logic.getFilteredExpenseList());
        } catch (NoUserSelectedException e) {
            throw new IllegalStateException(e.getMessage());
        }

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getExpenseTrackerDirPath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        BudgetPanel budgetpanel = new BudgetPanel(logic.getMaximumBudget());
        budgetPanelPlaceholder.getChildren().add(budgetpanel.getRoot());

        NotificationPanel notificationPanel = new NotificationPanel(logic.getNotificationList());
        notificationPanelPlaceholder.getChildren().add(notificationPanel.getRoot());

        categoriesPanel = new CategoriesPanel(logic.getCategoryBudgets());

        statisticsPanel = new StatisticsPanel(
                logic.getExpenseStats(),
                logic.getStatsPeriod(),
                logic.getStatsMode(),
                logic.getPeriodAmount()
        );

        statisticsPane = new AnchorPane();
        statisticsPane.setTopAnchor(statisticsPanel.getRoot(), 0.0);
        statisticsPane.setTopAnchor(categoriesPanel.getRoot(), 320.00);
        statisticsPane.getChildren().addAll(statisticsPanel.getRoot(), categoriesPanel.getRoot());

        rightSplitPane.lookupAll(".split-pane-divider").stream()
                .forEach(div -> div.setMouseTransparent(true));

        swapToStat();
        fadeInPanels();
    }

    /**
     * Hides the bottom part of the UI which shows entries in the ExpenseTracker and sync information.
     *
     */
    private void fadeInPanels() {
        leftPanelPlaceholder.setOpacity(0.0);
        budgetPanelPlaceholder.setOpacity(0.0);
        notificationPanelPlaceholder.setOpacity(0.0);
        Timeline timeline = new Timeline();
        addFadeInAnimation(leftPanelPlaceholder, 0.0, timeline);
        addFadeInAnimation(budgetPanelPlaceholder, 0.2, timeline);
        addFadeInAnimation(notificationPanelPlaceholder, 0.4, timeline);
        timeline.playFromStart();
    }

    /**
     *
     */
    public void addFadeInAnimation(Pane pane, double startTime, Timeline timeline) {
        KeyFrame start = new KeyFrame(Duration.seconds(startTime), new KeyValue(pane.opacityProperty(),
                0.0));
        KeyFrame end = new KeyFrame(Duration.seconds(INITIALIZE_ANIMATION_TIME + startTime), new KeyValue(
                pane.opacityProperty(), 1.0));
        timeline.getKeyFrames().addAll(start, end);
    }

    /**
     * Hides the bottom part of the UI which shows entries in the AddressBook and sync information.
     */
    private void hideLoggedInUi() {
        splitPane.setManaged(false);
        splitPane.setVisible(false);
        getPrimaryStage().setMaxHeight(250);
        getPrimaryStage().setMinHeight(250);
        statusbarPlaceholder.setManaged(false);
    }

    /**
     * Shows the bottom part of the UI which shows entries in the ExpenseTracker and sync information.
     */
    private void showLoggedInUi() {
        splitPane.setManaged(true);
        splitPane.setVisible(true);
        getPrimaryStage().setMaxHeight(Integer.MAX_VALUE);
        getPrimaryStage().setMinHeight(MIN_HEIGHT);
        getPrimaryStage().setMinWidth(MIN_WIDTH);
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
        primaryStage.setHeight(Math.max(MIN_HEIGHT, prefs.getGuiSettings().getWindowHeight()));
        primaryStage.setWidth(Math.max(MIN_WIDTH, prefs.getGuiSettings().getWindowWidth()));
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
            statisticsPanel.setData(
                    logic.getExpenseStats(),
                    logic.getStatsPeriod(),
                    logic.getStatsMode(),
                    logic.getPeriodAmount()
            );
        } catch (NoUserSelectedException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Subscribe
    public void handleLoggedInEvent(UserLoggedInEvent event) throws NoUserSelectedException {
        initializeAfterLogin();
        showLoggedInUi();
        initializeAfterLogin();
    }

    //@@author snookerballs
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

    @Subscribe
    public void handleUpdateCategoriesPanelEvent(UpdateCategoriesPanelEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        categoriesPanel.setConnection(event.categoryBudgets);
    }
}
