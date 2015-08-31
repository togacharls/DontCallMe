package charls.dontcallme.ListAdapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import charls.dontcallme.Database.Usuario;
import charls.dontcallme.ItemManagers.ItemManager;
import charls.dontcallme.R;

/**
 * Created by carlos on 14/05/15.
 */
public class TelefonoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ItemManager> lista;
    private static LayoutInflater inflater;

    //Constructor de la clase
    public TelefonoAdapter(Context c, ArrayList<ItemManager> telefonoslist){
        context = c;
        lista = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(telefonoslist != null){
            for(ItemManager item: telefonoslist){
                lista.add(item);
            }
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    //Este método debe devolver el tamaño de la lista que utilice, de lo contrario, genera un error.
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = inflater.inflate(R.layout.item_contacto, null);
        final ItemManager itemManager;
        final int posicion = i;

        //Al intentar reciclar los Views, los items de la lista aparecen con valores repetidos.
        //if(convertView == null) {
            //Se inicializan los atributos del elemento
            itemManager = lista.get(i);
            //view.setTag(itemManager);

            itemManager.setNombre((TextView) view.findViewById(R.id.nombreUsuarioItem));
            itemManager.setNumero((TextView) view.findViewById(R.id.telefonoUsuarioItem));
            itemManager.setCheck((ImageView) view.findViewById(R.id.checkimage));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!itemManager.getSeleccionado()) {
                        itemManager.setSeleccionado(true);
                    } else {
                        itemManager.setSeleccionado(false);
                    }
                }
            });
        //}
        /*else{
            itemManager = (ItemManager)convertView.getTag();
            convertView.getTag()
        }*/
        itemManager.update();
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return lista.isEmpty();
    }

    public void addItemtoBegining(String name, String number){
        Usuario usuario = new Usuario(name, number);
        ItemManager itemManager = new ItemManager(usuario);
        itemManager.setSeleccionado(true);
        lista.add(0, itemManager);

        //Se actualiza el BaseAdapter
        notifyDataSetChanged();
    }

    public ArrayList<ItemManager> getLista(){
        return lista;
    }
}
