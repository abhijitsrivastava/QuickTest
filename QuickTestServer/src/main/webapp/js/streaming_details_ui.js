(function ($) {
    "use strict";

    var data = [];
    var audio_context;
    var recorder;
    var intervalId= 0;
    var toggleIntervalId= 0;
    var element = null;
    var shown = true;
    var status = 0;
    var map;
    var googleMapMarker = null;

    // Outputs some logs about jwplayer
    function print(t,obj) {
        for (var a in obj) {
            if (typeof obj[a] === "object") print(t+'.'+a,obj[a]);
            else data[t+'.'+a] = obj[a];
        }
    }

    $(document).ready(function () {

	    startPlayer($('#stream').val());
	    initMap();
	    $("#send-backup-request").click(function() {
	    	
	    	if(status == 0) {
				return;
			}
	    	
	    	$.ajax({
			    url: "sendBackup.htm",
			    type: "POST",
			    dataType : 'json',
				data : "{\"glassName\":\""
						+ $("#stream").attr("glass-name")+ "\"}",
				contentType : 'application/json',
				mimeType : 'application/json',
			    complete: function(response) {
			    	
			    	alert(response.responseText);
			    }
        	});
	    });
	    
        $("#mute").click(function () {
			var src = $(this).attr("src");
			if(src == "images/button_mute.png") {
				jwplayer("player").setMute(true);
				$(this).attr("src", "images/button_unmute.png");
			} else {
				jwplayer("player").setMute(false);
				$(this).attr("src", "images/button_mute.png");
			}
    	});
        
        $("#chat").click(function () {
			
        	if(status == 0) {
				return;
			}
        	
        	if($("#chatwindow").hasClass("chat-window-invisible")) {
        		
        		$("#chatwindow").removeClass("chat-window-invisible");
        		$("#chatwindow").addClass("chat-window-visible");
        	} else {
        		$("#chatwindow").removeClass("chat-window-visible");
        		$("#chatwindow").addClass("chat-window-invisible");
        	}
    	});
        
        $("#chat-send").click(
        		function() {
		
        			if(status == 0) {
        				return;
        			}
        			
					$("#label-chat-result").html("");
					var message = $("#txt-chat").val().trim();
					var glassName = $("#stream").attr("glass-name");
					if (message != "") {
						
						$.ajax({
							url : "sendTextMessage.htm",
							type : "POST",
							dataType : 'json',
							data : "{\"glassName\":\""
									+ glassName
									+ "\",\"message\":\"" + message
									+ "\"}",
							contentType : 'application/json',
							mimeType : 'application/json',
							complete : function(response) {
		
								$("#label-chat-result").html(
										response.responseText);
								$("#txt-chat").val("");
		
								setTimeout(function() {
									$("#label-chat-result").html("");
								}, 3000);
							}
						});
					}
		});
        
        $("#txt-chat").keyup(function(event){
            if(event.keyCode == 13){
                $("#chat-send").click();
            }
        });
        
        $("#voice-chat").click(function () {
        	
			if(status == 0) {
				return;
			}
        	
        	if($("#voicechatwindow").hasClass("chat-window-invisible")) {
        		
        		$("#voicechatwindow").removeClass("chat-window-invisible");
        		$("#voicechatwindow").addClass("chat-window-visible");
        		init();
        	} else {
        		$("#voicechatwindow").removeClass("chat-window-visible");
        		$("#voicechatwindow").addClass("chat-window-invisible");
        	}
        	
    	});
        
        $("#voice-chat-start").click(function() {
        	
        	if(status == 0) {
        		return;
        	}
        	startRecording();
        });
        
        $("#location").click(function () {
    		
        	if(status == 0) {
        		return;
        	}
        	
        	var glassName = $("#stream").attr("glass-name");
        	$.ajax({
			    url: "getLocationUpdate.htm",
			    type: "POST",
			    dataType : 'json',
				data : "{\"glassName\":\""
						+ glassName + "\"}",
				contentType : 'application/json',
				mimeType : 'application/json',
			    complete: function(response) {
			    	
			    	alert(response.responseText);
			    }
        	});
    	});
        
        status = parseInt($("#stream").attr("status"));
        
        // Changing status span color
        setTimeout(function(){applySpanStatus(status);}, 1000);
        
        intervalId = setInterval(requestForStatusChange, 20000);
        
        // displaying glass name
        $("#glass-name").html($("#stream").attr("glass-name"));
        
        element = $(".blink");
        toggleIntervalId = setInterval(toggle, 500);
    });

    $(window).load(function() {
    	requestForStatusChange();
    });
    
    $(window).unload(function() { 
		
		clearInterval(intervalId);
		clearInterval(toggleIntervalId);
	});
    
    function applySpanStatus(spanStatus) {
    	
    	status = spanStatus;
    	
    	switch (spanStatus) {
		case 0:
			// Inactive
			$("#status-span").css("background-color","#949693");
			$("#status-span").css("border-color","#949693");
			$("#status-span").css("color","#000000");
			
			// hiding chat window on status change
	    	if($("#chatwindow").hasClass("chat-window-visible")) {
	    		
	    		$("#chatwindow").removeClass("chat-window-visible");
	    		$("#chatwindow").addClass("chat-window-invisible");
	    	}
	    	
	    	// hiding voice chat window on status change
	    	if($("#voicechatwindow").hasClass("chat-window-visible")) {
	    		
	    		$("#voicechatwindow").removeClass("chat-window-visible");
	    		$("#voicechatwindow").addClass("chat-window-invisible");
	    	}
	    	$("#not-streaming-label").html("");
	    	break;
		
		case 1:
			// Active
			$("#status-span").css("background-color","#8AC43C");
			$("#status-span").css("border-color","#8AC43C");
			$("#status-span").css("color","#FFFFFF");
			
			$("#not-streaming-label").html("");
			break;
		case 2:
			// Yellow
			$("#status-span").css("background-color","#FFFF01");
			$("#status-span").css("border-color","#FFFF01");
			$("#status-span").css("color","#000000");
			
			$("#not-streaming-label").html("");
			break;
		case 3:
			// Red
			$("#status-span").css("background-color","#DE0000");
			$("#status-span").css("border-color","#DE0000");
			$("#status-span").css("color","#000000");
			
			$("#not-streaming-label").html("");
			break;
		case 4:
			// Active Not Streaming
			$("#status-span").css("background-color","#8AC43C");
			$("#status-span").css("border-color","#8AC43C");
			$("#status-span").css("color","#FFFFFF");
			
			$("#not-streaming-label").html("Not streaming");
			break;
		default:
			break;
		}
    }
    
    // Starts the flash player
    function startPlayer(stream) {

        jwplayer('player').setup({
        	height: 240,
            width: 320,
   	      sources: [{
                file: stream
            }],
            rtmp: {
                bufferlength: 3 
                //0.8
            }
        });

	    jwplayer("player").onMeta( function(event){
	    	var info = "";
            for (var key in data) {
                info += key + " = " + data[key] + "<BR>";
            }
            document.getElementById("status").innerHTML = info;
            print("event",event);
        });

        jwplayer('player').play();
    }

    function startUserMedia(stream) {
        var input = audio_context.createMediaStreamSource(stream);
        recorder = new Recorder(input);
      }

      function startRecording() {
    	  
    	  var src = $("#voice-chat-start").attr("src");
    	  if(src == "images/button_record.png") {
    		  recorder && recorder.record();
    		  $("#voice-chat-start").attr("src", "images/button_stop_record.png");
    	  } else {
    		  
    		  $("#voice-chat-start").attr("src", "images/loader.gif");
    		  recorder && recorder.stop();
    	      // create WAV download link using audio data blob
    	      createDownloadLink();
    	      recorder.clear();
    	  }

    	  $("#voice-chat-send").attr("disabled", "disabled");
      }

      function createDownloadLink() {
        recorder && recorder.exportWAV(function(blob) {
          /*var url = URL.createObjectURL(blob);
          var li = document.createElement('li');
          var au = document.createElement('audio');
          var hf = document.createElement('a');
          
          au.controls = true;
          au.src = url;
          hf.href = url;
          hf.download = new Date().toISOString() + '.wav';
          hf.innerHTML = hf.download;
          li.appendChild(au);
          li.appendChild(hf);
          recordingslist.appendChild(li);*/
        });
      }

      function init() {
        try {
          // webkit shim
          window.AudioContext = window.AudioContext || window.webkitAudioContext;
          navigator.getUserMedia = ( navigator.getUserMedia ||
                           navigator.webkitGetUserMedia ||
                           navigator.mozGetUserMedia ||
                           navigator.msGetUserMedia);
          window.URL = window.URL || window.webkitURL;
          
          audio_context = new AudioContext;
          navigator.getUserMedia ? ""  : alert('Permission denied');
        } catch (e) {
          alert('No web audio support in this browser!');
        }
        
        navigator.getUserMedia({audio: true}, startUserMedia, function(e) {
          alert('No live audio input: ' + e);
        });
      }
      
      function requestForStatusChange() {

      	$.ajax({
  			url : "updateStatusForGlass.htm",
  			type : "POST",
  			data : "{\"glassName\":\""+ $("#stream").attr("glass-name")+ "\"}",
  			contentType : 'application/json',
  			complete : function(response) {
  				
  				var json = response.responseJSON;
  				//alert(JSON.stringify(json));
  				applySpanStatus(json.status)
				applyRequestBackupChange(json.backupRequested);
  				setMapMarker(json);
  				updateLastStatusUpdateTime(json.lastLocationUpdateTime);
  			}
  		});
      }
      
      function setMapMarker(json) {
    	  
    	  var markers = $(".marker");
    	  for(var i=0; i< markers.length; i++) {
    		  $(markers[i]).hide();
    	  }
    	  
    	  if(json.showMap) {
    		  
    		  $("#campus-blueprint").hide();
    		  $("#map-canvas").show();
    		  
    		  if(googleMapMarker != null) {
    			  googleMapMarker.setMap(null);
    		  }
			  googleMapMarker = null;
			  
			  // Refreshing google map
			  google.maps.event.trigger(map, 'resize');
			  
			  // Dropping pin
			  setGoogleMapMarker(map, json.latitude, json.longitude, json.glassName);
			  
			  $("#not-received-location").hide();
			  
    	  } else {
    		  
    		  $("#campus-blueprint").show();
    		  $("#map-canvas").hide();
    		  
    		  if(json.roomNumber == null) {
    			  $("#not-received-location").show();
    		  } else {
    			  $("#not-received-location").hide();
    			  $("#" + json.roomNumber).show();
    		  }
    		  
    		  
    	  }
      }
      
      function updateLastStatusUpdateTime(timeStamp) {
    	  if(timeStamp == null) {
    		  $("#last-updated-label").hide();
    	  } else {
    		  $("#last-updated-label").show();
    	  }
    	  $("#last-updated-label").html("Last location updated at " + timeStamp);
      }
      
      function applyRequestBackupChange(requestBackup) {
    	  if(requestBackup) {
  			$("#request-backup-label").html("Requesting backup");
  			$("#send-backup-request").show();
  		} else {
  			$("#request-backup-label").html("");
  			$("#send-backup-request").hide();
  		}
      }
      
      function toggle() {
          if(shown) {
              element.css("color", "transparent");
              shown = false;
          } else {
        	  element.css("color", "#DE0000");
              shown = true;
          }
      }
     
      function initMap() {
    	  function initialize() {
        	  
        	  var mapOptions = {
        			  zoom : 15,
        			  
        			  //center: new google.maps.LatLng(28.61, 77.23)
        			  //center : new google.maps.LatLng(28.57, -80)
        	  }
        	  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        	  //setMarkers(map, locations);
          }
    	  google.maps.event.addDomListener(window, 'load', initialize);
      }
        
      function setGoogleMapMarker(map, lat, lng, glassName) {
      	  
    	  var myLatLng = new google.maps.LatLng(lat, lng);
    	  var marker = new google.maps.Marker({
    		  position : myLatLng,
    		  map : map,
    		  title : glassName
    	  });
    	  
    	  map.setCenter(new google.maps.LatLng(lat, lng));
    	  googleMapMarker = marker;
     }
      
    /*function initMap() {
			
		function initialize() {
			var mapOptions = {
				zoom : 6,
				//center: new google.maps.LatLng(28.61, 77.23)
				center : new google.maps.LatLng(28.57, -80)
			}
			map = new google.maps.Map(document.getElementById('map-canvas'),
						mapOptions);
			setMarkers(map, locations);
		}
			
		google.maps.event.addDomListener(window, 'load', initialize);
    }
      
    function setMarkers(map, locations) {
    	  
		for (var i = 0; i < locations.length; i++) {
			var glass = locations[i];
			var myLatLng = new google.maps.LatLng(glass.lat, glass.lng);
			var marker = new google.maps.Marker({
				position : myLatLng,
				map : map,
				title : glass.name
			});
			googleMapMarker = marker;
		}
    }*/
}(jQuery));
