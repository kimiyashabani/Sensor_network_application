package com.example.sensor_network_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.example.sensor_network_application.rest.RestRequests;
import com.example.sensor_network_application.rest.ServiceGenerator;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "FCEpCQUHESInLQohNxsiAio";
    private static final String BROKER = "tcp://mqtt3.thingspeak.com:1883";
    int qos = 0;
    final String topic = "channels/2422894/subscribe/fields/+";
    private final String user = "FCEpCQUHESInLQohNxsiAio";
    private final String psswrd = "+/Y4P8d/ZpSi/KeI0XXJ+O/H";


    TextView temperatureValue;
    TextView humidityValue;
    TextView pressureValue;
    TextView batteryValue;
    TextView XValue;
    TextView YValue;
    TextView ZValue;
    MqttClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getValues();

        try {
            // Set up the persistence layer
            MemoryPersistence persistence = new MemoryPersistence();
            temperatureValue = findViewById(R.id.temperatureValueValue);
            humidityValue = findViewById(R.id.HumidityValue);
            pressureValue = findViewById(R.id.pressureValue);
            batteryValue = findViewById(R.id.BatterylevelValue);
            XValue = findViewById(R.id.XAccValue);
            YValue= findViewById(R.id.YAccValue);
            ZValue = findViewById(R.id.ZAccValue);

            // Initialize the MQTT client
            client = new MqttClient(BROKER, CLIENT_ID, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(user);
            connOpts.setPassword(psswrd.toCharArray());

            //connect to broker
            client.connect(connOpts);
            Toast.makeText(this, "Connected to broker", Toast.LENGTH_SHORT).show();

            //subscribed
            client.subscribe(topic, qos);
            Log.d("TAG", "Subscribed");
            Toast.makeText(this, "Subscribed to topic " + topic, Toast.LENGTH_SHORT).show();

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("TAG", "Connection lost");
                    try {
                        client.connect();
                    } catch (MqttException e) {
                        Log.e("TAG", "Failed to connect: " + e.getMessage());
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = message.toString();

                    switch (topic) {
                        case "channels/2425312/subscribe/fields/field1":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    temperatureValue.setText(payload);
                                }
                            });
                            break;
                        case "channels/2425312/subscribe/fields/field2":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    humidityValue.setText(payload);
                                }
                            });
                            break;
                        case "channels/2425312/subscribe/fields/field3":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pressureValue.setText(payload);
                                }
                            });
                            break;
                        case "channels/2425312/subscribe/fields/field4":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    batteryValue.setText(payload);
                                }
                            });
                            break;
                        case "channels/2425312/subscribe/fields/field5":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    XValue.setText(payload);
                                }
                            });
                            break;
                        case "channels/2425312/subscribe/fields/field6":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    YValue.setText(payload);
                                }
                            });
                            break;
                        case "channels/2425312/subscribe/fields/field7":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ZValue.setText(payload);
                                }
                            });
                            break;
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("TAG", "Delivery complete");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void getValues(){
        //get retrofit instance
        RestRequests rest = ServiceGenerator.createService(RestRequests.class);
        //execute request
        Call<JsonObject> resp = rest.getValues();
        resp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    //if it was a successfully request
                    try{
                        // get json in the body of the response message
                        JSONObject js = new JSONObject(response.body().toString());
                        JSONArray values = js.getJSONArray("feeds");


                        for(int i=0; i<2; i++){
                            JSONObject feed = values.getJSONObject(i);

                            temperatureValue = findViewById(R.id.temperatureValueValue);
                            humidityValue = findViewById(R.id.HumidityValue);
                            pressureValue = findViewById(R.id.pressureValue);
                            batteryValue = findViewById(R.id.BatterylevelValue);
                            XValue = findViewById(R.id.XAccValue);
                            YValue= findViewById(R.id.YAccValue);
                            ZValue = findViewById(R.id.ZAccValue);


                            //set values in the interface
                            if (!feed.isNull("field1")){
                                temperatureValue.setText(feed.getString("field1"));
                            }
                            if (!feed.isNull("field2")){
                                humidityValue.setText(feed.getString("field2"));
                            }
                            if (!feed.isNull("field3")){
                                pressureValue.setText(feed.getString("field3"));
                            }
                            if (!feed.isNull("field4")){
                                batteryValue.setText(feed.getString("field4"));
                            }
                            if (!feed.isNull("field5")){
                                XValue.setText(feed.getString("field5"));
                            }
                            if (!feed.isNull("field6")){
                                YValue.setText(feed.getString("field6"));
                            }
                            if (!feed.isNull("field7")){
                                ZValue.setText(feed.getString("field7"));
                            }


                        }



                        /*
                        humidityValue.setText(feed.getString("field2"));
                        pressureValue.setText(feed.getString("field3"));
                        batteryValue.setText(feed.getString("field4"));
                        XValue.setText(feed.getString("field5"));
                        YValue.setText(feed.getString("field6"));
                        ZValue.setText(feed.getString("field7"));

                         */


                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Error getting values", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure: check internet connection", Toast.LENGTH_SHORT).show();
                Log.d("Failure", t.toString());
            }
        });
    }

}