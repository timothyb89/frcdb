<%-- 
    Document   : wiki-history
    Created on : Sep 26, 2010, 2:09:52 AM
    Author     : tim
--%>

<%@page import="net.frcdb.wiki.Revision"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
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
			<li class="tab">
				<a href="/event/${shortName}/wiki/${wikiPageName}/edit">
					Edit
				</a>
			</li>
			<li class="tab-selected">
				<a href="/event/${shortName}/wiki/${wikiPageName}/history">
					History
				</a>
			</li>
		</ul>

		<div class="wiki">
			<h2>History: ${wikiPageName}</h2>

			<table width="100%">
				<thead>
					<tr>
						<th>Number</th>
						<th>Modified</th>
						<th>User</th>
					</tr>
				</thead>
				<tbody>
					<% List<Revision> revisions = wikiPage.getRevisions(); %>
					<% for (int i = revisions.size() - 1; i >= 0; i--) { %>
						<%
						Revision r = revisions.get(i);

						String day = null;
						String time = null;
						String date = null;

						if (r.getDate() == null) {
							date = "unknown";
						} else {
							day = new SimpleDateFormat(
									"EEEE, MMMM dd, yyyy").format(r.getDate());
							time = new SimpleDateFormat(
									"HH:mm").format(r.getDate());
							date = day + " at " + time;
						}

						String user = null;
						if (r.getEditor() == null) {
							user = "system";
						} else {
							user = r.getEditor().getUsername();
						}
						%>
						<tr>
							<td><%= i + 1 %></td>
							<td><%= date %></td>
							<td><%= user %></td>
						</tr>
					<% } %>
				</tbody>
			</table>
		</div>

		Back to
		<a href="/event/<%= e.getShortName() %>">
			Event: <%= e.getName() %>
		</a>
	</tiles:putAttribute>
</tiles:insertDefinition>