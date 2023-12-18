package com.ass2.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.ass2.NavBarFragments.MyOrdersFragment;
import com.ass2.project_smd.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle the incoming notification here
        if (remoteMessage.getData() != null) {
            // Extract data from the notification payload
            String orderId = remoteMessage.getData().get("order_id"); // Replace with your data keys

            // Create an Intent to open the FragmentMyOrders
            //Intent intent = new Intent(this, MyOrdersFragment.class);


            Intent intent = new Intent(this, MyOrdersFragment.class);
            intent.putExtra("order_id", orderId); // Pass data to the MyOrdersActivity if needed
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Create a PendingIntent and show the notification
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "your_channel_id")
                    .setSmallIcon(R.drawable.pakistan_flag_icon__1_)
                    .setContentTitle("New Order")
                    .setContentText("You have a new order.")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
