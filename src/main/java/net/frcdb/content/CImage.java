package net.frcdb.content;

/**
 *
 * @author tim
 */
public interface CImage extends Content {
	
	public String getPath();
	public void setPath(String path);
	
	public String getMediumPath();
	public void setMediumPath(String mediumPath);
	
	public String getThumbnailPath();
	public void setThumbnailPath(String thumbnailPath);
	
}
