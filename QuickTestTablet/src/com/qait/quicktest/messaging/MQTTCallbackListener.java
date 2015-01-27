package com.qait.quicktest.messaging;

public interface MQTTCallbackListener {

	/**
	 * Executes after receiving command from hub
	 * @param messageBytes
	 */
	public void onReceiveMessage(byte[] messageBytes);
}
