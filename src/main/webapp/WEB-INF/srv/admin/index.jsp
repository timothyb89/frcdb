<%-- 
    Document   : index
    Created on : Dec 27, 2012, 4:51:42 PM
    Author     : tim
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://frcdb.net/taglibs/utils" prefix="utils" %>

<tiles:insertDefinition name="layout-default">
	<tiles:putAttribute name="title" value="Admin"/>
	<tiles:putAttribute name="body">
		<h2>Administration</h2>
		<h3>Team Actions</h3>
		<button id="teamCreate">Create Team</button>
		<button id="teamsImport">Import Team Data</button>
		
		<p>Preexisting teams may be modified on their respective pages.</p>
		<p>
			Team data must be imported from a teams.json file, and can not be
			contained in a .zip file.
		</p>
		
		<h3>Event Actions</h3>
		<button id="eventCreate">Create Event</button>
		<button id="eventsImport">Import Event Data</button>
		
		<p>Event information can be modified from event pages.</p>
		<p>
			Event data must be imported from an events.json file, and can not be
			contained in a .zip file.
		</p>
		
		<h3>Game Actions</h3>
		<button id="gamesImport">Import Games (.zip)</button>
		
		<p>
			Zip files can be uploaded here, and will be extracted to the
			blobstore and queued for import. Import takes a fairly long time
			and progress can be viewed from the appengine's task queue admin
			panel. Note that this is extremely expensive: the 2010-2012 data
			import cost roughly $0.52 on top of the free quota for all 188
			events.
		</p>
		
		<h3>Statistics</h3>
		<button id="executeStatistic">Execute statistic</button>
		
		<h4>Types</h4>
		<ul>
			<li>
				<b>Global:</b> statistics that perform tasks on the whole site
				rather than a specific piece of data. No parameters other than
				<i>Name</i> are used.
				<ul>
					<li>
						<b>Counts:</b> updates the event, game, and team counts
						displayed on the homepage.
						<i>Aliases: none</i>
					</li>
					<li>
						<b>SitemapGenerator:</b> Regenerates the sitemap served
						at /sitemap.xml which can be submitted to search
						engines.
						<i>Aliases: sitemap</i>
					</li>
					<li>
						<b>TeamUpdater:</b> Updates the team list from FIRST.
						New information will be replaced and added as needed.
						<i>Aliases: update-teams, teamupdate</i>
						<u>Backend: stat-compute</u>
					</li>
				</ul>
			</li>
			<li>
				<b>Event:</b> statistics that are applicable to a single event,
				or all games in a particular event. Specify an event short name
				for <i>Event name</i> or leave it empty to apply to all events.
				<i>Year</i> should not be specified.
				<ul>
					<li>
						<b>ReferenceRepair:</b> fixes references to games. 
						Not likely to be needed.
						<i>Aliases: none</i>
					</li>
				</ul>
			</li>
			<li>
				<b>Game:</b> statistics applicable to a game or set of games.
				Specify an event short name for <i>Event name</i> or leave it 
				blank for all events. Specify a game year for <i>Year</i> or 0
				to use games from all years.
				<ul>
					<li>
						<b>OPRDPRCalc:</b> calculates OPR/DPR and all cached
						team statistics depending on it. This is a fairly
						expensive operation.
						<i>Aliases: opr, dpr</i>
						<u>Backend: stat-compute</u>
					</li>
					<li>
						<b>TeamStatistics:</b> calculates various team
						statistics, including average OPR. variance, etc.
						<i>Aliases: teamstats</i>
					</li>
					<li>
						<b>GameYearIndexes:</b> fixes the gameYearIndex field
						to allow filtering by game year. This is now handled 
						automatically and should not need to be run manually.
						<i>Aliases: none</i>
					</li>
					<li>
						<b>FixSources:</b> fixes data sources, mainly the URLs
						for event match results and standings. These URLs are
						not included in all data dumps, so this may need to be
						run in order to perform game data updates. URLs may
						also be entered manually via "Edit game" on event pages.
						<i>Aliases: sources</i>
					</li>
				</ul>
			</li>
		</ul>
		
		Note that statistics running on a backend may take longer to execute or
		require more memory. In particular, the <i>stat-compute</i> backend is a
		B4-class backend and consumes instances hours at a 4x rate. Backends
		are generally only required for data updates and expensive calculations,
		like OPR.
		
		<h4>Options</h4>
		<ul>
			<li>
				<b>Name:</b> the name of the statistic to run. Case insensitive.
				Any alias specified may also be used.
			</li>
			<li>
				<b>Event name:</b>  used for Event and Game statistics. Blank or
				"all" will match all events.
			</li>
			<li>
				<b>Year:</b> The year to match for game statistics. Blank or 
				0 (zero) will match all games from a given year.
			</li>
		</ul>
		Event name and year specifiers can be mixed and matched. You can apply
		a game statistic to all years of some event (event name: "alamo", year:
		0), all games from some year (event name: "all" or blank, year: 2012),
		or simply all games (event name: "all" or blank, year: 0 or blank).
		
		<script type="text/javascript">
			var teamImportTemplate = {
				"Teams JSON": { name: "file", type: "file" }
			};
			
			var eventImportTemplate = {
				"Events JSON": { name: "file", type: "file" }
			};
			
			var gamesImportTemplate = {
				"Games .zip": { name: "file", type: "file" }
			};
		
			var executeStatisticTemplate = {
				"Name":       { name: "name", type: "text" },
				"Event name": { name: "event", type: "text" },
				"Year":       { name: "year", type: "number" }
			};
			
			$("#teamCreate").button().click(function() {
				$.dialogform({
					title: "Create Team",
					fields: EditorTemplates.team,
					url: "/json/admin/team/create",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#teamModify").button().click(function() {
				var template = $.extend({
					"Number": {
						disabled: true
					}
				}, teamTemplate);
				
			});
		
			$("#teamsImport").button().click(function() {
				$.dialogform({
					title: "Import Teams",
					fields: teamImportTemplate,
					url: "/json/admin/team/import",
					contentType: "multipart/form-data",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#eventCreate").button().click(function() {
				$.dialogform({
					title: "Create Event",
					fields: EditorTemplates.event,
					url: "/json/admin/event/create",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#eventsImport").button().click(function() {
				$.dialogform({
					title: "Import Events",
					fields: eventImportTemplate,
					url: "/json/admin/event/import",
					contentType: "multipart/form-data",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#gamesImport").button().click(function() {
				$.dialogform({
					title: "Import Games",
					fields: gamesImportTemplate,
					url: "/json/admin/game/import",
					contentType: "multipart/form-data",
					success: function(response) {
						console.log(response);
						alert(response.message);
					}
				});
			});
			
			$("#executeStatistic").button().click(function() {
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
		</script>
		
	</tiles:putAttribute>
</tiles:insertDefinition>