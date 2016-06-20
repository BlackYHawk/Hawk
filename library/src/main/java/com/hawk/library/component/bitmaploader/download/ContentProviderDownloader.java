package com.hawk.library.component.bitmaploader.download;

import android.net.Uri;

import com.hawk.library.common.context.GlobalContext;
import com.hawk.library.common.utils.FileUtils;
import com.hawk.library.component.bitmaploader.core.ImageConfig;

import java.io.InputStream;


public class ContentProviderDownloader implements Downloader {

	@Override
	public byte[] downloadBitmap(String url, ImageConfig config) throws Exception {
		
		try {
			InputStream is = GlobalContext.getInstance().getContentResolver().openInputStream(Uri.parse(url));
			byte[] datas = FileUtils.readStreamToBytes(is);
			return datas;
		} catch (Exception e) {
			if (config.getProgress() != null)
				config.getProgress().downloadFailed(e);
			e.printStackTrace();
			throw e;
		}
	}

}
