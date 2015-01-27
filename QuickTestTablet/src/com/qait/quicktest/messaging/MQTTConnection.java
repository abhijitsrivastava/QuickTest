package com.qait.quicktest.messaging;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.app.Activity;

import com.qait.quicktest.PresentationActivity;
import com.qait.quicktest.constants.Constants;
import com.qait.quicktest.model.LoginData;
import com.qait.quicktest.utils.Utils;

public class MQTTConnection {

	private MqttAsyncClient mqttAsyncClient;
	private MqttConnectOptions connectionOption;
	private IMqttActionListener connectionListener;
	private boolean isConnected = false;
	private Activity activity;
	private MQTTCallbackListener mqttCallbackListener;
	private String subscriptionTopic;

	// Creating constant topics

	public MQTTConnection(Activity activity,MQTTCallbackListener mqttCallbackListener,String subscriptionTopic) {
		this.activity = activity;
		this.mqttCallbackListener = mqttCallbackListener;
		this.subscriptionTopic = subscriptionTopic;
		this.connect();
	}

	public boolean isConnected() {
		return isConnected;
	}

	private void connect() {

		connectionOption = new MqttConnectOptions();
		connectionOption.setCleanSession(true);
		connectionOption.setKeepAliveInterval(30);
		connectionOption.setUserName(Constants.USERNAME);
		connectionOption.setPassword(Constants.PASSWORD.toCharArray());

		// Connect to Broker
		try {
			String clientId = Constants.CLIENT_ID
					+ LoginData.getInstance().getStudentId();
			mqttAsyncClient = new MqttAsyncClient(Constants.BROKER_URL_SERVER,
					clientId, null);
			connectionListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					Utils.dLog("Connected to " + Constants.BROKER_URL_SERVER);
					Utils.dLog("subscriptionTopic : " + subscriptionTopic);
					isConnected = true;
					
					subscribe(subscriptionTopic, mqttCallbackListener);
					((PresentationActivity) activity).onMqttConnected(MQTTConnection.this);
				}

				public void onFailure(IMqttToken token, Throwable throwable) {
					throwable.printStackTrace();
					Utils.dLog("Reconnecting...");
					reconnectUsingMqtt();
				}
			};
			mqttAsyncClient.connect(connectionOption, null, connectionListener);

		} catch (MqttException e) {
			Utils.eLog(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Utils.eLog(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * runClient The main functionality of this simple example. Create a MQTT
	 * client, connect to broker, pub/sub, disconnect.
	 * 
	 */
	public void subscribe(String topic,
			final MQTTCallbackListener mqttCallbackListener) {

		mqttAsyncClient.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message)
					throws Exception {

				if (topic == null) {
					topic = "";
				}

				byte[] messageBytes = message.getPayload();

				if (topic.equals(subscriptionTopic)) {
					mqttCallbackListener.onReceiveMessage(messageBytes);
				}

			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {

			}

			@Override
			public void connectionLost(Throwable cause) {
				reconnectUsingMqtt();

			}
		});

		int subQoS = 0;
		try {
				// Subscribe for receiving command from client
				mqttAsyncClient.subscribe(topic, subQoS, null,
						new IMqttActionListener() {

							@Override
							public void onSuccess(IMqttToken token) {
								Utils.dLog("Subscribed topics "
										+ Arrays.asList(token.getTopics())
												.toString());
							}

							@Override
							public void onFailure(IMqttToken token,
									Throwable throwable) {
								Utils.eLog("Unable to subscribe topics "
										+ Arrays.asList(token.getTopics())
												.toString());
								throwable.printStackTrace();
							}
						});
		} catch (MqttException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Reconnect using mqtt connection
	 * 
	 * @param conOpt
	 *            Holds the set of options that control how the client connects
	 *            to a server
	 */
	private void reconnectUsingMqtt() {

		try {
			mqttAsyncClient.connect(connectionOption, null, connectionListener);
		} catch (MqttException e) {

			Utils.eLog("MQTT Connection failed" + e.getMessage());

		}
	}

	/**
	 * To publish command status form device over broker for desktop.
	 * 
	 * @param message
	 */
/*	public void publishMessage(String message, String topic) {

		int pubQoS = 0;

		// Publish the message
		try {

			MqttMessage commandMessage = new MqttMessage(message.getBytes());
			commandMessage.setQos(pubQoS);
			commandMessage.setRetained(false);
			mqttAsyncClient.publish(topic, commandMessage, null,
					new IMqttActionListener() {

						@Override
						public void onSuccess(IMqttToken token) {
							Utils.dLog("Published to topics "
									+ Arrays.asList(token.getTopics())
											.toString());
						}

						@Override
						public void onFailure(IMqttToken token,
								Throwable throwable) {
							Utils.eLog("Unable to publish to topics "
									+ Arrays.asList(token.getTopics())
											.toString());
							throwable.printStackTrace();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	public void close() {
		try {
			if (mqttAsyncClient != null) {
				mqttAsyncClient.disconnect();
				mqttAsyncClient.close();
				isConnected = false;
				connectionListener = null;
				connectionOption = null;
				mqttCallbackListener = null;
				mqttAsyncClient = null;
				Utils.dLog("MQTTConnection closed successfully");
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public MQTTCallbackListener getMqttCallbackListener() {
		return mqttCallbackListener;
	}

	public void setMqttCallbackListener(
			MQTTCallbackListener mqttCallbackListener) {
		this.mqttCallbackListener = mqttCallbackListener;
	}
}
