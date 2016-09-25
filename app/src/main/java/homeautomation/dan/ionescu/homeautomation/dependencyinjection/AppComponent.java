package homeautomation.dan.ionescu.homeautomation.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;
import homeautomation.dan.ionescu.homeautomation.BaseActivity;
import homeautomation.dan.ionescu.homeautomation.MainActivity;

@Singleton
@Component(modules = {RootModule.class, AppModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
}