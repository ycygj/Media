package com.uroad.widget.image;

import java.io.OutputStream;

public interface Downloader  {

	/**
	 * 请求网络的inputStream填充outputStream
	 * @param urlString
	 * @param outputStream
	 * @return
	 */
	public boolean downloadToLocalStreamByUrl(String urlString, OutputStream outputStream);
}
