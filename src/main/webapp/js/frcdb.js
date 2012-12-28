/*
 * FRCDB JavaScript API, using JSON.
 * Theoretically supports crappy browsers *cough* IE6/7 *cough* but that's
 * untested.
 */

var FRCDB_URL = "http://glass.frcdb.net";

/*
 * Generic utility functions
 */

/*
 * Includes another JS file.
 */
function include(file) {
	var script  = document.createElement('script');
	script.src  = file;
	script.type = 'text/javascript';
	script.defer = true;

	document.getElementsByTagName('head').item(0).appendChild(script);
}

/*
 * Include JSON libs if the browser doesn't support them
 */
if (typeof JSON == "undefined") {
	include(FRCDB_URL + "/js/json2.js");
}

var FRCDB = {
	
	getJSON: function(url) {
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		
		var text = null;
		
		request.onreadystatechange = function() {
			if (request.readyState == 4 && request.status == 200) {
				text = request.responseText;
			}
		}
		request.send(null);
		
		return JSON.parse(text);
	},
	
	/*
	 * Gets the team with the given number.
	 */
	getTeam: function(number) {
		return FRCDB.getJSON(FRCDB_URL + "/team/" + number + "/json");
	},
	
	/*
	 * Gets a list of events (incl. id, shortName, etc)
	 */
	getEvents: function() {
		return FRCDB.getJSON(FRCDB_URL + "/json/events");
	},
	
	/*
	 * Gets game info via frcdb-internal shortname. The short name can be found
	 * in the URL, such as: http://frcdb.net/event/san-diego where "san-diego"
	 * is the shortname.
	 * This function specifically gets the current year (as defined by the
	 * database).
	 */
	getGame: function(shortname) {
		return FRCDB.getJSON(FRCDB_URL + "/event/" + shortname + "/json");
	},
	
	/*
	 * Gets the basic database statistics (number of teams and events known).
	 */
	getStats: function() {
		return FRCDB.getJSON(FRCDB_URL + "/json/stats");
	}
	
}
