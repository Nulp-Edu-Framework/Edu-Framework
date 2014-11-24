package com.eduframework.edroid.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class PresentationContentManager {
	
	public final static String PRESENTATION_LOADED_CONTENT_FOLDER_NAME = Environment.getExternalStorageDirectory() + "/EDroidContent/";
	public final static String PRESENTATION_UNZIP_CONTENT_FOLDER_NAME = PRESENTATION_LOADED_CONTENT_FOLDER_NAME + "unzipped/";
	public final static String PRESENTATION_CONTENT_FOLDER_NAME = "lecture";
	public final static String PRESENTATION_CONTENT_FILE_NAME = "presentation";
	public final static String PRESENTATION_CONTENT_LOADED_FORMAT = "zip";


	public static void unzip(String zipFile, String location) {
		try {
			
			dirChecker(location, "");
			
			FileInputStream fin = new FileInputStream(zipFile);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;
            byte buffer[] = new byte[1024 * 10];
            Log.d(AppConstants.DEBUG_TAG_NAME, "Start Unzip Presentetion");
			
			while ((ze = zin.getNextEntry()) != null) {
			
				Log.v("Decompress", "Unzipping " + ze.getName());

				if (ze.isDirectory()) {
					dirChecker(location, ze.getName());
				} else {
					FileOutputStream fout = new FileOutputStream(location + "/"+ ze.getName());
					for (int c = zin.read(buffer); c != -1; c = zin.read(buffer)) {
						fout.write(buffer, 0, c);
					}

					zin.closeEntry();
					fout.close();
				}

			}
			zin.close();
		} catch (Exception e) {
			Log.e("Decompress", "unzip", e);
		}
		Log.d(AppConstants.DEBUG_TAG_NAME, "End Unzip Presentetion");
	}
	
	public static List<Bitmap> getAllImagesForPresentation (Integer presentationId) {
		List<Bitmap> lectureImages = new ArrayList<Bitmap>();
		File presentationContentDirectory = new File(PRESENTATION_UNZIP_CONTENT_FOLDER_NAME + "/" + presentationId + "/");
		
		if(presentationContentDirectory.exists()){
			File images[] = presentationContentDirectory.listFiles();

		    Arrays.sort(images, new Comparator<File>() {

				public int compare(File o1, File o2) {
					int n1 = extractNumber(o1.getName());
	                int n2 = extractNumber(o2.getName());
	                return n1 - n2;
				}
				
	            private int extractNumber(String name) {
	                int i = 0;
	                try {
	                    int s = name.indexOf('_')+1;
	                    int e = name.lastIndexOf('.');
	                    String number = name.substring(s, e);
	                    i = Integer.parseInt(number);
	                } catch(Exception e) {
	                    i = 0;
	                }
	                return i;
	            }

	        });
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.RGB_565;
			options.inDither = true;
			
			for (int i=0; i < images.length; i++)
			{
			    Bitmap bitmap = BitmapFactory.decodeFile(images[i].getAbsolutePath(), options);
			    lectureImages.add(bitmap);
			}
			
		}
		
		return lectureImages;
	}

	private static void dirChecker(String location, String dir) {
		File f = new File(location + dir);

		if (f.exists()) {

			File[] children = f.listFiles();
			for (int i = 0; i < children.length; i++) {
				children[i].delete();
			}

			f.delete();
			f.mkdirs();
		} else {
			f.mkdirs();
		}

	}
	
	public static File loadZipPresentetion(HttpResponse response, String fileName, String directoryName, Integer presentationId, Boolean unzip) {

		File file = null;
		File dir = null;
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		Log.d(AppConstants.DEBUG_TAG_NAME, "Start Load Zip Presentetion");

		try {
			final HttpEntity entity = response.getEntity();

			dir = new File(directoryName + presentationId);
			
			dir.mkdir();

			
			file = new File(dir, fileName);
			
			if(file.exists()){
				file.delete();
				file.createNewFile();
			} else {
				file.createNewFile();
			}

			inputStream = entity.getContent();
			outputStream = new FileOutputStream(file);

			byte buffer[] = new byte[1024];
			int dataSize;

			while ((dataSize = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, dataSize);
			}

		} catch (Exception e) {
			Log.e("LoadFileTask", "loading error");
			if (file != null && file.isFile()) {
				file.delete();
				file = null;
			}
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Log.e("LoadFileTask", "cant close stream");
			}
		}
		
		if(unzip && file != null) {
			String unzipLocation = PresentationContentManager.PRESENTATION_UNZIP_CONTENT_FOLDER_NAME + presentationId; 
			new File(unzipLocation).mkdir();			
			PresentationContentManager.unzip(directoryName + presentationId + "/" + fileName, unzipLocation);
		}
		Log.d(AppConstants.DEBUG_TAG_NAME, "End Load Zip Presentetion");
		return file;
	}

}
