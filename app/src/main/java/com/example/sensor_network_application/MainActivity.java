package com.example.sensor_network_application;

import androidx.appcompat.app.AppCompatActivity;
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
import org.json.JSONObject;
import org.json.JSONException;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    //MQTT
    private static final String BROKER_URL = "tcp://mqtt3.thingspeak.com:1883";
    private static final String CLIENT_ID = "JwIBCDIyAxEGGBozBgYLGDg";
    final String topic = "channels/2425312/subscribe/fields/+";
    private final String username = "JwIBCDIyAxEGGBozBgYLGDg";
    private final String password = "H6N/1Vyt6HvzFLAdGhPKKoHn";
    int qos = 0;
    public static final String LOADWEBTAG = "LOAD_WEB_TAG";
    private static final String URL_THINGSPEAK  = "https://thingspeak.com/channels/2422897";

    TextView temperatureValue;
    TextView humidityValue;
    TextView pressureValue;
    TextView batteryValue;
    TextView XValue;
    TextView YValue;
    TextView ZValue;
    MqttClient client;
    ExecutorService es;
    private String threadAndClass; // to clearly identify logs
    private static final String CONTENT_TYPE = "text/html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            client = new MqttClient(BROKER_URL, CLIENT_ID, persistence);
            Log.d("TAG", "Estoy en el onCreate!!!");

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            client.connect(connOpts);
            Log.d("TAG", "connected!!!");
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();

            Log.d("TAG", "Empiezo a subscribirme");

            // Subscribe to the topic
            client.subscribe(topic, qos);

            Log.d("TAG", "Subscribed to topic");
            Toast.makeText(this, "Subscribing to topic " + topic, Toast.LENGTH_SHORT).show();
            //client.subscribe(subscriptionTopic, qos);
            //Toast.makeText(this, "Subscribing to topic "+ subscriptionTopic, Toast.LENGTH_SHORT).show();

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("TAG", "Connection lost");
                    try {
                        client.connect();
                        Log.d("TAG", "Reconnected to the broker");
                    } catch (MqttException e) {
                        Log.e("TAG", "Failed to reconnect to the broker: " + e.getMessage());
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d("SUBS", "A message arrived");
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

    void setValue(String str){
        temperatureValue.setText(str);
    }
}