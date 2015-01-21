<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport" />
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/generic.css" type="text/css" />

<style type="text/css">
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
<script type="text/javascript" src='js/bootstrap.min.js'></script>
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC77KPDS-Bvqo4nDw4m56xVwRKWpCFzZKY">
    </script>
<script type="text/javascript">
		var map;
		locations = new Array();
		var count = 0;
      function initialize() {
    		  var mapOptions = {
    		    zoom: 12,
    		    center: new google.maps.LatLng(28.6100, 78)
    		  }
    		  map = new google.maps.Map(document.getElementById('map-canvas'),
    			        mapOptions);
    		    		  
    		  setMarkers(map, locations);
    		}

    		function setMarkers(map, locations) {     			
    			for (var i = 0; i < locations.length; i++) {    				
    			    var glass = locations[i];
    			    var myLatLng = new google.maps.LatLng(glass.lat, glass.lng);
    			    var marker = new google.maps.Marker({
    			        position: myLatLng,
    			        map: map,    			       
    			        title: glass.name
    			    });
    			  }
    		  }
      
      google.maps.event.addDomListener(window, 'load', initialize);
    </script>

</head>

<body>
	<div class="container">
		<div class="background_body">

			<%@ include file="header.jsp"%>
		</div>
		</div>
		<c:forEach var="item" items="${locations}" varStatus="counter">
		<script type="text/javascript">
		locations[count] = new Object();
		locations[count].name = "${item.glassName}";
		locations[count].lat = ${item.latitude};
		locations[count].lng = ${item.longitude};
		count = count+1;
	    </script>
		</c:forEach>		
		<div class="container" id="map-canvas"  style="width: 81%;"/>
		
</body>
</html>
