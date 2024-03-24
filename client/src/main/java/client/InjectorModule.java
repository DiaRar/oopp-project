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

import client.scenes.InvitationCtrl;
import client.scenes.MainCtrl;
import client.scenes.StartCtrl;
import client.utils.ConfigUtils;
import client.scenes.OverviewCtrl;
import client.utils.LanguageUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;


public class InjectorModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(InvitationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ConfigUtils.class).toInstance(createConfigUtils());
        binder.bind(LanguageUtils.class).in(Scopes.SINGLETON);
    }

    private ConfigUtils createConfigUtils() {
        ConfigUtils utils = new ConfigUtils();
        try {
            Path path = Paths.get("client/src/main/resources/config/recents.csv");
            //TODO REMOVE!!
            Path otherPath = Paths.get("client/src/main/resources/config/participants.csv");
            utils.setRecentsFile(new File(path.toAbsolutePath().toString()));
            utils.setParticipantsFile(new FileReader(otherPath.toFile()));
            return utils;
        } catch (FileNotFoundException e) {
            //TODO log and handle error
            throw new RuntimeException(e);
        }
    }
}