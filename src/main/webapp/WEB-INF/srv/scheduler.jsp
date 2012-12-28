<%-- 
    Document   : scheduler
    Created on : Mar 12, 2012, 5:47:24 PM
    Author     : tim
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js" prefix="js" %>
<%@taglib uri="/WEB-INF/tlds/content" prefix="content" %>
<%@taglib uri="http://frcdb.net/taglibs/permission" prefix="p" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Scheduled Tasks"/>
	<tiles:putAttribute name="body">
		<h1>Scheduled Tasks</h1>
		
		<h2>Current Task</h2>
		<div id="current">
			<c:choose>
				<c:when test="${not empty data.currentTask}">
					
				</c:when>
				<c:otherwise>
					There is no task currently being executed.
				</c:otherwise>
			</c:choose>
		</div>
		
		<h2>Pending Tasks</h2>
		<c:forEach items="${data.pendingTasks}" var="task">
			<div class="moderator-item">
				<ul>
					<li><b>ID:</b> ${task.id}</li>
					<li><b>Date:</b> ${task.date}</li>
					<li><b>Description:</b> ${task.description}</li>
				</ul>
				<div class="moderator-item-controls">
					<perm:switch>
						<perm:case name="root.task.remove">
							TODO: delete button
						</perm:case>
						<perm:default>
							Login to remove
						</perm:default>
					</perm:switch>
				</div>
			</div>
		</c:forEach>
		
		<h2>Future Tasks</h2>
		<c:forEach items="${data.futureTasks}" var="task">
			<div class="moderator-item">
				<ul>
					<li><b>ID:</b> ${task.id}</li>
					<li><b>Date:</b> ${task.date}</li>
					<li><b>Description:</b> ${task.description}</li>
				</ul>
				<div class="moderator-item-controls">
					<perm:switch>
						<perm:case name="root.task.remove">
							TODO: delete button
						</perm:case>
						<perm:default>
							Login to remove
						</perm:default>
					</perm:switch>
				</div>
			</div>
		</c:forEach>
		
		<h2>Completed Tasks</h2>
		<c:forEach items="${data.completedTasks}" var="task">
			<div class="moderator-item">
				<ul>
					<li><b>ID:</b> ${task.id}</li>
					<li><b>Date:</b> ${task.date}</li>
					<li><b>Description:</b> ${task.description}</li>
					<li><b>Result</b> ${task.result}</li>
				</ul>
				<div class="moderator-item-controls">
					<perm:switch>
						<perm:case name="root.task.remove">
							TODO: delete button
						</perm:case>
						<perm:default>
							Login to remove
						</perm:default>
					</perm:switch>
				</div>
			</div>
		</c:forEach>
		
		<script type="text/javascript">
			var updateCurrent = function() {
				jQuery.ajax("/scheduler/current/json", {
					success: function(data) {
						$("#current").empty();
						$("#current").html(JSON.stringify(data));
					},
					error: function(xhr, status, error) {
						console.log("Failed to retreive task data: " + status);
					}
				});
			}
			
			$(document).ready(function() {
				window.setInterval(updateCurrent, 5000);
			});
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>