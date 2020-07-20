package com.example.kafkastreamfunction.configuration;

import com.example.kafkastreamfunction.dtos.Activation;
import com.example.kafkastreamfunction.dtos.ActivationInfo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
class KafkaConfiguration {

    static final int WINDOW_SIZE_SECONDS = 30;

    @Bean
    public Function<KStream<String, String>, KStream<String, Activation>> activationUpperCase() {

        return activation -> activation
                .mapValues((readOnlyKey, value) -> value.toUpperCase())
                .map((key, value) -> new KeyValue<>("abc", new Activation(key, LocalDateTime.now().toEpochSecond(ZoneOffset.MAX))))
                .peek((key, value) -> System.out.println("ActivationUpperCase:\nKey: " + key + " Value: " + value));
    }


    @Bean
    public BiFunction<KStream<String, String>, KStream<String, Activation>, KStream<String, ActivationInfo>> activationInfo() {

        return (info, wordsCount) -> info
                .map((key, value) -> new KeyValue<>(key.replace("_random", ""), value))
                .join(wordsCount, ActivationInfo::new, JoinWindows.of(Duration.ofSeconds(WINDOW_SIZE_SECONDS)),
                        StreamJoined.with(Serdes.String(), new Serdes.StringSerde(), new JsonSerde<>(Activation.class)))
                .peek((key, value) -> System.out.println("ActivationInfoJoin:\nKey: " + key + " Value: " + value));
    }

    @Bean
    public Consumer<KStream<String, String>> consumeActivationOnly() {

        return activation -> activation
                .foreach((key, value) -> System.out.println("ConsumeActivationOnly:\nKey: " + key + " Value: " + value));

    }


    @Bean
    public Consumer<KStream<String, ActivationInfo>> afterJoinConsumer() {
        return afterJoinConsumer -> afterJoinConsumer
                .foreach((key, value) -> System.out.println("AfterJoin:\nKey: " + key + " Value: " + value));
    }

}