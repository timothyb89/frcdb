# FRC-DB Development #

This site is for all things related to the development of FRC-DB, including background information, bug reports, feature requests, and other resources.

We're currently in the process of migrating everything to the Google App Engine and Google Code, please bear with us during the transition.

## What is FRC-DB? ##

FRC-DB is a dynamic database of FRC teams, events, and robots intended to help with scouting by showing useful statistics for teams and events. It consists of:
  * A [Web Interface](WebInterface.md), to display information and statistics;
  * A [Crawler](Crawler.md), to gather information from [various sources](DataSources.md);
  * A series of [Algorithms](Algorithms.md), to calculate the statistics;
  * A [Database](Database.md), to store gathered information; and
  * A public [API](API.md) to let others work with the data.

## Can I see it in action? ##
Head on over to http://frcdb.net/! You can also [install a local copy](Install.md) of FRC-DB if you'd like.

## News ##

  * **January 10th, 2012**: We're back up and running!
  * **January 2nd, 2012**: A new data dump containing all information 2010-2012 is available in the downloads section.
  * **December 27th, 2012**: Work towards the port to the appengine is progressing quickly and should be finished within a week!

## FAQs ##

### What tools/languages were used to make FRC-DB? ###

It is 100% pure Java and HTML (and). As for libraries it's built on, we use:
  * The [Google App Engine](https://developers.google.com/appengine/) for hosting;
  * [Apache Tiles](http://tiles.apache.org/), to handle layout and theming;
  * [jsoup](http://jsoup.org) as the base for the crawler;
  * [Jama](http://math.nist.gov/javanumerics/jama/) to handle matrix math;
  * [Apache Commons Math](http://commons.apache.org/math/) for statistics calculations;
  * [jQuery](http://jquery.com/) for JavaScript additions, with plugins:
    * [dataTables](http://dataTables.net/) - for table sorting and filtering;
    * [jQuery UI](http://jqueryui.com/) - for theming the tables
  * [Jackson](http://jackson.codehaus.org) for JSON export and parsing;
  * [Objectify](https://code.google.com/p/objectify-appengine/) as a database abstraction layer; and
  * [NetBeans](http://netbeans.org) and [Maven](http://maven.apache.org) for development

Current stable version: 2.1beta
Current testing version: n/a

### ...Help? ###

Most support requests should be directed to either the issues section above, or brought to our attention through IRC (#robotics on irc.timothyb89.org) or email.

If you just want to report a bug or file a feature request, click the "Issues" link above and file a new issue. We'll get to it as soon as we can, but it helps if we're notified about it somehow (via IRC or email, for example).

If needed, you may also contact the developers directly:
  * Tim Buckley / timothyb89 - timothyb89@gmail.com

### What does FRC-DB's future look like? ###

We're currently working on refining our data and adding support for user-provided content. Currently this includes image uploads and links to YouTube videos, and will soon be expanded to include the addition of detailed team robot information. We're also in the process of expanding the database to include data from more years, which has mostly been completed.

Additionally, we're working on an Android app for both viewing and editing the database to make it much more useful during the competition.

### How is FRC-DB licensed? ###

It's licensed under the "GPLv3":http://www.gnu.org/copyleft/gpl.html. Make sure to direct all licensing-related questions to the IRC channel or email.

### Can I get a copy of the database? ###
Yes, you can get data dumps in the downloads section. These are generally JSON-formatted and can be used to initialize a blank, local copy of frc-db or for your own analyses. They can be found in the downloads section above.

If you'd like to run a local copy of the database, you can generate a copy of it yourself fairly quickly using the local dev server. Grab the code from the git repository and run:
```
$ mvn gae:run
```
For NetBeans users, you can just open the project in a recent version of NetBeans and hit the run button as the run target should be automatically remapped for you.

For more information on setting up a local copy of FRC-DB, see [Install](Install.md).