package charls.dontcallme.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import charls.dontcallme.Application.DontCalleMeApplication;
import charls.dontcallme.Database.Database;
import charls.dontcallme.Database.Usuario;
import charls.dontcallme.ItemManagers.ItemManager;
import charls.dontcallme.ListAdapters.TelefonoAdapter;
import charls.dontcallme.R;


public class ListaNegraActivity extends ActionBarActivity {
    private TelefonoAdapter adapter;
    private ListView listView;
    private Database db;
    private ArrayList<ItemManager> blackList;

    //En onCreate únicamente se inicializa la base de datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.w("LISTANEGRA", "ONCREATE");
        db = new Database(this);
    }

    //La lista se carga en onResume() ya que así, cuando se cambie de Activity y se vuelva a ésta, la lista estará actualizada.
    @Override
    protected void onResume(){
        super.onResume();
        cargarListaNegra();
        if(blackList.size()>0) {
            setContentView(R.layout.activity_lista_negra);
            adapter = new TelefonoAdapter(this, blackList);
            listView = (ListView) findViewById(R.id.blackListListView);
            listView.setAdapter(adapter);
        }
        else{
            setContentView(R.layout.lista_vacia);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_negra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Context context = this;
        switch(id){
            case R.id.go_to_contacts:
                Intent intent = new Intent(context, ContactosActivity.class);
                context.startActivity(intent);
                break;

            case R.id.action_delete:
                for(ItemManager itemManager: blackList){
                    if(itemManager.getSeleccionado()){
                        //db.remove(itemManager.getUsuario().getTelefono());
                        DontCalleMeApplication.removeUser(itemManager.getUsuario());
                    }
                }
                //Se actualiza la lista: TODO cambiar esto
                onResume();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarListaNegra(){
        blackList = new ArrayList();
        for(Usuario usuario: DontCalleMeApplication.getBlackList()) {
            blackList.add(new ItemManager(usuario));
        }
    }
}
