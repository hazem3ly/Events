package com.example.hazem.events.Broadcast;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.hazem.events.Activitys.MainActivity;
import com.example.hazem.events.R;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;
    NotificationCompat.Builder notification;
    private static final int uniqueId = 134;
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

      notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        Intent intentf = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intentf,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        intent.getStringExtra("name");

       notification.setSmallIcon(R.drawable.done);
        notification.setTicker("this is ticker").
                setContentText("this is time for Your Event")
                .setContentTitle("Event Time").setWhen(System.currentTimeMillis());

        Intent yesReceive = new Intent();
        yesReceive.setAction("yes");
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.addAction(R.drawable.done, "Snooze", pendingIntentYes);

        Intent maybeReceive = new Intent();
        maybeReceive.setAction("no");
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(context, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.addAction(R.drawable.delete, "Dismiss", pendingIntentMaybe);

        notificationManager.notify(uniqueId,notification.build());

        Vibrator vibrator = (Vibrator)context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
    }

    public static class snoozeButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long delay= 300000; int notificationId=134;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle("this is time for ")
                        .setContentText("this is time for ")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.done)
                        .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.done)).getBitmap());

                Intent intent1 = new Intent(context, MainActivity.class);
                PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(activity);

                Notification notification = builder.build();

                Intent notificationIntent = new Intent(context, AlarmReceiver.class);
                notificationIntent.putExtra("notifyId", notificationId);
                notificationIntent.putExtra("notify", notification);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                long futureInMillis = SystemClock.elapsedRealtime() + delay;
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        }
    }
    public static class dismissButtonListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Your Have Canceled Snoozing", Toast.LENGTH_SHORT).show();
        }
    }
}
