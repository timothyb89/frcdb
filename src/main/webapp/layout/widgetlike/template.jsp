<%-- 
    Document   : template
    Created on : Jul 25, 2010, 1:09:55 AM
    Author     : tim
--%>

<%@page import="net.frcdb.util.Config"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/js" prefix="js" %>
<%@ taglib uri="http://jtidy.sf.net" prefix="jtidy" %>
<%
session.setAttribute("page", request.getRequestURI());

pageContext.setAttribute("themePath", "/layout/widgetlike");

if (request.getParameter("error") != null) {
	session.setAttribute("error", request.getParameter("error"));
}
%>

<jtidy:tidy>
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	   "http://www.w3.org/TR/html4/loose.dtd">
	<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<link href="${themePath}/style.css" rel="stylesheet" type="text/css" media="screen" />

			<script type="text/javascript" src="/js/functions.js"></script>

			<script type="text/javascript" src="/js/jquery/jquery.min.js"></script>
			<script type="text/javascript" src="/js/jquery/jquery.ui.core.min.js"></script>
			<script type="text/javascript" src="/js/jquery/jquery.ui.widget.min.js"></script>
			<script type="text/javascript" src="/js/jquery/jquery.ui.tabs.min.js"></script>
			<link rel="stylesheet" 
				  type="text/css" 
				  href="/js/jquery/themes/ui-darkness/jquery-ui-1.8.7.custom.css">
			<script type='text/javascript' src='/js/jquery/jquery.dataTables.min.js'></script>
			<link rel="stylesheet" 
				  type="text/css"
				  href="/js/jquery/dataTables-jui.css">

			<script type='text/javascript' src='/js/jquery/jquery.autocomplete.pack.js'></script>
			<link rel="stylesheet" type="text/css"
				  href="/js/jquery/jquery.autocomplete.css">
			
			<link rel="stylesheet" type="text/css" href="/layout/common.css">

			<title>FRC-DB: <tiles:getAsString name="title"/></title>
		</head>
		<body>
			<div id="bg1">
				<div id="header">
					<h1><a href="/?new">FRC-DB</a></h1>
					<h2>A scouting database for FRC</h2>
				</div>
				<!-- end #header -->
			</div>
			<!-- end #bg1 -->
			<div id="bg2">
				<div id="header2">
					<div id="menu">
						<tiles:insertAttribute name="top-menu"/>
					</div>
					<!-- end #menu -->
					<div id="search">
						<form method="get" action="/search">
							<js:input name="q" value="search..." clazz="text"/><br>
							<div id="search_radios">
								<input type="radio"
									   name="type"
									   value="teams"
									   checked="checked"
									   id="teamsBox"><label for="teamsBox">Teams</label>
								<input type="radio"
									   name="type"
									   value="events" 
									   id="eventsBox"><label for="eventsBox">Events</label>
								<input type="submit"
									   value="Search"
									   class="button">
							</div>
						</form>
					</div>
					<!-- end #search -->
				</div>
				<!-- end #header2 -->
			</div>
			<!-- end #bg2 -->
			<div id="bg3">
				<div id="bg4">
					<div id="bg5">
						<div id="page">
							<div id="content">
								<% if (session.getAttribute("error") != null) { %>
									<div class="error">
										<table style="border: 0"><tr>
											<td>
												<img src="/layout/default/res/gfx/error.png"
													 alt="error">
											</td>
											<td style="color: black;">
												<b>Error:</b>
												<% out.println(session.getAttribute("error")); %>
											</td>
										</tr></table>
									</div>
									<% session.removeAttribute("error"); %>
								<% } %>
								<% if (session.getAttribute("warning") != null) { %>
									<div class="warning">
										<table border="0"><tr>
											<td>
												<img src="/layout/default/res/gfx/warning.png"
													 alt="warning">
											</td>
											<td style="color: black;">
												<b>Warning:</b>
												<% out.println(session.getAttribute("warning")); %>
											</td>
										</tr></table>
									</div>
									<% session.removeAttribute("warning"); %>
								<% } %>

								<tiles:insertAttribute name="body"/>
							</div>
							<!-- end #content -->
							<div id="sidebar">
								<tiles:insertAttribute name="menu"/>
							</div>
							<!-- end #sidebar -->
							<div style="clear: both; height: 40px;">&nbsp;</div>
						</div>
						<!-- end #page -->
					</div>
				</div>
			</div>
			<!-- end #bg3 -->
			<div id="footer">
				<p>
					(c) 2010 frcdb.net. Design by
					<a href="http://www.nodethirtythree.com/">nodeThirtyThree</a> +
					<a href="http://www.freewpthemes.net/">Free CSS Templates</a><br>
					frcdb <%= Config.VERSION %> running on 
					<%= application.getServerInfo() %>
				</p>
				<p>
					<a href="http://validator.w3.org/check?uri=referer"><img
						src="http://www.w3.org/Icons/valid-html401"
						alt="Valid HTML 4.01 Transitional" height="31" width="88"></a>
					<jtidy:validationImage/>
				</p>
			</div>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-22510876-1']);
  _gaq.push(['_setDomainName', '.frcdb.net']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
		</body>
	</html>
</jtidy:tidy>
