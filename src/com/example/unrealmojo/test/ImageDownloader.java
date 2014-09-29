package com.example.unrealmojo.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import android.util.Log;

public class ImageDownloader {

	public static String loadImageToDisc(String url, String path) {
		File file = new File(path);

		if (!file.exists()) {
			try {
				InputStream is = (InputStream) new URL(url).getContent();

				file.createNewFile();

				@SuppressWarnings("resource")
				FileOutputStream fos = new FileOutputStream(file);
				try {

					byte[] b = new byte[100];
					int l = 0;
					while ((l = is.read(b)) != -1)
						fos.write(b, 0, l);

				} catch (Exception e) {

				}

				
			} catch (Exception e) {
				System.out.println("Exc=" + e);
				return null;

			}
		}
		return file.getAbsolutePath();
	}

	// public static Bitmap getRemoteImage(String imageUrl) {//not used in this
	// version
	// try {
	// URL url = new URL(imageUrl);
	// final URLConnection conn = url.openConnection();
	// conn.connect();
	// final BufferedInputStream bis = new
	// BufferedInputStream(conn.getInputStream());
	// final Bitmap bm = BitmapFactory.decodeStream(bis);
	// bis.close();
	//
	// return bm;
	// } catch (IOException e) {
	// Log.d("myTAG", "ImageDownloader: Connection error");
	// }
	// return null;
	// }

}
