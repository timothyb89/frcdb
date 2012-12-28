package net.frcdb.content.video;

import com.googlecode.objectify.annotation.Id;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;
import net.frcdb.content.MRContent;

/**
 *
 * @author tim
 */
public class YouTubeVideo extends MRContent {
	
	@Id
	private Long internalId;
	
	private String id;
	
	private String videoId;
	private String title;
	private String description;
	private String thumbnail;
	private String thumbnailHires;
	
	private int views;
	private int favorites;
	private int likes;
	private int dislikes;

	public YouTubeVideo() {
		// create a UUID
		id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getContentType(String extra) {
		if (extra != null && extra.equalsIgnoreCase("thumbnail")) {
			return "image/jpeg";
		} else {
			return "text/html";
		}
	}

	@Override
	public void serve(OutputStream out, String extra) throws IOException {
		if (extra != null && extra.equalsIgnoreCase("thumbnail")) {
			// TODO: store this locally? some abuse potential here...
			URL url = new URL(thumbnail);
			InputStream in = url.openStream();
		
			byte[] buf = new byte[1024];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}

			in.close();
			out.close();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html>\n");
			sb.append("<html>\n");
			sb.append("  <head>\n");

			sb.append("    <title>");
			sb.append(title);
			sb.append("</title>\n");

			sb.append("  </head>\n");
			sb.append("  <body>\n");

			sb.append("    <iframe type=\"text/html\" ");
			sb.append("width=\"640\" height=\"385\" ");
			sb.append("src=\"http://www.youtube.com/embed/");
			sb.append(videoId);
			sb.append("\" frameborder=\"0\">");
			sb.append("</iframe>\n");

			sb.append("  </body>\n");
			sb.append("</html>\n");
			
			out.write(sb.toString().getBytes());
		}
	}
	
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getThumbnailHires() {
		return thumbnailHires;
	}

	public void setThumbnailHires(String thumbnailHires) {
		this.thumbnailHires = thumbnailHires;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getFavorites() {
		return favorites;
	}

	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	
}
