package admin;

import admin.scenes.LoginCtrl;
import admin.scenes.MainCtrl;
import admin.scenes.OverviewCtrl;
import admin.uicomponents.Alerts;
import admin.utils.Config;
import admin.utils.ServerUtils;
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
        binder.bind(LoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(Config.class).toInstance(createConfig());
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
    }

    private Config createConfig() {
        try {
            return Config.read(new File(normalizePath("./admin/src/main/resources/config/config.properties")));
        } catch (IOException e) {
            Alerts.configNotSetUpAlert();
            throw new RuntimeException(e);
        }
    }

    private String normalizePath(String strPath) {
        Path path = Paths.get(strPath);
        String absolutePath = path.toAbsolutePath().normalize().toString();
        if (absolutePath.contains("\\admin\\admin\\")) {
            absolutePath = absolutePath.replaceFirst("\\\\admin\\\\admin\\\\", "\\\\client\\\\");
        }
        return absolutePath;
    }
}
