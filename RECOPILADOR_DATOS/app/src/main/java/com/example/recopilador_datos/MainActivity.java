package com.example.recopilador_datos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    TextView txtDaton;
    private ArrayList<JSONObject> miLista = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        txtDaton = findViewById(R.id.txtDaton);

        MySQLAPIConnection cn =  new MySQLAPIConnection();
        cn.execute();
    }

    /**
     * CREATE CONNECTION
     */

    class MySQLAPIConnection extends AsyncTask<String, Void, String> {

        private final String MY_URL = "http://192.168.0.3:3000/Tasks";
        private String result = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                //LECTURA GET
                URL path = new URL(MY_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) path.openConnection();
                Log.wtf("wtf", "doInBackground: " );
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Log.wtf("wtf","en el if");
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    result = bufferedReader.readLine();
                    Log.wtf("WTF", result);
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
            String sVal ="";
            Log.wtf("wtf","post execute papa");
//            Toast.makeText(getApplicationContext(),"S"+s,Toast.LENGTH_SHORT).show();
            if(!s.equals("")){
                try {
                    JSONArray jsData = new JSONArray(s);
                    Toast.makeText(MainActivity.this, ""+jsData.get(0), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "pito"+e, Toast.LENGTH_LONG).show();
                    Log.wtf("WTF", "e "+e);
                    e.printStackTrace();
                }
            }else{
                Log.wtf("wtf", "nulo, papa");
            }
        }
    }

}
