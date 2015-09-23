package charls.dontcallme.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import charls.dontcallme.Application.DontCalleMeApplication;
import charls.dontcallme.Database.Database;
import charls.dontcallme.Database.Usuario;
import charls.dontcallme.ItemManagers.ItemManager;
import charls.dontcallme.ListAdapters.TelefonoAdapter;
import android.provider.ContactsContract;
import android.widget.Toast;

import charls.dontcallme.R;

/**
 * Created by carlos on 14/05/15.
 */

public class ContactosActivity extends ActionBarActivity {

    private ListView listView;
    private Button addCustomContact;
    private EditText nameTV;
    private EditText numberTV;
    private TelefonoAdapter adapter;
    private Database db;
    private Context context;

    //En onCreate únicamente se inicializa la base de datos y se asignan los widgets "estáticos" (TextViews y Buttton superiores)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_contactos);
        db = new Database(context);
    }

    //En onResume se asigna el ListView, el adapter, etc. ya que esta lista puede variar después de ejecutarse onCreate()
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ItemManager> items = new ArrayList();

        //Se obtienen todos los contactos registrados en el teléfono
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        ItemManager itemManager;
        ArrayList<String> usuarios = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                usuarios.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                ArrayList<String> telefonosAsociados = new ArrayList();

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String numero = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Se eliminan los espacios con los que se hayan podido guardar los números:
                        numero = numero.replaceAll(" ", "");
                        if(!telefonosAsociados.contains(numero)) {
                            telefonosAsociados.add(numero);
                        }
                    }
                    pCur.close();
                }
                //Se añaden cada uno de los teléfonos asociados a cada contacto
                if (telefonosAsociados.size() > 0) {
                    for (String telefono : telefonosAsociados) {
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Usuario usuario = new Usuario(name, telefono);
                        itemManager = new ItemManager(usuario);
                        items.add(itemManager);
                    }
                }
            } while (cursor.moveToNext());
        }

        //Se marcan los teléfonos que se encuentren en la lista negra:
        for (Usuario user : DontCalleMeApplication.getBlackList()) {
            boolean isContact = false;
            for (ItemManager item : items) {
                if (user.getNombre().equals(item.getUsuario().getNombre()) && user.getTelefono().equals(item.getUsuario().getTelefono())) {
                    item.setSeleccionado(true);
                    isContact = true;
                }
            }
            //Si el contacto de la lista negra no forma parte de los contactos del teléfono, se añade a la lista.
            if (!isContact) {
                ItemManager blackListItemManager = new ItemManager(user);
                blackListItemManager.setSeleccionado(true);
                items.add(0, blackListItemManager);
            }
        }

        //Se inicializan el Adapter y el ListView
        listView = (ListView) findViewById(R.id.contactosListView);
        adapter = new TelefonoAdapter(this, items);
        listView.setAdapter(adapter);

        //Se añade un header al listview que permitirá añadir nuevos números:
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.first_item_contacto, null);

        addCustomContact = (Button) header.findViewById(R.id.addnumberbutton);
        nameTV = (EditText) header.findViewById(R.id.nombreUsuarioCustom);
        numberTV = (EditText) header.findViewById(R.id.telefonoUsuarioCustom);

        addCustomContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se comprueba que el usuario no haya introducido los campos vacíos o un nombre formado sólo por espacios:
                String nombreSinEspacios = nameTV.getText().toString().replace(" ", "");
                if (nombreSinEspacios.length() == 0 || numberTV.getText().toString().length() == 0) {
                    Toast.makeText(context, "Debe completar los dos campos", Toast.LENGTH_SHORT).show();
                }
                //Si el nombre del usuario cuenta con más de 20 caracteres, éste se reduce a 20 caracteres.
                else if (nombreSinEspacios.length() > 20) {
                    String nombreAcortado = nameTV.getText().toString().substring(0, 19);
                    adapter.addItemtoBegining(nombreAcortado, numberTV.getText().toString());
                }
                //Si el número telefónico cuenta con más de 20 caracteres, aparece un mensaje de error y no se lleva a cabo ninguna acción.
                else if (numberTV.getText().toString().length() > 20) {
                    Toast.makeText(context, "Esta aplicación no soporta números telefónicos con más de 20 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    //Se actualiza la lista
                    adapter.addItemtoBegining(nameTV.getText().toString(), numberTV.getText().toString());
                }
            }
        });
        listView.addHeaderView(header);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contactos, menu);
        restoreActionBar();
        return true;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_activity_contactos));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_done:
                /*Se añaden los contactos que se hayan seleccionado y se eliminan los que se encuentren en la BD pero no se
                *hayan seleccionado*/
                for(ItemManager itemManager: adapter.getLista()){
                    //Si el itemManager está seleccionado y no se encuentra en la BD, se añade a ésta.
                    if(itemManager.getSeleccionado() && !  DontCalleMeApplication.getBlackList().contains(itemManager)){
                        //db.add(itemManager.getUsuario().getNombre(), itemManager.getUsuario().getTelefono());
                        DontCalleMeApplication.addUser(itemManager.getUsuario());
                    }

                    //Si el itemManager no está seleccionado pero se encuentra en la BD, se elimina de ésta.
                    else if(!itemManager.getSeleccionado()&& !DontCalleMeApplication.getBlackList().contains(itemManager)){
                        //db.remove(itemManager.getUsuario().getTelefono());
                        DontCalleMeApplication.removeUser(itemManager.getUsuario());
                    }
                }
                //Se mata la activity para que así, en caso de que el usuario presione el botón "back", salga de la app
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}