Various data elements may include references to `chartId`s - 16 character hexadecimal identifiers pointing to a unique chart. Our particular chart implementation is made to be compatible with the [Google Charts API](https://developers.google.com/chart/) but the JSON data rendered in the browser can be parsed fairly easily for use with any charting library.

## API Overview ##
The API is simple enough to use. Chart data can be accessed via:
```
http://www.frcdb.net/chart/[chartId]
```

...where the chartId is the 16-character hex id for some chart. For example, the following points to a sample chart found on the [stats page](http://www.frcdb.net/stats):
```
http://www.frcdb.net/chart/90a75c45971a3c5a
```

(Note that chartIds will not generally change even if the data contained in them is updated)

Fields:
  * **chartType:** The Google Charts API-compatible class name for the chart type. For example, `ScatterChart`, `LineChart`, `BubbleChart`, `ColumnChart`, etc.
  * **columns:** An array of columns defined in the chart.
    * **label:** (_required_) The column label, shown in the legend or as the title row in a data table.
    * **type:** (_required_) The JavaScript-compatible datatype. For example, `string`, `number`, `date`, ...
    * **role:** The role of the column. Google Charts API specific but useful for other clients as well. We generally use it to specify `tooltip` columns.
  * **rows:** An array of rows. Each row contains data in the same order as the columns
  * **options:** options passed directly to the Google Charts API. We should always include `title`, `hAxis.title`, and `vAxis.title` though this is not a strict requirement.

### Usage Notes ###
Unlike the main API, all charts are cached. While clients should still try to cache locally if possible, there should be no harm in polling them as frequently as is required.

### Google Charts API Specifics ###
We have a small client-side utility to handle chart data formatting, [jquery.chart.js](http://code.google.com/p/frcdb/source/browse/src/main/webapp/js/jquery/jquery.chart.js). It mainly handles creation of the DataTable and graph, but also handles some basic data conversion as well.

#### Date Conversion ####
As there's no way to natively represent dates in JSON, the server exports the data as a 64bit millisecond date. `jquery.chart.js` checks for date columns, and then converts dates in each row to the native JavaScript `Date` object which the Charts API accepts.

### Extra Chart Types ###
#### Timelines ####
To display timelines, we use the 3rd-party [Timeline plugin](http://almende.github.com/chap-links-library/timeline.html) from the [CHAP Links Library](http://almende.github.com/chap-links-library/). It's compatible with the Google Charts API and mainly requires a special loading of the library class in `jquery.chart.js`.

[Example](http://www.frcdb.net/chart/90adc46a7c39a935) of the 2013 event timeline found on the FRC-DB homepage.