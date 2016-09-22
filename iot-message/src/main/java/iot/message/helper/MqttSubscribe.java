package iot.message.helper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscribe implements MqttCallback {

    public void readMessage() {
        String topic = "MQTT Examples";
        int qos = 2;
        String broker = "tcp://rastreamento.xyz:1883";
        String clientId = "JavaAsyncClient";
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        try {
            MqttAsyncClient client = new MqttAsyncClient(broker, clientId, memoryPersistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.setCallback(new MqttSubscribe());
            client.connect(options);
            Thread.sleep(1000);
            client.subscribe(topic, qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.err.println("connection lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic: " + topic);
        System.out.println("message: " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        System.err.println("delivery complete");
    }

}
