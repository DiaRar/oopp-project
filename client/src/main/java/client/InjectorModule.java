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
import client.utils.Config;
import client.utils.ConfigUtils;
import client.utils.WebSocketUtils;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class InjectorModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ContactDetailsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(InvitationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StatisticsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(WebSocketUtils.class).in(Scopes.SINGLETON);
        binder.bind(ConfigUtils.class).toInstance(createConfigUtils());
        binder.bind(Config.class).toInstance(createConfig());
        binder.bind(ServerUtils.class).in(Scopes.NO_SCOPE);
        binder.bind(LanguageUtils.class).in(Scopes.SINGLETON);
        binder.bind(AddExpenseCtrl.class).in(Scopes.SINGLETON);
    }

    private ConfigUtils createConfigUtils() {
        ConfigUtils utils = new ConfigUtils();
        Path path = Paths.get("./client/src/main/resources/config/recents.csv");
        utils.setRecentsFile(new File(path.toAbsolutePath().toString()));
        return utils;
    }

    private Config createConfig() {
        try {
            return Config.read(new File("client/src/main/resources/config/config.properties"));
        } catch (IOException e) {
            // TODO: Add alert box to say config was not set up
            throw new RuntimeException(e);
        }
    }

}