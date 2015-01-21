<html lang="en">

  <meta charset="utf-8"></meta>
  
  <head>
  	<title>EduShield</title>
    <link rel="stylesheet" href="css/streaming.css" type="text/css" media="screen, handheld"/>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
    <style>
    .chat-window-invisible {
    	margin-top:10px;
    	display:none;
    }
    
    .chat-window-visible {
    	margin-top:10px;
    	display:block;
    }
    
    ul { list-style: none; }
    #recordingslist audio { display: block; margin-bottom: 10px; }
    
	html {
		height: 100%
	}
	
	body {
		height: 80%;
		margin: 0;
		padding: 0
	}
	
	#map-canvas {
		height: 70%
	}
	</style>
    <script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
    <script type="text/JavaScript" src="js/mqttws31.js"></script>
    <script type="text/javascript" src="js/recorder.js" ></script>
    <script type="text/javascript" src='js/bootstrap.min.js'></script>
    <script type="text/javascript" src="js/streaming_details_ui.js"></script>
    <script type="text/javascript" src="js/jwplayer.js" ></script>
    <script>jwplayer.key=""</script>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC77KPDS-Bvqo4nDw4m56xVwRKWpCFzZKY">
    </script>

  </head>

  <body>
		<div class="container">
			<div class="background_body">
				<%@ include file="header.jsp" %>
				<div style="vertical-align:middle;padding-top:10px;padding-bottom:10px;clear:both;">
    				<h3 style="color:#8AC43C; display:inline;" id="glass-name">My Glass</h3>
    				<span id="last-updated-label" style="text-decoration:none; float:right;">Last updated at 00:00:00</span>
    				<h4 id="request-backup-label" class="blink" style="margin-left:50px;color:#DE0000; display:inline;"></h4>
    				<button id="send-backup-request" class="btn btn-primary">Send Backup</button>
    				<h4 id="not-streaming-label" class="blink" style="margin-left:50px;color:#DE0000; display:inline;"></h4>
    			</div>
					<script type="text/javascript">
						locations = new Object();
						locations.name = "${locations.glassName}";
						locations.lat = ${locations.latitude};
						locations.lng = ${locations.longitude};
				    </script>
					
					<div id="map-canvas"  style="width: 65%; float:right;"></div>
					
					<div style="width: 65%; float:right;">
					<img id="campus-blueprint" alt="Campus Blueprint"  src="images/campus_blueprint.png">
					</div>
					
					<div id="1-131" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-131"  src="images/1-131.png">
					</div>
					
					<div id="1-132" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-132"  src="images/1-132.png">
					</div>
					
					<div id="1-133" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-133"  src="images/1-133.png">
					</div>
					
					<div id="1-136" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-136"  src="images/1-136.png">
					</div>
					
					<div id="1-161" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-161"  src="images/1-161.png">
					</div>
					
					<div id="1-137" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-137"  src="images/1-137.png">
					</div>
					
					
					<div id="1-138" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-138"  src="images/1-138.png">
					</div>
					
										
					<div id="1-140" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-140"  src="images/1-140.png">
					</div>
					
								
					<div id="1-152" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-152"  src="images/1-152.png">
					</div>
					
						
					<div id="1-159" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-159"  src="images/1-159.png">
					</div>
						
					<div id="1-160" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-160"  src="images/1-160.png">
					</div>
						
					<div id="1-161" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-161"  src="images/1-161.png">
					</div>
					
					<div id="1-163" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-163"  src="images/1-163.png">
					</div>
					
					<div id="1-163A" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-163A"  src="images/1-163A.png">
					</div>
					
					<div id="1-164" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-164"  src="images/1-164.png">
					</div>
					
					<div id="1-168" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-168"  src="images/1-168.png">
					</div>
					
					<div id="1-170" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-170"  src="images/1-170.png">
					</div>
					
					<div id="1-173" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-173"  src="images/1-173.png">
					</div>
					
    				<div id="1-171" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-171"  src="images/1-171.png">
					</div>
					
    				<div id="1-169" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-169"  src="images/1-169.png">
					</div>
					
    				<div id="1-167" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-167"  src="images/1-167.png">
					</div>
					
    				<div id="1-165" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-165"  src="images/1-165.png">
					</div>
					
    				<div id="1-174" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-174"  src="images/1-174.png">
					</div>
					
    				<div id="1-175" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-175"  src="images/1-175.png">
					</div>
					
    				<div id="1-176" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-176"  src="images/1-176.png">
					</div>
					
    				<div id="1-178" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-178"  src="images/1-178.png">
					</div>
					
    				<div id="1-183" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-183"  src="images/1-183.png">
					</div>
					
    				<div id="1-184" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-184"  src="images/1-184.png">
					</div>
					
    				<div id="1-129" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-129"  src="images/1-129.png">
					</div>
					
    				<div id="1-126" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-126"  src="images/1-126.png">
					</div>
					
    				<div id="1-135" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-135"  src="images/1-135.png">
					</div>
					
    				<div id="1-121" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-121"  src="images/1-121.png">
					</div>
					
    				<div id="1-128" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-128"  src="images/1-128.png">
					</div>
					
    				<div id="1-127" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-127"  src="images/1-127.png">
					</div>
					
    				<div id="1-125" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-125"  src="images/1-125.png">
					</div>
					
    				<div id="1-123" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-123"  src="images/1-123.png">
					</div>
					
    				<div id="1-122" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-122"  src="images/1-122.png">
					</div>
					
    				<div id="1-116" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-116"  src="images/1-116.png">
					</div>
					
    				<div id="1-115" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-115"  src="images/1-115.png">
					</div>
					
    				<div id="1-103" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-103"  src="images/1-103.png">
					</div>
					
    				<div id="1-112" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-112"  src="images/1-112.png">
					</div>
					
    				<div id="1-107" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-107"  src="images/1-107.png">
					</div>
					
    				<div id="1-110" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-110"  src="images/1-110.png">
					</div>
					
    				<div id="1-108" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-108"  src="images/1-108.png">
					</div>
					
    				<div id="1-181" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-181"  src="images/1-181.png">
					</div>
					
    				<div id="1-181A" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-181A"  src="images/1-181A.png">
					</div>
					
    				<div id="1-181B" class="marker" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="1-181B"  src="images/1-181B.png">
					</div>
					
					<div id="not-received-location" style="width: 65%; margin-top:-564px; float:right;display:none;">
					<img alt="Location is not received"  src="images/not_received_location.png">
					</div>
					
    				<div id="player"></div>
    				
    				<br/>
						
						<input type="hidden" id="stream" value="${requestScope.streamingURL}" 
										status="${requestScope.status}" 
										glass-name="${requestScope.glassName}" 
										random-id="${requestScope.randomId}"
										broker-url="${requestScope.brokerUrl}"></input>

						<!-- <img id="start" class="btn btn-primary" src="images/button_play.png" width="60"></img>
				  		<img id="stop" class="btn btn-primary" src="images/button_stop.png" width="60"></img> -->
				  		<img id="mute" class="btn btn-primary" src="images/button_mute.png" width="60" height="50"></img>
				  		<img id="chat" class="btn btn-primary" src="images/button_chat.png" width="60" height="50"></img>
				  		<img id="voice-chat" class="btn btn-primary" src="images/button_voice_chat.png" width="60" height="50"></img>
				  		<img id="location" class="btn btn-primary" src="images/button_location.png" width="60" height="50"></img>
				  		<span id="status-span" class="btn" style="width:70px; height:50px; cursor: default;"></span>

    					<div id="chatwindow" class="chat-window-invisible">
    						<input id="txt-chat" type="text" placeholder="Enter message here" style="width:200px; padding:7px;"/>
    						<button id="chat-send" class="btn btn-primary">Send</button>
    						<br/>
    						<label id="label-chat-result"></label>
    					</div>
    					
    					<div id="voicechatwindow" class="chat-window-invisible">
    						<img id="voice-chat-start" class="" src="images/button_record.png" width="60"></img>
    						<!-- <button id="voice-chat-start" class="btn btn-primary">Start</button> -->
    						<!-- <button id="voice-chat-stop" class="btn btn-primary" disabled="disabled" >Stop</button> -->
    						<button id="voice-chat-send" class="btn btn-primary" disabled="disabled">Send</button>
    					</div>
    					
    					
    					<div id="status1" style="clear:both;"></div>
   						 <%@ include file="footer.jsp" %>
    				</div>
			</div>
  	</body>
</html>