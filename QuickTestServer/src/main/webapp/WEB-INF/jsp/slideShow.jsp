<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%-- <%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
 --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Quick Test SlideShow</title>
<meta content="width=device-width, initial-scale=1" name="viewport" />
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/generic.css" type="text/css" />
<link rel="stylesheet" href="css/admin_home.css" type="text/css" />

<style>
#svg {
	width: 500px;
	height: 300px;
}
</style>

<script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
<script type="text/javascript" src='js/bootstrap.min.js'></script>
<script type="text/JavaScript" src="js/mqttws31.js"></script>
<script type="text/JavaScript" src="js/ui.js"></script>
<script type="text/javascript" src="js/jwplayer.js"></script>
<script type="text/javascript" src="js/jquery.fullscreen-min.js"></script>
<script type="text/javascript" src="js/mytimer.js"></script>
<script type="text/JavaScript" src="js/chart.min.js"></script>


<script>
	jwplayer.key = ""
</script>


<script type="text/JavaScript">

	var client;
	var stopwatch;
	var assessmentId = 0;
	var questionNumber = 0;
	var totalQuestion = "${requestScope.numberOfQuestions}";
	var resourceUrls = [];
	
	$(function() {
		
		$(document).bind("fullscreenchange", function(e) {
			console.log("Full screen changed.");
		});
	
		$(document).bind("fullscreenerror", function(e) {
			console.log("Full screen error.");
		});
		
		var lessonId = "${requestScope.lessonId}";
    	var sectionId = "${requestScope.sectionId}";
    	
    	console.log("totalQuestion: " + totalQuestion + "\nlessonId: " + lessonId + "\nsectionId: " + sectionId);
		for(var i=0; i<totalQuestion; i++) {
			resourceUrls[i] = "${requestScope.serverUrl}"+ "/" + (i+1) + ".png";
		}
	});
	
	function doConnect() {

		$("#label-message").hide();
		
		// Random number among 1 and 1000
		var clientId = "client-" + Math.floor((Math.random() * 1000) + 1);
		console.log("Client ID: " + clientId);
		client = new Messaging.Client("${requestScope.brokerUrl}", 8000, "", clientId);
		console.log("Client instantiated.");
		client.startTrace();
		console.log("Now trying to connect...");
		client.onMessageArrived = onMessageArrived;
		client.onConnectionLost = onConnectionLost;

		var options = {
			timeout : 10,
			onSuccess : function() {
				console.log("Mqtt connected");
				// Connection succeeded; subscribe to our topic, you can add multile lines of these
				console.log("Connection established");
				doSubscribe();
			},
			onFailure : function(message) {
				$("#div-loader").hide();
				console.log("Connection failed: " + message.errorMessage);
				$(".container").hide();
				alert("Connection failed. Please check your Internet connection or try again later!");
			}
		};
		client.connect(options);
	}

	// called when the client loses its connection
	function onConnectionLost(responseObject) {

		if (responseObject.errorCode !== 0) {
			console.log("onConnectionLost:" + responseObject.errorMessage);
		}
		
		$(".container").hide();
		alert("Connection lost. Please check your Internet connection or try again later!");
	}

	function doSubscribe() {
		client.subscribe("${requestScope.topic}");
		console.log('Subscription established on topic "${requestScope.topic}"');
		$("#div-loader").hide();
		$("#label-message").show();
		$("#label-message").html("Connected!<br/>Quick Test is running, please wait for Question!");
		if (${requestScope.isTeacher}){		
		$("#button-edugrade").show();
		}
	}
	
	function videoFullScreen() {
		$('#video').toggleFullScreen();
	}
	
	function onMessageArrived(message) {
		//var dataReceived = message.payloadBytes;
		//var dataString = arrayBufferToBase64(dataReceived);
		console.log("onMessageArrived:" + message.payloadString);
		var json = $.parseJSON(message.payloadString);
		var type = json.type;
		var url = decodeURIComponent(json.url);
		console.log(type + "\n" + url);

		$("#label-message").hide();
		$("#div-video").hide();
		$("#div-eduteach").hide();
		$("#img-powerpoint").hide();
		$("#img-edugrade").hide();
		$("#label-edutools-stopwatch").hide();
		$("#label-edugrade-assessment").hide();
		$("#chart-area").hide();
		$("#ul-legend").hide();
		$("#div-edutools-noise-level").hide();

		jwplayer('player').stop();
		$("video").each(function() {
			this.pause();
		});

		switch(type) {
		
		case "POWERPOINT":

			$("#img-powerpoint").show();
			$("#img-powerpoint").attr("src", url);
			break;
			
		case "VIDEO":

			$("#div-video").show();
			$("#div-video").html('<video id="video" width="720" height="480" controls><source src="' 
					+ url + '" type="video/mp4" />Your browser does not support the video tag.</video>');
			break;
		
		// VIDEO playback
		case "PLAY_VIDEO":
			
			$("#div-video").show();
			$("#video").trigger("play");
			break;
		
		// VIDEO playback
		case "PAUSE_VIDEO":
			
			$("#div-video").show();			
			$("#video").trigger("pause");
			break;
			
		case "EDUGRADE":

			$("#label-message").show();
			$("#label-message").html("QuickTest is Running!");
			break;
		
		// EduGrade callback
		case "ASSESSMENT_CREATE":

			var assessmentId = json.assessmentId;
			var teacherId = json.teacherId;
			var serial = json.questionNumber; // -1
			$("#label-message").show();
			$("#label-edugrade-assessment").show();
			
			$("#label-message").html("QuickTest is Running!");
			$("#label-edugrade-assessment").html("Assessment ID: " + assessmentId);
			break;
			
		// EduGrade callback
		case "QUESTION_START":

			var assessmentId = json.assessmentId;
			var teacherId = json.teacherId;
			var serial = json.questionNumber;
					
			$("#img-edugrade").show();
			$("#img-edugrade").attr("src", url);

			break;

		// EduGrade callback	
		case "QUESTION_STOP":

			var assessmentId = json.assessmentId;
			var teacherId = json.teacherId;
			var serial = json.questionNumber;
			
			$("#label-message").show();
			$("#label-message").html("Submissions are closed for Question no. " + serial);
			
			if(serial == 0) {
				$("#label-message").show();
				$("#label-message").html("QuickTest is Running!");
			}
			break;
			
		// EduGrade callback
		case "EDUGRADE_OPEN_CHART":
			if (${requestScope.isTeacher}){
				$("#chart-area").show();
				var answers = json.answers;
				renderPieChart(answers);
			} else {
				$("#label-message").show();
				$("#label-message").html("Please wait for next Question.");
			}
		    
		break;
		
		case "EDUTEACH_LAUNCH":

			$("#label-message").show();
			$("#label-message").html(
					"Please wait while EduTeach is being loaded...");
			break;
		
		case "EDUTEACH_START_STREAMING":

			$("#stream").val(url);
			startEduTeachStreaming();
			break;
			
		case "CONNECTED":

			$("#label-message").show();
			$("#label-message").html("Quick Test is running, please wait for Question!");
			break;

		case "EDUDEFINE":

			$("#label-message").show();
			$("#label-message").html("EduDefine is running!");
			break;
			
		case "EDUTOOLS":

			$("#label-message").show();
			$("#label-message").html("EduTools is running!");
			break;
		
		// EduTools StopWatch callback
		case "EDUTOOLS_STOPWATCH_START":
			
			$("#label-message").show();
			$("#label-message").html("StopWatch started!");
			$("#label-edutools-stopwatch").show();
			// start the timer
			startTimer();
			break;
		
		// EduTools StopWatch callback
		case "EDUTOOLS_STOPWATCH_STOP":
			
			$("#label-message").show();
			$("#label-message").html("EduTools is running!");
			
			stopTimer();
			break;
		
		// EduTools StopWatch callback
		case "EDUTOOLS_STOPWATCH_PAUSE":
			
			$("#label-message").show();
			$("#label-edutools-stopwatch").show();
			$("#label-message").html("StopWatch paused!");
			
			pauseTimer();
			break;
			
		// EduTools StopWatch callback
		case "EDUTOOLS_STOPWATCH_RESTART":
			
			$("#label-message").show();
			$("#label-edutools-stopwatch").show();
			$("#label-message").html("StopWatch restarted!");
			
			resetTimer();
			break;
			
		// EduTools StopWatch callback
		case "EDUTOOLS_STOPWATCH_RESUME":
			
			$("#label-message").show();
			$("#label-edutools-stopwatch").show();
			$("#label-message").html("StopWatch resume");
			startTimer();
			break;
			
		// EduTools Noise Level Meter callback
		case "EDUTOOLS_NOISE_LEVEL_START":
			$("#div-edutools-noise-level").show();
			
			// Since there are 15 div blocks and 100 / 15 = 6.67
			var level = json.url/6.67;
			
			
			var colors = [ "#088A08", "#298A08", "#4B8A08", "#688A08",
					"#AEB404", "#D7DF01", "#FFFF00", "#FFBF00", "#FFBF00",
					"#FF8000", "#FF4000", "#FF0000", "#DF0101", "#B40404",
					"#B40404" ];

			var gray = "#d3d3d3";
			
			level = level.toFixed(0);
			var blocks = $("#div-edutools-noise-level div");
			var index = 0;
			$.each(blocks, function(index) {
				$(blocks[index]).css("background-color", gray);
			});

			for (var i = 1; i <= level; i++) {
				$("#l" + i).css("background-color", colors[i-1]);
			}

			if (level <= 5) {
				$("#lable-noise-status").html("Good work!");
			} else if (level <= 10) {
				$("#lable-noise-status").html("Too noisy!");
			} else {
				$("#lable-noise-status").html("Scream!");
			}

			break;
		}
	}

	function startEduTeachStreaming() {

		$("#label-message").hide();
		$("#div-eduteach").show();

		startPlayer($('#stream').val());
	}

	window.onload = function() {

		this.doConnect();
	 	//alert(navigator.userAgent);
		//if(navigator.userAgent.toLowerCase().indexOf("android") != -1) {
			
			if (!${requestScope.isTeacher}){
			
			$("#div-header-container").hide();
			$("#div-footer-container").hide();
			$("#button-toggle-fullscreen-ppt").hide();
			$("#span-fullscreen-msg").hide();
			$("#button-edugrade").hide();
			$("body").css("padding", 0);
			$("body").css("margin", 0);
			
			$("div.container").css("width", $(window).width());
			$("div.container").css("height", $(window).height());
			
			$("#div-loader").css("margin-top", (($(window).height() / 2) - 30) + "px");
			$("#label-message").css("margin-top", (($(window).height() / 2) - 30) + "px");
			
			$("div.background_body").css("background-color", "#D7D7D7");
		} else {
			$("div.background_body").css("box-shadow", "0 0 15px rgba(1, 1, 1, 1)");
		} 
		
	}

	function arrayBufferToBase64(buffer) {

		var binary = ''
		var bytes = new Uint8Array(buffer)
		var len = buffer.byteLength;
		for (var i = 0; i < len; i++) {
			binary += String.fromCharCode(bytes[i])
		}
		return window.btoa(binary);
	}

	function renderPieChart(answers) {

		var a = b = c = d = e = 0;
		$.each(answers, function(i, v) {

			switch (i) {

			case 0:
				a = v;
				break;

			case 1:
				b = v;
				break;

			case 2:
				c = v;
				break;

			case 3:
				d = v;
				break;

			case 4:
				e = v;
				break;
			}
		});

		var pieData = [ {
			value : a,
			color : "#F7464A",
			highlight : "#FF5A5E",
			label : "A"
		}, {
			value : b,
			color : "#46BFBD",
			highlight : "#5AD3D1",
			label : "B"
		}, {
			value : c,
			color : "#FDB45C",
			highlight : "#FFC870",
			label : "C"
		}, {
			value : d,
			color : "#949FB1",
			highlight : "#A8B3C5",
			label : "D"
		}, {
			value : e,
			color : "#4D5360",
			highlight : "#616774",
			label : "E"
		} ];

		var ctx = document.getElementById("chart-area").getContext("2d");
		window.myPie = new Chart(ctx).Pie(pieData);

		$("#ul-legend").show();
		$("#li-a").html(
				'<span style="color:transparent;background-color:#F7464A;">.....</span> A:'
						+ a);
		$("#li-b").html(
				'<span style="color:transparent;background-color:#46BFBD;">.....</span> B:'
						+ b);
		$("#li-c").html(
				'<span style="color:transparent;background-color:#FDB45C;">.....</span> C:'
						+ c);
		$("#li-d").html(
				'<span style="color:transparent;background-color:#949FB1;">.....</span> D:'
						+ d);
		$("#li-e").html(
				'<span style="color:transparent;background-color:#4D5360;">.....</span> E:'
						+ e);
	}
	
	function handleQuiz(button) {
		
		console.log($(button).html() + " -> Teacher ID :" + "${requestScope.teacherId}");
		
		switch($(button).html()) {
		case "Start Assessment":
			
			var jsonString = '{"teacherId":"${requestScope.teacherId}"}';
			$.ajax({
			    url: "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/EduGrade/rest/edu-grade-service/create-assessment",
			    type: "POST",
			    dataType : 'json',
				data : jsonString,
				contentType : 'application/json',
				mimeType : 'application/json',
			    complete: function(response) {
			    	
			    	console.log(response.responseText);
			    	var responseJson = JSON.parse(response.responseText);
			    	if(responseJson.statusCode == "EDU_102") {
			    		$(button).html("Start Next Question");
			    		
			    		assessmentId = responseJson.assessmentId;
			    		questionNumber = 1;
			    		
			    		$("#label-message").show();
						$("#label-edugrade-assessment").show();
						
						$("#label-message").html("QuickTest is Running!");
						$("#label-edugrade-assessment").html("Assessment ID: " + assessmentId);
						
						var jsonString = '{"type":"ASSESSMENT_CREATE", "url":"", "assessmentId":"'+assessmentId+'", "teacherId":"${requestScope.teacherId}", "questionNumber":"-1"}';
						sendMessage(jsonString);
						
			    	} else {
			    		alert(responseJson.message);	
			    	}
			    },
			    error: function(jqXHR,error, errorThrown) {  
		               
			    	if(jqXHR.status && jqXHR.status == 400) {
		            	alert(jqXHR.responseText); 
		            } else {
		                alert("Something went wrong");
		            }
		        }
        	});
			break;
		
		case "Start Next Question":
			
			/* JSONObject json = new JSONObject();
			try {
				json.put("type", "QUESTION_START");
				json.put("url", slides.get(questionCounter-1));
				json.put("assessmentId", assessmentId + "");
				json.put("teacherId", teacherId + "");
				json.put("questionNumber", questionCounter + "");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mqttConnection.publishMessage(json); */
			
			/* JSONObject holder = new JSONObject();
            holder.put(Constants.KEY_ASSESSMENT_ID, params[1]);
            holder.put(Constants.KEY_QUESTION_NUMBER, params[2]);
            holder.put(Constants.KEY_QUESTION_STATUS, params[3]); */
			
            if(resourceUrls.length <= 0) {
            	alert("No question found");
            	return;
            }
            
            var jsonString = '{"assessmentId":"' + assessmentId +'","questionNumber":"' + questionNumber + '","questionStatus":"1"}';
            //console.log("Request Json: " + jsonString);
			$.ajax({
			    url: "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/EduGrade/rest/edu-grade-service/question-update",
			    type: "POST",
			    dataType : 'json',
				data : jsonString,
				contentType : 'application/json',
				mimeType : 'application/json',
			    complete: function(response) {
			    	
			    	//console.log(response.responseText);
			    	//http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/lessons/29/141/1.png
			    	//var lessonId = 29;
			    	//var sectionId = 141;
			    	//var resourceUrl = "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/lessons/29/141/1.png";
			    	var responseJson = JSON.parse(response.responseText);
			    	if(responseJson.statusCode == "EDU_105_1") {
			    		$(button).html("Close Question and Show Result");
			    		
			    		var jsonString = '{"type":"QUESTION_START", "url":"' 
			    			+ resourceUrls[questionNumber-1] + '", "assessmentId":"' 
			    			+ assessmentId + '", "teacherId":"${requestScope.teacherId}", "questionNumber":"' 
			    			+ questionNumber + '"}';
			    		sendMessage(jsonString);
			    	} else {
			    		alert(responseJson.message);
			    	}
			    	console.log(responseJson.message);
			    },
			    error: function(jqXHR, error, errorThrown) {  
		               
			    	if(jqXHR.status && jqXHR.status == 400) {
		            	alert(jqXHR.responseText); 
		            } else {
		                alert("Something went wrong");
		            }
		        }
        	});
			
			break;
		
		case "Close Question and Show Result":
			
			/* JSONObject json = new JSONObject();
			try {
				json.put("type", "QUESTION_STOP");
				json.put("url", slides.get(questionCounter-2));
				json.put("assessmentId", assessmentId + "");
				json.put("teacherId", teacherId + "");
				json.put("questionNumber", (questionCounter-1) + "");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mqttConnection.publishMessage(json); */
			
			var jsonString = '{"assessmentId":"' + assessmentId +'","questionNumber":"' + questionNumber + '","questionStatus":"0"}';
            //alert(jsonString);
			$.ajax({
			    url: "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/EduGrade/rest/edu-grade-service/question-update",
			    type: "POST",
			    dataType : 'json',
				data : jsonString,
				contentType : 'application/json',
				mimeType : 'application/json',
			    complete: function(response) {
			    	
			    	//console.log(response.responseText);
			    	var responseJson = JSON.parse(response.responseText);
			    	if(responseJson.statusCode == "EDU_105_0") {
			    		
			    		var jsonString = '{"type":"QUESTION_STOP", "url":"' 
			    			+ resourceUrls[questionNumber-1] + '", "assessmentId":"' 
			    			+ assessmentId + '", "teacherId":"${requestScope.teacherId}", "questionNumber":"' 
			    			+ questionNumber + '"}';
						
			    		sendMessage(jsonString);
			    		fetchResult(button);
			    	} else {
			    		
			    		alert(responseJson.message);	
			    	}
			    	
			    	console.log(responseJson.message);
			    },
			    error: function(jqXHR,error, errorThrown) {  
		               
			    	if(jqXHR.status && jqXHR.status == 400) {
		            	alert(jqXHR.responseText); 
		            } else {
		                alert("Something went wrong");
		            }
		        }
        	});
			
			break;
		
		case "End Assessment":
			$(button).html("Start Assessment");
			var jsonString = '{"type":"EDUGRADE", "url":""}';
    		sendMessage(jsonString);
			break;
		}
	}
	
	function fetchResult(button) {
		
		/* JSONObject json = new JSONObject();
		try {

			JSONArray array = new JSONArray(distribution);
			json.put("type", "EDUGRADE_OPEN_CHART");
			json.put("url", "");
			json.put("answers", array);

			mqttConnection.publishMessage(json);

		} catch (JSONException e) {
			e.printStackTrace();
		} */
		
		var jsonString = '{"assessmentId":"' + assessmentId +'","questionNumber":"' + questionNumber + '"}';
		console.log("Request String: " + jsonString);
		$.ajax({
		    url: "http://ec2-54-187-106-124.us-west-2.compute.amazonaws.com:8080/EduGrade/rest/edu-grade-service/assessment-data",
		    type: "POST",
		    dataType : 'json',
			data : jsonString,
			contentType : 'application/json',
			mimeType : 'application/json',
		    complete: function(response) {
		    	
		    	console.log(response.responseText);
		    	var responseJson = JSON.parse(response.responseText);
		    	if(responseJson.statusCode == "EDU_112") {
		    		questionNumber++;
		    		if(questionNumber > totalQuestion) {
		    			$(button).html("End Assessment");		
		    		} else {
		    			$(button).html("Start Next Question");
		    		}
		    		
		    		var studentAnswers = responseJson.studentAnswers;
		    		if(studentAnswers.length > 0) {
		    		
			    		var answerA = answerB = answerC = answerD = answerE = 0;
			    		for(var i=0; i<studentAnswers.length; i++) {
			    			var studentAnswer = studentAnswers[i];
			    			var answer = studentAnswer.selectedAnswer;
			    			var studentName = studentAnswer.studentName
			    			if(answer == "A") {
			    				answerA++;
			    			} else if(answer == "B") {
			    				answerB++;
			    			} else if(answer == "C") {
			    				answerC++;
			    			} else if(answer == "D") {
			    				answerD++;
			    			} else {
			    				answerE++;
			    			}
			    		}
			    		var answers = [answerA, answerB, answerC, answerD, answerE];
			    		var jsonString = '{"type":"EDUGRADE_OPEN_CHART", "url":"", "answers":[' 
			    			+ answers + ']}';
						
			    		sendMessage(jsonString);
		    		}
		    	} else {
		    		alert(responseJson.message);	
		    	}
		    	console.log(responseJson.message);
		    },
		    error: function(jqXHR,error, errorThrown) {  
	               
		    	if(jqXHR.status && jqXHR.status == 400) {
	            	alert(jqXHR.responseText); 
	            } else {
	                alert("Something went wrong");
	            }
	        }
    	});
	}
	
	function sendMessage(json) {
		var message = new Messaging.Message(json);
		message.destinationName = "${requestScope.topic}"
		client.send(message);
	}
	
