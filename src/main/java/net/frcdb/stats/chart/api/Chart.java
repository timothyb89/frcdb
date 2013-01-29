package net.frcdb.stats.chart.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Parent;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.List;
import java.util.UUID;
import net.frcdb.db.Database;
import net.frcdb.stats.chart.ChartRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Chart class capable of producing JSON that can be passed directly to the
 * ChartWrapper class in the Google Chart API. These charts are cached in the
 * blobstore and can be updated by calling generate() again. The 'containerId'
 * field will not be set - this can be safely added by the client.
 * @author tim
 */
@Entity
public abstract class Chart {
	
	public static final String TYPE_AREA = "AreaChart";
	public static final String TYPE_BAR = "BarChart";
	public static final String TYPE_BUBBLE = "BubbleChart";
	public static final String TYPE_CANDLESTICK = "CandlestickChart";
	public static final String TYPE_COLUMN = "ColumnChart";
	public static final String TYPE_COMBO = "ComboChart";
	public static final String TYPE_GAUGE = "Gauge";
	public static final String TYPE_GEO = "GeoChart";
	public static final String TYPE_LINE = "LineChart";
	public static final String TYPE_PIE = "PieChart";
	public static final String TYPE_SCATTER = "ScatterChart";
	public static final String TYPE_STEPPEDAREA = "SteppedAreaChart";
	public static final String TYPE_TABLE = "Table";
	public static final String TYPE_TREEMAP = "TreeMap";
	
	@Ignore
	private Logger logger = LoggerFactory.getLogger(Chart.class);
	
	@Id
	private String id;
	private String key;
	
	@Parent
	private Key<ChartRoot> parent;
	
	public Chart() {
		parent = Key.create(ChartRoot.get());
		do {
			id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
		} while (!checkCollisions());
	}
	
	private boolean checkCollisions() {
		return Database.getInstance().getChart(id) == null;
	}

	/**
	 * Gets the id for this chart. 
	 * @return 
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the blobstore key for this chart. This can be used to serve the 
	 * cached chart data. This value will be null if the chart has not been
	 * generated yet.
	 * @return the key, or null if none exists
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public abstract String getName();
	public abstract String getDisplayName();
	public abstract String getChartType();
	
	public abstract ColumnDefinition[] getColumns();
	public abstract List<Row> getRows();
	
	/**
	 * Writes chart options. These are passed directly to the underlying 
	 * Google Chart API.
	 * @param g the current JsonGenerator
	 * @throws IOException 
	 */
	public void writeOptions(JsonGenerator g) throws IOException {
		// no options by default
	}
	
	public void generate() {
		delete(); // delete the old chart if needed
		
		FileService service = FileServiceFactory.getFileService();
		try {
			AppEngineFile file =
					service.createNewBlobFile("application/json", getName());
			FileWriteChannel channel = service.openWriteChannel(file, true);
			
			JsonFactory f = new JsonFactory();
			JsonGenerator g = f.createJsonGenerator(
					Channels.newWriter(channel, "UTF-8"));
			g.useDefaultPrettyPrinter();
			
			g.writeStartObject();
			
			g.writeStringField("chartType", getChartType());
			
			ColumnDefinition[] columns = getColumns();
			g.writeArrayFieldStart("columns");
			for (ColumnDefinition c : columns) {
				c.serialize(g);
			}
			g.writeEndArray();
			
			g.writeArrayFieldStart("rows");
			for (Row r : getRows()) {
				r.serialize(g);
			}
			g.writeEndArray();
			
			g.writeObjectFieldStart("options");
			writeOptions(g);
			g.writeEndObject();
			
			g.writeEndObject();
			
			g.close();
			channel.closeFinally();
			
			key = service.getBlobKey(file).getKeyString();
		} catch (Exception ex) {
			logger.error("Failed to generate chart", ex);
		}
	}
	
	protected void writeStringRow(JsonGenerator g, String... cols)
			throws IOException {
		g.writeStartArray();
		
		for (String s : cols) {
			g.writeString(s);
		}
		
		g.writeEndArray();
	}
	
	protected ColumnBuilder column() {
		return new ColumnBuilder();
	}
	
	/**
	 * Creates a new Row containing the given values. The values should either
	 * be primitives (string/number) or CellValues. Use a LiteralCellValue to
	 * provide different formatted (tooltip) and cell values.
	 * @param values the values for the row
	 * @return 
	 */
	protected Row row(Object... values) {
		Row ret = new Row();
		
		for (Object o : values) {
			if (o instanceof CellValue) {
				CellValue cv = (CellValue) o;
				ret.addValue(cv);
			} else {
				ret.addValue(o);
			}
		}
		
		return ret;
	}
	
	/**
	 * Deletes the cached version of this chart.
	 */
	public void delete() {
		if (key == null) {
			return;
		}
		
		FileService service = FileServiceFactory.getFileService();
		AppEngineFile old = service.getBlobFile(new BlobKey(key));
		try {
			service.delete(old);
		} catch (Exception ex) {
			logger.error("Failed to delete chart", ex);
		}
	}

}
