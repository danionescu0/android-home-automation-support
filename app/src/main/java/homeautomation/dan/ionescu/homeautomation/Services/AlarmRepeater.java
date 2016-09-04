package homeautomation.dan.ionescu.homeautomation.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class AlarmRepeater {
    private Context context;

    public AlarmRepeater(Context context) {
        this.context = context;
    }

    public void repeat(Class<?> targetClass, int seconds) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent repeatClass = new Intent(context, targetClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, repeatClass, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                seconds * 1000,
                pendingIntent);
        Log.d("cici", "repeater started");
    }
}
