package it.terna.nas.service.configuration;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;
import static it.terna.kernel.basement.utility.Assorted.*;
import it.terna.nas.service.material.dto.worker.MessageStreamCounter;
import it.terna.nas.service.material.dto.worker.WorkerFeedbackRequestEvent;
import it.terna.nas.service.material.utility.Constants;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig 
{
	@Value(value = "${kafka.streams.applicationId}")
	private String applicationId;
	
	@Value(value = "${kafka.streams.bootstrapServers}")
	private String bootstrapServer;
	
	@Value(value = "${broker.endPoints.dssStreamFeedbackEventHandlerManager.entityName}")
	private String feedbackTopic;
	
	@Value(value = "${kafka.streams.protocol:#{null}}")
	private String protocol;
	
	@Value(value = "${kafka.streams.stateStoreName}")
	private String stateStoreName;
	
	@Value(value = "${broker.endPoints.dssStreamResultEventHandlerManager.entityName}")
	private String topicToSend;
	
	@Value(value = "${kafka.streams.truststoreLocation:#{null}}")
	private String truststoreLocation;

	@Value(value = "${kafka.streams.truststorePassword:#{null}}")
	private String truststorePassword;
	
	
    @Bean
	public KStream<String, WorkerFeedbackRequestEvent> kStream(StreamsBuilder builder) 
	{
		JsonSerde<WorkerFeedbackRequestEvent> injestorDataSerde           = new JsonSerde<>(WorkerFeedbackRequestEvent.class);
		KStream<String, WorkerFeedbackRequestEvent> injestorDataStream    = builder.stream(feedbackTopic, Consumed.with(Serdes.String(), injestorDataSerde));
		KTable<String, MessageStreamCounter> aggregate2                   = injestorDataStream.groupByKey().
			aggregate(MessageStreamCounter::new, (key, value, aggregate) -> aggregate.addMessage(key, value), 
	                Materialized.<String, MessageStreamCounter, KeyValueStore<Bytes, byte[]>>as(stateStoreName)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(MessageStreamCounter.class))
            );

		aggregate2.filter((key, value) -> notNull(value) && notNull(value.getPolicy()) && value.inviaMessaggio())
                  .toStream()
                  .filter((key, value) -> notNull(value) && (!value.isHasSended()))
                  .mapValues(y -> {y.setHasSended(true);return y;})
                  .to(topicToSend, Produced.with(Serdes.String(), new JsonSerde<>(MessageStreamCounter.class)));
		
		aggregate2.toStream().transformValues(() -> new MessageStreamCounterVT(stateStoreName), stateStoreName);
		
		return injestorDataStream;
	}
    
    
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() 
    {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,                      applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,                   bootstrapServer);
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,   WallclockTimestampExtractor.class.getName());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,             Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,           Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        
        if (Constants.SSL.equals(protocol)) 
        {	
            props.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, protocol);

            if (isNotEmpty(truststoreLocation))
                props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
            
            if (isNotEmpty(truststorePassword))
                props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        }
		
        return new KafkaStreamsConfiguration(props);
    }
}

