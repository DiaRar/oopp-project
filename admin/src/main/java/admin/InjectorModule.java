package admin;

import admin.scenes.LoginCtrl;
import admin.scenes.MainCtrl;
import admin.scenes.OverviewCtrl;
import admin.utils.ServerUtils;
import admin.utils.WebSocketUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class InjectorModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(LoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(WebSocketUtils.class).in(Scopes.SINGLETON);
    }
}
