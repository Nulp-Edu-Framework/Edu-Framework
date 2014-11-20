package com.nulp.eduframework.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 
public class ZipUtil {
    /**
     * A constants for buffer size used to read/write data
     */
    private static final int BUFFER_SIZE = 1024 * 10;
    
    /**
     * Compresses a collection of files to a destination zip file
     * @param listFiles A collection of files and directories
     * @param destZipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
   public static Integer compressFiles(List<File> listFiles, String destZipFile) throws FileNotFoundException, IOException {
       
	   Integer compressedFiles = 0;
       ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));
        
       for (File file : listFiles) {
           if (file.isFile()) {
        	   System.out.println("try to add file" + file.getAbsolutePath() + "to " + destZipFile);
               addFileToZip(file, zos);
               System.out.println("file added to zip");
               compressedFiles++;
           }
       }
        
       zos.flush();
       zos.close();
       
       return compressedFiles;
   }
 
    /**
     * Adds a file to the current zip output stream
     * @param file the file to be added
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void addFileToZip(File file, ZipOutputStream zos)
            throws FileNotFoundException, IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));
 
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
 
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
 
        while ((read = bis.read(bytesIn)) != -1) {
            zos.write(bytesIn, 0, read);
        }
 
        zos.closeEntry();
        bis.close();
    }
}