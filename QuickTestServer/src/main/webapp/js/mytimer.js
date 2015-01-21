var offset = 0;
var interval = 0;
var clock = 0;
var mm = 0;
function startTimer() {
	
	offset   = Date.now();
    interval = setInterval(updateTimer, 1);
  }

function stopTimer() {
      
	clearInterval(interval);
    interval = 0;
    offset = 0;
    clock = 0;
    mm = 0;
    $("#label-edutools-stopwatch").html("");
  }

function pauseTimer() {

	clearInterval(interval);
    interval = 0;
  }

function resetTimer() {
	$("#label-edutools-stopwatch").html("Restarting...");
    clock = 0;
    mm = 0;
  }

  function updateTimer() {
    clock += delta();
    var floatingNumber = clock/1000;
    var timerText = floatingNumber.toFixed(2);
    if(timerText >= 60) {
    	mm += 1;
    	clock = 0;
    }
    $("#label-edutools-stopwatch").html((mm < 10 ? "0" + mm : mm) + ":" + (timerText < 10 ? "0" + timerText : timerText));
  }

  function delta() {
    var now = Date.now(),
        d   = now - offset;

    offset = now;
    return d;
  }