package homeautomation.dan.ionescu.homeautomation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import homeautomation.dan.ionescu.homeautomation.Services.AlarmRepeater;

public class GpsTrackerBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new AlarmRepeater(context).repeat(GpsTrackerAlarmReceiver.class, 60);
    }
}