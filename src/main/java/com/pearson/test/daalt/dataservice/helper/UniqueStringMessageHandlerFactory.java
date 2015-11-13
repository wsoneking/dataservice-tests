package com.pearson.test.daalt.dataservice.helper;

import com.pearson.qa.common.kafkaconsumer.handlers.MessageHandler;
import com.pearson.qa.common.kafkaconsumer.handlers.MessageHandlerFactory;
import kafka.consumer.KafkaStream;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * <b>UniqueStringMessageHandlerFactory</b>
 * </p>
 * <p>
 * Processes messages with a specific unique string.
 * </p>
 */
public class UniqueStringMessageHandlerFactory implements MessageHandlerFactory {

	private final String unique;

	public UniqueStringMessageHandlerFactory(final String uniqueString) {
		this.unique = uniqueString;
	}

	/**
	 * Processes messages with a specific unique string.
	 * @param kstream as the KafkaStream
	 * @param threadIndex as the thread index
	 * @param rawMessageMap as the map to which to save the raw messages
	 * @param processedMessageMap as the CurrentHashMap to save received messages to.
	 * @return a new instance of a message handler.
	 */
	@Override
	public MessageHandler newHandler(KafkaStream kstream, int threadIndex,
	                                 ConcurrentHashMap<String, String> rawMessageMap,
	                                 ConcurrentHashMap<Object, Object> processedMessageMap) {
		return new UniqueStringMessageHandler(kstream, threadIndex, rawMessageMap, processedMessageMap, unique);
	}

}
