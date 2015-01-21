<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">  
  <head>
  	<title>EduShield</title>
    <link rel="stylesheet" href="css/streaming.css" type="text/css" media="screen, handheld"/>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
    
    <script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
    <script type="text/javascript" src='js/bootstrap.min.js'></script>
    <script type="text/javascript" src="js/streaming_ui.js"></script>
    <script type="text/javascript" src="js/jwplayer.js" ></script>
    <script>jwplayer.key=""</script>
  </head>

  <body>
		<div class="container">
			<div class="background_body">
				<%@ include file="header.jsp" %>
				
   				<c:forEach var="item" items="${glassList}" varStatus="counter">
   				 	<div id="player${counter.count}" class="player-class"></div>
					<input type="hidden" id="stream${counter.count}"
						value="${item.streamUrl}" device-name="${item.glassName}"
						status="${item.status}" random-id="${item.randomId}"
						backup-requested="${item.backupRequested}" class="stream-class"></input>
				</c:forEach>
				<%-- <%@ include file="footer.jsp" %> --%>
			</div>
		</div>	
  	</body>

</html>