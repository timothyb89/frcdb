package net.frcdb.stats.calc;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.stats.StatisticsRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates a sitemap xml file and saves it to the blobstore. This is a rather
 * expensive process and should be used carefully!
 * @author tim
 */
public class SitemapGenerator implements GlobalStatistic {

	private Logger logger = LoggerFactory.getLogger(SitemapGenerator.class);
	
	public static final String URL_PREFIX = "http://www.frcdb.net";
	
	public static final String TEAM_PRIORITY = "0.9";
	public static final String CURRENT_EVENT_PRIORITY = "1.0";
	public static final String NORMAL_EVENT_PRIORITY = "0.9";
	public static final String OTHER_PRIORITY = "0.5";
	
	@Override
	public String[] getNames() {
		return new String[] {"sitemapgenerator", "sitemap"};
	}
	
	@Override
	public void calculate() {
		FileService service = FileServiceFactory.getFileService();
		
		// delete the old sitemap
		String oldKey = StatisticsRoot.get().getSitemapKey();
		if (oldKey != null) {
			AppEngineFile old = service.getBlobFile(new BlobKey(oldKey));
			try {
				service.delete(old);
			} catch (Exception ex) {
				logger.error("Failed to delete old sitemap", ex);
			}
		}
		
		try {
			AppEngineFile file = service.createNewBlobFile("application/xml");
			
			FileWriteChannel channel = service.openWriteChannel(file, true);
			PrintWriter out = new PrintWriter(Channels.newWriter(channel, "UTF8"));
			
			generate(out);
			
			out.close();
			channel.closeFinally();
			
			BlobKey key = service.getBlobKey(file);
			StatisticsRoot r = StatisticsRoot.get();
			r.setSitemapKey(key.getKeyString());
			Database.save().entity(r);
		} catch (IOException ex) {
			logger.error("Sitemap writing failed", ex);
		}
	}

	private void generate(PrintWriter out) {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		out.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
		
		// teams
		for (Team t : Database.getInstance().getTeams()) {
			writeUrl(out, "/team/" + t.getNumber(), "weekly", TEAM_PRIORITY);
		}
		
		// event-years - current events have a higher priority
		for (Event e : Database.getInstance().getEvents()) {
			for (Game g : e.getGames()) {
				String priority;
				if (g.getGameYear() == Event.CURRENT_YEAR) {
					priority = CURRENT_EVENT_PRIORITY;
				} else {
					priority = NORMAL_EVENT_PRIORITY;
				}
				
				writeUrl(out,
						"/event/" + e.getShortName() + "/" + g.getGameYear() + "/",
						"weekly", priority);
			}
		}
		
		out.println("</urlset>");
	}
	
	private void writeUrl(PrintWriter out,
			String relLoc, String changefreq, String priority) {
		out.println("\t<url>");
		
		out.print("\t\t<loc>");
		out.print(URL_PREFIX);
		out.print(relLoc);
		out.print("</loc>\n");
		
		if (changefreq != null) {
			out.print("\t\t<changefreq>");
			out.print(changefreq);
			out.print("</changefreq>\n");
		}
		
		if (priority != null) {
			out.print("\t\t<priority>");
			out.print(priority);
			out.print("</priority>\n");
		}
		
		out.println("\t</url>");
	}
	
	@Override
	public String getBackendName() {
		return null;
	}
	
}
