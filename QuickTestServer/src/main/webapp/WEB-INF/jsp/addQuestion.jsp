<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Quick Test Question</title>
		<meta content="width=device-width, initial-scale=1" name="viewport"/>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
		<link rel="stylesheet" href="css/generic.css" type="text/css"/>
		
		<script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
		<script type="text/javascript" src='js/bootstrap.min.js'></script>
		<script type="text/javascript">
			window.onload = function() {
				$("div.background_body").css("box-shadow", "0 0 15px rgba(1, 1, 1, 1)");
			}
		</script>
	</head>
	
	<body>
		<div class="container">
		
			<!-- <div class="control-group background_image_header" align="center" >
				<img src="images/logo.png" alt="logoImg" class="img-circle img-responsive" style="height:auto; width:auto; max-width:150px; max-height:150px;"/>
			</div> -->
			<div class="background_body">
				
				<%@ include file="header.jsp" %>
				<div class="lesson_body">
				<div style=" clear: both; text-align: right;margin-bottom:30px;">
					<h2 class="legend" style="margin-bottom:0px;">Create a Question</h2>
					<span style="color: black;">*indicates mandatory fields</span>
				</div>
				<div class="form-addEditLogo">
				<form:form method="POST" commandName="addQuestionForm" action="addQuestion.htm" cssClass="form-horizontal"> 
        			<div class="form-group">
            			<label class="control-label col-xs-5">Question Description*</label>
            			<div class="col-xs-6">
                			<form:textarea cssClass="form-control" path="description" id="description" placeholder="Question Description"/>
                			<form:errors path="description" cssClass="error" />
            			</div>
        			</div>
        			
        			<div class="form-group">
            			<label class="control-label col-xs-5">Option A</label>
            			<div class="col-xs-6">
                			<form:input cssClass="form-control" path="optionA" id="optionA" placeholder="Option A"/>
                			<form:errors path="optionA" cssClass="error" />
            			</div>
        			</div>
        			
        			<div class="form-group">
            			<label class="control-label col-xs-5">Option B</label>
            			<div class="col-xs-6">
                			<form:input cssClass="form-control" path="optionB" id="optionB" placeholder="Option B"/>
                			<form:errors path="optionB" cssClass="error" />
            			</div>
        			</div>
        			
        			<div class="form-group">
            			<label class="control-label col-xs-5">Option C</label>
            			<div class="col-xs-6">
                			<form:input cssClass="form-control" path="optionC" id="optionC" placeholder="Option C"/>
                			<form:errors path="optionC" cssClass="error" />
            			</div>
        			</div>
        			<div class="form-group">
            			<label class="control-label col-xs-5">Option D</label>
            			<div class="col-xs-6">
                			<form:input cssClass="form-control" path="optionD" id="optionD" placeholder="Option D"/>
                			<form:errors path="optionD" cssClass="error" />
            			</div>
        			</div>
        			<div class="form-group">
            			<label class="control-label col-xs-5">Option E</label>
            			<div class="col-xs-6">
                			<form:input cssClass="form-control" path="optionE" id="optionE" placeholder="Option E"/>
                			<form:errors path="optionE" cssClass="error" />
            			</div>
        			</div>
        			<div class="form-group">
            			<div class="col-xs-offset-5 col-xs-8">
                			<button type="submit" class="btn btn-primary" value="Submit">Submit</button>&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;<a class="btn btn-primary" href="home.htm">Cancel</a>
            			</div>
        			</div>
        			
				</form:form>
				</div>
				</div>
				<%@ include file="footer.jsp" %>
			</div>
		</div>
	</body>
</html>