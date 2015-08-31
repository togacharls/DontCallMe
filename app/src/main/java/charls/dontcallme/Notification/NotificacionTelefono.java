package charls.dontcallme.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.widget.RemoteViews;

import charls.dontcallme.Activities.ListaNegraActivity;
import charls.dontcallme.R;

/**
 * Created by Carlos on 26/08/2015.
 */
public class NotificacionTelefono{

    private static final int ID_NOTIFICACION = 1;

    static public void sendNotification(Context context, String text){

        RemoteViews view = new RemoteViews("customView", R.mipmap.ic_logo );

        Intent notificacionIntent = new Intent(context, ListaNegraActivity.class);

        PendingIntent contenidoIntent = PendingIntent.getActivity(context, 0,
                notificacionIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        Notification.Builder notificationBuilder =
                new Notification.Builder(context)
                        .setTicker("Notificación de DontCallMe")
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setAutoCancel(true)
                        .setContentTitle("DontCallMe")
                        .setContentText(text)
                        .setContentIntent(contenidoIntent)
                        //URI del sonido de notificación por defecto de Android
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(null);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICACION, notificationBuilder.build());
    }
}
