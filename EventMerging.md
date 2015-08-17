# Introduction #

With the move to the App Engine, we no longer do fully server-side event additions. Due to the large amount of temporary space and user input required, a desktop application was developed to bypass the complexity of handling the event adding and merging process on the App Engine.

The merge tool allows for semi-intelligent merging of known FRC-DB events and new yearly FIRST event definitions. FIRST events can then either be linked to existing FRC-DB events, or added as new events. Game definitions for all new events are also added as needed. When finished, the tool generates a series of .json files which can be uploaded via the administrator console.

![http://wiki.frcdb.googlecode.com/git/eventmanager.png](http://wiki.frcdb.googlecode.com/git/eventmanager.png)

## Downloading the Event Manager ##

The merge tool is available under the `tools/eventmanager` directory. It is a maven project and can be built and run with:
```
$ mvn assembly:single
$ java -jar target/FRCDBEventManager-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Basic Usage ##
On startup, the merge tool will ask for data URLs from FRC-DB and FIRST. Data will then be fetched and compared, with FIRST data being matched as accurately as possible with known FRC-DB events.

Initially, events with a value in the "short name" column were automatically paired with an existing FRC-DB event. Events with a missing value for the short name were not paired and will either need to be assigned to an existing FRC-DB event, or given a short name and added as a new event.

## Merging Events ##

### Adding New Events ###

_**Note:** completely new events cannot currently be added with the event manager application. Instead, use the web interface which is accessible from the FRC-DB admin panel._

Creating a new event from an unpaired FIRST definition only requires setting of the short name field to a unique and compatible short name. Generally speaking, this means implies all lower case (and possibly hyphenated) text. While this is not strictly enforced, it should match the following regex:
```
[a-z\-]+
```

To set the short name, select the desired event to open the merge options panel. Then, enter the desired short name for the event in the "Edit Short Name" field, and press the "Modify" button. Note that this field is different than the "Set FRC-DB Event" field, which is used to manually pair an event with an existing FRC-DB event, rather than create a new event.



### Manually Pairing Events ###



If for some reason automatic event pairing fails, you can pair events individually via the merge options panel. Specifically, there are two methods for pairing events manually:

  1. Direct: in the "Set FRC-DB Event" field, enter the preexisting FRC-DB short name to pair with
  1. Search: Use the "Search Event" button to query a list of "best guesses". In most cases the first result will be the proper event.

In the event that an event was improperly paired, you can either set a new pairing using either of the above methods, or use the "Clear FRC-DB Link" button to delete the pairing and start from scratch.

_**Note:** In 2013, FIRST changed the identifiers for all events from a two character state ID to a 4 character state/city ID. As the automatic pairing is based on this identifier, pairing against an FRC-DB instance using the 2012 data dump will require manual pairing of every old event._

### Setting Data Preferences ###
FIRST has been known to helpfully include, among other things, invalid characters, misspellings, and other typos in event names. If FRC-DB is known to have more accurate data for a particular event, you can suppress the default FIRST-preferring behavior by selecting the "Prefer FRC-DB" button.

Data may also be corrected for individual fields by double clicking on the associated table cell. If the cell is editable, your changes will be reflected during export.

## Game Generation ##
By default the merge tool creates game definitions for each event. Use the checkboxes in the "New Game" column to specify whether a game definition for each particular event will be generated.

## Exporting Data ##
To export the generated data, use the "Export" option found in the File menu, and select a directory to place generated files in. The export process will generate an `events.json` file, as well as a `games` directory containing all generated game definitions.

For events, note that only events with at least one different field be included. Thus, if you run the event manager against a copy of FRC-DB that is fully up-to-date with FIRST, the generated `events.json` may be empty, as no updates were needed.

To import the updated `events.json`, just the event importer on the FRC-DB admin panel. Events will be updated or added as needed.

To import games, add all of the .json files found under the `games` directory to a .zip file. This .zip file can then be imported via the games importer found in the admin panel.

## Syntax and Formatting Overview ##
Short name field:
  * Automatically paired: normal text
  * New event: **bold text**
  * Unknown event: _empty_