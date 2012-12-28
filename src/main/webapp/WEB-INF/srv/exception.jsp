<%-- 
    Document   : exception
    Created on : Apr 10, 2011, 4:43:57 PM
    Author     : tim
--%>

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>

<%
System.err.println("ERROR at " + session.getAttribute("original_url"));
exception.printStackTrace();
%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event: Error"/>
	<tiles:putAttribute name="body">
		<h1>Error</h1>
		An error occurred while viewing the requested page, and has been logged.
		If this error persists, please visit
		<a href="http://dev.frcdb.net/projects/main/issues/new">the bug tracker</a>
		and report what you were doing what the error occurred, and include the
		following information:
		<div class="category">
			Page: <%= session.getAttribute("original_url") %><br>
			Error:<br>
<pre>
<%
StringWriter writer = new StringWriter();
exception.printStackTrace(new PrintWriter(writer));
out.println(StringEscapeUtils.escapeHtml(writer.toString()));
%>
</pre>
		</div>
		<br>
		<a href="javascript:history.go(-1)">Go back?</a>
	</tiles:putAttribute>
</tiles:insertDefinition>
