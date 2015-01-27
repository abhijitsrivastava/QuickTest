<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%-- <%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
 --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Quick Test Lesson Details</title>
<meta content="width=device-width, initial-scale=1" name="viewport" />
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/generic.css" type="text/css" />
<link rel="stylesheet" href="css/admin_home.css" type="text/css" />

<script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
<script type="text/javascript" src='js/bootstrap.min.js'></script>

<script type="text/javascript">
		
	function deleteSection(sectionId){ 
		$("#myModal").show();
		$('#confirm-delete').on('click', function(e) {
			$("#myModal").hide();
			window.location.href="./deleteSection.htm?sectionId=" + sectionId; 
		});
	}
			
	function question(sectionId){ 
			window.location.href="./questions.htm?sectionId=" + sectionId; 
	}

	window.onload = function() {
		$("div.background_body").css("box-shadow", "0 0 15px rgba(1, 1, 1, 1)");
	}
</script>
<style>
.custom-search-form {
	margin-top: 5px;
}

.btn-primary-custom {
	color: #fff;
	background-color: #428bca;
	border-color: #357ebd
}
</style>
</head>

<body>
	<div class="container">
	
	<!-- <div class="control-group background_image_header" align="center" >
				<img src="images/logo.png" alt="logoImg" class="img-circle img-responsive" style="height:auto; width:auto; max-width:150px; max-height:150px;"/>
			</div> -->
		<div class="background_body">

			<%@ include file="header.jsp"%>
			<div class="lesson_body">
				<h2 class="legend">
					Sections 
				</h2>

				<div class="table-responsive displaytable">
					<form:form commandName="lessonDetailsForm"
						action="lessonDetailsForm.htm" method="POST"
						id="lessonDetailsForm">
						<c:if test="${ !empty(sessionScope.sectionList)}">
							<table class="table table-hover table-striped">
								<thead class="header_table">
									<tr>
										<th>Section Name</th>
										<th>Section Description</th>
										<th></th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="section" items="${sessionScope.sectionList}">
										<tr>
											<td>${section.sectionName}</td>
											<td>${section.sectionDescription}</td>
											<td><c:if test="${section.sectionType eq 'QUIZ'}">
													<a class="btn btn-primary"
														onclick="question(${section.id})" href="#">Questions</a>
												</c:if></td>

											<td><a class="btn btn-primary" data-toggle="modal"
												data-target="#myModal"
												onclick="deleteSection(${section.id})" href="#">Delete</a></td>

										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>

						<c:if test="${empty(sessionScope.sectionList)}">
							<center>No sections found for this lesson</center>
						</c:if>
					</form:form>
					
					<a class="btn btn-primary" href="showAddSectionForm.htm">Create a Section</a>
					
					<!-- <a title="Add Section" href="showAddSectionForm.htm">
						<img class="img-thumbnail img-responsive" align="left"
						src="images/add_section.png"
						style="height: auto; width: auto; max-width: 80px; max-height: 60px; margin-top: 5px; margin-left: 5px;" />
					</a> -->
				</div>
			</div>
			<%@ include file="footer.jsp"%>
		</div>
	</div>


	<!-- <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Delete</h4>
				</div>
				<div class="modal-body">Do you really want to delete this
					section?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">No</button>
					<button type="button" class="btn btn-primary-custom"
						id="confirm-delete">Yes</button>
				</div>
			</div>
		</div>
	</div> -->
	
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		        <h4 class="modal-title" id="myModalLabel">Delete</h4>
		      </div>
		      <div class="modal-body">
		        Do you really want to delete this lesson?
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
		        <button type="button" class="btn btn-primary-custom" id="confirm-delete">Yes</button>
		      </div>
		    </div>
		  </div>
		</div>


	<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
		aria-labelledby="mySmallModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" aria-hidden="true" data-dismiss="modal"
						type="button">&times;</button>
					<h4 id="mySmallModalLabel" class="modal-title"></h4>
				</div>
				<div class="modal-body" id="info-modal-body">...</div>
			</div>
		</div>
	</div>
</body>
</html>
