package com.reinforcementcoach.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.app.Notification;

public class ReminderReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "reinforcement_reminders";
    @Override public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Daily reinforcement reminder", NotificationManager.IMPORTANCE_DEFAULT);
            ch.setDescription("Reminds you to support the person and evaluate the strategy.");
            nm.createNotificationChannel(ch);
        }
        Intent open = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, open, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder b = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? new Notification.Builder(context, CHANNEL_ID) : new Notification.Builder(context);
        b.setSmallIcon(com.reinforcementcoach.app.R.drawable.ic_launcher)
         .setContentTitle("Pause before advising")
         .setContentText("Support the person. Evaluate the strategy. Start with one true statement.")
         .setContentIntent(pi).setAutoCancel(true);
        nm.notify(1001, b.build());
        MainActivity.scheduleNextReminder(context);
    }
}
