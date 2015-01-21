package com.eduglasses.frontflip.messaging;

public interface MQTTCallbackListener {

	public void processMessage(String topic, byte[] messageBytes);
	
}
