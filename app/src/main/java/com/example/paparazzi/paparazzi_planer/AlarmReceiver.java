package com.example.paparazzi.paparazzi_planer;

/**
 * Created by LeeJinKyu on 2018-04-18.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.paparazzi.paparazzi_planer.AddAtivity;
import com.example.paparazzi.paparazzi_planer.R;
import com.example.paparazzi.paparazzi_planer.TodayFragment;

public class AlarmReceiver extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.checkmark).setTicker("!!Paparazzi!!").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle(AddAtivity.edtTitle.getText()).setContentText(AddAtivity.edtContent.getText())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);

        notificationmanager.notify(1, builder.build());
    }

}