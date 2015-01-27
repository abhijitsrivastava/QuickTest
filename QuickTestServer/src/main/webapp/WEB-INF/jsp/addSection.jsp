<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Quick Test Section</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/generic.css" type="text/css" />
<script type="text/javascript">
	window.onload = function() {

		$("div.background_body").css("box-shadow", "0 0 15px rgba(1, 1, 1, 1)");
		
		var value = $('#select-type').val();
		if (value == -1) {
			$('#fileUpload').hide();
			$('#teacherId').hide();
			return;
		}
		if (value == 0 || value == 1) {
			$('#fileUpload').show();
			$('#teacherId').hide();
		} else if (value == 2 || value == 4 || value == 5) {
			$('#fileUpload').hide();
			$('#teacherId').hide();
		} else if (value == 3) {
			$('#fileUpload').hide();
			$('#teacherId').hide();
		}

	}

	function onSectionTypeChange(element) {
		var value = $('#select-type').val();
		if (value == -1) {
			$('#fileUpload').hide();
			$('#teacherId').hide();
			return;
		}
		//alert(value);
		if (value == 0 || value == 1) {
			$('#fileUpload').show();
			$('#teacherId').hide();
		} else if (value == 2 || value == 4 || value == 5) {
			$('#fileUpload').hide();
			$('#teacherId').hide();
		} else if (value == 3) {
			$('#fileUpload').hide();
			$('#teacherId').hide();
		}
	}
</script>

</head>

<body>
	<div class="container">
		<!-- <div class="control-group background_image_header" align="center">
			<img src="images/logo.png" alt="logoImg"
				class="img-circle img-responsive"
				style="height: auto; width: auto; max-width: 150px; max-height: 150px;" />
		</div> -->

		<div class="background_body">

			<%@ include file="header.jsp"%>
			<div class="lesson_body">
				<div style="clear: both; text-align: right; margin-bottom: 30px;">
					<h2 class="legend" style="margin-bottom: 0px;">Create a Section</h2>
					<span style="color: black;">*indicates mandatory fields</span>
				</div>
				<div class="form-addEditLogo">
					<form:form method="POST" commandName="addSectionForm"
						action="addSection.htm" enctype="multipart/form-data"
						cssClass="form-horizontal">

						<div class="form-group">
							<label for="sectionName" class="control-label col-xs-5">Section
								Name*</label>
							<div class="col-xs-5">
								<form:input cssClass="form-control" path="sectionName"
									id="sectionName" placeholder="Section Name" />
								<form:errors path="sectionName" cssClass="error" />
							</div>
						</div>

						<div class="form-group">
							<label for="sectionDescription" class="control-label col-xs-5">Section
								Description</label>
							<div class="col-xs-5">
								<form:textarea cssClass="form-control" path="sectionDescription"
									id="sectionDescription" placeholder="Section Description" />
								<form:errors path="sectionDescription" cssClass="error" />
							</div>
						</div>

						<div class="form-group">
							<label for="sectionType" class="control-label col-xs-5">Section
								Type*</label>
							<div class="col-xs-3">
								<form:select path="sectionType" cssClass="form-control"
									onchange="onSectionTypeChange()" id="select-type">
									<form:option value="-1" label="   Select Section Type     " />
									<form:option value="0" label="    POWERPOINT              " />
									
									<form:option value="3" label="    QUIZ              " />
									
								</form:select>
								<form:errors path="sectionType" cssClass="error" />
							</div>
						</div>


						<div class="form-group" id="fileUpload" style="display: none">
							<label for="sectionDetails" class="control-label col-xs-5">Upload
								File (.pptx, ppt or .mp4)</label>
							<div class="col-xs-3">
								<input type="file" size="200" name="sectionDetails" accept="*/*" />
								<form:errors path="sectionDetails" cssClass="error" />
							</div>
						</div>


						<div class="form-group" id="teacherId" style="display: none">
							<label for="teacherId" class="control-label col-xs-5">Teacher
								Id</label>
							<div class="col-xs-5">
								<form:input cssClass="form-control" path="teacherId"
									id="teacherId" placeholder="EduGrade Teacher Id" />
								<form:errors path="teacherId" cssClass="error" />
							</div>
						</div>

						<div class="form-group">
							<div class="col-xs-offset-5 col-xs-8">
								<button type="submit" class="btn btn-primary" value="Submit">Submit</button>
								&nbsp;<a class="btn btn-primary"
									href="lessonDetails.htm?lessonId=">Cancel</a>
							</div>
						</div>

					</form:form>
				</div>
			</div>
			<%@ include file="footer.jsp"%>
		</div>
	</div>
	<script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
	<script type="text/javascript" src='js/bootstrap.min.js'></script>
</body>
</html>