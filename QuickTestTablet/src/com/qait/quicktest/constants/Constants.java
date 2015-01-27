package com.qait.quicktest.constants;

public class Constants {

	//public static final String PRESENTATION_URL = "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/FrontFlip/viewSlideShow.htm?lessonId=";
	public static final String PRESENTATION_URL = "http://115.113.54.10:8080/QuickTest/viewSlideShow.htm?isTeacher=false&lessonId=";
	
	public static final String KEY_PRESENTATION_ID = "presentation_id";
	public static final String KEY_STUDENT_ID = "presentation_id";
	//public static final String PRESENTATION_URL = "http://10.0.10.26:8080/FrontFlip/viewSlideShow.htm?lessonId=";
	
	public static final String USERNAME = "username@eduglasses.com";
	public static final String PASSWORD = "eduglasses@123";
	public static final String CLIENT_ID = "client_id_";
	public static final String BROKER_URL_SERVER = "tcp://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:1883";
	public static final String FLAG_MQTT_CONNECTED = "CONNECTED";
	public static final String SUBSCRIBTION_TOPIC = "";
	public static final String TOPIC_SLIDE_NUMBER = "com/eduglasses/slide_show/";	
	
	
	public static String KEY_ASSESSMENT_ID = "assessmentId";
	public static String KEY_STUDENT_NAME = "studentName";
	public static String KEY_QUESTION_NUMBER = "questionNumber";
	public static String KEY_SELECTED_ANSWER = "selectedAnswer";
	
	public static final String BASE_URL_EDUGRADE = "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/EduGrade/rest/edu-grade-service";
	//public static final String BASE_URL = "http://172.16.4.164:8080/EduGradeServer/rest/edu-grade-service";
	public static final String SERVICE_UPDATE_ANSWER = "/answer-update";
	
	//public static final String BASE_URL = "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080";
	public static final String BASE_URL = "http://115.113.54.10:8080";
	
	//public static final String SERVICE = "/FrontFlip/rest/frontflip-service/get-lesson-details?lessonId=";
	public static final String SERVICE = "/QuickTest/rest/frontflip-service/get-lesson-details?lessonId=";
}
