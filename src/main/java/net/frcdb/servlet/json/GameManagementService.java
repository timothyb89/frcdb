package net.frcdb.servlet.json;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import net.frcdb.export.GamesImport;
import net.frcdb.util.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A little hacky management service...
 * @author tim
 */
@Path("/admin/game")
public class GameManagementService {
	
	public static final Pattern CRAPPY_GAME_FORMAT = Pattern.compile(
			"([\\w\\-]+)\\=(\\d+)");
	
	private Logger logger = LoggerFactory.getLogger(GameManagementService.class);
	
	@POST
	@Path("/modify")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse modifyGame(
			@FormParam("shortName") String shortName,
			@FormParam("gameYear") int gameYear,
			@FormParam("eid") String eid,
			@FormParam("startDate") String startDate,
			@FormParam("endDate") String endDate,
			@FormParam("resultsURL") String resultsURL,
			@FormParam("standingsURL") String standingsURL,
			@FormParam("awardsURL") String awardsURL,
			@FormParam("parent") String parentName,
			@FormParam("children") String children) {
		
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to create events.");
		}
		
		Event e = Database.getInstance().getEventByShortName(shortName);
		if (e == null) {
			return JsonResponse.error("Event not found: " + shortName);
		}
		
		Game g = e.getGame(gameYear);
		if (g == null) {
			return JsonResponse.error("No game for year " + gameYear);
		}
		
		String messageExtra = "";
		
		if (crappyValidate(eid)) {
			g.setEid(Integer.parseInt(eid));
		}
		
		if (crappyValidate(startDate)) {
			// todo
		}
		
		if (crappyValidate(endDate)) {
			// todo
		}
		
		if (crappyValidate(resultsURL)) {
			g.setResultsURL(resultsURL);
		}
		
		if (crappyValidate(standingsURL)) {
			g.setStandingsURL(standingsURL);
		}
		
		if (crappyValidate(awardsURL)) {
			g.setAwardsURL(awardsURL);
		}
		
		if (parentName != null) {
			Game newParent;
			
			// allow an empty string to remove the parent
			if (parentName.isEmpty()) {
				newParent = null;
			} else {
				newParent = getGame(parentName);
				if (newParent == null) {
					return JsonResponse.error(
							"Game not found or invalid format specified.");
				}
			}
			
			// remove the old parent if needed
			Game oldParent = g.getParent();
			if (oldParent != null) {
				oldParent.removeChild(g);
				Database.save().entity(oldParent);
			}
			
			g.setParent(newParent);
			
			if (newParent != null) {
				newParent.addChild(g);
				Database.save().entity(newParent);
			}
		}
		
		if (children != null) {
			String[] gameStrings = StringUtils.split(children, ',');
			
			List<Game> finalGames = new ArrayList<Game>();
			
			for (String gameString : gameStrings) {
				Game game = getGame(gameString);
				
				if (g.containsChild(game)) {
					finalGames.add(game);
					// skip
				} else {
					if (game.getParent() != null) {
						// other game is already parented. warn and skip
						messageExtra += gameString
								+ " already has a parent, skipped. ";
					} else {
						game.setParent(g);
						Database.save().entity(game);
						
						g.addChild(game);
						finalGames.add(game);
					}
				}
			}
			
			// diff the games to prune removed
			List<Game> removedGames = new ArrayList<Game>();
			removedGames.addAll(g.getChildren());
			removedGames.removeAll(finalGames);
			
			// remove the diff
			for (Game removed : removedGames) {
				g.removeChild(removed);
				
				removed.setParent(null);
				Database.save().entity(removed);
			}
		}
		
		Database.save().entity(g);
		return JsonResponse.success("Game updated. " + messageExtra);
	}
	
	private boolean crappyValidate(String s) {
		return s != null && !s.isEmpty();
	}
	
	private Game getGame(String crappyName) {
		Matcher m = CRAPPY_GAME_FORMAT.matcher(crappyName);
		
		if (!m.matches()) {
			return null;
		}
		
		String name = m.group(1);
		Event event = Database.getInstance().getEventByShortName(name);
		if (event == null) {
			return null;
		}
		
		int year = Integer.parseInt(m.group(2));
		
		return event.getGame(year);
	}
	
	@POST
	@Path("/import")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public JsonResponse importGames(FormDataMultiPart form) throws IOException {
		// writing zip file contents to blobstore:
		// http://stackoverflow.com/a/10137086/549639
		
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to import games.");
		}
		
		FormDataBodyPart field = form.getField("file");
		BodyPartEntity entity = (BodyPartEntity) field.getEntity();
		
		ZipInputStream zis = new ZipInputStream(entity.getInputStream());
		
		// extract zip files to the blobstore and create tasks to parse them
		FileService service = FileServiceFactory.getFileService();
		Queue queue = QueueFactory.getQueue("import");
		
		int count = 0;
		
		List<String> keys = new ArrayList<String>();
		
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				continue;
			}
			
			// extract the file to the blobstore
			
			AppEngineFile file = service.createNewBlobFile(
					"application/json", entry.getName());
			
			FileWriteChannel channel = service.openWriteChannel(file, true);
			byte[] buffer = new byte[BlobstoreService.MAX_BLOB_FETCH_SIZE];
			
			int len;
			while ((len = zis.read(buffer)) >= 0) {
				ByteBuffer bb = ByteBuffer.wrap(buffer, 0, len);
				channel.write(bb);
			}
			
			channel.closeFinally();
			
			keys.add(service.getBlobKey(file).getKeyString());
			
			count++;
		}
		
		// queue later to make the request return faster
		// writing the files while parsing causes problems
		
		for (String key : keys) {
			// queue it
			queue.add(withUrl("/json/admin/game/import-task")
					.method(TaskOptions.Method.POST)
					.countdownMillis(1000)
					.param("key", key));
		}
		
		return JsonResponse.success("Queued " + count + " events for import.");
	}
	
	@POST
	@Path("/import-task")
	public Response importTask(@FormParam("key") String key) {
		BlobstoreService s = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey bk = new BlobKey(key);
		
		try {
			BlobstoreInputStream stream = new BlobstoreInputStream(bk);
			
			// import
			GamesImport i = new GamesImport(stream);

			// clean up
			stream.close();
			
			FileService service = FileServiceFactory.getFileService();
			AppEngineFile file = service.getBlobFile(bk);
			service.delete(file);
			
			return Response.ok("Imported " + i).build();
		} catch (Exception ex) {
			logger.error("Failed to import game.", ex);
			return Response
					.serverError()
					.entity("Import failed: " + ex.getMessage())
					.build();
		}
	}
	
}
