package homeautomation.dan.ionescu.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import homeautomation.dan.ionescu.homeautomation.Services.LocationService;

public class GpsTrackerAlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GpsTrackerAlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LocationService.class));
    }
}