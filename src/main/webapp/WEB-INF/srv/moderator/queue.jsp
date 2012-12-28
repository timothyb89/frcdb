<%-- 
    Document   : moderator
    Created on : Dec 30, 2011, 10:17:26 PM
    Author     : tim
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="/WEB-INF/tlds/content" prefix="content" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js" prefix="js" %>
<%@taglib uri="http://frcdb.net/taglibs/permission" prefix="perm" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Moderation Queue"/>
	<tiles:putAttribute name="body">
		This is the FRC-DB moderation queue, and shows all content pending
		moderation. 
		
		<c:forEach items="${queue}" var="item">
			<div class="moderator-item">
				<div class="moderator-item-image">
					<content:display cid="${item.id}" size="small"/>
				</div>
				<div class="moderator-item-right">
					<div class="moderator-item-info">
						<ul>
							<li><strong>Uploaded by:</strong> ${item.author}</li>
							<li><strong>Uploaded on:</strong> ${item.date}</li>
							<c:if test="${not empty item.description}">
								<li><strong>Description:</strong> ${item.description}</li>
							</c:if>
							<li><strong>Score:</strong> ${item.score}</li>
						</ul>
					</div>
					<div class="moderator-item-controls">
						<perm:switch>
							<perm:case name="root.content.moderate">
								<a class="mbutton"
								   href="/moderator/${item.id}/approve">Approve</a>
								<a class="mbutton"
								   href="/moderator/${item.id}/deny">Deny</a>
							</perm:case>
							<perm:case name="anonymous">
								Please log in to moderate content.
							</perm:case>
							<perm:default>
								You aren't allowed to moderate content.
							</perm:default>
						</perm:switch>
					</div>
				</div>
				<br class="clear"/>
			</div>
		</c:forEach>
		
		<script type="text/javascript">
			$(".moderator-item-controls a.mbutton").button();
			$(".fbthumbnail").fancybox();
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>