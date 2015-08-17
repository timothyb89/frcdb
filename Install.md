As of version 2.1, FRC-DB can be set up without too much hassle using App Engine's development server. This can be useful to run FRC-DB completely offline, or to work with the code and create patches.

## 1. Environment Setup ##
FRC-DB requires at least JDK 6 and [Apache Maven](http://maven.apache.org) to build and run. [NetBeans](http://netbeans.org) version 7 or later will be able to work with the project natively as it contains a bundled copy of Maven, and will also help to ensure a proper JDK is installed.

## 2. Get the code ##
The latest code can always be obtained from the git repository:
```
git clone http://code.google.com/p/frcdb/
```

## 3. Install the App Engine SDK ##
The SDK can be installed to your local maven repository automatically, which will also help avoid the need to muck about with configuring a path. To do this, execute:
```
mvn gae:unpack
```

The required version will be downloaded and configured automatically. If using NetBeans, you can just run the custom maven target `gae:unpack`

## 4. Run the development server ##
To run the development server, execute:
```
mvn gae:run
```

The development server will be started at http://localhost:8181/

If using NetBeans, the standard Run (F6) action is configured to execute this same task.

## 5. Data import ##
With the dev server running, browse to http://localhost:8181/ and log in. The username doesn't matter so long as you select the "log in as administrator" checkbox. Once logged in, browse to the admin panel. You should fine a list of available actions.

All of the relevant premade files necessary for import can be found in the Downloads section above. Note that you must import the data in a particular order to avoid errors.

### 5.1. Import teams ###
Data dumps contain a teams.json file, which is alternatively available (and potentially more recent) at http://www.frcdb.net/teams.json. This file can be directly uploaded via the "Import teams" button.

The import process can take anywhere from 30 seconds to 5 minutes. As the task is queued to run in the background, progress can only be monitored from the terminal.

If deploying to the actual App Engine, you may need to split the teams into smaller chunks to avoid and lessen the impact of errors. Our import process used 500-team chunks as there were issues importing the whole list (~2715 teams). In order to split the teams.json, you can use the [TeamsRedump](http://code.google.com/p/frcdb/source/browse/src/main/java/net/frcdb/export/TeamsRedump.java) utility. Place the file to be split in export/teams.json (relative to the project root directory) and execute the file from NetBeans. New .json files will be generated in export/teams/teams-xyz.json which can then be uploaded individually.

The team importer is somewhat resilient and can be run again after a failure and it will attempt to skip teams already imported. If there are still errors, particularly game imports failing completely due to missing teams, you can execute:
```
mvn clean:clean
```

or use NetBeans' "Clean and Build" option to reset local data, and start over.

### 5.2. Event import ###
Simply upload the events.json found in the data dump, or use the latest http://www.frcdb.net/events.json

Note that teams and events can be imported in any order. However, all teams and all events must be imported before starting game import.

Note that if the event importer encounters a preexisting event with a matching short name, the data from the imported event will overwrite the internally stored data. This allows for use of the [event merge tool](EventMerging.md), which is used to handle event updates and new game creation between years.

### 5.3. Game Import ###
Game import is a bit trickier than team and event import, due to the particularly large amount of data to process. Imports must be a series of .json files, preferably 1 for each game or event-year, and must be contained in a .zip file. You can create a compatible zip file simply by compressing the .json files found in the `games` directory of a data dump. The structure for the zip must look something like the following:
```
games.zip
 |- alamo-2010.json
 |- alamo-2011.json
 | ...
```

We imported games in this manner 5 at a time, but it shouldn't cause many problems, other than quotas, to import all 188 (or so) at once. The import process can be monitored from the terminal or from the task queue admin panel at http://localhost:8181/_ah/admin/ and can take about a half hour to process all of the games.

As with Events, the game import process is somewhat flimsy and will cause issues if you try to import the same game twice. Issues that may be encountered are mainly related to missing teams. If a team can't be found while importing a team entry, standing, or match, the import for that entire entity will fail. Generally this is mostly harmless, but occasionally - for standings in particular - this can cause an entire game to fail to import and leave a large mess in the database. The usual cause of this is related to inconsistencies in the data provided by FIRST, such that teams are occasionally missing from the official team lists.

Also note that a large number of errors may occur on events with delayed team lists, such as the Michigan Championship and other district championships, and in particular the FIRST Championship. These can generally be ignored without the import failing. The Championship event is actually a special case that we currently handle improperly: it's a parent event to the four different fields (curie, archimedes, newton, galileo), contains no actual matches, but _does_ contain standings and (very often incorrect) team attendance lists. The current bad workaround is to separate the four fields into their own events, but in the future we hope to merge them a bit more cleanly.