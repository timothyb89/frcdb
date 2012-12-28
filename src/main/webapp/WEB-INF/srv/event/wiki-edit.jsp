<%-- 
    Document   : wiki-edit
    Created on : Aug 16, 2010, 10:27:40 PM
    Author     : tim
--%>

<%--
    Document   : wiki
    Created on : Aug 16, 2010, 7:34:17 PM
    Author     : tim
--%>

<%@page import="net.frcdb.wiki.WikiPage"%>
<%@page import="net.frcdb.wiki.Wiki"%>
<%@page import="java.util.List" %>
<%@page import="net.frcdb.api.event.Event" %>
<%@page import="net.frcdb.db.Database" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://timothyb89.org/tlds/js" prefix="js" %>

<%
Event e = (Event) session.getAttribute("event");
pageContext.setAttribute("name", e.getName());
pageContext.setAttribute("shortName", e.getShortName());
session.removeAttribute("event");

Wiki wiki = (Wiki) session.getAttribute("eventWiki");
pageContext.setAttribute("wiki", wiki);
session.removeAttribute("eventWiki");

WikiPage wikiPage = (WikiPage) session.getAttribute("eventWikiPage");
pageContext.setAttribute("wikiPage", wikiPage);
session.removeAttribute("eventWikiPage");

String eventWikiPageName = (String) session.getAttribute("eventWikiPageName");
pageContext.setAttribute("wikiPageName", eventWikiPageName);
session.removeAttribute("eventWikiPageName");

String editing = null;
if (request.getParameter("content") == null) {
	if (wikiPage == null) {
		editing = "";
	} else {
		editing = wikiPage.getLatestRawText();
	}
} else {
	editing = request.getParameter("content");
}

%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event: ${name}, Team #${number}"/>
	<tiles:putAttribute name="body">
		<h1>Editing Wiki Page: ${wikiPageName}</h1>
		<p class="breadcrumbs">
			<a href="/event/${shortName}">Event Page</a>,
			<a href="/event/${shortName}/wiki/">Wiki Home</a>
		</p>

		<% String type = request.getParameter("submitType"); %>
		<% if (type != null && type.equals("Preview")) { %>
			<div class="preview">
<%
out.println("TODO: previews are broken!");
%>
			</div>
		<% } %>

		<ul class="tabs">
			<li class="tab">
				<a href="/event/${shortName}/wiki/${wikiPageName}">
					View
				</a>
			</li>
			<li class="tab">
				<a href="/event/${shortName}/wiki/${wikiPageName}/source">
					Source
				</a>
			</li>
			<li class="tab-selected">
				<a href="/event/${shortName}/wiki/${wikiPageName}/edit">
					Edit
				</a>
			</li>
			<li class="tab">
				<a href="/event/${shortName}/wiki/${wikiPageName}/history">
					History
				</a>
			</li>
		</ul>

		<div class="wiki">
			<div class="editor"
				 style="width: 100%; margin: auto; padding-bottom: 3em;">
				<form action="/event/${shortName}/wiki/${wikiPageName}/edit"
					  method="POST">
					<textarea style="width: 100%; height: 300px;" name="content"><%= editing %></textarea>
					<input style="width: 5em;"
						   name="submitType"
						   type="submit"
						   value="Preview">
					<input style="width: 5em;"
						   name="submitType"
						   type="submit"
						   value="Save">
				</form>
			</div>

			<h2>Editing Help</h2>
			<ul>
				<li>
					For syntax reference, see
					<a href="http://www.textism.com/tools/textile/">
					http://www.textism.com/tools/textile/</a>.
				</li>
				<li>
					To link to other pages on this wiki, use: ((pageName)), or
					((pageName|Link text Here)). Link text is wiki parsed.
				</li>
			</ul>

		</div>

		Back to
		<a href="/event/<%= e.getShortName() %>">
			Event: <%= e.getName() %>
		</a>
	</tiles:putAttribute>
</tiles:insertDefinition>
