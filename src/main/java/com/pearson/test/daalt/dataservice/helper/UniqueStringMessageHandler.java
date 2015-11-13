package com.pearson.test.daalt.dataservice.helper;

import com.pearson.qa.common.kafkaconsumer.handlers.MessageHandler;
import kafka.consumer.KafkaStream;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * <b>UniqueStringMessageHandler</b>
 * </p>
 * <p>
 *
 * </p>
 */
public class UniqueStringMessageHandler extends MessageHandler {

	private final String unique;

	public UniqueStringMessageHandler(KafkaStream kstream, int threadIndex,
	                                  ConcurrentHashMap<String, String> rawMessageMap,
	                                  ConcurrentHashMap<Object, Object> processedMessageMap, String unique) {
		super(kstream, threadIndex, rawMessageMap, processedMessageMap);
		this.unique = unique;
	}

	@Override
	public void process(String message) {
		if (message.contains(unique)) {
			String key = UUID.randomUUID().toString();
			processedMessageMap.put(key, message);
		}
	}

}
