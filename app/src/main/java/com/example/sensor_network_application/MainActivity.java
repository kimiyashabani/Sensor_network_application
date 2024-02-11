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
    private static final String CLIENT_ID = "FCEpCQUHESInLQohNxsiAio";
    private final String username = "FCEpCQUHESInLQohNxsiAio";
    private final String password = "+/Y4P8d/ZpSi/KeI0XXJ+O/H";
    int qos = 1;
    MqttClient client;
    TextView temperatureValue;
    TextView humidityTextView;
    TextView pressureTextView;
    TextView batteryTextView;
    TextView XTextView;
    TextView YTextView;
    TextView ZTextView;
    private static final String TAG = "MQTT";
    Map<String, MqttCallback> topicCallbacks = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Set up the persistence layer
            MemoryPersistence persistence = new MemoryPersistence();

            temperatureValue = findViewById(R.id.temperatureValueValue);
            humidityTextView = findViewById(R.id.HumidityValue);
            pressureTextView = findViewById(R.id.pressureValue);
            batteryTextView = findViewById(R.id.BatterylevelValue);
            XTextView = findViewById(R.id.XAccValue);
            YTextView = findViewById(R.id.YAccValue);
            ZTextView = findViewById(R.id.ZAccValue);

            // Initialize the MQTT client
            client = new MqttClient(BROKER_URL, CLIENT_ID, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            client.connect(connOpts);
            Log.d("TAG", "connected!!!");

            //Subscribe to the topic
            String[] topics = {"channels/2422894/publish", "channels/2428811/publish", "channels/2428994/publish"};
            for (String topic : topics) {
                client.subscribe(topic, qos);
                Log.d("TAG", "Subscribed to topic: " + topic);
                // Create a callback for each topic
                MqttCallback callback = new MqttCallback() {
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
                        Log.d("TAG", "A message arrived on topic: " + topic);
                        String payload = new String(message.getPayload());
                        // Handle the message payload here
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.d("TAG", "Delivery complete");
                    }
                };

                // Add the callback to the map
                topicCallbacks.put(topic, callback);
            }


        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}