package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StatisticsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;

    private ObservableList<PieChart.Data> data;
    private double sum;

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
        sum = getSum();
        amount.setText("" + sum + "$");
        data = FXCollections.observableArrayList(getData());
        chart.setData(data);
        server.registerForUpdates(mainCtrl.getEvent().getId(), e -> Platform.runLater(() -> {
            if (e.getTag() == null) {
                boolean b = true;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getName().equals("Other")) {
                        double temp = e.getAmount() + data.get(i).getPieValue();
                        data.remove(i);
                        data.add(new PieChart.Data("Other", temp));
                        b = false;
                        break;
                    }
                }
                if (b) data.add(new PieChart.Data("Other", e.getAmount()));
            } else {
                    Tag x = e.getTag();
                    boolean b = true;
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getName().equals(x.getName())) {
                            double temp = e.getAmount() + data.get(i).getPieValue();
                            data.remove(i);
                            data.add(new PieChart.Data(x.getName(), temp));
                            b = false;
                            break;
                        }
                    }
                    if (b) data.add(new PieChart.Data(x.getName(), e.getAmount()));
                }
            sum = sum + e.getAmount();
            amount.setText("" + sum + "$");
        }));
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
        Tag other = new Tag("Other", "#000000");
        for (Expense x : mainCtrl.getExpenseList()) {
            if (x.getTag() == null) {
                if (!map.containsKey(other)) {
                    map.put(other, x.getAmount());
                } else {
                    map.replace(other, map.get(other) + x.getAmount());
                }
                continue;
            };
            Tag y = x.getTag();
            if (map.containsKey(y)) {
                map.replace(y, map.get(y) + x.getAmount());
            } else {
                map.put(y, x.getAmount());
            }
        }
        for (Tag x : map.keySet()) {
            data.add(new PieChart.Data(x.getName(), map.get(x)));
        }
        return data;
    }

    private double getSum() {
        double sum = 0;
        for (Expense x : mainCtrl.getExpenseList()) {
            sum = sum + x.getAmount();
        }
        return sum;
    }
}