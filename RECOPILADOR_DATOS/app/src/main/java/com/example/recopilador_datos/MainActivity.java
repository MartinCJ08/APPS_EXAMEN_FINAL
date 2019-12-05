package com.example.recopilador_datos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<JSONObject> miLista = new ArrayList<>();
    List<JSONObject> myAdapterList = new ArrayList<>();
    JSONAdapter myAdapter;
    MySQLAPIConnection cn;

    private static final String ZERO = "0";
    private static final String SLASH = "/";

    public final Calendar c = Calendar.getInstance();

    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    EditText etFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        etFecha = findViewById(R.id.etFecha);

        myAdapter = new JSONAdapter(MainActivity.this,R.layout.json_item,myAdapterList);
        listView.setAdapter(myAdapter);

        cn =  new MySQLAPIConnection();
        cn.execute();
    }

    public void onClick(View view){
        showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        DatePickerDialog pickDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String searchDay = (dayOfMonth < 10)? ZERO + dayOfMonth :String.valueOf(dayOfMonth);
                String searchMonth = (mesActual < 10)? ZERO + mesActual :String.valueOf(mesActual);
                String finalDate = searchDay + SLASH + searchMonth + SLASH + year;
                etFecha.setText(finalDate);

                myAdapterList.clear();
                for(int i = 0;i<miLista.size();i++){
                    try {
                        if(miLista.get(i).getString("fecha").equals(finalDate)){
                            myAdapterList.add(miLista.get(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                myAdapter.notifyDataSetChanged();

            }
        },anio, mes, dia);
        pickDate.show();
    }

    /**
     * CREATE CONNECTION
     */
    class MySQLAPIConnection extends AsyncTask<String, Void, String> {

        private String MY_URL = "http://10.1.10.79:3000/Tasks";
//        private String MY_URL = "http://10.8.18.43:3000/Tasks/fechas/04/12/2019";
        private String result = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                // LECTURA GET
                URL path = new URL(MY_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) path.openConnection();
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    result = bufferedReader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                try {
                    JSONArray jsData = new JSONArray(s);
                    for (int i  = 0; i<jsData.length();i++){
                        miLista.add(jsData.getJSONObject(i));
                        myAdapterList.add(jsData.getJSONObject(i));
                    }

                    if(!miLista.isEmpty()){
                        myAdapter.notifyDataSetChanged();
//                        listView.setAdapter(
//                                new JSONAdapter(MainActivity.this,R.layout.json_item,myAdapterList)
//                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
