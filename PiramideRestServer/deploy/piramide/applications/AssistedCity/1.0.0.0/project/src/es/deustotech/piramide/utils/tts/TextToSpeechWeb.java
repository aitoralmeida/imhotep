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
 * Author: Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */

package es.deustotech.piramide.utils.tts;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Xml.Encoding;
import es.deustotech.piramide.utils.constants.Constants;

/**
 * This class implements TextToSpeechManager, and it sends requests
 * to a web service to get the needed spoken items 
 */
public class TextToSpeechWeb implements TextToSpeechManager {

	private static ExecutorService executor = createExecutor();
	private Context context;
	private String language;
	
	@Override
	public void init(Context context, String language) {
		this.language = Constants.DEFAULT_LANGUAGE;
		this.context = context;
	}

	@Override
	public void speech(String text) {
		speech(context, text, language);
	}

	@Override
	public void stop() {
		executor.shutdownNow();
		try {
			executor.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		executor = createExecutor();
	}
	
	private static ExecutorService createExecutor(){
		return Executors.newSingleThreadExecutor();
	}
	
	private static synchronized void speech(final Context context, final String text, final String language){
		executor.submit(new Runnable(){
			@Override
			public void run(){
				try {
					final String encodedUrl = Constants.URL + language + "&q=" + URLEncoder.encode(text,Encoding.UTF_8.name());
					final DefaultHttpClient client = new DefaultHttpClient();
					HttpParams params = new BasicHttpParams();
					params.setParameter("http.protocol.content-charset", "UTF-8");
					client.setParams(params);
					final FileOutputStream fos = context.openFileOutput(Constants.MP3_FILE, Context.MODE_WORLD_READABLE);
					try {
						try {
							final HttpResponse response = client.execute(new HttpGet(encodedUrl));
							downloadFile(response, fos);
						} finally {
							fos.close();
						}
						final String filePath 		= context.getFilesDir().getAbsolutePath() + "/" + Constants.MP3_FILE;
						final MediaPlayer player 	= MediaPlayer.create(context.getApplicationContext(), Uri.fromFile(new File(filePath)));
						player.start();
						Thread.sleep(player.getDuration());
						while(player.isPlaying()){
							Thread.sleep(100);
						}
						player.stop();

					} finally{
						context.deleteFile(Constants.MP3_FILE);
					}
				} catch(InterruptedException ie){
					// ok
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void downloadFile(HttpResponse response, OutputStream os) throws IOException {
		final InputStream is = response.getEntity().getContent();
		long size = response.getEntity().getContentLength();
		final BufferedInputStream bis = new BufferedInputStream(is);
		final byte [] buffer = new byte[1024 * 1024]; // 1 MB
		long position = 0;
		while(position < size) {
			final int read = bis.read(buffer, 0, buffer.length);
			if(read <= 0){
				break;
			}
			os.write(buffer, 0, read);
			os.flush();

			position += read;
		}
		is.close();
	}
}