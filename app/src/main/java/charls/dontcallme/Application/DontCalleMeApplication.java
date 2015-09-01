package charls.dontcallme.Application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import charls.dontcallme.Activities.ListaNegraActivity;
import charls.dontcallme.Database.Database;
import charls.dontcallme.Database.Usuario;


/**
 * Created by Carlos on 26/08/2015.
 */
public class DontCalleMeApplication extends Application {
    private static Context context;
    private static Object notificationService;
    private static Intent notificacionIntent;
    private static Database db;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        notificationService = getSystemService(Context.NOTIFICATION_SERVICE);
        notificacionIntent = new Intent(context, ListaNegraActivity.class);

        db = new Database(context);
    }

    public static Context getContext(){
        return context;
    }

    public static Object getNotificationService(){
        return notificationService;
    }

    public static Intent getNotificacionIntent() {
        return notificacionIntent;
    }

    public static ArrayList<Usuario> getBlackList(){
        return db.getBlackList();
    }

    public static void addUser(Usuario usuario){
        db.add(usuario.getNombre(), usuario.getTelefono());
    }

    public static void removeUser(Usuario usuario){
        db.remove(usuario.getTelefono());
    }
}
