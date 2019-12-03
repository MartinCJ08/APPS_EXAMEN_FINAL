package com.example.recopilador_datos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class JSONAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private int layout;
    private List<JSONObject> objects;

    public JSONAdapter(@NonNull Context context, int resource, @NonNull List<JSONObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layout = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView txtTemp, txtHum, txtFecha;

        if(convertView == null){ //NO EXISTE LA FILA, HAY QUE CREARLA
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            convertView = layoutInflater.inflate(layout, parent, false);
        }

        txtTemp = convertView.findViewById(R.id.txtTemp);
        txtHum = convertView.findViewById(R.id.txtHum);
        txtFecha = convertView.findViewById(R.id.txtFecha);

        try {
            txtTemp.setText("Temperatura: "+objects.get(position).getString("temp"));
            txtHum.setText("Humedad: "+objects.get(position).getString("temp"));
            txtFecha.setText("Fecha: "+objects.get(position).getString("temp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}