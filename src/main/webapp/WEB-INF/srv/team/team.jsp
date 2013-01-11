<%-- 
Document   : team
    Created on : Jul 25, 2010, 4:12:08 AM
    Author     : tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js" prefix="js" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Team #${data.team.number}"/>
	<tiles:putAttribute name="body">
		<h1>Team #${data.team.number}: ${data.team.nickname}</h1>
		<p class="breadcrumbs">
			<a href="/team/${data.team.number}/robot">Robot Info</a>
		</p>
		
		<h2>Team Info</h2>
		<table width="90%" border="1" style="margin: auto;">
			</thead>
			<tbody>
				<tr>
					<td>Nickname</td>
					<td>${data.team.nickname}</td>
				</tr>
				<tr>
					<td>Name</td>
					<td>${data.team.name}</td>
				</tr>
				<tr>
					<td>Location</td>
					<td>${data.team.city}, ${data.team.state}</td>
				</tr>
				<tr>
					<td>Number</td>
					<td>${data.team.number}</td>
				</tr>
				<tr>
					<td>Rookie Season</td>
					<td>${data.team.rookieSeason}</td>
				</tr>
				<c:if test="${not empty data.team.motto}">
					<tr>
						<td>Motto</td>
						<td>${data.team.motto}</td>
					</tr>
				</c:if>
				<c:if test="${not empty data.team.website}">
					<tr>
						<td>Website</td>
						<td>${data.team.website}</td>
					</tr>
				</c:if>
			</tbody>
		</table>

		<h2>Team History</h2>
		<js:tabPane id="teamHistoryTabs" style="width: 90%; margin: 0 auto;">
			<c:forEach items="${data.years}" var="year">
				<js:tab id="teamHistory${year.year}"
						title="${year.year} - ${year.name}">
					<c:forEach items="${year.games}" var="game">
						<h3>
							<a href="/event/${game.game.event.shortName}/${year.year}">
								${game.eventName}
							</a>
						</h3>
						<ul>
							<li><b>Qualifications Rank:</b> ${game.entry.rank}</li>
							<li><b>Progressed to:</b> ${game.entry.finalMatchLevel.text}</li>
						</ul>
					</c:forEach>
				</js:tab>
			</c:forEach>
		</js:tabPane>
	</tiles:putAttribute>
</tiles:insertDefinition>