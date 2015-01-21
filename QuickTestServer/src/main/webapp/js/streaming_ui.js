(function ($) {
    "use strict";

    var data = [];

    // Outputs some logs about jwplayer
    function print(t,obj) {
        for (var a in obj) {
            if (typeof obj[a] === "object") print(t+'.'+a,obj[a]);
            else data[t+'.'+a] = obj[a];
        }
    }

    var players = null;
	var streams = null;
	var intervalId = 0;
	var toggleIntervalId = 0;
	var element = null;
    var shown = true;
	
	$(window).load(function() {
		
		var playerElements = {};
		for(var i=1; i<= players.length; i++) {
			
			var playerWrapperId = "player" + i + "_wrapper";
	        var stream = $(streams[i-1]).attr("value");
	        designPlayer(playerWrapperId, i, stream);
	        
	        playerElements[i-1] = $("#" + playerWrapperId).html();
		}
		
		/*for(var i=1; i<=players.length; i++) {
			$("#player" + i + "_wrapper").remove();
			console.log(i + " = i");
		}
		
		for(var i=1; i<=players.length; i++) {
			//$("player" + i + "_wrapper").html(playerElements[i-1]);
			$(".background_body")
					.append(
							'<div id="player"'
									+ i
									+ '"_wrapper" style="position: relative; width: 320px; height: 240px; float: left; margin: 50px 10px 10px; display: inline;">'
									+ playerElements[i - 1]
									+ '</div>');
		}*/
		
		
		
		$('body').append(
				'<div class="container" style="margin-top:100px; padding:10px;"><div class="background_body"><div class="footer"><p>Copyright &copy; <a style="color:white;" ' 
				+ 'target="_blank" '
				+ 'href="http://www.eduglasses.com/">EduGlasses</a>,2014. All rights reserved.</p></div></div></div>');
		
		intervalId = setInterval(requestForStatusChange, 20000);
		
		element = $(".blink");
		toggleIntervalId = setInterval(toggle, 500);
	});
	
	$(window).unload(function() { 
		
		clearInterval(intervalId);
		clearInterval(toggleIntervalId);
	});
	
    $(document).ready(function () {
    	
    	players = $(".player-class");
    	streams = $(".stream-class");
    	
    	for(var i=0; i<players.length; i++) {
    		
    		var playerId = $(players[i]).attr("id");
    		var streamValue = $(streams[i]).attr("value");
    		startPlayer(playerId, streamValue);
       	}    	    
    });

    // Starts the flash player
    function startPlayer(id, stream) {
    	
		jwplayer(id).setup({
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

	     /*jwplayer(id).onMeta( function(event){
            var info = "";
            for (var key in data) {
                info += key + " = " + data[key] + "<BR>";
            }
            $("#status").html(info);

            print("event",event);

        });*/

        jwplayer(id).play();
        jwplayer(id).setMute(true);
        
    }
    
    function designPlayer(playerWrapperId, playerIndex, stream) {
    	
    	var randomId = $("#stream" + playerIndex).attr("random-id");
		var status = parseInt($("#stream" + playerIndex).attr("status"));
		var requestBackup = $("#stream" + playerIndex).attr("backup-requested") == "true";
		var glassName = $("#stream" + playerIndex).attr("device-name");
		
    	$("#" + playerWrapperId).css("float", "left");
		$("#" + playerWrapperId).css("margin", "10px");
		$("#" + playerWrapperId).css("margin-top", "50px");
		$("#" + playerWrapperId).css("display", "inline");
		
		/*$("#" + playerWrapperId).append(
				'<button player-id="' + ("player" + playerIndex) 
				+ '" stream-value="' + stream 
				+ '" id="start' + playerIndex + '" class="btn btn-primary" style="margin:1px;">Start</button>');
		
		$("#" + playerWrapperId).append(
				'<button player-id="' + ("player" + playerIndex)
				+ '" id="stop' + playerIndex + '" class="btn btn-primary" style="margin:1px;">Stop</button>');*/
		
		$("#" + playerWrapperId).append(
				'<button player-id="' + ("player" + playerIndex)
				+ '" id="mute' + playerIndex + '" class="btn btn-primary" style="margin:1px;">Unmute</button>');
		
		$("#" + playerWrapperId).prepend(
				'<label class="blink" id="span-streaming-status' + playerIndex + '" style="margin:1px; float:right; color:#DE0000;"></label>');
		
		if(requestBackup) {
			$("#" + playerWrapperId).prepend(
				'<label class="blink" id="request-backup-label-' + playerIndex + '" style="margin:1px; float:right; color:#DE0000;">Requesting backup</label>');
		} else {
			$("#" + playerWrapperId).prepend(
				'<label class="blink" id="request-backup-label-' + playerIndex + '" style="margin:1px; float:right; color:#DE0000;"></label>');
		}
		
		$("#" + playerWrapperId).prepend(
				'<label style="margin:1px;">' + glassName + '</label>');
		
		$("#" + playerWrapperId).append(
				'<button player-id="' + ("player" + playerIndex)
				+ '" id="expand' + playerIndex + '" class="btn btn-primary" style="margin:1px;">Expand video</button>');
		
		/*$("#start" + playerIndex).click(function () {
			var playerId = $(this).attr("player-id");
			var streamValue = $(this).attr("stream-value");
			//alert(playerId + "\n" + streamValue);
         	startPlayer(playerId, streamValue);         
		});
		
		$("#stop" + playerIndex).click(function () {
			var playerId = $(this).attr("player-id");
			//alert(playerId);
			jwplayer(playerId).setMute(true);
			jwplayer(playerId).stop();            
    	});*/
		
		$("#mute" + playerIndex).click(function () {
			
			var label = $(this).html();
			var playerId = $(this).attr("player-id");
			if(label == "Mute") {
				jwplayer(playerId).setMute(true);
				$(this).html("Unmute");
			} else {
				jwplayer(playerId).setMute(false);
				$(this).html("Mute");
			}
    	});
		

		$("#expand" + playerIndex).click(function() {

			window.open("showExpandedVideo.htm?randomId=" + randomId, '_self');
		});
		
    	applyStatus(playerIndex, status);
    }
    
    function applyStatus(playerIndex, status) {
    	
    	switch (status) {
		case 0:
			// Inactive
			$("#expand" + playerIndex).css("background-color","#949693");
			$("#expand" + playerIndex).css("border-color","#949693");
			$("#expand" + playerIndex).css("color","#000000");
			$("#span-streaming-status" + playerIndex).html("");
			break;
		
		case 1:
			// Active
			$("#expand" + playerIndex).css("background-color","#8AC43C");
			$("#expand" + playerIndex).css("border-color","#8AC43C");
			$("#expand" + playerIndex).css("color","#FFFFFF");
			$("#span-streaming-status" + playerIndex).html("");
			break;
			
		case 2:
			// Yellow
			$("#expand" + playerIndex).css("background-color","#FFFF01");
			$("#expand" + playerIndex).css("border-color","#FFFF01");
			$("#expand" + playerIndex).css("color","#000000");
			$("#span-streaming-status" + playerIndex).html("");
			break;
		case 3:
			// Red
			$("#expand" + playerIndex).css("background-color","#DE0000");
			$("#expand" + playerIndex).css("border-color","#DE0000");
			$("#expand" + playerIndex).css("color","#000000");
			$("#span-streaming-status" + playerIndex).html("");
			break;
			
		case 4:
			// Active Not STREAMING
			$("#expand" + playerIndex).css("background-color","#8AC43C");
			$("#expand" + playerIndex).css("border-color","#8AC43C");
			$("#expand" + playerIndex).css("color","#FFFFFF");
			$("#span-streaming-status" + playerIndex).html("Not streaming");
			break;
		
		default:
			break;
		}
    }
    
    function requestForStatusChange() {

    	$.ajax({
			url : "updateStatus.htm",
			type : "POST",
			contentType : 'application/json',
			complete : function(response) {
				var json = response.responseJSON;
				var glassList = json.glassList;
				//alert(JSON.stringify(glassList));
				for(var i =0; i< glassList.length; i++) {
					applyStatus(i+1, glassList[i].status);
					updateRequestBackup(i+1, glassList[i].backupRequested);
				}
			}
		});
    }
    
    function updateRequestBackup(playerIndex, request) {
    	
    	if(request) {
			$("#request-backup-label-" + playerIndex).html("Requesting backup");
		} else {
			$("#request-backup-label-" + playerIndex).html("");
		}
    }

    function toggle() {
        if(shown) {
            element.hide();
            shown = false;
        } else {
            element.show();
            shown = true;
        }
    }
}(jQuery));
