<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
<title>Quick Test Login</title>
<meta content="width=device-width, initial-scale=1" name="viewport" />

<script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
<script type="text/javascript" src='js/bootstrap.min.js'></script>

<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/signin.css" type="text/css" />
</head>
<body>
	<div class="container">
		<div class="background_body">
			<img src="images/front_flip_header.png" alt="logoImg"
				class="img-responsive" style="margin-bottom: 10px;" />

			<div class="row main-box">
				<div class="col-md-6">
					<!-- <video width="98%" controls style="margin-left:5px;"> <source
						src="http://www.w3schools.com/html/mov_bbb.mp4" type="video/mp4" />
					Your browser does not support HTML5 video. </video> -->
					
					<iframe class="iframe-box" src="http://www.youtube.com/embed/4EvNxWhskf8"></iframe>
					
				</div>

				<div class="col-md-6">
					<form:form action="login.htm" commandName="loginForm" method="post"
						cssClass="form-signin" name="loginForm">

						<div class="control-group">
							<label class="control-label" for="inputEmail"
								style="font-weight: normal;">Email</label>
							<div class="controls">
								<form:input path="email" type="text" id="inputEmail"
									placeholder="abc@gmail.com" cssClass="form-control"
									autofocus="autofocus" />
							</div>
							<form:errors path="email" cssClass="error" id="emailerror" />
						</div>

						<div class="control-group">
							<label class="control-label" for="inputPassword"
								style="margin-top: 5px; font-weight: normal;">Password</label>
							<div class="controls">
								<form:input path="password" type="password" id="inputPassword"
									placeholder="Password" cssClass="form-control" />
							</div>
							<form:errors path="password" cssClass="error" />
						</div>


						<c:if test="${not empty loginFailed}">
							<div class="control-group error" align="center">Invalid
								Email and Password Combination</div>
						</c:if>

						</br>
						<div class="control-group">
							<div class="controls">
								<button type="submit" class="btn btn-lg btn-primary btn-block"
									value="Submit">Login</button>
							</div>
						</div>

						<div class="control-group">
							<div class="controls">
								<a href="registration.htm">New User? Sign Up</a>
							</div>
						</div>

						<div class="control-group">
							<div class="controls">
								<a href="forgetPassword.htm">Forgot Password?</a>
							</div>
						</div>

					</form:form>
				</div>
			</div>
			<%@ include file="footer.jsp"%>
		</div>
	</div>
</body>
</html>
