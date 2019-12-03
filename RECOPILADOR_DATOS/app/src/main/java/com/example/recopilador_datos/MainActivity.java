package com.example.recopilador_datos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
//        ApiConection myApi = new ApiConection();
//        myApi.execute();

        MySQLAPIConnection cn =  new MySQLAPIConnection();
        cn.execute();
    }
/*
    protected class ApiConection extends AsyncTask<Void, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(Void... params)
        {

            String str="http://192.168.0.14:3000/Tasks";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONObject(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null)
            {
                try {
                    Log.wtf("WTF", "Success: " + response.getString("yourJsonElement") );
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }*/

    /**
     * CREATE CONNECTION
     */

    class MySQLAPIConnection extends AsyncTask<String, Void, String> {

        private final String MY_URL = "http://10.1.9.32:3000/Tasks";
        private String result = null;

        @Override
        protected String doInBackground(String... strings) {
//            String sResu = "";
//
//            try {
//                URL myUrl = new URL(MY_URL);
//                HttpURLConnection httpCon = (HttpURLConnection) myUrl.openConnection();
//                if(httpCon.getResponseCode() == HttpURLConnection.HTTP_OK){
//                    BufferedReader dataJSON = new BufferedReader(
//                            new InputStreamReader(
//                                    httpCon.getInputStream()
//                            )
//                    );
//                    sResu = dataJSON.readLine();
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return sResu;
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
            if(s != null){
                try {
                    JSONObject jsData = new JSONObject(s);
                    Log.wtf("wtf","s"+s);
                    Log.wtf("wtf","s2"+jsData.toString());
                    Toast.makeText(getApplicationContext(), "S"+s,Toast.LENGTH_SHORT).show();
//                    lat = jsData.getDouble("lat");
//                    lon = jsData.getDouble("lon");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Log.wtf("wtf", "nulo, papa");
            }
        }
    }

}
