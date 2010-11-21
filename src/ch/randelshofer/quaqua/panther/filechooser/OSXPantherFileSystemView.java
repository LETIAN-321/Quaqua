/*
 * @(#)OSXPantherFileSystemView.java 4.0  2008-05-10
 *
 * Copyright (c) 2001-2010 Werner Randelshofer, Immensee, Switzerland.
 * All rights reserved.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * license agreement you entered into with Werner Randelshofer.
 * For details see accompanying license terms.
 */
package ch.randelshofer.quaqua.panther.filechooser;

import ch.randelshofer.quaqua.filechooser.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.plaf.*;
import java.io.*;
import java.util.*;

/**
 * A file system view for Mac OS X 10.3 (Panther).
 *
 * @author  Werner Randelshofer
 * @version 4.0 2008-05-09 If native code is available, use native methods from
 * Files class to retrieve file infos. If no native code is available, fall back
 * to FileSystemView.getFileSystemView() rather than relying on Apple's FileView
 * class.
 * <br>3.2 2007-01-24 Determine system volume lazily. Create file view
 * lazily.
 * <br>3.1 2005-11-26 Added more hidden top level files.
 * <br>3.0 2005-08-26 Rewritten.
 * <br>2.1 2004-12-28 Added "TheVolumeSettingsFolder" to the list of
 * hiddenTopLevelNames. Removed "Desktop Folder" from the list of
 * hiddenTopLevelNames as it appears on the Finder on Mac OS X 10.3.
 * Filenames that end with 0x0d are considered as hidden.
 * <br>2.0 2004-10-31 New super class QuaquaFileSystemView.
 * <br>1.0.1 2004-10-12 Made static initializer robust against IO
 * exceptions that may occur, when we attempt to canonicalize a file name.
 * <br>1.0 2004-06-30 Created.
 */
public class OSXPantherFileSystemView extends QuaquaFileSystemView {

    private static final File volumesFolder = new File("/Volumes");
    private static final File networkFolder = new File("/Network");
    private static final File computer = new File("/");
    private static File systemVolume;
 
    public OSXPantherFileSystemView() {
    }

    public File getSystemVolume() {
        if (systemVolume == null) {
            File[] volumes = volumesFolder.listFiles();
            File sys = null;
            for (int i = 0; i < volumes.length; i++) {
                try {
                    if (volumes[i].getCanonicalFile().equals(computer)) {
                        sys = volumes[i];
                        break;
                    }
                } catch (IOException e) {
                    // We get here because we can't determine the
                    // canonical path for the volume. We suppress this
                    // exception, in the hope that it did not happen for
                    // the system volume. If it happened for the system
                    // volume, there is fallback code in method
                    // getSystemVolume() that handles this problem.
                    // System.err.println(
                    //   "Unable to canonicalize volume "+volumes[i]
                    // );
                    // e.printStackTrace();
                } catch (SecurityException e) {
                    // We get here because we are not allowed to read the
                    // file. We suppress this exception, in the hope that
                    // it did not happen for the system volume. If it
                    // happened for the system volume, there is fallback
                    // code in method getSystemVolume() that handles this
                    // problem.
                }
            }
            // If we couldn't determine the system volume, we use the
            // root folder instead.
            systemVolume = (sys == null) ? computer : sys;
        }

        return systemVolume;
    }

    public File getComputer() {
        return computer;
    }
    /**
     * This is a list of file names that are treated as invisible by the AWT
     * FileDialog when they are at the top directory level of a volume.
     * The file names are wrongly treated as visible by
     * Apple's implementation FileSystemView, so we use this HashSet here, to
     * hide them 'manually'.
     */
    private final static HashSet hiddenTopLevelNames = new HashSet();
    

    static {
        String[] names = {
            "AppleShare PDS",
            "automount",
            "bin",
            "Cleanup At Startup",
            "cores",
            "Desktop DB",
            "Desktop DF",
            "dev",
            "etc",
            "mach",
            "mach_kernel",
            "mach.sym",
            "private",
            "sbin",
            "Temporary Items",
            "TheVolumeSettingsFolder",
            "TheFindByContentFolder",
            "tmp",
            "Trash",
            "usr",
            "var",
            "Volumes",
            "\u0003\u0002\u0001Move&Rename",
        };

        hiddenTopLevelNames.addAll(Arrays.asList(names));
    }
    ;

