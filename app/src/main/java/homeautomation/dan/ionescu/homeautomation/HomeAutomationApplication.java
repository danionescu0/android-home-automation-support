package homeautomation.dan.ionescu.homeautomation;

import android.app.Application;

import homeautomation.dan.ionescu.homeautomation.dependencyinjection.AppComponent;
import homeautomation.dan.ionescu.homeautomation.dependencyinjection.AppModule;
import homeautomation.dan.ionescu.homeautomation.dependencyinjection.RootModule;
import homeautomation.dan.ionescu.homeautomation.dependencyinjection.DaggerAppComponent;

public class HomeAutomationApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .rootModule(new RootModule(this))
                .appModule(new AppModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}