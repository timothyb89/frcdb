<%-- 
    Document   : content_uploader
    Created on : Jan 3, 2012, 8:39:57 PM
    Author     : tim
	TODO: This currently only supports image uploads!
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- for some reason this breaks the compiler - tag files aren't recognized properly --%>
<%--<%@tag description="Displays a content uploader button" pageEncoding="UTF-8"%>--%>

<%--
<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="cpid" required="true"%>
<%@attribute name="type" required="true" type="java.lang.String"%>
<%@attribute name="buttontext" required="false" type="java.lang.String"%>--%>

<%--
<button id="${id}-upload-button">
	<c:choose>
		<c:when test="${empty buttontext}">
			Upload
		</c:when>
		<c:otherwise>
			${buttontext}
		</c:otherwise>
	</c:choose>
</button>--%>

<script type="text/javascript">
	$("#${id}-upload-button").uploader({
		basePath: "", <%-- empty for server-side, this isn't rewritten --%>
		cpid: "${cpid}",
		type: "${type}"
	});
</script>
