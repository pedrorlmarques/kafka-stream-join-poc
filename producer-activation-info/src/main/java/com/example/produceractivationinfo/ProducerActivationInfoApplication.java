package com.example.produceractivationinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

@EnableBinding(DataProducer.class)
@SpringBootApplication
public class ProducerActivationInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerActivationInfoApplication.class, args);
    }

}


@RestController
@RequiredArgsConstructor
class Producer {

    private final DataProducer dataProducer;

    @GetMapping(value = "/activation/{key}/{activation}")
    public void activation(@PathVariable("key") String key, @PathVariable("activation") String activation) {
        dataProducer.activation()
                .send(MessageBuilder.withPayload(activation)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                        .build());
    }

    @GetMapping(value = "/info/{key}/{infoKey}")
    public void clicks(@PathVariable("key") String key, @PathVariable String infoKey) {
        dataProducer.info()
                .send(MessageBuilder.withPayload(infoKey)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                        .build());
    }
}


interface DataProducer {

    String ACTIVATION = "activation";
    String INFO = "info";

    @Output(ACTIVATION)
    MessageChannel activation();

    @Output(INFO)
    MessageChannel info();
}