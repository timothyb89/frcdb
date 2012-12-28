package net.frcdb.content;

import com.googlecode.objectify.annotation.Id;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Represents an image that can be both moderated and rated.
 * @author tim
 */
public class MRImage extends MRContent implements CImage {

	@Id
	private Long internalId;
	
	private String id;
	private String path;
	private String mediumPath;
	private String thumbnailPath;
	private String contentType;
	
	/**
	 * Creates a new pathless MRImage. File uploads, for example, should use
	 * this constructor to generate a content ID, and save the file.
	 */
	public MRImage() {
		// create a UUID
		id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
		
		// todo: check for id collisions? shouldn't be necessary but...
	}
	
	public MRImage(String path) {
		this.path = path;
		
		// create a UUID
		id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getContentType(String pathExtra) {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getMediumPath() {
		return mediumPath;
	}

	@Override
	public void setMediumPath(String mediumPath) {
		this.mediumPath = mediumPath;
	}

	@Override
	public String getThumbnailPath() {
		return thumbnailPath;
	}

	@Override
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	
	@Override
	public void serve(OutputStream out, String extra) throws IOException {
		String p;
		
		if (extra != null) {
			extra = extra.toLowerCase();
			
			if (extra.equals("small") || extra.equals("thumbnail")) {
				p = thumbnailPath;
			} else if (extra.equals("medium")) {
				p = mediumPath;
			} else if (extra.equals("large")) {
				p = path;
			} else {
				p = thumbnailPath;
			}
		} else {
			p = thumbnailPath;
		}
		
		File file = new File(p);
		if (!file.exists()) {
			throw new IllegalArgumentException("File not found");
		}
		
		if (!file.canRead()) {
			throw new IllegalArgumentException("Unable to read file");
		}
		
		// stream file to client
		
		FileInputStream in = new FileInputStream(file);
		
		byte[] buf = new byte[1024];
		int count = 0;
		while ((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		
		in.close();
		out.close();
	}

	@Override
	public String toString() {
		return "MRImage["
				+ "id=" + id + ", "
				+ "path=\"" + path + "\", "
				+ "moderationStatus=" + getModerationStatus() + ", "
				+ "upvotes=" + getUpvotes() + ", "
				+ "downvotes=" + getDownvotes()
				+ "]";
	}
	
}
