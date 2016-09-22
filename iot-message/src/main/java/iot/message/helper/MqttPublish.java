package iot.message.helper;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublish {

    public void sendMessage(String messageContent) {
        String topic = "MQTT Examples";
        String content = "Conteudo da mensagem enviada por MqttPublishDemo";
        int qos = 2;
        String broker = "tcp://rastreamento.xyz:1883";
        String clientId = "JavaClient";
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        try {
            MqttClient client = new MqttClient(broker, clientId, memoryPersistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
            client.disconnect();
            System.out.println("disconnect");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
