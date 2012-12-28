<%-- 
    Document   : team
    Created on : Jul 25, 2010, 6:27:31 PM
    Author     : tim
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/tlds/js" prefix="js" %>
<%@taglib uri="/WEB-INF/tlds/cewolf.tld" prefix="cewolf" %>
<%@taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
    <tiles:putAttribute name="title">Event: ${data.event.name}, Team #${data.team.teamNumber}"</tiles:putAttribute>
    <tiles:putAttribute name="body">
        <h1>Event: ${data.event.name}, Team #${data.team.teamNumber}</h1>
        <p class="breadcrumbs">
            <a href="/event/${data.event.shortName}/${data.game.gameYear}">Event Page</a>,
            <a href="/team/${data.team.teamNumber}">Team Page</a>,
            <a href="/team/${data.team.teamNumber}/robot/${data.game.gameYear}">Robot Info</a>
        </p>

        <h2>Scores</h2>
        <table width="90%" border="1" style="margin: auto; margin-bottom: 15px;">
            <tbody>
                <tr>
                    <td>Nickname</td>
                    <td>${data.team.teamNickname}</td>
                </tr>
                <c:if test="${utils:hasProperty(data.team, 'OPR')}">
                    <tr>
                        <td>OPR</td>
                        <td>${utils:format('%.3f', data.team.OPR)}</td>
                    </tr>
                    <tr>
                        <td>DPR</td>
                        <td>${utils:format('%.3f', data.team.DPR)}</td>
                    </tr>
                </c:if>
                <tr>
                    <td>Rank</td>
                    <td>${data.team.rank}</td>
                </tr>
                <tr>
                    <td>Matches Played</td>
                    <td>${data.team.matchesPlayed}</td>
                </tr>
                <c:if test="${utils:hasProperty(data.team, 'seedingScore')}">

                    <tr>
                        <td>Seeding Score</td>
                        <td>${data.team.seedingScore}</td>
                    </tr>
                    <tr>
                        <td>Coopertition Bonus</td>
                        <td>${data.team.coopertitionBonus}</td>
                    </tr>
                </c:if>
                <c:if test="${utils:hasProperty(data.team, 'hangingPoints')}">
                    <tr>
                        <td>Hanging Points</td>
                        <td>${data.team.hangingPoints}</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <c:if test="${utils:hasProperty(data.team, 'OPR')}">
            <jsp:useBean id="teamOPR" 
                         class="net.frcdb.servlet.event.TeamOPRPie"/>
            <jsp:setProperty name="teamOPR" 
                             property="entry" 
                             value="${data.team}"/>
            <jsp:setProperty name="teamOPR" 
                             property="game" 
                             value="${data.game}"/>

            <cewolf:chart id="OPRPie"
                          type="pie3d">
                <cewolf:data>
                    <cewolf:producer id="teamOPR"/>
                </cewolf:data>
            </cewolf:chart>

            <jsp:useBean id="teamOPRBar" 
                         class="net.frcdb.servlet.event.TeamOPRBar"/>
            <jsp:setProperty name="teamOPRBar" 
                             property="entry" 
                             value="${data.team}"/>
            <jsp:setProperty name="teamOPRBar" 
                             property="game" 
                             value="${data.game}"/>

            <cewolf:chart id="OPRBar"
                          type="verticalbar">
                <cewolf:data>
                    <cewolf:producer id="teamOPRBar"/>
                </cewolf:data>
            </cewolf:chart>

            <h2>OPR Share and Comparison</h2>
            <table style="margin: 0 auto;" border="1">
                <tbody>
                    <tr>
                        <td>
                            <cewolf:img chartid="OPRPie" 
                                        renderer="/cewolf" 
                                        width="300"
                                        height="200">
                                <cewolf:map tooltipgeneratorid="teamOPR"/>
                            </cewolf:img>
                        </td>
                        <td>
                            <cewolf:img chartid="OPRBar" 
                                        renderer="/cewolf" 
                                        width="300"
                                        height="200">
                            </cewolf:img>
                        </td>
                    </tr>
                </tbody>
            </table>

            <jsp:useBean id="teamMatchScores" 
                         class="net.frcdb.servlet.event.TeamMatchScore"/>
            <jsp:setProperty name="teamMatchScores" 
                             property="entry" 
                             value="${data.team}"/>
            <jsp:setProperty name="teamMatchScores"
                             property="game"
                             value="${data.game}"/>

            <cewolf:chart id="matchScores"
                          type="line">
                <cewolf:data>
                    <cewolf:producer id="teamMatchScores"/>
                </cewolf:data>
            </cewolf:chart>

            <h2>Match Scores</h2>
            <table style="margin: 0 auto;" border="1">
                <tbody>
                    <tr><td>
                            <cewolf:img chartid="matchScores" 
                                        renderer="/cewolf" 
                                        width="600"
                                        height="200">
                            </cewolf:img>
                        </td></tr>
                </tbody>
            </table>
        </c:if>

        <h2 style="padding-top: 15px;">Matches Played</h2>

        <js:table id="event_teamMatches" width="90%" align="center">
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
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${data.matches}" var="m">
                    <tr>
                        <c:set var="prefix" value="${m.type.prefix}" />
                        <c:set var="url" value="/event/${data.event.shortName}/${data.game.gameYear}/match/${prefix}${m.number}" />
                        <c:set var="title" value="${m.type.text} match #${m.number}" />
                        <td>${m.number}</td>
                        <td>${m.redTeams[0]}</td>
                        <td>${m.redTeams[1]}</td>
                        <td>${m.redTeams[2]}</td>
                        <td>${m.blueTeams[0]}</td>
                        <td>${m.blueTeams[1]}</td>
                        <td>${m.blueTeams[2]}</td>
                        <td>
                            <a href="${url}" title="${title}">
                                ${m.redScore}
                            </a>
                        </td>
                        <td>
                            <a href="${url}" title="${title}">
                                ${m.blueScore}
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
		</js:table>

		<h2 style="padding-top: 15px;">Teams Played With</h2>
		<js:table id="event_teamsPlayedWith" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Nickname</th>
					<c:if test="${utils:hasProperty(data.team, 'OPR')}">
						<th>OPR</th>
						<th>DPR</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.playedWith}" var="team">
					<c:set var="url" value="/event/${data.event.shortName}/${data.game.gameYear}/team/${team.teamNumber}"/>
					<tr>
						<td>${team.teamNumber}</td>
						<td><a href="${url}">${team.teamNickname}</a></td>
						<c:if test="${utils:hasProperty(team, 'OPR')}">
							<td>${utils:format('%.3f', team.OPR)}</td>
							<td>${utils:format('%.3f', team.DPR)}</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>

		<h2 style="padding-top: 15px;">Teams Played Against</h2>
		<js:table id="event_teamsPlayedAgainst" width="90%" align="center">
			<thead>
				<tr>
					<th>Number</th>
					<th>Nickname</th>
					<c:if test="${utils:hasProperty(data.team, 'OPR')}">
						<th>OPR</th>
						<th>DPR</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${data.playedAgainst}" var="team">
					<c:set var="url" value="/event/${data.event.shortName}/${data.game.gameYear}/team/${team.teamNumber}" />
					<tr>
						<td>${team.teamNumber}</td>
						<td><a href="${url}">${team.teamNickname}</a></td>
						<c:if test="${utils:hasProperty(team, 'OPR')}">
							<td>${utils:format('%.3f', team.OPR)}</td>
							<td>${utils:format('%.3f', team.DPR)}</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</js:table>

		Back to
		<a href="/event/${data.game.eventName}/${data.game.gameYear}">
			Event: ${data.event.name}
		</a>
	</tiles:putAttribute>
</tiles:insertDefinition>
