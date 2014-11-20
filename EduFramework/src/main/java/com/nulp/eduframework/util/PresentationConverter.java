package com.nulp.eduframework.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PresentationConverter {

	public static Integer convertPresentationToImages(String filePath, String folderpath) {
		
		File slidesFolder = new File(folderpath + "/" + "lectureSlides");
		
		slidesFolder.delete();
		slidesFolder.mkdir();
		
		FileInputStream is = null;
		XMLSlideShow ppt = null;

		try {
			is = new FileInputStream(filePath);
			ppt = new XMLSlideShow(is);
			is.close();
		} catch (Exception e) {
			System.out.println("EXEPTION : " + e.getStackTrace().toString());
			return -1;
		}

		double zoom = 2; // magnify it by 2
		AffineTransform at = new AffineTransform();
		at.setToScale(zoom, zoom);

		Dimension pgsize = ppt.getPageSize();

		XSLFSlide[] slide = ppt.getSlides();
		for (int i = 0; i < slide.length; i++) {
			BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width* zoom), (int) Math.ceil(pgsize.height * zoom),BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			graphics.setTransform(at);

			graphics.setPaint(Color.white);
			graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,pgsize.height));
			slide[i].draw(graphics);

			FileOutputStream out = null;

			try {
				out = new FileOutputStream(slidesFolder + "/" + "slide_" + (i + 1) + ".png");
				System.out.println("SAVED ON : " + slidesFolder + "/" + "slide_" + (i + 1) + ".png");
				javax.imageio.ImageIO.write(img, "png", out);
				out.close();
			} catch (Exception e) {
				System.out.println("EXEPTION : " + e.getStackTrace().toString());
				return -1;
			}

		}

		return zipAllImagesToFile(slidesFolder.getAbsolutePath(), folderpath + "/" + "lecture.zip");
	}
	
	public static Integer zipAllImagesToFile(String folderPath, String zipArchiveName) {

		System.out.println("ZIP FILES FROM : " + folderPath);
		
		Integer compressedFiles = 0;
		
		File folder = new File(folderPath);

		if (folder.exists() && folder.isDirectory()) {
			try {
				compressedFiles = ZipUtil.compressFiles(Arrays.asList(folder.listFiles()), zipArchiveName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return compressedFiles;
	}
	
	

}
