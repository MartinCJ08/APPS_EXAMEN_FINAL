package com.example.recopilador_datos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        TextView txtTemp, txtHum, txtFecha, txtHour;
        ImageView imgData;

        if(convertView == null){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            convertView = layoutInflater.inflate(layout, parent, false);
        }


        txtTemp = convertView.findViewById(R.id.txtTemp);
        txtHum = convertView.findViewById(R.id.txtHum);
        txtFecha = convertView.findViewById(R.id.txtFecha);
        txtHour = convertView.findViewById(R.id.txtHour);
        imgData = convertView.findViewById(R.id.imgData);


        try {
            txtTemp.setText("Temperatura: "+objects.get(position).getDouble("temp"));
            txtHum.setText("Humedad: "+objects.get(position).getDouble("hum"));
            txtFecha.setText("Fecha: "+objects.get(position).getString("fecha"));
            txtHour.setText("Hora: "+objects.get(position).getString("hour"));
            double nats = objects.get(position).getDouble("temp");
            if(nats <= 0) {
                imgData.setImageResource(R.drawable.cold);
            }else if(nats > 0 && nats <= 30){
                imgData.setImageResource(R.drawable.normal);
            }else if(nats > 30) {
                imgData.setImageResource(R.drawable.hot);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}