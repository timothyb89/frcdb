<%-- 
    Document   : robot
    Created on : Apr 7, 2011, 3:06:21 PM
    Author     : Tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>
<%@taglib uri="/WEB-INF/tlds/js" prefix="js" %>
<%@taglib uri="http://frcdb.net/taglibs/content" prefix="content" %>

<c:set var="self" value="/team/${data.robot.team}/robot/${data.robot.year}"/>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Team ${data.team.number}'s Robot"/>
	<tiles:putAttribute name="body">
		<h1>Team ${data.robot.team}'s Robot: ${data.robot.year}</h1>
		<p class="breadcrumbs">
			<a href="/team/${number}">Team Page</a>
		</p>
		
		Not implemented yet, sorry!
		<%--
		Viewing info for ${data.robot.year}. Years available:<br>
		<ul>
			<c:forEach items="${data.years}" var="year">
				<li>
					<a href="/team/${data.robot.team}/robot/${year}">
						${year}
					</a>
				</li>
			</c:forEach>
		</ul>
		
		<h2>Robot Overview</h2>
		<div id="robot-overview">
			<content:show provider="${data.robot}" uploader="true" pending="true" id="overview"/>
		</div>
		
		<h2>Robot Details</h2>
		<div id="robot-details">
			<c:forEach items="${data.robot.components}" var="component">
				<div class="robot-component"
					 data-componentId="${component.id}"
					 data-cpid="${component.recordId}"
					 data-contentType="${component.contentType}">
					<h3>${component.name}</h3>
					<c:set var="properties" value="${component.properties}"/>
					
					<c:if test="${!utils:isEmpty(properties)}">
						<c:forEach items="${properties}" var="prop">
							<li>
								<b>${prop.name}:</b> ${prop.value}
							</li>
						</c:forEach>
					</c:if>
					
					<div class="robot-component-dialog">
						<div class="robot-component-dialog-display">
							<c:if test="${not utils:isEmpty(component.content)}">
								<img src="/content/${component.content[0].id}/large">
							</c:if>
						</div>
						<div class="robot-component-dialog-properties">
							<c:if test="${!utils:isEmpty(properties)}">
								<c:forEach items="${properties}" var="prop">
									<li>
										<b>${prop.name}:</b> ${prop.value}
									</li>
								</c:forEach>
							</c:if>
						</div>
						<div class="robot-component-dialog-thumbs">
							<ul class="content-list">
								<c:forEach items="${component.content}" var="item">
									<li class="content-item" data-componentId="${item.id}">
										<img src="/content/${item.id}/thumb">
									</li>
								</c:forEach>
							</ul>
						</div>
						<br style="clear: both">
						<div class="robot-component-dialog-bar">
							<button class="robot-component-upload">Upload New</button>
						</div>
					</div>

					<button>More Info</button>
				</div>
			</c:forEach>
		</div>
		
		<script type="text/javascript">
			var refresh = function() {
				jQuery.ajax("${self}/json", {
					success: function(data) {
						$("#robot-details").empty();
					},
					error: function(xhr, status, error) {
						alert("Failed to retreive robot data: " + status);
					}
				});
			}
			
			$(document).ready(function() {
				$(".robot-component").each(function() {
					var componentId = $(this).attr("data-componentId");
					var cpid = $(this).attr("data-cpid");
					var contentType = $(this).attr("data-contentType");
					
					var button = $(this).children("button");
					var dialog = $(this).children(".robot-component-dialog");
					
					button.click(function() {
						$.fancybox(dialog, {
							type: 'inline',
							closeBtn: true,
							minWidth: 400,
							minHeight: 400
						});
					});
					button.button();
					
					var display = dialog.children(".robot-component-dialog-display");
					
					var thumbs = dialog.children(".robot-component-dialog-thumbs");
					
					thumbs.find("ul li").click(function() {
						var id = $(this).attr("data-componentId");
						
						alert("Load: /content/" + id);
						display.load("/content/" + id);
					});
					
					$(this).find(".robot-component-upload").uploader({
						basePath: "",
						cpid: cpid,
						type: contentType
					});
				});
			});
		</script>--%>
	</tiles:putAttribute>
</tiles:insertDefinition>
		