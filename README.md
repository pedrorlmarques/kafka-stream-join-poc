# kafka-stream-join-poc
Poc to demonstrate the kafka streams join capability


1 - Start Kafka with docker-compose.yml inside of kafka-stream-function: docker-compose up
2 - Run the kafka-stream-function and producer-activation-info service
3 -  curl localhost:8081/activation/key/activation
4 -  curl localhost:8081/info/abc_random/infoKey
When you run the command 3 and 4 you will see the activationInfo (join function) applied and afterJoinConsumer (output function)

Disclaimer:
- There's a window size of 30 seconds if the activationInfo function doesn't have message in both streams after the 30 seconds the messages will be discard by kafka stream.
- If the key of both messages aren't the same the the join will not be applied and a window of 30 seconds will be applied. 
Internally kafka will keep the message on the internal db and once we receive a message with the same key the function will be applied.
