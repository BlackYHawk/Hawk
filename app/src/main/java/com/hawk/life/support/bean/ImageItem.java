package com.hawk.life.support.bean;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public String imageName;
	public String imageTitle;
	public boolean isSelected = false;
}
