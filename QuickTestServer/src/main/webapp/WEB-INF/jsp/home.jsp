<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%-- <%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
 --%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Quick Test</title>
		<meta content="width=device-width, initial-scale=1" name="viewport"/>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
		<link rel="stylesheet" href="css/generic.css" type="text/css"/>
		<link rel="stylesheet" href="css/admin_home.css" type="text/css"/>
		
		<script type="text/javascript" src='js/jquery-1.11.0.min.js'></script>
		<script type="text/javascript" src='js/bootstrap.min.js'></script>
		
		<script type="text/javascript">
		
		function slideShow(lessonId) {
			window.location.href="./viewSlideShow.htm?isTeacher="+true+"&lessonId="+lessonId; 
		}
		
			function deleteLesson(lessonId){ 
				$("#myModal").show();
				$('#confirm-delete').on('click', function(e) {
					$("#myModal").hide();
					window.location.href="./deleteLesson.htm?lessonId=" + lessonId; 
				});
			}
			
			function lessonDetails(lessonId){ 
					window.location.href="./lessonDetails.htm?lessonId=" + lessonId; 
			}
			
			function scanQRCode(lessonId) {
				window.location.href="./generateQRCode.htm?lessonId=" + lessonId; 
			}
			
			window.onload = function() {
				$("div.background_body").css("box-shadow", "0 0 15px rgba(1, 1, 1, 1)");
			}
		</script>
		<style>
			.custom-search-form{
    			margin-top:5px;
			}
			
			.btn-primary-custom{color:#fff;background-color:#428bca;border-color:#357ebd}
		</style>
	</head>
	
	<body>
		<div class="container">

		<div class="background_body">
			<%@ include file="header.jsp"%>

			<div class="lesson_body">
				<h2 class="legend">Lessons</h2>
				<div class="table-responsive displaytable">
					<form:form commandName="securityLiveStreamForm"
						action="securityLiveStream.htm" method="POST"
						id="securityLiveStreamForm">
						<c:if test="${ !empty(sessionScope.lessonList)}">
							<table class="table table-hover table-striped">
								<thead class="header_table">
									<tr>
										<th>Name</th>
										<th>ID</th>
										<th></th>
										<th></th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="lesson" items="${sessionScope.lessonList}">
										<tr>
											<td>${lesson.lessonName}</td>

											<td>${lesson.id}</td>

											<td><a class="btn btn-primary"
												onclick="slideShow(${lesson.id})" href="#">Start a session</a>
											</td>

											<td><a class="btn btn-primary"
												onclick="lessonDetails(${lesson.id})" href="#">List</a></td>

											<td><a class="btn btn-primary" data-toggle="modal"
												data-target="#myModal" onclick="deleteLesson(${lesson.id})"
												href="#">Delete</a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>

						<c:if test="${empty(sessionScope.lessonList)}">
							<center>No Saved Lesson Found</center>
						</c:if>
					</form:form>
					<a class="btn btn-primary" href="showAddLessonForm.htm">Create
						a Lesson</a>
					<!-- <a title="Add Lesson" href="showAddLessonForm.htm"> 
						<img class="img-thumbnail img-responsive" align="left" src="images/add_lesson.png" style="height:auto; width:auto; max-width:80px; max-height:60px;margin-top: 5px;margin-left: 5px;"/>
					</a> -->

				</div>
			</div>
			<%@ include file="footer.jsp"%>
		</div>

	</div>		

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

		
   		<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-sm">
   				 <div class="modal-content">
      				<div class="modal-header">
						<button class="close" aria-hidden="true" data-dismiss="modal" type="button">&times;</button>
						<h4 id="mySmallModalLabel" class="modal-title"></h4>
					</div>
					<div class="modal-body" id="info-modal-body"> ... </div>
    			</div>
  			</div>
		</div>						
	</body>
</html>
