package com.puulapp.ethio_cargo.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.puulapp.ethio_cargo.EthioCargo;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.mainUi.Manage;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmHandler {
    private Context context;

    public AlarmHandler(Context context){
        this.context = context;
    }

    public void notification(String message, int id) {
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.mipmap.ic_launcher_round);
                builder.setColor(context.getResources().getColor(R.color.purple_200));
                builder.setContentTitle("EthioCargo");
                builder.setContentText(message);
                    builder.setVibrate(new long[]{1000, 1000});
                builder.setLights(Color.RED, 3000, 3000);
                builder.setSound(defaultSoundUri);
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setContentIntent(PendingIntent.getActivity(context, id, new Intent(context, Manage.class), 0));

        Picasso.get().load(R.mipmap.ic_launcher_round).placeholder(R.mipmap.ic_launcher_round).networkPolicy(NetworkPolicy.OFFLINE).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                builder.setLargeIcon(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    // notificationId is a unique int for each notification that you must define

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = String.valueOf(id);
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    message,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

    notificationManager.notify(id, builder.build());


    }

}
