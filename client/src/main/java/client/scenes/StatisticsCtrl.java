package client.scenes;

import client.utils.ServerUtils;
import client.utils.ConfigUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

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


    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigUtils utils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        amount.setText("42$");
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("DATA1",10),
                new PieChart.Data("DATA2",10),
                new PieChart.Data("DATA3",10),
                new PieChart.Data("DATA4",12)
        );
        chart.setData(data);
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

}
