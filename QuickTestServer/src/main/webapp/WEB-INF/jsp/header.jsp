<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<div id="div-header-container" class="control-group background_image_header">
	<img src="images/front_flip_header.png" alt="logoImg" class="img-responsive"/>

	<div class="header-background">
		<div style="clear: both; text-align: right;">
			<span style="color: #1F4E79; font-weight: bold;">Welcome:</span> <span
				style="font-weight: bold; color: black;">${sessionScope.email}</span>
		</div>
	
		<div style="clear: both">
			<span style="font-weight: bold;"><a href="home.htm">Home</a></span> <span
				style="font-weight: bold; float: right"><a href="signOut.htm">Sign
					out</a></span>
		</div>
	</div>
</div>