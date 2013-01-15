<%-- 
    Document   : template
    Created on : Jun 28, 2011, 2:06:35 PM
    Author     : tim
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="net.frcdb.config.Config"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="net.frcdb.util.UserUtil"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>
<%
	request.setAttribute("page", request.getRequestURI());
	pageContext.setAttribute("themePath", "/layout/shades-of-gray");

// hack for testing error display
	if (request.getParameter("error") != null) {
		request.setAttribute("error", request.getParameter("error"));
	}

	if (request.getParameter("warning") != null) {
		request.setAttribute("warning", request.getParameter("warning"));
	}

	if (request.getParameter("info") != null) {
		request.setAttribute("info", request.getParameter("info"));
	}

// initialize the user
	UserUtil.init(request);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="${themePath}/default.css" rel="stylesheet" type="text/css" media="screen">

		<script type="text/javascript" src="/js/functions.js"></script>
		<script type="text/javascript" src="/js/editor-templates.js"></script>

		<script type="text/javascript" src="/js/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="/js/jquery/jquery.ui.all.min.js"></script>
		<link rel="stylesheet" 
			  type="text/css" 
			  href="/js/jquery/themes/ui-darkness/jquery-ui-1.8.7.custom.css">
		<script type='text/javascript' src='/js/jquery/jquery.dataTables.min.js'></script>
		<link rel="stylesheet" 
			  type="text/css"
			  href="/js/jquery/dataTables-jui.css">

		<link rel="stylesheet" type="text/css" href="/layout/common.css">

		<script type="text/javascript" src="/js/jquery.fancybox.pack.js"></script>
		<link rel="stylesheet" type="text/css" href="/js/jquery.fancybox.css">

		<script type="text/javascript" src="/js/jquery/jquery.fileupload.js"></script>
		<script type="text/javascript" src="/js/jquery/jquery.uploader.js"></script>
		<script type="text/javascript" src="/js/jquery/jquery.dialogform.js"></script>

		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		
		<tiles:insertAttribute name="head-extra" defaultValue=""/>
		
		<title>FRC-DB: <tiles:getAsString name="title"/></title>
	</head>
	<body style="margin: 0 12%">

		<div class="container">

			<div class="header">
				<a href="/">
					<img alt="FIRST logo" src="/res/first-logo.png"
						 class="left"
						 style="padding: 5px 15px;">
					<span class="left">FRC-DB</span>
				</a>
			</div>

			<div class="stripes"><span>&nbsp;</span></div>

			<div class="nav">
				<tiles:insertAttribute name="top-menu"/>

				<div class="clearer"><span>&nbsp;</span></div>
			</div>

			<div class="stripes"><span>&nbsp;</span></div>

			<div class="main">

				<div class="left">
					<div class="content">
						<c:if test="${not empty error}">
							<div class="error">
								<table style="border: 0"><tr>
										<td>
											<img src="/layout/default/res/gfx/error.png"
												 alt="error">
										</td>
										<td style="color: black;">
											<b>Error:</b>
											${error}
										</td>
									</tr></table>
							</div>
						</c:if>
						<c:if test="${not empty warning}">
							<div class="warning">
								<table border="0">
									<tr>
										<td>
											<img src="/layout/default/res/gfx/warning.png"
												 alt="warning">
										</td>
										<td style="color: black;">
											<b>Warning:</b>
											${warning}
										</td>
									</tr>
								</table>
							</div>
						</c:if>
						<c:if test="${not empty info}">
							<div class="info">
								<table border="0">
									<tr>
										<td>
											<img src="/layout/default/res/gfx/info.png"
												 alt="info">
										</td>
										<td style="color: black;">
											<b>Info:</b>
											${info}
										</td>
									</tr>
								</table>
							</div>
						</c:if>

						<tiles:insertAttribute name="body"/>
					</div>
				</div>

				<div class="right">
					<div class="subnav">
						<tiles:insertAttribute name="menu"/>
					</div>
				</div>

				<div class="clearer"><span>&nbsp;</span></div>

			</div>

			<div class="footer">

				<div class="col3">
					<tiles:insertAttribute name="menu-bottom-left"/>
				</div>

				<div class="col3center">
					<tiles:insertAttribute name="menu-bottom-center"/>
				</div>

				<div class="col3">
					<tiles:insertAttribute name="menu-bottom-right"/>
				</div>

				<div class="sgbottom">

					<span class="left">
						&copy; 2011 <a href="/">frcdb.net</a>. Valid 
						<a href="http://jigsaw.w3.org/css-validator/check/referer">CSS</a> 
						&amp; <a href="http://validator.w3.org/check?uri=referer">HTML</a>. 
						frcdb <%= Config.VERSION%> running on <%= application.getServerInfo()%>
						<br><%--<jtidy:validationImage/>--%>
					</span>

					<span class="right"><a href="http://templates.arcsin.se/">Website template</a> by <a href="http://arcsin.se/">Arcsin</a></span>

					<div class="clearer"><span>&nbsp;</span></div>

				</div>

			</div>

		</div>

		<!--
					<div id="bg2">
						<div id="header2">
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
</div>
</div>
		-->
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
