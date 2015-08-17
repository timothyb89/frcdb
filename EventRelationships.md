# Introduction #

Event relationships define a semantic parent-child link between two events. Primarily, this is used by events like the [FIRST Championship](http://www.frcdb.net/event/championship) to give clients a cue that more data may be available on other pages for the particular event.


## Editing Relationships ##

Relationships can be edited for any game when logged in as an administrator. To modify, navigate to the game you wish to modify and select "Edit Game". The fields of interest in the resulting dialog are "parent game" and "child games".

Relationships are two-way and can be created or removed from either the parent or child event. The syntax is:
```
shortName=year
```

Where `shortName` is the event short name, and `year` is the year to link to. If editing the "child games" field, you can specify a comma-delimited list of tokens using the syntax above, such as:
```
archimedes=2012,curie=2012,galileo=2012,newton=2012
```

Saving on the dialog will commit the changes as needed. To remove a parent or child, simply remove the token or clear the field, and save. Refresh the page to see the changes.

When making a change to either the parent or child event, the change will actually be applied to both events. Thus, there's no need to manually set both the parent and child relationships - one or the other will suffice.

## Viewing Relationships ##
Relationships manifest themselves primarily as a list of links to child events or a single link back to the parent event, if any. An example of this can be seen on the [Championship](http://www.frcdb.net/event/championship) page, or one the championship fields, such as [Curie](http://www.frcdb.net/event/curie).

This relationship is also exposed via the JSON API in the form of `parent` and `children` fields:
```
{
  "gameYear" : 2013,
  "gameName" : "Ultimate Ascent",
  "eventShortName" : "championship",
  "parent" : null,
  "children" : [ {
    "name" : "archimedes",
    "year" : 2013
  }, {
    "name" : "curie",
    "year" : 2013
  }, {
    "name" : "galileo",
    "year" : 2013
  }, {
    "name" : "newton",
    "year" : 2013
  } ]
  // ...
}
```