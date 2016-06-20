package com.hawk.library.component.bitmaploader.download;

import com.hawk.library.common.utils.Logger;
import com.hawk.library.component.bitmaploader.core.ImageConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


public class WebDownloader implements Downloader {

	private OkHttpClient httpClient;

	public WebDownloader() {
		httpClient = new OkHttpClient();
	}

	@Override
	public byte[] downloadBitmap(String url, ImageConfig config) throws Exception {
		Logger.v(url);

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final DownloadProcess progress = config.getProgress();

			if (progress != null) {
				progress.sendPrepareDownload(url);

				httpClient.networkInterceptors().add(new Interceptor() {
					@Override
					public Response intercept(Chain chain) throws IOException {
						Response response = chain.proceed(chain.request());

						return response.newBuilder().body(new ProgressResponseBody(response.body(),
								progress)).build();
					}
				});
			}

			Request request = new Request.Builder().url(url).build();
			Response response = httpClient.newCall(request).execute();

			if (response.isSuccessful()) {
				if(response.code() == 200) {
					InputStream in = response.body().byteStream();
					// 获取图片数据
					byte[] buffer = new byte[1024 * 8];
					int readLen = -1;

					while ((readLen = in.read(buffer)) != -1) {
						out.write(buffer, 0, readLen);
					}

					out.flush();
					byte[] bs = out.toByteArray();

					in.close();
					out.close();
					return bs;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (config.getProgress() != null)
				config.getProgress().sendException(e);
			throw new Exception(e.getCause());
		}
		return null;
	}

	/**
	 * 添加进度监听的ResponseBody
	 */
	private static class ProgressResponseBody extends ResponseBody {
		private final ResponseBody responseBody;
		private final DownloadProcess progressListener;
		private BufferedSource bufferedSource;

		public ProgressResponseBody(ResponseBody responseBody, DownloadProcess progressListener) {
			this.responseBody = responseBody;
			this.progressListener = progressListener;
		}

		@Override public MediaType contentType() {
			return responseBody.contentType();
		}

		@Override public long contentLength() {
			return responseBody.contentLength();
		}

		@Override public BufferedSource source() {
			if (bufferedSource == null) {
				bufferedSource = Okio.buffer(source(responseBody.source()));
			}
			return bufferedSource;
		}

		private Source source(Source source) {
			return new ForwardingSource(source) {
				long totalBytesRead = 0L;

				@Override public long read(Buffer sink, long byteCount) throws IOException {
					long bytesRead = super.read(sink, byteCount);
					totalBytesRead += (bytesRead != -1 ? bytesRead : 0);

					if(bytesRead == -1) {
						progressListener.sendFinishedDownload();
					}
					else {
						progressListener.sendProgress(totalBytesRead);
					}

					return bytesRead;
				}
			};
		}
	}

	interface ProgressListener {
		/**
		 * @param bytesRead     已下载字节数
		 * @param contentLength 总字节数
		 * @param done          是否下载完成
		 */
		void update(long bytesRead, long contentLength, boolean done);
	}

}
