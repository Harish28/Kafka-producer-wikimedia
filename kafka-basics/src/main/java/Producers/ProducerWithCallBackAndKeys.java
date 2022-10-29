package Producers;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallBackAndKeys {
    private static final Logger log = LoggerFactory.getLogger(ProducerWithCallBackAndKeys.class.getName());
    public static void main(String[] args) {
        log.info("I am a kafka producer!");
        // Property
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);


        for (int i = 0; i < 10; i++) {
            String key = "id_" + i;
            ProducerRecord<String,String> record = new ProducerRecord<>("demo_java",key,i + " Message From Producer");
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        log.info("Metadata: \n" +
                                "Topic: " + metadata.topic() + "\n" +
                                "Key: " + record.key() + "\n" +
                                "Partition: " + metadata.partition() + "\n");
                    } else {
                        log.error("Error in producing", exception);
                    }
                }
            });
        }
        producer.flush();
        producer.close();
    }
}
