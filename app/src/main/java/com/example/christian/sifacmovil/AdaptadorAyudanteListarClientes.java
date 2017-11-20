package com.example.christian.sifacmovil;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Christian on 19/11/2017.
 */

public class AdaptadorAyudanteListarClientes extends ArrayAdapter<AyudanteListViewClientes> {
Context mycontext;
    int mylayoutResourceId;
    AyudanteListViewClientes dta[]=null;

    public AdaptadorAyudanteListarClientes(Context context, int LayoutResourceId,AyudanteListViewClientes[] data){
        super(context,LayoutResourceId,data);


        this.mycontext=context;
        this.mylayoutResourceId=LayoutResourceId;
        this.dta=data;

    }


    public View getView(int posicion, View convertView, ViewGroup parent){
        View row=convertView;

        AdaptadorAyudanteListarClientesHolder holder=null;
        if(row==null){
            LayoutInflater inflater=((Activity)mycontext).getLayoutInflater();
            row=inflater.inflate(mylayoutResourceId,parent,false);
            holder= new AdaptadorAyudanteListarClientesHolder();
            holder.image = (ImageView) row.findViewById(R.id.ImagenCliente);
            holder.nombre = (TextView) row.findViewById(R.id.NCliente);
            holder.info = (TextView) row.findViewById(R.id.NombreCliente);
            row.setTag(holder);

        }else {
                holder= (AdaptadorAyudanteListarClientesHolder)row.getTag();
        }

        AyudanteListViewClientes ayudante= dta[posicion];
        holder.nombre.setText(ayudante.ncliente);
        holder.image.setImageResource(ayudante.icon);
        holder.info.setText(ayudante.direccion);

        return row;
    }



    static class AdaptadorAyudanteListarClientesHolder {
        ImageView image;
        TextView nombre;
        TextView info;
    }

}
