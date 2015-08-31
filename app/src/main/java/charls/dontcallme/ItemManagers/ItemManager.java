package charls.dontcallme.ItemManagers;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import charls.dontcallme.Database.Usuario;

/**
 * Created by carlos on 21/05/15.
 */
public class ItemManager {

    private TextView nombre;
    private TextView numero;
    private boolean seleccionado;
    private Usuario usuario;

    private ImageView check;

    public ItemManager(Usuario user){
        usuario = user;
        seleccionado = false;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public void setNumero(TextView numero) {
        this.numero = numero;
    }

    public boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        if(this.check != null){
            if(this.seleccionado) {
                this.check.setVisibility(View.VISIBLE);
            }
            else{
                this.check.setVisibility(View.INVISIBLE);
            }
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void update(){
        nombre.setText(usuario.getNombre());
        numero.setText(usuario.getTelefono());
        if(seleccionado){
            check.setVisibility(View.VISIBLE);
        }
        else{
            check.setVisibility(View.INVISIBLE);
        }
    }

    //Se asigna la ImageView. Por defecto se hace invisible.
    public void setCheck(ImageView check) {
        this.check = check;
        this.check.setVisibility(View.INVISIBLE);
    }
}
