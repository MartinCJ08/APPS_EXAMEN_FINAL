/**
 * Created by Miguel Balderrama 12/02/2019
 * MainActivity.java
 */

package edu.itchii.temperatura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorTemperature;

    private double currentTemperature;
    private int currentHour;
    private int currentMinute;
    private Calendar rightNow;

    private Thread thread;

    private TextView txtTemp, txtHour, txtMin;

    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp = findViewById(R.id.txtTemp);
        txtHour = findViewById(R.id.txtHour);
        txtMin = findViewById(R.id.txtMin);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        rightNow = Calendar.getInstance();
        currentHour = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        currentMinute = rightNow.get(Calendar.MINUTE);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c);

        currentTemperature = sensorEvent.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Register a listener for the sensor
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorTemperature, SensorManager.SENSOR_DELAY_UI);

        thread = new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    try{
//                        txtTemp.setText("Temp: " + currentTemperature + "°C");
//                        txtHour.setText("Hour: " + currentHour);
//                        txtMin.setText("Minute: " + currentMinute);
                        Log.wtf("Temp: ", currentTemperature+"°C");
                        Log.wtf("Fecha: ", formattedDate);
                        Log.wtf("Hora: ", currentHour+":"+currentMinute);
                        MySQLAPIConnection mySQLAPIConnection = new MySQLAPIConnection(currentTemperature,formattedDate,currentHour+"",currentMinute+"");
                        mySQLAPIConnection.execute();
                        Thread.sleep(10000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

}

class MySQLAPIConnection extends AsyncTask<String, Void, String> {

    private final String MY_URL = "http://10.1.10.79:3000/Tasks";
    private String result = null;

    private double temp;
    private String date, hour, minute;

    public MySQLAPIConnection(double temp, String date, String hour, String minute){
        this.temp = temp;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL path = new URL(MY_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) path.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type","application/json;charset=utf-8");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("temp",this.temp);
            jsonObject.put("hum","0");
            jsonObject.put("lat","0");
            jsonObject.put("lon","0");
            jsonObject.put("fecha",this.date);
            jsonObject.put("hour",this.hour+":"+this.minute);

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(jsonObject.toString().getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            while((result = bufferedReader.readLine()) != null){
                stringBuffer.append(result);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
