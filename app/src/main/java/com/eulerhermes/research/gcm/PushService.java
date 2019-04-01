package com.eulerhermes.research.gcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.MainActivity;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.PubliFlashRegisterResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class PushService extends FirebaseMessagingService
{
    private static final String KEY = "alert";

    public static final String              TAG             = "GcmIntentService";
    public static final int                 NOTIFICATION_ID = 1;

    private SpiceManager mSpiceManager;


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token)
    {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        CorePrefs.setGCMRegistrationId(token);
        sendRegistrationIdToBackend();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app.
     */
    private void sendRegistrationIdToBackend()
    {
        mSpiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
        mSpiceManager.execute(new PubliFlashRegisterRequest(CorePrefs.getGCMRegistrationId()),
                              null, DurationInMillis.ALWAYS_EXPIRED, new ServerRegisterListener());
    }

    public final class ServerRegisterListener implements RequestListener<PubliFlashRegisterResult>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            Log.d("RssRequestListener", "onRequestFailure: " + spiceException);
        }

        @Override
        public void onRequestSuccess(final PubliFlashRegisterResult result)
        {
            Log.d("ServerRegisterListener", "onRequestSuccess: " + result);
            Log.d("ServerRegisterListener", "onRequestSuccess: " + result.getD());
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            createNotificationChannel();
            sendNotification(remoteMessage.getData().get(KEY));
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence        name        = getString(R.string.app_name);
//            String              description = getString(R.string.channel_description);
            int                 importance  = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel     = new NotificationChannel("main", name, importance);
//            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Put the message into a notification and post it.
    private void sendNotification(String msg)
    {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, mBuilder.build());
    }
}
