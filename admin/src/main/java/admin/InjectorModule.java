package admin;

import admin.scenes.LoginCtrl;
import admin.scenes.MainCtrl;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class InjectorModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(LoginCtrl.class).in(Scopes.SINGLETON);
    }
}