    /**
     * Returns the parent directory of dir.
     * This method returns the system volume instead of the computer folder ("/").
     */
    public File getParentDirectory(File dir) {
        File parent = (isRoot(dir)) ? null : super.getParentDirectory(dir);
        if (parent != null && parent.equals(computer)) {
            parent = getSystemVolume();
        }
        return parent;
    }

    /**
     * Returns all root partitians on this system.
     * This method returns the contents of the volumes folder.
     * The computer folder ("/") is considered as hidden and not
     * returned by this method.
     */
    public File[] getRoots() {
        File[] fileArray;
        ArrayList roots = new ArrayList();
        /*
        fileArray = sysView.getRoots();
        if (fileArray != null) {
        roots.addAll(Arrays.asList(fileArray));
        }
         */
        fileArray = volumesFolder.listFiles();
        if (fileArray != null) {
            roots.addAll(Arrays.asList(fileArray));
        //roots.remove(getSystemVolume());
        }
        roots.add(networkFolder);

        return (File[]) roots.toArray(new File[roots.size()]);

    }

    /**
     * Returns whether a file is hidden or not.
     */
    public boolean isHiddenFile(File f) {
        if (f.isHidden()) {
            return true;
        } else {
            String name = f.getName();
            if (name.length() == 0) {
                return false;
            } else if (name.charAt(name.length() - 1) == (char) 0x0d) {
                // File names ending with 0x0d are considered as
                // hidden
                return true;
            } else if (name.charAt(0) == '.') {
                // File names starting with '.' are considered as
                // hidden
                return true;
            } else if (hiddenTopLevelNames.contains(name) && (f.getParent() == null || isRoot(f.getParentFile()))) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Determines if the given file is a root partition or drive.
     */
    public boolean isRoot(File aFile) {
        return aFile.equals(computer) || aFile.equals(networkFolder) || aFile.getParentFile() != null && aFile.getParentFile().equals(volumesFolder);
    }



    /**
     * On Windows, a file can appear in multiple folders, other than its
     * parent directory in the filesystem. Folder could for example be the
     * "Desktop" folder which is not the same as file.getParentFile().
     *
     * @param folder a <code>File</code> object repesenting a directory or special folder
     * @param file a <code>File</code> object
     * @return <code>true</code> if <code>folder</code> is a directory or special folder and contains <code>file</code>.
     */
    public boolean isParent(File folder, File file) {
        if (folder == null || file == null) {
            return false;
        } else {
            return folder.equals(file.getParentFile());
        }
    }

    /**
     *
     * @param parent a <code>File</code> object repesenting a directory or special folder
     * @param fileName a name of a file or folder which exists in <code>parent</code>
     * @return a File object. This is normally constructed with <code>new
     * File(parent, fileName)</code> except when parent and child are both
     * special folders, in which case the <code>File</code> is a wrapper containing
     * a <code>ShellFolder</code> object.
     */
    public File getChild(File parent, String fileName) {
        return new File(parent, fileName);
    }


    /**
     * Is dir the root of a tree in the file system, such as a drive
     * or partition. Example: Returns true for "C:\" on Windows 98.
     *
     * @param dir a <code>File</code> object representing a directory
     * @return <code>true</code> if <code>f</code> is a root of a filesystem
     * @see #isRoot
     */
    public boolean isFileSystemRoot(File dir) {
        File parentFile = dir.getParentFile();
        return parentFile == null || parentFile.equals(volumesFolder);
    }


    // Providing default implementations for the remaining methods
    // because most OS file systems will likely be able to use this
    // code. If a given OS can't, override these methods in its
    // implementation.
    public File getHomeDirectory() {
        return createFileObject(System.getProperty("user.home"));
    }

    /**
     * Return the user's default starting directory for the file chooser.
     *
     * @return a <code>File</code> object representing the default
     *         starting folder
     */
    public File getDefaultDirectory() {
        return getHomeDirectory();
    }
}

