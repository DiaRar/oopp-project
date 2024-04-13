package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StatisticsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;

    private ObservableList<Expense> expenses;
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
        startUpdates();
    }
    public void startUpdates() {
        createUpdates();
        editUpdates();
        deleteUpdates();
    }
    public void createUpdates() {
        server.registerForUpdates(mainCtrl.getEvent().getId(), e -> Platform.runLater(() -> {
            expenses.add(e);
            createUI(e);
        }));
    }
    public void editUpdates() {
        server.registerForEditUpdates(mainCtrl.getEvent().getId(), e -> Platform.runLater(() -> {
            for (Expense x: expenses) {
                if (x.getId().equals(e.getId())) {
                    updateUI(x, e);
                    expenses.remove(x);
                    expenses.add(e);
                    break;
                }
            }
        }));
    }
    public void deleteUpdates() {
        server.registerForDeleteUpdates(mainCtrl.getEvent().getId(), e -> Platform.runLater(() -> {
            for (Expense x : expenses) {
                if (x.getId().equals(e)) {
                    removeUI(x);
                    expenses.remove(x);
                    break;
                }
            }
        }));
    }
    public void updateUI(Expense x, Expense e) {
        if ((x.getTag() == null && e.getTag() == null) || (x.getTag() != null && x.getTag().equals(e.getTag()))) {
            if (x.getAmount() == e.getAmount()) return;
            else if (e.getTag() == null) {
                data.stream().forEach(y -> {
                    if (y.getName().equals("Other"))y.setPieValue(y.getPieValue() - x.getAmount() + e.getAmount());
                });
            } else {
                data.stream().forEach(y -> {
                    if (y.getName().equals(e.getTag().getName()))y.setPieValue(y.getPieValue() - x.getAmount() + e.getAmount());
                });
            }
        } else {
            removeUI(x);
            createUI(e);
        }
        sum = sum - x.getAmount() + e.getAmount();
        amount.setText("" + sum + "$");
    }
    public void createUI(Expense e) {
        if (e.getTag() == null) {
            data.stream().forEach(x -> {
                if (x.getName().equals("Other")) {
                    x.setPieValue(x.getPieValue() + e.getAmount());
                }
            });
            if (data.stream().filter(x -> x.getName().equals("Other"))
                    .collect(Collectors.toList()).size() == 0) data.add(new PieChart.Data("Other", e.getAmount()));
        } else {
            data.stream().forEach(x -> {
                if (x.getName().equals(e.getTag().getName())) {
                    x.setPieValue(x.getPieValue() + e.getAmount());
                }
            });
            if (data.stream().filter(x -> x.getName().equals(e.getTag().getName())).
                    collect(Collectors.toList()).size() == 0)data.add(new PieChart.Data(e.getTag().getName(), e.getAmount()));
        }
        sum = sum + e.getAmount();
        amount.setText("" + sum + "$");
    }

    public void removeUI(Expense e) {
        if (e.getTag() == null) {
            for (PieChart.Data x : data) {
                if (x.getName().equals("Other")) {
                    if (x.getPieValue() == e.getAmount()) {
                        data.remove(x);
                        break;
                    } else {
                        x.setPieValue(x.getPieValue() - e.getAmount());
                        break;
                    }
                }
            }
        } else {
            for (PieChart.Data x : data) {
                if (x.getName().equals(e.getTag().getName())) {
                   if (x.getPieValue() == e.getAmount()) {
                       data.remove(x);
                       break;
                   } else {
                       x.setPieValue(x.getPieValue() - e.getAmount());
                       break;
                   }
                }
            }
        }

        sum = sum - e.getAmount();
        amount.setText("" + sum + "$");
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
        expenses = FXCollections.observableArrayList(mainCtrl.getExpenseList().stream().collect(Collectors.toList()));
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