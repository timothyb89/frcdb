package net.frcdb.stats.calc;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.stats.StatisticsRoot;
import net.frcdb.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class TeamCacheGenerator implements GlobalStatistic {

	private Logger logger = LoggerFactory.getLogger(TeamCacheGenerator.class);
	
	private JsonFactory factory = new JsonFactory();
	
	/**
	 * The amount of memory to preallocate. This is needed to keep the time to
	 * write to the blobstore under 30 seconds, as longer write times will time
	 * out.
	 */
	public static final int BUFFER_SIZE = 10 * 1024 * 1024;
	
	@Override
	public String[] getNames() {
		return new String[] { "teamcachegenerator", "teamcache" };
	}
	
	@Override
	public void calculate() {
		FileService service = FileServiceFactory.getFileService();
		
		// delete the old cache
		String oldKey = StatisticsRoot.get().getTeamsKey();
		if (oldKey != null) {
			logger.info("Deleting old team cache");
			AppEngineFile old = service.getBlobFile(new BlobKey(oldKey));
			try {
				service.delete(old);
			} catch (Exception ex) {
				logger.error("Failed to delete old team cache", ex);
			}
		}
		
		try {
			// output to memory initially
			ByteArrayOutputStream buffer = new ByteArrayOutputStream(BUFFER_SIZE);
			JsonGenerator g = factory.createJsonGenerator(buffer);
			
			logger.info("Generating team json in memory...");
			
			g.writeStartArray();
			for (Team t : Database.getInstance().getTeams()) {
				JSONUtil.exportTeam(g, t, false);
				Database.ofy().clear();
			}
			g.writeEndArray();
			
			g.close();
			
			logger.info("Writing team json to the blobstore");
			
			AppEngineFile file = service.createNewBlobFile("application/json");
			FileWriteChannel channel = service.openWriteChannel(file, true);
			
			// write to the  file
			OutputStream out = Channels.newOutputStream(channel);
			buffer.writeTo(out);
			out.close();
			channel.closeFinally();
			
			// save the file key
			BlobKey key = service.getBlobKey(file);
			StatisticsRoot r = StatisticsRoot.get();
			r.setTeamsKey(key.getKeyString());
			Database.save().entity(r);
		} catch (IOException ex) {
			logger.error("Team cache writing failed", ex);
		}
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
