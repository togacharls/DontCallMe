package charls.dontcallme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by carlos on 14/05/15.
 */
public class Database extends SQLiteOpenHelper{

    private ContentValues registro;
    private Cursor cursor;

    //Este ArrayList tiene como finalidad reducir el tiempo de ejecución que se emplea en cada consulta.
    private ArrayList<Usuario> blackList;

    public Database(Context c){
        super(c, "databaseDontCallMe.db", null, 1);
        updateBlackList();
    }

    @Override
    public void onCreate(SQLiteDatabase bd){

        bd.execSQL("CREATE TABLE IF NOT EXISTS Telefonos (" +
                "telefono VARCHAR(20), nombre VARCHAR(20), PRIMARY KEY (telefono, nombre))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            /*Si se desea añadir alguna tabla o realizar algún cambio en la base
            de datos en una versión posterior, los cambios se han de definir en este
            método
             */
        }
    }
    //Actualiza el ArrayList que incluye toda la lista negra.
    public void updateBlackList(){
        blackList = new ArrayList();
        Usuario user;
        cursor = this.getReadableDatabase().rawQuery("SELECT nombre, telefono FROM Telefonos", null);
        if(cursor.moveToFirst()) {
            do{
                if(cursor.getString(0) != null && cursor.getString(0) != ""
                        && cursor.getString(1) != null && cursor.getString(1) != "") {

                    user = new Usuario(cursor.getString(0), cursor.getString(1));
                    blackList.add(user);
                }
            }while(cursor.moveToNext());
        }
    }

    public ArrayList<Usuario> getBlackList(){
        return blackList;
    }

    //Añade un teléfono a la lista negra.
    public void add(String nombre, String telefono){
        registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("telefono", telefono);
        this.getWritableDatabase().insert("Telefonos", null, registro);
        updateBlackList();
    }

    //Elimina un teléfono de la lista negra
    public void remove(String telefono){
        registro = new ContentValues();
        registro.put("telefono", telefono);
        this.getWritableDatabase().delete("Telefonos", "telefono=?", new String[] {telefono});
        updateBlackList();
    }

}
