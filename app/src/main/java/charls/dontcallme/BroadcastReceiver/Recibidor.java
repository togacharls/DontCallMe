package charls.dontcallme.BroadcastReceiver;

/**
 * Created by carlos on 14/05/15.
 */

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

import charls.dontcallme.Application.DontCalleMeApplication;
import charls.dontcallme.Database.Database;
import charls.dontcallme.Database.Usuario;
import charls.dontcallme.Notification.NotificacionTelefono;

import com.android.internal.telephony.*;

public class Recibidor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Database db = new Database(context);

            //Toast.makeText(context, "Está llamando: "+ number, Toast.LENGTH_SHORT).show();
            for(Usuario user: DontCalleMeApplication.getBlackList()){
                //Se hace un "Contains" por si el usuario se ha registrado con el prefijo.
                //Esta condición permite al usuario bloquear todas las llamadas que contengan una serie de números concreta.
                if ( user.getTelefono().contains(number)) {
                    disconnectPhone(context, user.getNombre());
                    break;
                }
            }
        }
    }


    //Este metodo se encarga de desconectar el teléfono
    private void disconnectPhone(Context context, String name){

        //Para bloquear la llamada se hace uso de la reflexión de Java
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class clazz = Class.forName(telephonyManager.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
            telephonyService.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificacionTelefono.sendNotification(context, name);
    }
}