
<%-- 
    Document   : event
    Created on : Jul 25, 2010, 6:27:17 PM
    Author     : tim
--%>

<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js.tld" prefix="js" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="${data.event.name}"/>
	<tiles:putAttribute name="head-extra">
		<script type="text/javascript">
			google.load("visualization", "1", { packages: ["corechart"] });
		</script>
	</tiles:putAttribute>
	<tiles:putAttribute name="body">
		<h1>Event: ${data.event.name} (${data.game.gameYear})</h1>
		
		<c:if test="${not empty data.eventJson}">
			<script type="application/json" id="event-data">
${data.eventJson}
			</script>
		</c:if>
		
		<c:if test="${not empty data.eventJson}">
			<script type="application/json" id="game-data">
${data.gameJson}
			</script>
		</c:if>
			
		<p class="breadcrumbs">
			<c:if test="${not empty data.game.parent}">
				<a href="/event/${data.game.parent.event.shortName}/${data.game.parent.gameYear}">
					Parent: ${data.game.parent.event.name}
				</a> &nbsp;
			</c:if>
			<a href="/event/${data.event.shortName}/${data.game.gameYear}/standings">
				Standings
			</a> &nbsp;
			
			<c:if test="${utils:isUserAdmin()}">
				<a href="#" id="runStatistic">Run Statistic</a> &nbsp;
				<a href="#" id="event-edit">Edit Event</a> &nbsp;
				<a href="#" id="game-edit">Edit Game</a>
				<script type="text/javascript">
					var executeStatisticTemplate = {
						"Name":       { name: "name", type: "text" },
						"Event name": { name: "event", type: "text",
						                readonly: "true",
										value: "${data.event.shortName}" },
						"Year":       { name: "year", type: "number",
										value: "${data.game.gameYear}" }
					};
				
					$("#runStatistic").click(function() {
						$.dialogform({
							title: "Execute Statistic",
							fields: executeStatisticTemplate,
							url: "/json/admin/stats/execute",
							success: function(response) {
								console.log(response);
								alert(response.message);
							}
						});
					});
					
					var eventTemplate = $.extend(true, {
						"Short Name": {
							readonly: true,
							value: "${data.event.shortName}"
						}
					}, EditorTemplates.event);
					
					eventTemplate = $.pushJsonToTemplate(
							JSON.parse($("#event-data").html()), eventTemplate);
					
					$("#event-edit").click(function() {
						$.dialogform({
							title: "Edit Event",
							fields: eventTemplate,
							url: "/json/admin/event/modify",
							success: function(response) {
								console.log(response);
								alert(response.message);
							}
						});
					});
					
					var gameData = JSON.parse($("#game-data").html());
					
					var parentString = "";
					if (gameData.parent) {
						parentString = new String(
								gameData.parent.name
								+ "="
								+ gameData.parent.year);
					}
					
					var children = [];
					gameData.children.forEach(function(child) {
						children.push(new String(child.name + "=" + child.year));
					});
					var childrenString = children.join();
					
					var gameTemplate = $.extend(true, {
						"Event Name": {
							readonly: true,
							value: "${data.event.shortName}"
						},
						"Game Year": {
							readonly: true,
							value: "${data.game.gameYear}"
						}
					}, EditorTemplates.game);
					
					gameTemplate = $.pushJsonToTemplate(gameData, gameTemplate);
					
					// overwrite with proper parent / children strings
					gameTemplate["Parent Game"].value = parentString;
					gameTemplate["Child Games"].value = childrenString;
					
					$("#game-edit").click(function() {
						$.dialogform({
							title: "Edit Game",
							fields: gameTemplate,
							url: "/json/admin/game/modify",
							success: function(response) {
								console.log(response);
								alert(response.message);
							}
						});
					});
				</script>
			</c:if>
		</p>
		
		Viewing results for ${data.game.gameYear}. Years available:<br>
		<ul>
			<c:forEach items="${data.games}" var="g">
				<li>
					<a href="/event/${data.event.shortName}/${g.gameYear}">
						${g.gameYear} - ${g.gameName}
					</a>
				</li>
			</c:forEach>
		</ul>
		
		<c:if test="${not utils:isEmpty(data.game.children)}">
			Child events:
			<ul>
				<c:forEach items="${data.game.children}" var="g">
					<li>
						<a href="/event/${g.event.shortName}/${g.gameYear}">
							${g.event.name}
						</a>
					</li>
				</c:forEach>
			</ul>
		</c:if>
		
		<h2>Event Info</h2>
		<table width="90%" border="1" style="margin: auto; margin-bottom: 15px;">
			<tbody>
				<tr>
					<td>Name</td>
					<td>${data.event.name}</td>
				</tr>
				<tr>
					<td>Location</td>
					<td>${data.event.city}, ${data.event.state}</td>
				</tr>
				<tr>
					<td>Date</td>
					<td>${utils:date(data.game.startDate, 'MMMM dd yyyy')} - 
						${utils:date(data.game.endDate, 'MMMM dd yyyy')}</td>
				</tr>
				<c:if test="${utils:hasProperty(data.game, 'averageOPR')}">
					<tr>
						<td>Average OPR</td>
						<td>${utils:format("%.2f", data.game.averageOPR)}</td>
					</tr>
					<%-- TODO: fixme! --%>
					<%--<c:if test="${not empty data.game.highestOPRTeam}">
						<c:set property="hoprTeam" value="${data.game.highestOPRTeam.team}"/>
						<tr>
							<td>Highest OPR</td>
							<td>
								<a href="/event/${data.event.shortName}/${data.game.gameYear}/team/${hoprTeam.number}"
								   title="#${hoprTeam.number}: ${hoprTeam.nickname}">
									${utils:format("%.2f", data.game.highestOPR)}
								</a>
							</td>
						</tr>
					</c:if>--%>
					
				</c:if>
			</tbody>
		</table>

		<c:if test="${not empty data.oprShareData}">
			<h2>OPR Distribution</h2>
			<div id="oprShareChart"
				 style="width: 90%; height: 250px; margin: 0 auto;"></div>
			
			<script type="application/json" id="oprShareData">
				${data.oprShareData}
			</script>
			
			<script type="text/javascript">
				$(document).ready(function() {
					var data = JSON.parse($("#oprShareData").html());
					var table = google.visualization.arrayToDataTable(data);
					var options = { };
					var chart = new google.visualization.PieChart(
							$("#oprShareChart")[0]);
					chart.draw(table, options);
				});
			</script>
		</c:if>
		
		<h2 style="padding-top: 15px;">Teams Attending</h2>
		<js:table id="event_teams" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Nickname</th>
					<th>Rank</th>
					<c:if test="${utils:hasProperty(data.game, 'averageOPR')}">
						<th>OPR</th>
						<th>DPR</th>
					</c:if>
					<%--<th> TODO: content APIs
						Robot Info
					</th>--%>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.teams}" var="t">
					<tr>
						<td>${t.team.number}</td>
						<td>
							<a href="/event/${data.event.shortName}/${data.game.gameYear}/team/${t.team.number}">
								${t.team.nickname}
							</a>
						</td>
						<td>${t.rank}</td>
						<c:if test="${utils:hasProperty(t, 'OPR')}">
							<td>${utils:format("%.2f", t.OPR)}</td>
							<td>${utils:format("%.2f", t.DPR)}</td>
						</c:if>
						<%--<td>
							<a href="/team/${t.team.number}/robot/${gameYear}">
								Info
							</a>
						</td>--%>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>
		
		<c:if test="${not utils:isEmpty(data.qualificationMatches)}">
			<h2 style="padding-top: 15px;">Qualification Matches</h2>
			<js:table id="event_qualificationResults" width="90%" align="center">
				<thead>
					<tr>
						<th>Number</th>
						<th>Red 1</th>
						<th>Red 2</th>
						<th>Red 3</th>
						<th>Blue 1</th>
						<th>Blue 2</th>
						<th>Blue 3</th>
						<th>Red Score</th>
						<th>Blue Score</th>
						<th>Link</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.qualificationMatches}" var="m">
						<tr>
							<td>${m.number}</td>
							<c:forEach items="${m.redTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<c:forEach items="${m.blueTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<td>${m.redScore}</td>
							<td>${m.blueScore}</td>
							<td>
								<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/${m.number}">
									Info
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</js:table>
		</c:if>
		
		<c:if test="${not utils:isEmpty(data.quarterfinalMatches)}">
			<h2 style="padding-top: 15px;">Quaterfinals Matches</h2>
			<js:table id="event_quarterfinalsResults" width="90%" align="center">
				<thead>
					<tr>
						<th>Number</th>
						<th>Red 1</th>
						<th>Red 2</th>
						<th>Red 3</th>
						<th>Blue 1</th>
						<th>Blue 2</th>
						<th>Blue 3</th>
						<th>Red Score</th>
						<th>Blue Score</th>
						<th>Link</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.quarterfinalMatches}" var="m">
						<tr>
							<td>${m.number}</td>
							<c:forEach items="${m.redTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<c:forEach items="${m.blueTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<td>${m.redScore}</td>
							<td>${m.blueScore}</td>
							<td>
								<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/q${m.number}">
									Info
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</js:table>
		</c:if>
		
		<c:if test="${not utils:isEmpty(data.semifinalMatches)}">
			<h2 style="padding-top: 15px;">Semifinals Matches</h2>
			<js:table id="event_semifinalsResults" width="90%" align="center">
				<thead>
					<tr>
						<th>Number</th>
						<th>Red 1</th>
						<th>Red 2</th>
						<th>Red 3</th>
						<th>Blue 1</th>
						<th>Blue 2</th>
						<th>Blue 3</th>
						<th>Red Score</th>
						<th>Blue Score</th>
						<th>Link</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.semifinalMatches}" var="m">
						<tr>
							<td>${m.number}</td>
							<c:forEach items="${m.redTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<c:forEach items="${m.blueTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<td>${m.redScore}</td>
							<td>${m.blueScore}</td>
							<td>
								<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/s${m.number}">
									Info
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</js:table>
		</c:if>
		
		<c:if test="${not utils:isEmpty(data.finalMatches)}">
			<h2 style="padding-top: 15px;">Finals Matches</h2>
			<js:table id="event_finalsResults" width="90%" align="center">
				<thead>
					<tr>
						<th>Number</th>
						<th>Red 1</th>
						<th>Red 2</th>
						<th>Red 3</th>
						<th>Blue 1</th>
						<th>Blue 2</th>
						<th>Blue 3</th>
						<th>Red Score</th>
						<th>Blue Score</th>
						<th>Link</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.finalMatches}" var="m">
						<tr>
							<td>${m.number}</td>
							<c:forEach items="${m.redTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<c:forEach items="${m.blueTeams}" var="t">
								<td>${t.number}</td>
							</c:forEach>
							<td>${m.redScore}</td>
							<td>${m.blueScore}</td>
							<td>
								<a href="/event/${data.event.shortName}/${data.game.gameYear}/match/f${m.number}">
									Info
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</js:table>
		</c:if>

		<%-- todo: rewrite this
		<c:if test="${admin}">
			<h2>Scheduled Updates</h2>
			<js:schedule id="eventScheduler"
						 type="eventUpdate"
						 eventName="${shortName}"
						 gameYear="${gameYear}"/>
		</c:if>--%>
	</tiles:putAttribute>
</tiles:insertDefinition>
