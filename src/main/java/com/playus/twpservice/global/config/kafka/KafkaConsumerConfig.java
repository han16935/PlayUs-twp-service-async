//package com.playus.twpservice.global.config.kafka;
//
//import com.playus.twpservice.domain.chat.dto.request.ChattingMessage;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConsumerConfig {
//
//  @Value("${spring.kafka.bootstrap-servers}")
//  private String bootstrapServers;
//  @Value("${spring.kafka.consumer.chat-group-id}")
//  private String chatGroupId;
//
//  @Bean
//  public ConsumerFactory<String, ChattingMessage> chatMessageConsumerFactory() {
//    Map<String, Object> configs = new HashMap<>();
//    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//    configs.put(ConsumerConfig.GROUP_ID_CONFIG, chatGroupId);
//    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//    JsonDeserializer<ChattingMessage> jsonDeserializer =
//            new JsonDeserializer<>(ChattingMessage.class);
//    jsonDeserializer.addTrustedPackages("com.playus.twpservice.domain.chat.dto.request");
//
//    return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
//  }
//
//  @Bean
//  public ConcurrentKafkaListenerContainerFactory<String, ChattingMessage> chatMessageListenerFactory() {
//    ConcurrentKafkaListenerContainerFactory<String, ChattingMessage> factory
//            = new ConcurrentKafkaListenerContainerFactory<>();
//
//    factory.setConsumerFactory(chatMessageConsumerFactory());
//    factory.setConcurrency(3);
//    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//    return factory;
//  }
//}
