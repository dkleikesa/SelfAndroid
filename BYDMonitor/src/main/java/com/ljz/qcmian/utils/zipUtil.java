package com.ljz.qcmian.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class zipUtil {

    private static final int ZIP_UTIL_BUFFER_SIZE = 1024;

    public static File zipFile(String inFile, String outFileName) {
        if ((inFile == null) || (inFile.length() == 0)) {
            return null;
        }
        File f = new File(inFile);
        if (!f.exists())
            return null;
        if (f.isDirectory()) {
            return zipDirectory(inFile, outFileName);
        } else {
            return zipSingleFile(inFile, outFileName);
        }
    }

    @SuppressWarnings("resource")
    public static File zipSingleFile(String inFileName, String outFileName) {
        if ((inFileName == null) || (inFileName.length() == 0)) {
            return null;
        }
        if ((outFileName == null) || (outFileName.length() == 0)) {
            outFileName = inFileName + ".zip";
        }
        if (inFileName.equals(outFileName))
            return null;

        File inFile = null;
        File outFile = null;
        FileOutputStream fos = null;
        try {
            inFile = new File(inFileName);
            if (!inFile.exists()) {
                return null;
            }
            if (inFile.length() == 0) {
                return null;
            }
            outFile = new File(outFileName);
            if (outFile.exists()) {
                outFile.delete();
                outFile.createNewFile();
            } else {
                outFile.createNewFile();
            }
            fos = new FileOutputStream(outFile);
            if (fos == null)
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        try {
            writeAFile(zipOut, inFile, null);
            zipOut.flush();
            zipOut.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outFile;
    }

    public static File zipDirectory(String path, String outFileName) {
        if ((path == null) || (path.length() == 0)) {
            return null;
        }
        File inPath = new File(path);
        if ((!inPath.isDirectory()) || (!inPath.exists()))
            return null;
        if ((outFileName == null) || (outFileName.length() == 0)) {
            outFileName = path + File.separator + inPath.getName() + ".zip";
        }
        File[] inFiles = null;
        File outFile = null;
        ArrayList<File> fList = new ArrayList<File>();
        try {
            outFile = new File(outFileName);
            pathToFiles(inPath, fList);
            inFiles = new File[fList.size()];
            fList.toArray(inFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipFiles(inFiles, outFile, path);
    }

    private static File zipFiles(File inFiles[], File outFile, String path) {
        if ((inFiles == null) || (inFiles.length == 0)) {
            return null;
        }
        if (outFile == null)
            return null;
        FileOutputStream fos = null;
        File[] inf = null;
        try {
            for (File f : inFiles) {
                if (!f.exists()) {
                    System.out.println("file not exist:" + f.getAbsolutePath());
                    return null;
                }
                if (f.length() == 0) {
                    System.out.println("file length =0:" + f.getAbsolutePath());
                    return null;
                }
                if (f.isDirectory()) {
                    System.out.println("file is directory:" + f.getAbsolutePath());
                    return null;
                }
            }
            String oName = outFile.getAbsolutePath();
            ArrayList<File> flist = new ArrayList<File>();
            for (File f : inFiles) {
                if (!oName.equals(f.getAbsolutePath())) {
                    flist.add(f);
                }
            }
            if (flist.size() == 0) {
                return null;
            }
            inf = new File[flist.size()];
            inf = flist.toArray(inf);
            if ((inf == null) || (inf.length == 0)) {
                return null;
            }

            if (outFile.exists()) {
                outFile.delete();
                outFile.createNewFile();
            } else {
                outFile.createNewFile();
            }
            fos = new FileOutputStream(outFile);
            if (fos == null)
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        try {
            for (File f : inf) {
                writeAFile(zipOut, f, path);
            }
            zipOut.flush();
            zipOut.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outFile;
    }

    private static void writeAFile(ZipOutputStream zipOut, File f, String path) {
        FileInputStream fis = null;
        ZipEntry zen = null;
        String entryName = null;
        try {
            fis = new FileInputStream(f);
            if (path == null) {
                entryName = f.getName();
            } else {
                entryName = f.getAbsolutePath().substring(path.length());
            }
            zen = new ZipEntry(entryName);
            zipOut.putNextEntry(zen);
            byte[] buffer = new byte[ZIP_UTIL_BUFFER_SIZE];
            int len = 0;
            while ((len = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, len);
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pathToFiles(File path, ArrayList<File> fList) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    pathToFiles(f, fList);
                } else {
                    fList.add(f);
                }
            }
        }
    }

    public static File zipFiles(String inFileNames[], String outFileName) {
        if ((inFileNames == null) || (inFileNames.length == 0)) {
            return null;
        }
        for (String str : inFileNames) {
            if ((str == null) || (str.length() == 0)) {
                return null;
            }
        }
        if ((outFileName == null) || (outFileName.length() == 0)) {
            outFileName = inFileNames[0] + ".zip";
        }
        File[] inFiles = new File[inFileNames.length];
        File outFile = new File(outFileName);
        for (int i = 0; i < inFileNames.length; i++) {
            inFiles[i] = new File(inFileNames[i]);
        }
        return zipFiles(inFiles, outFile);
    }

    public static File zipFiles(File[] inFiles, File outFile) {
        return zipFiles(inFiles, outFile, null);
    }

    public static File unZipFile(String inFile, String outFile) {
        if ((inFile == null) || (inFile.length() == 0)) {
            return null;
        }
        File infile = new File(inFile);
        if (!infile.exists())
            return null;
        if (infile.isDirectory())
            return null;
        if ((outFile == null) || (outFile.length() == 0)) {
            outFile = infile.getParent();
        }
        if ((outFile == null) || (outFile.length() == 0)) {
            return null;
        }
        File outPath = new File(outFile);
        if (outPath.exists() && (!outPath.isDirectory()))
            return null;
        if (!outPath.exists())
            System.out.println("create path:" + outPath.isDirectory());
        outPath.mkdirs();

        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry entry = null;
        try {
            fis = new FileInputStream(infile);
            zis = new ZipInputStream(fis);
            while ((entry = zis.getNextEntry()) != null) {
                File outf = new File(outPath, entry.getName());
                if (outf.exists()) {
                    outf.delete();
                }
                outf.getParentFile().mkdirs();
                outf.createNewFile();
                FileOutputStream fos = new FileOutputStream(outf);
                byte[] buffer = new byte[ZIP_UTIL_BUFFER_SIZE];
                int len = 0;
                while ((len = zis.read(buffer)) >= 0) {
                    fos.write(buffer, 0, len);
                }
                zis.closeEntry();
                fos.close();

            }
            fis.close();
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outPath;
    }
}