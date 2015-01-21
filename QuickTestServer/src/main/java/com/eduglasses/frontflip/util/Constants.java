package com.eduglasses.frontflip.util;

public interface Constants {

	public String USERNAME = "username";
	public String PASSWORD = "password";
	public String CLIENT_ID = "EDUSHIELD_HUB";
	public String TOPIC_START = "eduglasses/edushield/";
	public String COMMAND_TO_GLASS = TOPIC_START+"command-to-glass/";
	public String COMMAND_TO_GLASS_RESPONSE = TOPIC_START+"command-to-glass/response/";
	public String COMMAND_TO_HUB = TOPIC_START+"command-to-hub/";
	public String CHAT_MSG = TOPIC_START+"chat-msg/";
	public String VOICE_MSG = TOPIC_START+"voice-msg/";
	public String UPDATE_LOCATION = TOPIC_START+"update-location/";
	public final int OUT_OF_BOUNDS_LOCATION_ID = 68;
	
}
