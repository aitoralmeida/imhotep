/*
 * Copyright (C) 2010 PIRAmIDE-SP3 authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Almeida <aitor.almeida@deusto.es>
 *         Pablo Orduña <pablo.orduna@deusto.es>
 *         Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */
package piramide.multimodal.client.tester;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ApplicationDownloader {

	private static final String APK_FILENAME = "application.apk";
	// TODO: change this URL
	private static final String URL = "http://www.morelab.deusto.es/pub/piramide/PiramideSimpleSample.apk";
	
	
	private final Activity activity;
	
	ApplicationDownloader(Activity activity){
		this.activity = activity;
	}
	
	void download() throws DownloaderException{
    	final HttpClient client = new DefaultHttpClient();
    	try {
    		final FileOutputStream fos = this.activity.openFileOutput(APK_FILENAME, Context.MODE_WORLD_READABLE);
    		final HttpResponse response = client.execute(new HttpGet(URL));
			downloadFile(response, fos);
			fos.close();
		} catch (ClientProtocolException e) {
			throw new DownloaderException(e);
		} catch (IOException e) {
			throw new DownloaderException(e);
		}
	}
	
	void install(){
    	final Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + this.activity.getFilesDir().getAbsolutePath() + "/" + APK_FILENAME), "application/vnd.android.package-archive");
		this.activity.startActivity(intent);
	}
	
	private void downloadFile(HttpResponse response, OutputStream os) throws IOException {
		final InputStream is = response.getEntity().getContent();
		long size = response.getEntity().getContentLength();
		BufferedInputStream bis = new BufferedInputStream(is);
		final byte [] buffer = new byte[1024 * 1024]; // 1 MB
		long position = 0;
		while(position < size){
			final int read = bis.read(buffer, 0, buffer.length);
			if(read <= 0)
				break;
			
			os.write(buffer, 0, read);
			os.flush();
			
			position += read;
		}
		is.close();
	}
	
}
