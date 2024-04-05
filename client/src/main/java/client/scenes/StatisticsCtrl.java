package client.scenes;

import client.utils.ServerUtils;
import client.utils.ConfigUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.net.URL;
import java.util.*;

public class StatisticsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;


    @FXML
    private Label amount;
    @FXML
    private PieChart chart;
    @FXML
    private Label title;

    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigUtils utils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void startup() {
        title.setText("Statistics of event: " + mainCtrl.getEvent().getName());
        amount.setText("" + getSum() + "$");
        chart.setData(FXCollections.observableArrayList(getData()));
    }

    public void back() {
        mainCtrl.showOverview();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ESCAPE:
                back();
                break;
            default:
                break;
        }
    }

    private ArrayList<PieChart.Data> getData() {
        ArrayList<PieChart.Data> data = new ArrayList<>();
        HashMap<Tag, Double> map = new HashMap<>();
        Tag other = new Tag("Other", new Color(0));
        for (Expense x : mainCtrl.getEvent().getExpenses()) {
            if (x.getTags() == null || x.getTags().size() == 0) {
                if (!map.containsKey(other)) {
                    map.put(other, x.getAmount());
                } else {
                    map.replace(other, map.get(other) + x.getAmount());
                }
                continue;
            };
            for (Tag y : x.getTags()) {
                if (map.containsKey(y)) {
                    map.replace(y, map.get(y) + x.getAmount());
                } else {
                    map.put(y, x.getAmount());
                }
            }
        }
        for (Tag x : map.keySet()) {
            data.add(new PieChart.Data(x.getName(), map.get(x)));
        }
        return data;
    }

    private double getSum() {
        double sum = 0;
        for (Expense x : mainCtrl.getEvent().getExpenses()) {
            sum = sum + x.getAmount();
        }
        return sum;
    }
}