package net.frcdb.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.frcdb.content.Content;
import net.frcdb.content.MRImage;
import net.frcdb.content.moderation.ModerationStatus;

/**
 * TODO: this may cause trouble if the image uploaded is a png, java's jpeg
 * writer tries to (wrongly) include the alpha channel which doesn't render
 * properly
 * @author tim
 */
public class ImageUtil {
	
	public static final String UPLOAD_DIRECTORY = "/opt/frcdb/user-content/images";
	
	/**
	 * The maximum image size. Larger images are scaled before being stored.
	 */
	public static final int MAX_BOUNDS = 1024;
	
	/**
	 * The medium image size. Approximately the available screen space with the
	 * current layout (shades-of-gray) on a 1024x768 monitor.
	 */
	public static final int MED_BOUNDS = 540;
	
	public static final int THUMB_BOUNDS = 150;
	
	public static Content handleUpload(InputStream stream) throws IOException {
		// TODO: This should have some logic for the image type
		// (not just MRImage)
		
		MRImage ret = new MRImage();
		ret.setContentType("image/jpeg");
		ret.setModerationStatus(ModerationStatus.PENDING);
		
		BufferedImage image = ImageIO.read(stream);
		if (image.getWidth() > MAX_BOUNDS
				|| image.getHeight() > MAX_BOUNDS) {
			// resize
			image = aspectScale(image, MAX_BOUNDS, MAX_BOUNDS);
		}
		
		File file = new File(UPLOAD_DIRECTORY, ret.getId() + ".jpg");
		
		// write the large version
		ImageIO.write(image, "jpeg", file);
		ret.setPath(file.getAbsolutePath());
		
		// write the medium version
		if (image.getWidth() > MED_BOUNDS
				|| image.getHeight() > MED_BOUNDS) {
			image = aspectScale(image, MED_BOUNDS, MED_BOUNDS);
		}
		file = new File(UPLOAD_DIRECTORY, ret.getId() + "-med.jpg");
		ImageIO.write(image, "jpeg", file);
		ret.setMediumPath(file.getAbsolutePath());
		
		// write the thumbnail version
		if (image.getWidth() > THUMB_BOUNDS
				|| image.getHeight() > THUMB_BOUNDS) {
			image = aspectScale(image, THUMB_BOUNDS, THUMB_BOUNDS);
		}
		file = new File(UPLOAD_DIRECTORY, ret.getId() + "-thumb.jpg");
		ImageIO.write(image, "jpeg", file);
		ret.setThumbnailPath(file.getAbsolutePath());
		
		return ret;
	}
	
	public static BufferedImage scaleImage(BufferedImage image, int w, int h) {
		if (image.getWidth() == w && image.getHeight() == h) {
			return image;
		}

		BufferedImage dest =
				new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = dest.createGraphics();
		
		AffineTransform at =
				AffineTransform.getScaleInstance((double) w / image.getWidth(),
				(double) h / image.getHeight());

		g.drawRenderedImage(image, at);
		return dest;
	}

	/**
	 * Scales the given image to fit within the given width and height,
	 * preserving aspect ratio.
	 * @param image the image to scale
	 * @param maxW the maximum width
	 * @param maxH the maximum height
	 * @return a scaled instance of the given image
	 */
	public static BufferedImage aspectScale(BufferedImage image, int maxW, int maxH) {
		double ratioX = (double) maxW / image.getWidth();
		double ratioY = (double) maxH / image.getHeight();
		
		double ratio = Math.min(ratioX, ratioY);
		
		return scaleImage(
				image, 
				(int) (image.getWidth() * ratio),
				(int) (image.getHeight() * ratio));
	}
	
}
