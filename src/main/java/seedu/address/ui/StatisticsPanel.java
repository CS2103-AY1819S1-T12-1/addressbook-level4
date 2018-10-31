package seedu.address.ui;

import java.util.LinkedHashMap;
import java.util.Set;

import javafx.beans.binding.Bindings;

import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import seedu.address.logic.commands.StatsCommand.StatsMode;
import seedu.address.logic.commands.StatsCommand.StatsPeriod;

//@@author jonathantjm
/**
 * Panel containing the statistic information.
 */
public class StatisticsPanel extends UiPart<Region> {
    private static final String FXML = "StatisticsPanel.fxml";

    @FXML
    private StackPane chartArea;


    public StatisticsPanel(
            LinkedHashMap<String, Double> statsData,
            StatsPeriod statsPeriod,
            StatsMode statsMode,
            int periodAmount
    ) {
        super(FXML);
        setData(statsData, statsPeriod, statsMode, periodAmount);
    }

    public void setData(LinkedHashMap<String, Double> statsData,
                        StatsPeriod statsPeriod,
                        StatsMode statsMode,
                        int periodAmount
    ) {
        chartArea.getChildren().clear();
        if (statsData.size() == 0) {
            Text text = new Text();
            if (statsPeriod == StatsPeriod.DAY) {
                if (periodAmount == 1) {
                    text.setText("There are no recorded expenditures in the past day");
                } else {
                    text.setText("There are no recorded expenditures in the past " + periodAmount + " days");
                }
            } else {
                if (periodAmount == 1) {
                    text.setText("There are no recorded expenditures in the past month");
                } else {
                    text.setText("There are no recorded expenditures in the past " + periodAmount + " months");
                }
            }
            chartArea.getChildren().add(text);
        } else if (statsMode == StatsMode.TIME) {
            setTimeBasedData(statsData, statsPeriod, periodAmount);
        } else {
            setCategoryBasedData(statsData);
        }
    }

    public void setTimeBasedData(LinkedHashMap<String, Double> statsData, StatsPeriod statsPeriod, int periodAmount) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");
        BarChart stats = new BarChart(xAxis, yAxis);
        if (statsPeriod == StatsPeriod.DAY) {
            if (periodAmount == 1) {
                stats.setTitle("Expenditure over the past " + periodAmount + " Day");
            } else {
                stats.setTitle("Expenditure over the past " + periodAmount + " Days");
            }
        } else {
            if (periodAmount == 1) {
                stats.setTitle("Expenditure over the past " + periodAmount + " Month");
            } else {
                stats.setTitle("Expenditure over the past " + periodAmount + " Months");
            }
        }

        XYChart.Series series = new XYChart.Series();
        series.setName("Day Amounts");
        Set<String> keySet = statsData.keySet();
        for (String s : keySet) {
            series.getData().add(
                    new XYChart.Data<>(
                            s + "\n$" + String.format("%.2f", statsData.get(s)), statsData.get(s)
                    )
            );
        }

        stats.getData().add(series);

        chartArea.getChildren().add(stats);
    }

    public void setCategoryBasedData(LinkedHashMap<String, Double> statsData) {
        PieChart pieChart = new PieChart();

        Set<String> keySet = statsData.keySet();
        for (String s : keySet) {
            pieChart.getData().add(new PieChart.Data(s, statsData.get(s)));
        }

        pieChart.getData().forEach(data ->
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " - $", data.pieValueProperty()
                    )
            )
        );

        pieChart.setLegendSide(Side.BOTTOM);

        chartArea.getChildren().add(pieChart);
    }
}
