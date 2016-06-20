package com.hawk.library.component.bitmaploader.download;


import com.hawk.library.component.bitmaploader.core.ImageConfig;

public interface Downloader {

	public byte[] downloadBitmap(String url, ImageConfig config) throws Exception;

}
