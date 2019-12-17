package android.beginnerranch.bariatriapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.beginnerranch.bariatriapp.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder notify = null;
        NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notif.createNotificationChannel(mChannel);
            notify = new NotificationCompat.Builder(context, chanel_id);
        } else {
            notify = new NotificationCompat.Builder(context);
        }
        String tittle= intent.getStringExtra("message");
        Notification notific = notify.setContentTitle(tittle).
                setContentTitle(tittle).setSmallIcon(R.drawable.apple_tuna_sandwich).build();

        notific.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notific);
    }
}
