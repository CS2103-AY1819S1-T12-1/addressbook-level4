package seedu.address.ui;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

//@@author jonathantjm
/**
 * Controller for a stats page
 */
public class StatsWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(StatsWindow.class);
    private static final String FXML = "StatsWindow.fxml";

    @FXML
    private Pane chartArea;

    /**
     * Creates a new StatsWindow.
     *
     * @param statsData to use to populate the stats of the StatsWindow
     */
    public StatsWindow(LinkedHashMap<String, Double> statsData) {
        super(FXML, new Stage());

        setData(statsData);
    }

    public void setData(LinkedHashMap<String, Double> statsData) {
        chartArea.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");
        BarChart stats = new BarChart(xAxis, yAxis);
        stats.setTitle("Daily Expenditure");

        XYChart.Series series = new XYChart.Series();
        series.setName("Day Amounts");
        Set<String> keySet = statsData.keySet();
        for (String s : keySet) {
            series.getData().add(new XYChart.Data<>(s, statsData.get(s)));
        }

        stats.getData().add(series);

        chartArea.getChildren().add(stats);
    }

    /**
     * Shows the stats window.
     * @throws IllegalStateException
     * <ul>
     *     <li>
     *         if this method is called on a thread other than the JavaFX Application Thread.
     *     </li>
     *     <li>
     *         if this method is called during animation or layout processing.
     *     </li>
     *     <li>
     *         if this method is called on the primary stage.
     *     </li>
     *     <li>
     *         if {@code dialogStage} is already showing.
     *     </li>
     * </ul>
     */
    public void show() {
        logger.fine("Showing stats page.");
        getRoot().show();
    }


    /**
     * Closes the stats window.
     */
    public void close() {
        getRoot().close();
    }

    /**
     * Returns true if the stats window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Focuses on the stats window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    public Pane getChartArea() {
        return chartArea;
    }
}
