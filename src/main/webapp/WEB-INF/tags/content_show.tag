<%-- 
    Document   : content_show
    Created on : Feb 19, 2012, 3:57:12 PM
    Author     : tjbuckley12
--%>

<%@tag description="Lists and allows for uploading of content"
	   pageEncoding="UTF-8"
	   body-content="empty"%>

<%@taglib uri="http://frcdb.net/taglibs/content" prefix="content" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@attribute name="id" type="java.lang.String" required="true"%>
<%@attribute name="provider" type="java.lang.Object" required="true"%>
<%@attribute name="uploader" type="java.lang.Boolean" required="false" %>
<%@attribute name="pending" type="java.lang.Boolean" required="false" %>

<c:if test="${empty uploader}">
	<c:set var="uploader" value="true"/>
</c:if>

<c:if test="${empty pending}">
	<c:set var="pending" value="true"/>
</c:if>

<div class="content-images-list">
	<h3>Approved Content</h3>
	User-submitted content that has passed moderation goes here.
	<ul class="content-list">
		<c:forEach items="${provider.content}" var="c">
			<c:if test="${c.moderationStatus.approved}">
				<li class="content-item">
					<content:display cid="${c.id}"/>
				</li>
			</c:if>
		</c:forEach>
	</ul>
	<br class="clear">
	<c:if test="${pending}">
		<h3>Pending Content</h3>
		Pending content is user-submitted content that has not yet been approved
		by a moderator. Show at your own risk! (Note that it hasn't yet been
		denied by moderators, either.)
		<div class="content-hidden">
			<ul>
				<c:forEach items="${provider.content}" var="c">
					<c:if test="${c.moderationStatus.pending}">
						<li class="content-item">
							<content:display cid="${c.id}"/>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
		<br class="clear">
		<button class="content-unhide">Show Pending Content</button>
	</c:if>
	<script type="text/javascript">
		$(".fbthumbnail").fancybox();
		$(".content-unhide").button().click(function() {
			$(".content-hidden").show();
			$(".content-unhide").remove();
		});
	</script>

	<c:if test="${uploader}">
		<h3>Add your own!</h3>
		Upload your own content! Note that you must have an account and log in to
		make content submissions.
		<br class="clear"/>
		<content:uploader buttontext="Add Media"
						id="${id}"
						type="${provider.contentType}"
						cpid="${provider.recordId}"/>
	</c:if>
</div>
