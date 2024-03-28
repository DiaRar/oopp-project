/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static final Injector INJECTOR = Guice.createInjector(new InjectorModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var startScreen = FXML.load(StartCtrl.class, "client", "scenes", "Start.fxml");
        var overview = FXML.load(OverviewCtrl.class, "client", "scenes", "Overview.fxml");
        var addExpense = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var contactDetails = FXML.load(ContactDetailsCtrl.class, "client", "scenes", "ContactDetails.fxml");
        var debts = FXML.load(DebtsCtrl.class, "client", "scenes", "Debts.fxml");
        var mc = INJECTOR.getInstance(MainCtrl.class);
        var languageUtils = INJECTOR.getInstance(LanguageUtils.class);
        var serverUtils = INJECTOR.getInstance(ServerUtils.class);
        mc.init(primaryStage, startScreen, overview, addExpense, statistics, invitation, contactDetails, debts, serverUtils, languageUtils);
    }
}
