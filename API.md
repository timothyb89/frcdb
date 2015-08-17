# API #

Currently, our API is JSON based. Additionally, some libraries exist to simplify use in external applications.

Note that most API URLs found under http://frcdb.net/json/* are non-working and deprecated. This includes full event and team lists due mainly to the cost of processing the large amounts of data associated with these datasets. Cached versions of these lists are detailed below.

Some issues have been reported accessing frcdb.net to make API requests on some systems. This may be due to CloudFlare blocking or otherwise interfering with some users. If you experience issues, make sure to contact us so we can help resolve them.

## Usage Notes ##
Unfortunately, the App Engine has fairly strict quotas that make pageviews and API calls somewhat costly. In order to help us keep the FRC-DB API open and the website ad-free, please keep the following in mind when making requests:
  * New instances will be dynamically started by the App Engine if a large number of requests occur at the same time, and instance hour consumption increases faster with every new instance started. With this in mind, please:
    * Don't make multiple concurrent requests if possible. Requests in quick succession are fine, but several at once will become extremely costly for even short amounts of time
    * Be careful when spacing out requests. A request every 15 minutes (App Engine's instance spin-down time) will prevent an instance from spinning down and leave it relatively unused. Instead, make all needed requests in quick succession (but not concurrently) to allow unused instances to be recycled.
  * Cache fetched data locally whenever possible. The only data updated often is current event/game pages, and those are generally only changed on game days.
  * Used the cached [team](http://www.frcdb.net/teams.json) and [event](http://www.frcdb.net/events.json) lists for large updates - these are very cheap to serve
  * Try to avoid fetching each team individually. The cached teams.json contains identical information to that found in individual team requests
  * If you don't need the absolute latest data, use the data dumps available in the Downloads section above. They contain all of the information available online, in the same format as the API, and should be compatible with your preexisting parsers.

While App Engine's database operations also tend to be quite costly, we keep the vast majority of it cached server-side and completely avoid most database operations. As such, our primary concern is related to the instance hour quota (28 hours per day).

### Ideal Use Case ###
Ideally, clients should do roughly the following in order to stay up-to-date and avoid making costly requests:
  1. Perform an initial mass update using the cached teams.json and events.json - this fetches the majority of the needed data and is year independent.
  1. Fetch and cache game data on demand. This (particularly, matches) accounts for the vast majority of the data
  1. Update teams as needed, either using single requests (i.e. /team/1/json/) sparingly, or /teams.json for large updates.
  1. Update games as needed, or automatically on game days. We're already planning for a large volume of requests on game days, so updating ongoing games hourly or so should be acceptable.

Also keep in mind that the entirety of the database contains a fairly large amount of information. While the JSON data dumps are reasonably small (~4 MiB), a properly serialized object graph can easily take 40 MiB depending on the database software used. For reference, our official database contains about 264 MiB of data, including indexes. For embedded clients (such as phones and tablets), you may not want to store all data locally.

## API Functions ##

### Team Information ###

Team information dumps can be accessed at the following URL:

> http://www.frcdb.net/team/number/json

For example, data for team 1 (The Juggernauts) can be accessed at http://frcdb.net/team/1/json and should contain the following data:

```
{
	"number":1,
	"name":"Autodesk/BAE Systems/The Chrysler Fondation & Oakland Schools Technical Campus Northeast High School",
	"nickname":"The Juggernauts",
	"location":"Pontiac, MI  USA",
	"rookieSeason":1997,
	"website":"http://www.juggernauts.org/",
	"motto":"Real Winners are Ordinary People with Extraordinary Determination"
}
```

Fields here:
  * **number:** _(int)_ The team number
  * **name:** _(string)_ The official team name. Probably not the information you're looking for (mostly sponsors)
  * **nickname:** _(string)_ The more common team name, what teams are generally known as.
  * **city:** _(string)_
  * **state:** _(string)_
  * **country:** _(string)_
  * **website:** _(string)_ The team website, generally available
  * **motto:** _(string)_ The team motto, often nonexistant

Keep in mind properties here may be missing, so don't depend on them. Some properties (number, name, nickname, location) are required and should always be available whereas websites and mottos occasionally don't exist. In such cases, the property won't even be printed here.

A cached list of teams can be found at http://frcdb.net/teams.json. This file _should_ remain up-to-date with the rest of the team lists, but the latest information for a particular team can always be found at the specific team page.

### Event Information ###

We maintain a list of events accessible at http://www.frcdb.net/events.json which can aid in the mapping of event IDs and names to FRCDB shortnames. The output is a JSON array that looks like this:

```
[{
	"name":"Troy FIRST Robotics District Competition",
	"shortName":"troy",
	"location":"Troy Athens High School\n Troy, MI  48085\n USA",
	"id":"OC",
	"years":[2010,2011]
	},{
	"name":"Detroit FIRST Robotics District Competition",
	"shortName":"detroit",
	"location":"Wayne State University\n Detroit, MI  48201\n USA",
	"id":"DT",
	"years":[2010,2011]
	},{
	"name":"West Michigan FIRST Robotics District Competition",
	"shortName":"west-michigan",
	"location":"Grand Valley State University\n Allendale, MI  49401\n USA",
	"id":"MI",
	"years":[2010,2011]
	// many more events here
}]
```

Fields here:
  * **name:** _(string)_ The official name of the event
  * **shortName:** _(string)_ The FRCDB internal shortname for the event.
  * **city:** _(string)_
  * **state:** _(string)_
  * **country:** _(string)_
  * **id:** _(string)_ The event ID used more or less globally in the FRC community.
  * **years:** _(int array)_ The years for which data is available when requesting game information (see below)

### Game Information ###

Game information dumps can be accessed at the following URLs:

> http://frcdb.net/event/shortname/json (current year)
> http://frcdb.net/event/year/shortname/json (specific year)

Short names can be found simply by browsing to the event on the database itself. We don't use the usual FIRST codes except in very specific cases (FLR, for example) so be weary of the differences. Additionally, all short names are lower case and while any case should work, lower is preferred.

To get data for the "san-diego" game for the current year, use the URL http://frcdb.net/event/san-diego/json which should contain data similar to:

```
{
  "gameYear" : 2012,
  "gameName" : "Rebound Rumble",
  "eventShortName" : "san-diego",
  "startDate" : 1330671600000,
  "endDate" : 1330927200000,
  "averageOPR" : 5.18637002045851,
  "averageDPR" : 5.194354392093376,
  "totalOPR" : 290.43672114567664,
  "totalDPR" : 290.8838459572291,
  "highestOPR" : 16.420693654260955,
  "highestOPRTeam" : 3476,
  "lowestOPR" : -2.325249839815887,
  "lowestOPRTeam" : 3704,
  "teams" : [ {
    "number" : 4322,
    "rank" : 50,
    "matchesPlayed" : 10,
    "gameYear" : 2012,
    "wins" : 3,
    "losses" : 7,
    "ties" : 0,
    "finalMatchLevel" : "qualifications",
    "opr" : 5.208970007461823,
    "dpr" : 7.091203228826963
  }, /* more teams here */ {
    "number" : 399,
    "rank" : 10,
    "matchesPlayed" : 10,
    "gameYear" : 2012,
    "wins" : 5,
    "losses" : 4,
    "ties" : 1,
    "finalMatchLevel" : "finals",
    "opr" : 9.988380575800665,
    "dpr" : 5.108175817037077
  } ],
  "matches" : [ {
    "type" : "qualifications",
    "number" : 94,
    "time" : "11:57 AM",
    "redScore" : 21,
    "redTeams" : [ 599, 2496, 1622 ],
    "blueScore" : 19,
    "blueTeams" : [ 3647, 4322, 3491 ],
    "winner" : "red"
  }, /* more matches here */ {
    "type" : "quarterfinals",
    "number" : 11,
    "time" : "2:46 PM",
    "redScore" : 42,
    "redTeams" : [ 599, 2599, 1661 ],
    "blueScore" : 25,
    "blueTeams" : [ 1622, 1572, 1266 ],
    "winner" : "red"
  }, /* more matches here */ {
    "type" : "semifinals",
    "number" : 16,
    "time" : "3:45 PM",
    "redScore" : 10,
    "redTeams" : [ 2599, 599, 1661 ],
    "blueScore" : 58,
    "blueTeams" : [ 1138, 399, 2339 ],
    "winner" : "blue"
  }, /* more matches here */ {
    "type" : "finals",
    "number" : 19,
    "time" : "3:59 PM",
    "redScore" : 43,
    "redTeams" : [ 3476, 4161, 3255 ],
    "blueScore" : 34,
    "blueTeams" : [ 1138, 2339, 399 ],
    "winner" : "red"
  } ],
  "standings" : [ {
    "rank" : 56,
    "team" : 4117,
    "matchesPlayed" : 10
  } /* many more standings here */]
}
```

Fields here:
  * **gameYear:** _(int)_ The year games of this type took place (2012 for Rebound Rumble, etc)
  * **gameName:** _(string)_ The name of the game type
  * **eventShortName:** _(string)_ The short name of the event in which this game took place
  * **startDate:** _(long)_ The starting date of the event. Standard Java time representation
  * **endDate:** _(long)_ The ending date of the event. Standard Java time representation
  * **teams:** An array of teams attending the event, and specific statistics and scores associated.
    * **number:** _(int)_ The number of the team
    * **rank:** _(int)_ The rank the team reach in qualification matches.
    * **matchesPlayed:** _(int)_ The number of matches this team played in at this event
    * **wins:** _(int)_ The number of matches this team won
    * **losses:** _(int)_ The number of matches this team lost
    * **ties:** _(int)_ The number of matches this team tied in
    * **finalMatchLevel:** _(string)_ One of: qualifications, quarterfinals, semifinals, finals. The farthest match type this team reached.
    * **opr** and **dpr**: _(double)_ The OPR and DPR of this team for all qualifications matches. See [here](http://www.frcdb.net/about#stats-opr) for more information on this statistic.
  * **matches:** An array of matches.
    * **type:** The type of match. Should be one of: qualifications, quarterfinals, semifinals, finals, none. Reference: [MatchType.java](http://code.google.com/p/frcdb/source/browse/src/main/java/net/frcdb/api/game/match/MatchType.java)
    * **number:** _(int)_ The match number.
    * **time:** _(string)_ The time (local to the event) that the match took place, such as "10:30 AM"
    * **redScore:** _(int)_ The score of the red alliance.
    * **redTeams:** _(int array)_ A list of teams on the red alliance.
    * **blueScore:** _(int)_ The score of the blue alliance.
    * **blueTeams:** _(int array)_ A list of teams on the blue alliance.
    * **winner:** _(string)_ The alliance that won this match. Should be one of: red, blue, tie. Reference: [Alliance.java](http://code.google.com/p/frcdb/source/browse/src/main/java/net/frcdb/api/game/match/Alliance.java)

This function directly represents an internal [Game](http://code.google.com/p/frcdb/source/browse/src/main/java/net/frcdb/api/game/event/Game.java) object, and holds all information for a particular year of some event. As scoring varies from year to year, some properties, particularly in team entries and standings, may not be available. Internally, our parser uses templates based on the game year to handle property mapping.

### Basic stats ###

Some very basic database stats can be found at http://frcdb.net/json/stats and currently look like:

```
{
	"teams":2245,
	"events":65
}
```

Fields here:
  * **teams:** The number of teams contained in the database
  * **events:** The number of events (not including games) in the database

Though basic, these stats are useful for enabling the use of progress bars for applications parsing the data.

## Language Support ##

We have basic support for a few different programming languages. For anything not listed here, take a look at the comprehensive list at http://json.org/

### Java ###

Although no official API exists at this time, many libraries exist to make the use of the raw JSON functions easy.

  * [Jackson](http://jackson.codehaus.org/) is the library we use to export our data, and is also very capable of importing data. For a sample implementation, see our [import/export code](http://code.google.com/p/frcdb/source/browse/#git%2Fsrc%2Fmain%2Fjava%2Fnet%2Ffrcdb%2Fexport)
  * Android has [built-in support](http://developer.android.com/reference/org/json/package-summary.html) for parsing JSON data, though it isn't as intuitive as libraries like Jackson.