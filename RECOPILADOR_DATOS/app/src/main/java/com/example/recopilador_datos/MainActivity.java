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
    MySQLAPIConnection cn;

    static String searchDay = "";
    static String searchMonth = "";
    static String searchYear = "";
    static String finalDate = "";

    private static final String ZERO = "0";
    private static final String SLASH = "/";

    public final Calendar c = Calendar.getInstance();

    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    EditText etFecha;

//    String MY_URL = "http://192.168.0.14:3000/Tasks/"+searchDay+"/"+searchMonth+"/"+searchYear;
//    String result = "";
//    Handler mHandler = new Handler();
//
//    Thread mThread = new Thread(new Runnable() {
//        @Override
//        public void run () {
//            try {
//                // LECTURA GET
//                URL path = new URL(MY_URL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) path.openConnection();
//                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
//                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    result = bufferedReader.readLine();
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mHandler.post(new Runnable() {
//                @Override
//                public void run () {
//                    if(result != null){
//                        try {
//                            JSONArray jsData = new JSONArray(result);
//                            for (int i  = 0; i<jsData.length();i++){
//                                miLista.add(jsData.getJSONObject(i));
//                            }
//                            if(!miLista.isEmpty()){
//                                listView.setAdapter(
//                                        new JSONAdapter(MainActivity.this,R.layout.json_item,miLista)
//                                );
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        }
//    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        etFecha = findViewById(R.id.etFecha);

//        mThread.start();

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
                searchDay = (dayOfMonth < 10)? ZERO + dayOfMonth :String.valueOf(dayOfMonth);
                searchMonth = (mesActual < 10)? ZERO + mesActual :String.valueOf(mesActual);
                searchYear = year+"";
                finalDate = searchDay + SLASH + searchMonth + SLASH + year;
                etFecha.setText(finalDate);

//                cn =  new MySQLAPIConnection();
//                cn.execute();

//                cn.cancel(true);
//                if(cn.isCancelled()){
//                    Log.wtf("wtf", "Cancelado alv");
//                   cn.execute();
//                }
            }
        },anio, mes, dia);
        pickDate.show();
    }


    /**
     * CREATE CONNECTION
     */
    class MySQLAPIConnection extends AsyncTask<String, Void, String> {

//        private String MY_URL = "http://192.168.0.14:3000/Tasks/11/12/2019";
        private String MY_URL = "http://192.168.0.14:3000/Tasks/fechas/11/12/2019";
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
                    }
                    if(!miLista.isEmpty()){
                        listView.setAdapter(
                                new JSONAdapter(MainActivity.this,R.layout.json_item,miLista)
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
