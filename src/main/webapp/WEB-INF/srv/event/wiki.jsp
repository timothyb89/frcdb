<%-- 
    Document   : wiki
    Created on : Aug 16, 2010, 7:34:17 PM
    Author     : tim
--%>

<%@page import="net.frcdb.wiki.Revision"%>
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

String rev = request.getParameter("rev");
Revision revision = null;
if (rev != null) {
	try {
		revision = wikiPage.getRevisions().get(Integer.parseInt(rev));
	} catch (Exception ex) {

	}
} else {
	revision = wikiPage.getLatestRevision();
}
pageContext.setAttribute("revision", revision);

String wikiPageName = (String) session.getAttribute("eventWikiPageName");
if (wikiPage != null) {
	wikiPageName = wikiPage.getName();
}
pageContext.setAttribute("wikiPageName", wikiPageName);
session.removeAttribute("eventWikiPageName");
%>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Event Wiki: ${name}"/>
	<tiles:putAttribute name="body">
		<h1>${name} Wiki</h1>
		<p class="breadcrumbs">
			<a href="/event/${shortName}">Event Page</a>,
			<a href="/event/${shortName}/wiki/">Wiki Home</a>
		</p>

		<ul class="tabs">
			<li class="tab-selected">
				<a href="/event/${shortName}/wiki/${wikiPageName}">
					View
				</a>
			</li>
			<li class="tab">
				<a href="/event/${shortName}/wiki/${wikiPageName}/source">
					Source
				</a>
			</li>
			<li class="tab">
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
<% if (wikiPage == null) { %>
	This page does not exist. Create it using the 'edit' button above.
<% } else if (revision == null) { %>
	The specified revision is invalid.
<% } else { %>
	<%= revision.getParsed() %>
<% } %>
		</div>

		Back to
		<a href="/event/<%= e.getShortName() %>">
			Event: <%= e.getName() %>
		</a>
	</tiles:putAttribute>
</tiles:insertDefinition>