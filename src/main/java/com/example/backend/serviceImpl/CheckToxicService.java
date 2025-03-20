package com.example.backend.serviceImpl;

import com.example.backend.exception.BadRequestException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@Service
public class CheckToxicService {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String REQUEST_TOPIC = "toxicity-request";
    private static final String RESPONSE_TOPIC = "toxicity-response";
    private Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }
    private Consumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "toxicity-checker-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // Chỉ đọc tin nhắn mới
        Consumer<String, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(RESPONSE_TOPIC));
        return consumer;
    }
    public Boolean checkToxic(String description){
//        String flaskApiUrl = "http://localhost:5000/predict"; // Địa chỉ API Flask
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Tạo nội dung request
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("review", description);
//        HttpHeaders headers = restTemplate.headForHeaders(flaskApiUrl);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        org.springframework.http.HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
//
//        // Gửi yêu cầu POST đến Flask API
//        ResponseEntity<Map> response = restTemplate.exchange(flaskApiUrl, HttpMethod.POST, (org.springframework.http.HttpEntity<?>) entity, Map.class);
//
//        // Xử lý phản hồi từ API
//        if (response.getStatusCode() == HttpStatus.OK) {
//            Map<String, Object> responseBody = response.getBody();
//            if (responseBody != null && "Toxic".equals(responseBody.get("Toxic"))) {
//                return true;
//            }
//        } else {
//            throw new RuntimeException("Error occurred while calling toxicity API: " + response.getStatusCode());
//        }
//        return false;
        if (description == null || description.trim().isEmpty()) {
            throw new BadRequestException("Description cannot be null or empty");
        }

        // Tạo requestId duy nhất để theo dõi phản hồi
        String requestId = UUID.randomUUID().toString();

        // Gửi yêu cầu đến Kafka topic "toxicity-request"
        try (Producer<String, String> producer = createProducer()) {
            ProducerRecord<String, String> record = new ProducerRecord<>(REQUEST_TOPIC, requestId, description);
            producer.send(record).get(); // Gửi đồng bộ
        } catch (Exception e) {
            throw new RuntimeException("Error sending request to Kafka: " + e.getMessage());
        }

        // Nhận phản hồi từ topic "toxicity-response"
        try (Consumer<String, String> consumer = createConsumer()) {
            long startTime = System.currentTimeMillis();
            long timeout = 10000; // Timeout 10 giây

            while (System.currentTimeMillis() - startTime < timeout) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    if (record.key().equals(requestId)) { // Kiểm tra requestId
                        String response = record.value();
                        return "Toxic".equals(response); // Giả sử Flask trả về "Toxic" hoặc "Non-Toxic"
                    }
                }
            }
            throw new RuntimeException("Timeout waiting for toxicity response from Kafka");
        }
    }
}
