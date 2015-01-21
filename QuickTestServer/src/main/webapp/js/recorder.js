(function(window){

  var WORKER_PATH = 'js/recorderWorker.js';
  var encoderWorker = new Worker('js/mp3Worker.js');

  var Recorder = function(source, cfg){
    var config = cfg || {};
    var bufferLen = config.bufferLen || 4096;
    this.context = source.context;
    this.node = (this.context.createScriptProcessor ||
                 this.context.createJavaScriptNode).call(this.context,
                                                         bufferLen, 2, 2);
    var worker = new Worker(config.workerPath || WORKER_PATH);
    worker.postMessage({
      command: 'init',
      config: {
        sampleRate: this.context.sampleRate
      }
    });
    var recording = false,
      currCallback;

    this.node.onaudioprocess = function(e){
      if (!recording) return;
      worker.postMessage({
        command: 'record',
        buffer: [
          e.inputBuffer.getChannelData(0),
          //e.inputBuffer.getChannelData(1)
        ]
      });
    }

    this.configure = function(cfg){
      for (var prop in cfg){
        if (cfg.hasOwnProperty(prop)){
          config[prop] = cfg[prop];
        }
      }
    }

    this.record = function(){
      recording = true;
    }

    this.stop = function(){
      recording = false;
    }

    this.clear = function(){
      worker.postMessage({ command: 'clear' });
    }

    this.getBuffer = function(cb) {
      currCallback = cb || config.callback;
      worker.postMessage({ command: 'getBuffer' })
    }

    this.exportWAV = function(cb, type){
      currCallback = cb || config.callback;
      type = type || config.type || 'audio/wav';
      if (!currCallback) throw new Error('Callback not set');
      worker.postMessage({
        command: 'exportWAV',
        type: type
      });
    }
	
	//Mp3 conversion
    worker.onmessage = function(e){
      var blob = e.data;
	  //console.log("the blob " +  blob + " " + blob.size + " " + blob.type);
	  
	  var arrayBuffer;
	  var fileReader = new FileReader();
	  
	  fileReader.onload = function(){
		arrayBuffer = this.result;
		var buffer = new Uint8Array(arrayBuffer),
        data = parseWav(buffer);
        
        console.log(data);
		console.log("Converting to Mp3");
		//log.innerHTML += "\n" + "Converting to Mp3";

        encoderWorker.postMessage({ cmd: 'init', config:{
            mode : 3,
			channels:1,
			samplerate: data.sampleRate,
			bitrate: data.bitsPerSample
        }});

        encoderWorker.postMessage({ cmd: 'encode', buf: Uint8ArrayToFloat32Array(data.samples) });
        encoderWorker.postMessage({ cmd: 'finish'});
        encoderWorker.onmessage = function(e) {
            if (e.data.cmd == 'data') {
			
				console.log("Done converting to Mp3");
				//log.innerHTML += "\n" + "Done converting to Mp3";
				
				/*var audio = new Audio();
				audio.src = 'data:audio/mp3;base64,'+encode64(e.data.buf);
				audio.play();*/
                
				//console.log ("The Mp3 data " + e.data.buf);
				byteMp3 = new Uint8Array(e.data.buf);
				$("#voice-chat-send").removeAttr("disabled");
				$("#voice-chat-start").attr("src", "images/button_record.png");
				
				/*var mp3Blob = new Blob([new Uint8Array(e.data.buf)], {type: 'audio/mp3'});
				uploadAudio(mp3Blob);
				
				var url = 'data:audio/mp3;base64,'+encode64(e.data.buf);
				//downloadURL(url);
				//alert(url);
				
				var time = new Date().getTime();
				var id = "download-url-" + time;
				var hf = document.createElement('a');
				hf.id = id;
				hf.href = url;
				hf.download = 'audio_recording_' + time + '.mp3';
				hf.innerHTML = '<span>' + hf.download + '</span>';
				$("body").append(hf);
				$("#" + id).find("span").trigger('click'); alert("done");*/
            }
        };
	  };
	  
	  fileReader.readAsArrayBuffer(blob);
	  
      currCallback(blob);
    }
	
    function downloadURL(url) {
	    var hiddenIFrameID = 'hiddenDownloader',
	        iframe = document.getElementById(hiddenIFrameID);
	    if (iframe === null) {
	        iframe = document.createElement('iframe');
	        iframe.id = hiddenIFrameID;
	        iframe.style.display = 'none';
	        document.body.appendChild(iframe);
	    }
	    iframe.src = url;
	};
    
	function encode64(buffer) {
		var binary = '',
			bytes = new Uint8Array( buffer ),
			len = bytes.byteLength;

		for (var i = 0; i < len; i++) {
			binary += String.fromCharCode( bytes[ i ] );
		}
		return window.btoa( binary );
	}

	function parseWav(wav) {
		function readInt(i, bytes) {
			var ret = 0,
				shft = 0;

			while (bytes) {
				ret += wav[i] << shft;
				shft += 8;
				i++;
				bytes--;
			}
			return ret;
		}
		if (readInt(20, 2) != 1) throw 'Invalid compression code, not PCM';
		if (readInt(22, 2) != 1) throw 'Invalid number of channels, not 1';
		return {
			sampleRate: readInt(24, 4),
			bitsPerSample: readInt(34, 2),
			samples: wav.subarray(44)
		};
	}

	function Uint8ArrayToFloat32Array(u8a){
		var f32Buffer = new Float32Array(u8a.length);
		for (var i = 0; i < u8a.length; i++) {
			var value = u8a[i<<1] + (u8a[(i<<1)+1]<<8);
			if (value >= 0x8000) value |= ~0x7FFF;
			f32Buffer[i] = value / 0x8000;
		}
		return f32Buffer;
	}
	
	function uploadAudio(mp3Data){
		var reader = new FileReader();
		reader.onload = function(event){
			var fd = new FormData();
			var mp3Name = encodeURIComponent('audio_recording_' + new Date().getTime() + '.mp3');
			console.log("mp3name = " + mp3Name);
			fd.append('fname', mp3Name);
			fd.append('data', event.target.result);
			$.ajax({
				type: 'POST',
				url: 'upload.php',
				data: fd,
				processData: false,
				contentType: false
			}).done(function(data) {
				console.log(data);
				//log.innerHTML += "\n" + data;
			});
		};      
		reader.readAsDataURL(mp3Data);
	}
	
    source.connect(this.node);
    this.node.connect(this.context.destination);    //this should not be necessary
  };
  
  /*Recorder.forceDownload = function(blob, filename){
	console.log("Force download");
    var url = (window.URL || window.webkitURL).createObjectURL(blob);
    var link = window.document.createElement('a');
    link.href = url;
    link.download = filename || 'output.wav';
    var click = document.createEvent("Event");
    click.initEvent("click", true, true);
    link.dispatchEvent(click);
  }*/

  window.Recorder = Recorder;
  
  //Start MQTT
  var client;
  function doConnect() {
		
	 client = new Messaging.Client($("#stream").attr("broker-url"), 8000, "mqttv3.1");
	 console.log("Client instantiated.");
	 client.startTrace();
	 console.log("Now trying to connect...");
	 client.onMessageArrived = onMessageArrived;
	 client.onConnectionLost = onConnectionLost;
	 client.connect({
		 onSuccess : onConnect
	});
  }

	// called when the client connects to its connection
	function onConnect() {
		
		if($("#voice-chat-start").attr("send-msg") == "yes") {
			console.log("retrying send voice message");
			setTimeout(onConnect, 1000);
		} else {
			console.log("connection established and message sent");
			sendMessage();
		}
		
	}

	// called when the client loses its connection
	function onConnectionLost(responseObject) {
	  if (responseObject.errorCode !== 0) {
	    console.log("onConnectionLost:" + responseObject.errorMessage);
	  }
	}
	
	function doSubscribe() {

		client.subscribe("eduglass/test/display");
		console.log("Subscription established");
	}
	
	function onMessageArrived(message) {
		var dataReceived = message.payloadBytes;
		var dataString = arrayBufferToBase64(dataReceived);
		console.log("onMessageArrived:" + dataString);

		$('img').attr('src', "data:image/jpg;base64," + dataString);

		// $('#messagelist').prepend('<li>'+message.destinationName+ '->'
		// +message.payloadString+'</li>');

		// var form = document.getElementById("example");

		// form.receiveMsg.value = message.payloadString;

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
	
	function sendMessage() {

		var message = new Messaging.Message(byteMp3);
		var randomId = parseInt($("#stream").attr("random-id"));
		message.destinationName = "eduglasses/edushield/voice-msg/" + randomId;
		client.send(message);
		$("#voice-chat-send").attr("disabled", "disabled");
	}
	// End MQTT
	
	$(window).load(function() {
		$("#voice-chat-send").click(function() {
			
			doConnect();
        });
	});
	
})(window);
