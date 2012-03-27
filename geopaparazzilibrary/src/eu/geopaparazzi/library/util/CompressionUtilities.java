/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2010  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.geopaparazzi.library.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import eu.geopaparazzi.library.util.debug.Debug;
import eu.geopaparazzi.library.util.debug.Logger;
import eu.geopaparazzi.library.webproject.ReturnCodes;

/**
 * Utilities class to zip and unzip folders.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class CompressionUtilities {

    /**
     * Compress a folder and its contents.
     * 
     * @param srcFolder path to the folder to be compressed.
     * @param destZipFile path to the final output zip file.
     * @param addBaseFolder flag to decide whether to add also the provided base folder or not.
     * @param excludeNames names of files to exclude.
     */
    static public void zipFolder( String srcFolder, String destZipFile, boolean addBaseFolder, String... excludeNames )
            throws IOException {
        if (new File(srcFolder).isDirectory()) {
            ZipOutputStream zip = null;
            FileOutputStream fileWriter = null;
            try {
                fileWriter = new FileOutputStream(destZipFile);
                zip = new ZipOutputStream(fileWriter);
                addFolderToZip("", srcFolder, zip, addBaseFolder, excludeNames); //$NON-NLS-1$
            } finally {
                if (zip != null) {
                    zip.flush();
                    zip.close();
                }
                if (fileWriter != null)
                    fileWriter.close();
            }
        } else {
            throw new IOException("The base file is supposed to be a directory."); //$NON-NLS-1$
        }
    }

    /**
     * Uncompress a compressed file to the contained structure.
     * 
     * @param zipFile the zip file that needs to be unzipped
     * @param destFolder the folder into which unzip the zip file and create the folder structure
     */
    public static void unzipFolder( String zipFile, String destFolder ) throws IOException {
        ZipFile zf = new ZipFile(zipFile);
        Enumeration< ? extends ZipEntry> zipEnum = zf.entries();

        boolean isFirst = true;
        while( zipEnum.hasMoreElements() ) {
            ZipEntry item = (ZipEntry) zipEnum.nextElement();

            if (item.isDirectory()) {
                File newdir = new File(destFolder + File.separator + item.getName());
                if (isFirst) {
                    isFirst = false;
                    Logger.d("COMPRESSIONUTILS", "Checking for existing: " + newdir);
                    if (newdir.exists()) {
                        throw new IOException(ReturnCodes.FILEEXISTS.getMsgString() + " " + newdir);
                    }
                }
                newdir.mkdir();
            } else {
                String newfilePath = destFolder + File.separator + item.getName();
                File newFile = new File(newfilePath);
                if (isFirst) {
                    isFirst = false;
                    File parentFile = newFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    } else {
                        throw new IOException(ReturnCodes.FILEEXISTS.getMsgString() + " " + parentFile);
                    }
                } else {
                    File parentFile = newFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                }
                InputStream is = zf.getInputStream(item);
                FileOutputStream fos = new FileOutputStream(newfilePath);
                byte[] buffer = new byte[512];
                int readchars = 0;
                while( (readchars = is.read(buffer)) != -1 ) {
                    fos.write(buffer, 0, readchars);
                }
                is.close();
                fos.close();
            }
        }
        zf.close();
    }

    static private void addToZip( String path, String srcFile, ZipOutputStream zip, String... excludeNames ) throws IOException {
        File file = new File(srcFile);
        if (file.isDirectory()) {
            addFolderToZip(path, srcFile, zip, true, excludeNames);
        } else {
            if (isInArray(file.getName(), excludeNames)) {
                // jump if excluded
                return;
            }
            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = null;
            try {
                in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + File.separator + file.getName()));
                while( (len = in.read(buf)) > 0 ) {
                    zip.write(buf, 0, len);
                }
            } finally {
                if (in != null)
                    in.close();
            }
        }
    }

    static private void addFolderToZip( String path, String srcFolder, ZipOutputStream zip, boolean addFolder,
            String... excludeNames ) throws IOException {
        if (isInArray(srcFolder, excludeNames)) {
            // jump folder if excluded
            return;
        }
        File folder = new File(srcFolder);
        String listOfFiles[] = folder.list();
        for( int i = 0; i < listOfFiles.length; i++ ) {
            if (isInArray(listOfFiles[i], excludeNames)) {
                // jump if excluded
                continue;
            }

            String folderPath = null;
            if (path.length() < 1) {
                folderPath = folder.getName();
            } else {
                folderPath = path + File.separator + folder.getName();
            }
            String srcFile = srcFolder + File.separator + listOfFiles[i];
            addToZip(folderPath, srcFile, zip, excludeNames);
        }
    }
    private static boolean isInArray( String checkString, String[] array ) {
        for( String arrayString : array ) {
            if (arrayString.trim().equals(checkString.trim())) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("nls")
    public static void createZipFromFiles( File destinationZip, File... files ) throws IOException {
        FileOutputStream fos = new FileOutputStream(destinationZip);
        ZipOutputStream zos = new ZipOutputStream(fos);
        int bytesRead;
        byte[] buffer = new byte[1024];
        CRC32 crc = new CRC32();
        for( int i = 0, n = files.length; i < n; i++ ) {
            String name = files[i].getName();
            File file = files[i];
            if (!file.exists()) {
                if (Debug.D)
                    Logger.d("COMPRESSIONUTILITIES", "Skipping: " + name);
                continue;
            }
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            crc.reset();
            while( (bytesRead = bis.read(buffer)) != -1 ) {
                crc.update(buffer, 0, bytesRead);
            }
            bis.close();
            // Reset to beginning of input stream
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(name);
            entry.setMethod(ZipEntry.STORED);
            entry.setCompressedSize(file.length());
            entry.setSize(file.length());
            entry.setCrc(crc.getValue());
            zos.putNextEntry(entry);
            while( (bytesRead = bis.read(buffer)) != -1 ) {
                zos.write(buffer, 0, bytesRead);
            }
            bis.close();
        }
        zos.close();

    }
}
