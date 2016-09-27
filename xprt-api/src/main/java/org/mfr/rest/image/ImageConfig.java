package org.mfr.rest.image;

import org.mfr.rest.util.Constants;

public enum ImageConfig {
	PROFILECONFIG(
			85, 160, 160, Constants.PROFILEPATH, ".JPG", false),
	TMPCONFIG(
			85, 160, 160, Constants.TMPPATH, ".JPG", false);
	double quality = 0;
	Integer width = null;
	Integer height = null;
	String storePath = null;
	String ext = null;
	boolean replaceExt;

	ImageConfig(double quantity, Integer width, Integer height,
			String storePath, String extension, boolean ext) {
		this.width = width;
		this.height = height;
		this.quality = quantity;
		this.storePath = Constants.STOREDIR + storePath;
		this.ext = extension;
		this.replaceExt = ext;
	}

	public String getPath(String path) {
		if (replaceExt) {
			path = ImageTools.convertFileExtension(path, ext);
		}
		return storePath + path;
	}
}
