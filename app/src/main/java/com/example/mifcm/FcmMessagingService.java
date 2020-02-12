package com.example.mifcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService  extends FirebaseMessagingService {
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    sendNotification(remoteMessage);
  }
  private void sendNotification(RemoteMessage remoteMessage) {
    Intent intent = new Intent(this, MainActivity.class);
    if(remoteMessage.getData() != null) {
      String desc = remoteMessage.getData().get("descuento");
      intent.putExtra("descuento", desc + " administrador por app");
    }

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT);
    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    RemoteMessage.Notification notification = remoteMessage.getNotification();
    if (notification != null) {
      Notification.Builder notificationBuilder = new Notification.Builder(this)
              .setSmallIcon(R.drawable.ic_stat_name).setContentTitle(notification.getTitle())
              .setContentText(notification.getBody()).setAutoCancel(true)
              .setSound(defaultSoundUri).setContentIntent(pendingIntent);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel("idmichanel", "Ofertachanel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        if (notificationManager != null) {
          notificationManager.createNotificationChannel(channel);
        }
        notificationBuilder.setChannelId("idmichanel");
      }
      if (notificationManager != null) {
        notificationManager.notify(0, notificationBuilder.build());
      }
    }
  }

  @Override
  public void onNewToken(String token) {
    super.onNewToken(token);
    sendRegistrationToServer(token);
  }
  private void sendRegistrationToServer(String token) {
    Log.d("newToken_ID", token);
  }
}