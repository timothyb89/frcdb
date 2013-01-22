package net.frcdb.stats.calc;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import java.io.IOException;
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
			AppEngineFile old = service.getBlobFile(new BlobKey(oldKey));
			try {
				service.delete(old);
			} catch (Exception ex) {
				logger.error("Failed to delete old team cache", ex);
			}
		}
		
		try {
			AppEngineFile file = service.createNewBlobFile("application/json");
			
			FileWriteChannel channel = service.openWriteChannel(file, true);
			
			JsonGenerator g = factory
					.createJsonGenerator(Channels.newWriter(channel, "UTF8"));
			
			g.writeStartArray();
			for (Team t : Database.getInstance().getTeams()) {
				JSONUtil.exportTeam(g, t);
			}
			g.writeEndArray();
			
			g.close();
			channel.closeFinally();
			
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
