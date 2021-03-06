package homeautomation.dan.ionescu.homeautomation.dependencyinjection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import homeautomation.dan.ionescu.homeautomation.Services.AlarmRepeater;
import homeautomation.dan.ionescu.homeautomation.Services.ApiHandler;
import homeautomation.dan.ionescu.homeautomation.Services.MetaDataContainer;
import homeautomation.dan.ionescu.homeautomation.Services.UserPreferences;

@Module
public class AppModule {

    @Provides @Named("default")
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    AlarmRepeater providesAlarmRepeater(Context context) {
        return new AlarmRepeater(context);
    }

    @Provides
    @Singleton
    ApiHandler providesApiHandler(UserPreferences userPreferences, Context context) {
        String apiTokenSecret = MetaDataContainer.get(context,
                "homeautomation.dan.ionescu.homeautomation.apiTokenSecret");

        return new ApiHandler(userPreferences, apiTokenSecret);
    }

    @Provides
    @Singleton
    UserPreferences providesUserPreferences(Context context) {
        return new UserPreferences(context);
    }
}