</script>

<style>
.custom-search-form {
	margin-top: 5px;
}

.btn-primary-custom {
	color: #fff;
	background-color: #428bca;
	border-color: #357ebd
}

.li-legend {
	display: inline;
	margin-right: 20px;
}
</style>
</head>

<body>
	<div class="container">

		<!-- <div class="control-group background_image_header" align="center" >
				<img src="images/logo.png" alt="logoImg" class="img-circle img-responsive" id="logo" style="height:auto; width:auto; max-width:150px; max-height:150px;"/>
			</div> -->
		<div class="background_body">
			<%@ include file="header.jsp"%>
			<center>
				<div id="dashboard"
					style="width: 100%; height: 100%; padding: 0; margin: 0;">

					<div id="div-loader" style="margin-top: 200px;">
						<img style="display: block;" src="images/loader.gif"
							alt="Loading..." /> <label>Connecting to server, please
							wait...</label>
					</div>

					<label id="label-message"
						style="font-size: 20px; display: block; margin-top: 200px;">No
						section to display</label> <label id="label-edugrade-assessment"
						style="font-size: 30px; display: block;"></label> <label
						id="label-edutools-stopwatch"
						style="font-size: 120px; display: block; margin-top: 50px;"></label>

					<img id="img-powerpoint"
						style="width: 100%; height: 100%; display: none;"
						alt="PowerPoint Slide" /> <img id="img-edugrade"
						alt="PowerPoint Slide"
						style="width: 100%; height: 100%; display: none;" />

					<div id="div-edutools-noise-level"
						style="display: none; margin-top: 125px;">

						<div id="l15"
							style="background-color: #B40404; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l14"
							style="background-color: #B40404; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l13"
							style="background-color: #DF0101; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l12"
							style="background-color: #FF0000; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l11"
							style="background-color: #FF4000; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>

						<div id="l10"
							style="background-color: #FF8000; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l9"
							style="background-color: #FFBF00; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l8"
							style="background-color: #FFBF00; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l7"
							style="background-color: #FFFF00; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l6"
							style="background-color: #D7DF01; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>

						<div id="l5"
							style="background-color: #AEB404; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l4"
							style="background-color: #688A08; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l3"
							style="background-color: #4B8A08; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l2"
							style="background-color: #298A08; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<div id="l1"
							style="background-color: #088A08; width: 200px; height: 25px; margin: 0 200px 2px 0;"></div>
						<label id="lable-noise-status"
							style="font-size: 50px; margin: -250px 0 0 300px;">Good
							work!</label>

					</div>

					<div id="div-video" style="display: none;"></div>
					<div id="div-eduteach" style="display: none;">
						<div id="player"></div>
						<input type="hidden" id="stream" value="" class="stream-class"></input>
					</div>

					<ul id="ul-legend"
						style="margin-top: 150px; padding: 0; display: none;">
						<li id="li-a" class="li-legend"></li>
						<li id="li-b" class="li-legend"></li>
						<li id="li-c" class="li-legend"></li>
						<li id="li-d" class="li-legend"></li>
						<li id="li-e" class="li-legend"></li>
					</ul>

					<canvas id="chart-area" style="margin-top: 50px;display:none;"
						width="300" height="300" />
				</div>
				<button id="button-edugrade" onclick="javascript: handleQuiz(this);"
					style="margin-top: 50px; display: none;">Start Assessment</button>
				<button id="button-toggle-fullscreen-ppt"
					onclick="$('#dashboard').toggleFullScreen();"
					style="display: block; margin-top: 100px;">Toggle
					fullscreen mode</button>
				<span id="span-fullscreen-msg" style="display: block;">*For
					best view and experience, select full screen mode</span>
			</center>
			<%@ include file="footer.jsp"%>

		</div>
	</div>
</body>
</html>
