package javaxt.io;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
//******************************************************************************
//**  Directory Class - By Peter Borissow
//******************************************************************************
/**
 *  Used to represent a directory on a file system. In many ways, this class 
 *  is an extension of the java.io.File class. However, unlike the java.io.File 
 *  class, this object provides functions that are relevant and specific to
 *  directories. For example, this class provides a mechanism to move and copy
 *  directories - something not offered by the java.io.File class. In addition,
 *  this class provides a mechanism to retrieve files and folders found in a
 *  directory AND any subdirectories. This is accomplished via a multi-threaded
 *  recursive search. Finally, this class provides a powerful tool to monitor
 *  changes made to the directory (e.g. getEvents).
 *
 ******************************************************************************/

public class Directory implements Comparable {
    
    private java.io.File file; //<--DO NOT USE DIRECTLY! Use getFile() instead.
    private String name = "";
    private String path = "";

    //private boolean useCache = false;
    private FileSystemWatcher FileSystemWatcher;
    private File.FileAttributes attr;
    private long lastAttrUpdate = 0;
    
    public static final String PathSeparator = System.getProperty("file.separator");
    protected static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    
  //**************************************************************************
  //** Constructor
  //**************************************************************************   
  /** Creates a new instance of Directory using a path to a directory. */
    
    public Directory(String Path) {

	if (Path==null) throw new NullPointerException();
        
        if (Path.startsWith("\"") && Path.endsWith("\"")){
            Path = Path.substring(1,Path.length()-1);
        }

        init(Path);
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Directory using a java.io.File */

    public Directory(java.io.File File) {
        init(File);
    }


  //**************************************************************************
  //** init
  //**************************************************************************

    private void init(java.io.File File){

	if (File==null) throw new NullPointerException();

        if (File.exists() && !File.isDirectory()){
            File = File.getParentFile();
        }

        attr = null;
        this.file = File;
        init(file.getAbsolutePath());
    }


  //**************************************************************************
  //** init
  //**************************************************************************
  /** Used to parse the absolute path to the directory. For performance reasons,
   *  we do not rely on the java.io.File to determine the path and name
   *  variables.
   */
    private void init(String Path){
        Path = Path.replace("\\", "/");
        String[] arr = Path.split("/");
        if (arr.length>1){
            name = arr[arr.length-1];
            path = Path.substring(0, Path.lastIndexOf(name));
        }
        else{
            path = Path;
        }

        if (path.isEmpty()) path = PathSeparator;
        else path = path.replace("/", PathSeparator);

        if (!path.endsWith(PathSeparator)) path += PathSeparator;
    }


  //**************************************************************************
  //** getFile
  //**************************************************************************
  /**  Returns the java.io.File representation of this object. */

    private java.io.File getFile(){
        if (file==null) file = new java.io.File(path + name);
        return file;
    }


  //**************************************************************************
  //** getRootDirectories
  //**************************************************************************
  /** Returns an array of root directories on the filesystem (e.g. "/" or C:\").
   *  On Windows, this method will return all mounted drives, including any
   *  that might have been disconnected. The array will be empty if there are
   *  no root directories or if the set of roots could not be determined.
   */
    public static Directory[] getRootDirectories(){

        java.util.TreeSet<Directory> directories = new java.util.TreeSet<Directory>();
        java.io.File[] files = java.io.File.listRoots();
        if (files==null) return new Directory[0];
        for (int i=0; i<files.length; i++){
            directories.add(new Directory(files[i]));
        }

      //Pick up any windows mounted drives that might not have been returned
      //from the listRoots() method (e.g. disconnected mounted drives).
        if (isWindows){
            
            boolean doNetUse = false;
            if (File.loadDLL()){
                try{
                    String drives = File.GetNetworkDrives();
                    if (drives!=null){
                        for (String drive : drives.split("\n")){
                            if (drive.trim().length()>0){
                                String[] arr = drive.split("\t");
                                String driveName = arr[0];
                                String driveType = arr[1];
                                directories.add(new Directory(driveName));
                            }
                        }
                    }
                }
                catch(Exception e){
                    doNetUse = true;
                }
            }            
            else{
                doNetUse = true;
            }
            
            if (doNetUse){
                javaxt.io.Shell cmd = new javaxt.io.Shell("net use");
                cmd.run();

                java.util.Iterator<String> it = cmd.getOutput().iterator();
                boolean parse = false;
                while (it.hasNext()){
                    String line = it.next();
                    if (line==null) break;
                    else line = line.trim();
                    if (!parse && line.startsWith("----")){
                        parse = true;
                        line = it.next();
                        if (line==null) break;
                    }
                    if (line.contains(":")){
                        line = line.substring(0, line.indexOf(":")+1);
                        line = line.substring(line.lastIndexOf(" ")+1);
                        directories.add(new Directory(line.trim()));
                    }
                }
            }
        }

        return directories.toArray(new Directory[directories.size()]);
    }

    
  //**************************************************************************
  //** Exists
  //**************************************************************************
  /** Used to determine whether a directory exists on the file system. */
    
    public boolean exists(){

        String path = this.path + name;
        
      //Special case for a directory whose path represents a server name (e.g. "\\192.168.0.1")
        if (isWindows && path.startsWith("\\\\")){
            if (path.endsWith(PathSeparator)) path = path.substring(0, path.length()-1);
            if (!path.substring(2).contains(PathSeparator)){
                boolean doNetUse = false;
                if (File.loadDLL()){
                    try{
                        File.GetSharedDrives(path.substring(2));
                        return true;
                    }
                    catch(Exception e){
                        if (e.getMessage().trim().equals("53")){
                            return false;
                        }
                        else{
                            doNetUse = true;
                        }
                    }
                }
                else{
                    doNetUse = true;
                }
                
                if (doNetUse){
                    javaxt.io.Shell cmd = new javaxt.io.Shell("net view " + path);
                    cmd.run();
                    java.util.List errors = cmd.getErrors();
                    errors.remove(null);
                    if (errors.isEmpty()){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                
            }
        }


        if (file!=null){
            return (file.isDirectory() && file.exists());
        }

        try{
            File.FileAttributes attr = getFileAttributes();
            if (attr!=null) return true;
            else{
                getFile();
                return (file.isDirectory() && file.exists());
            }
        }
        catch(java.io.FileNotFoundException e){
            return false;
        }
    }


  //**************************************************************************
  //** isEmpty
  //**************************************************************************
  /** Used to determine whether the directory is empty. Returns true if no
   *  files or directories are present in the current directory.
   */
    public boolean isEmpty(){
        if (!exists()) return true;
        Object[] files = listFiles(null);
        return (files==null);
    }


  //**************************************************************************
  //** Create Directory
  //**************************************************************************
  /**  Used to create the directory. */
    
    public boolean create(){
        return getFile().mkdirs();
    }
    
    
  //**************************************************************************
  //** Delete Directory
  //**************************************************************************
  /**  Used to delete the directory. */
    
    public boolean delete(){
        if (getFile().delete()){
            attr = null;
            return true;
        }
        else{
            return false;
        }
    }


  //**************************************************************************
  //** Copy To
  //**************************************************************************
  /** Used to copy a directory to another directory. Preserves the last modified 
   *  date associated with the source files and directories. Returns a list of
   *  any files that failed to copy.
   */
    public String[] copyTo(Directory Destination, boolean Overwrite){
        return copyTo(Destination, null, Overwrite);
    }


  //**************************************************************************
  //** Copy To
  //**************************************************************************
  /** Used to copy a directory to another directory. Provides a filter option 
   *  to copy specific files (e.g. "*.jpg"). Preserves the last modified date
   *  associated with the source files and directories. Returns a list of any
   *  files that failed to copy.
   *
   *  @param filter A file filter. You can pass in a java.io.FileFilter, a
   *  String (e.g. "*.txt"), or an array of Strings (e.g. String[]{"*.txt", "*.doc"}).
   *  Wildcard filters are supported. Note that the filter is only applied to
   *  files, not directories.
   *
   *  @param Overwrite If true, overwrites any existing files/directories
   */
    public String[] copyTo(Directory Destination, Object filter, boolean Overwrite){

        int source = toString().length();
        String destination = Destination.toString();
        java.util.ArrayList<String> failures = new java.util.ArrayList<String>();


      //Create new folder
        if (!Destination.exists()) Destination.create();



      //Initiate search
        java.util.List results = this.getChildren(true, filter, false);
        while (true){

            Object item;
            synchronized (results) {


              //Wait for files/directories to be added to the list
                while (results.isEmpty()) {
                    try {
                        results.wait();
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                }

              //Grab the next available file/directory from the list. Note that
              //we are INSIDE the synchronized block! This forces the directory
              //search to insert only one record at a time. The primary motivation
              //for this is to ensure that we don't run out of memory when
              //copying a large number of files/directories.
                item = results.get(0);
                if (item!=null){

                    if (item instanceof javaxt.io.File){

                        javaxt.io.File file = (javaxt.io.File) item;
                        String FilePath = file.toString();
                        File out = new File(destination + FilePath.substring(source));
    
                        boolean success = file.copyTo(out, Overwrite);
                        if (!success) failures.add(FilePath);
                    }
                    else{
                        javaxt.io.Directory dir = (javaxt.io.Directory) item;
                        new Directory(destination + dir.toString().substring(source)).create();
                    }

                }
                else{
                    break;
                }

                results.remove(0);
                results.notifyAll();
            }
        }
        
      //Return list of failed copies
        return failures.toArray(new String[failures.size()]);
    }


  //**************************************************************************
  //** Move To
  //**************************************************************************
  /**  Used to move a directory from one directory to another. */
    
    public void moveTo(Directory Destination, boolean Overwrite){
        if (Overwrite || !Destination.exists()){
            java.io.File newFile = Destination.toFile();
            if (getFile().renameTo(newFile)){
                init(newFile);
            }
        }
    }

    
  //**************************************************************************
  //** Rename
  //**************************************************************************
  /**  Used to rename the directory. */
    
    public void rename(String Name){
        java.io.File newFile = new java.io.File(path + Name);
        if (getFile().renameTo(newFile)){
            init(newFile);
        }
    }
    
    
  //**************************************************************************
  //** getName
  //**************************************************************************
  /**  Returns the name of the directory (excluding the path). */
    
    public String getName(){
        return name;
    }
    
    
  //**************************************************************************
  //** getPath
  //**************************************************************************
  /** Returns the full path to this directory, including the directory name.
   *  The path includes a system-dependent default name-separator 
   *  character at the end of the string (e.g. "/" or "\").
   */
    public String getPath(){
       String path = this.path + name;
       if (path.endsWith(PathSeparator)) return path;
       else return path + PathSeparator;
    }
    
    
  //**************************************************************************
  //** getDate
  //**************************************************************************
  /** Returns a timestamp of when the directory was last modified. This is
   *  identical to the getLastModifiedTime() method.
   */
    public java.util.Date getDate(){
        if (file!=null) return getFileDate();

        try{
            File.FileAttributes attr = getFileAttributes();
            if (attr!=null) return attr.getLastWriteTime();
            else{
                getFile();
                return getFileDate();
            }
        }
        catch(java.io.FileNotFoundException e){
            return null;
        }

    }

    private java.util.Date getFileDate(){
        if (file.exists() && file.isDirectory()) return new java.util.Date(file.lastModified());
        else return null;
    }


  //**************************************************************************
  //** setDate
  //**************************************************************************
  /**  Used to set the timestamp of when the directory was last modified.
   */
    public boolean setDate(java.util.Date lastModified){
        if (lastModified!=null){
            java.io.File file = getFile();
            if (file.exists() && file.isDirectory()){
                long t = lastModified.getTime();
                if (getDate().getTime()!=t){
                    attr = null;
                    return getFile().setLastModified(t);
                }
            }
        }
        return false;
    }


//  //**************************************************************************
//  //** getSize
//  //**************************************************************************
//  /** Used to retrieve the size of the directory object, in bytes. Note that
//   *  the getContentSize() method will return the total size of all the files 
//   *  and folders found in this directory.
//   */
//    public long getSize(){
//        if (file!=null) return getFileSize();
//
//        try{
//            File.FileAttributes attr = getFileAttributes();
//            if (attr!=null) return attr.getSize();
//            else{
//                getFile();
//                return getFileSize();
//            }
//        }
//        catch(java.io.FileNotFoundException e){
//            return 0L;
//        }
//    }
//
//    private long getFileSize(){
//        long size = file.length();
//        if (size>0L && file.isDirectory()) return size;
//        else return 0L;
//    }


  //**************************************************************************
  //** getSize
  //**************************************************************************
  /** Returns the total size of all the files and folders found in this
   *  directory, in bytes.
   */
    public long getSize(){

        long size = 0L;

      //Initiate search
        java.util.List results = getChildren(true, null, false);

        while (true){
            Object item;
            synchronized (results) {
                while (results.isEmpty()) {
                  try {
                      results.wait();
                  }
                  catch (InterruptedException e) {
                      break;
                  }
                }
                item = results.remove(0);
                results.notifyAll();
            }

            if (item!=null){ //Do something with the item.

                if (item instanceof javaxt.io.File){
                    javaxt.io.File file = (javaxt.io.File) item;
                    size += file.getSize();
                }
                else{
                    javaxt.io.Directory dir = (javaxt.io.Directory) item;
                    try{
                        //size += dir.getFileAttributes().getSize();
                    }
                    catch(Exception e){
                    }
                }
            }
            else{ //item is null. This is our queue that the search is done!
                return size;
            }
        }
    }


    
  //**************************************************************************
  //** isHidden
  //**************************************************************************
  /**  Used to determine whether this Directory is Hidden */
    
    public boolean isHidden(){

        if (file!=null){
            return (file.isDirectory() && file.isHidden());
        }

        try{
            File.FileAttributes attr = getFileAttributes();
            if (attr!=null) return attr.isHidden();
            else{
                getFile();
                return (file.isDirectory() && file.isHidden());
            }
        }
        catch(java.io.FileNotFoundException e){
            return false;
        }
    }


  //**************************************************************************
  //** isLink
  //**************************************************************************
  /**  Used to determine whether the directory is actually a link to another
   *   file or directory. Returns true for symbolic links and Windows
   *   junctions.
   */
    public boolean isLink(){
        return getLink()!=null;
    }


  //**************************************************************************
  //** getLink
  //**************************************************************************
  /**  Returns the target of a symbolic link or Windows junction
   */
    public java.io.File getLink(){
        try{
            return getFileAttributes().getLink();
        }
        catch(Exception e){
            return null;
        }
    }


  //**************************************************************************
  //** getLastModifiedTime
  //**************************************************************************
  /** Returns a timestamp of when the directory was last modified. This is
   *  identical to the getDate() method.
   */
    public java.util.Date getLastModifiedTime(){
        return this.getDate();
    }


  //**************************************************************************
  //** getCreationTime
  //**************************************************************************
  /** Returns a timestamp of when the directory was first created. Returns a
   *  null if the timestamp is not available.
   */
    public java.util.Date getCreationTime(){
        try{
            return getFileAttributes().getCreationTime();
        }
        catch(Exception e){
            return null;
        }
    }


  //**************************************************************************
  //** getLastAccessTime
  //**************************************************************************
  /** Returns a timestamp of when the directory was last accessed. Returns a
   *  null if the timestamp is not available. 
   */
    public java.util.Date getLastAccessTime(){
        try{
            return getFileAttributes().getLastAccessTime();
        }
        catch(Exception e){
            return null;
        }
    }


  //**************************************************************************
  //** getFlags
  //**************************************************************************
  /** Returns keywords representing directory attributes. Returns an empty 
   *  HashSet if the attributes are not available.
   */
    public java.util.HashSet<String> getFlags(){
        try{
            return getFileAttributes().getFlags();
        }
        catch(Exception e){
            return new java.util.HashSet<String>();
        }
    }


  //**************************************************************************
  //** getFileAttributes
  //**************************************************************************
  /** Returns file attributes such as when the file was first created and when
   *  it was last accessed. File attributes are cached for up to one second.
   *  This provides users the ability to retrieve multiple attributes at once. 
   *  Without caching, we would have to ping the file system every time we call 
   *  getLastAccessTime(), getLastAccessTime(), getLastWriteTime(), etc. The
   *  cached attributes are automatically updated when the file is updated or
   *  deleted by this class.
   */
    public File.FileAttributes getFileAttributes() throws java.io.FileNotFoundException {

        if (attr==null) lastAttrUpdate = 0;

        if (attr==null || (new java.util.Date().getTime()-lastAttrUpdate)>1000){
            try{
              //Get directory attributes
                String pathToFile = toString();
                attr = new File.FileAttributes(pathToFile);
                if (!attr.isDirectory()) throw new java.io.FileNotFoundException(pathToFile);

              //Set lastUpdate (used to cache file attributes)
                lastAttrUpdate = new java.util.Date().getTime();
            }
            catch(java.io.FileNotFoundException e){
                attr = null;
                throw e;
            }
            catch(Exception e){
                //e.printStackTrace();
                attr = null;
            }
        }
        return attr;
    }


  //**************************************************************************
  //** getParentDirectory
  //**************************************************************************
  /** Used to retrieve this Directory's Parent. Returns null if there is no
   *  parent directory.
   */
    public Directory getParentDirectory(){
        if (name.isEmpty()){
            return null;
        }
        else{
            return new Directory(path);
        }
    }


  //**************************************************************************
  //** toFile
  //**************************************************************************
  /**  Used to retrieve the java.io.File representation by this object. */

    public java.io.File toFile(){
        return getFile();
    }


    
  //**************************************************************************
  //** getFiles
  //**************************************************************************
  /**  Used to retrieve an array of files found in this directory. Returns an
   *   empty array if no files are found.
   *
   *   @param filter A file filter. You can pass in a java.io.FileFilter, a
   *   String (e.g. "*.txt"), or an array of Strings (e.g. String[]{"*.txt", "*.doc"}).
   *   Wildcard filters are supported. Note that the filter is only applied to
   *   files, not directories.
   */    
    public File[] getFiles(Object filter){

        if (this.exists()){

            FileFilter fileFilter = new FileFilter(filter);
            Object[] files = listFiles(fileFilter);
            if (files==null){
                return new File[0];
            }
            else{
              
                java.util.ArrayList<javaxt.io.File> list = new java.util.ArrayList<javaxt.io.File>();
                for (int i=0; i<files.length; i++){
                    java.io.File file = null;
                    if (files[i] instanceof java.io.File){
                        file = (java.io.File) files[i];
                    }
                    else{
                        file = new java.io.File(files[i].toString());
                    }
                    if (file.exists() && file.isFile()){
                        list.add(new javaxt.io.File(file));
                    }
                }
                Collections.sort(list, new FileComparer(this));
                return list.toArray(new javaxt.io.File[list.size()]);
            }
        }
        else{
            return new File[0];
        }
    }
    
    
  //**************************************************************************
  //** getFiles
  //**************************************************************************
  /**  Used to retrieve an array of files found in this directory. Returns an
   *   empty array if no files are found.
   */
    public File[] getFiles(){
        return getFiles(null);
    }
    
    
  //**************************************************************************
  //** getFiles
  //**************************************************************************
  /**  Used to retrieve an array of files found in this directory. Returns an
   *   empty array if no files are found.
   *
   *   @param filter A file filter. You can pass in a java.io.FileFilter, a
   *   String (e.g. "*.txt"), or an array of Strings (e.g. String[]{"*.txt", "*.doc"}).
   *   Wildcard filters are supported. Note that the filter is only applied to
   *   files, not directories.
   *
   *   @param RecursiveSearch If true, will perform a multi-threaded, recursive
   *   directory search to find all the files found in the current directory,
   *   including any subdirectories. If false, the method will simply return
   *   files found in the current directory. <br/>
   *
   *   Note that if the thread is interrupted for whatever reason during a
   *   recursive search, the search will stop immediately. Consequently, the
   *   returned array may be incomplete. You can check the interrupted status
   *   with the Thread.isInterrupted() method. Alternatively, you can read and
   *   clear the interrupted status in a single operation using the
   *   Thread.interrupted() method.
   */    
    public File[] getFiles(Object filter, boolean RecursiveSearch){        

        if (this.exists()){
            if (RecursiveSearch){

              //Get list of all the files and folders in the directory
                List items = getChildren(true, filter, false);
                ArrayList<File> files = new ArrayList<File>();
                try {
                    Object item;
                    while (true){
                        synchronized (items) {
                            while (items.isEmpty()) {
                              items.wait();
                            }
                            item = items.remove(0);
                            items.notifyAll();
                        }

                        if (item==null){
                            break;
                        }
                        else{
                            if (item instanceof File){
                                files.add((File) item);
                            }
                        }
                    }
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

              //Sort the list
                Collections.sort(files, new FileComparer(this));
                
                
              //Convert the list into an array of files
                return files.toArray(new File[files.size()]);
            }
            else{
                return getFiles(filter);
            }
        }
        else{
            return new File[0];
        }
    }
    
    
  //**************************************************************************
  //** getFiles
  //**************************************************************************
  /**  Used to retrieve an array of files found in this directory. 
   *
   *   @param RecursiveSearch If true, will perform a multi-threaded, recursive
   *   directory search to find all the files found in the current directory,
   *   including any subdirectories. If false, the method will simply return
   *   files found in the current directory. <br/>
   *
   *   Note that if the thread is interrupted for whatever reason during a
   *   recursive search, the search will stop immediately. Consequently, the
   *   returned array may be incomplete. You can check the interrupted status
   *   with the Thread.isInterrupted() method. Alternatively, you can read and
   *   clear the interrupted status in a single operation using the
   *   Thread.interrupted() method.
   */
    public File[] getFiles(boolean RecursiveSearch){
        return getFiles(null, RecursiveSearch);
    }
        
    
  //**************************************************************************
  //** getSubDirectories
  //**************************************************************************
  /**  Used to retrieve an array of directories found in this directory.
   */
    public Directory[] getSubDirectories(){
        return getSubDirectories(false);
    }
    
    
  //**************************************************************************
  //** getSubDirectories
  //**************************************************************************
  /**  Used to retrieve an array of directories found in this directory.
   *
   *   @param RecursiveSearch If true, will perform a multi-threaded, recursive
   *   directory search to find all the directories found in the current
   *   directory, including any subdirectories. If false, the method will simply
   *   return directories found in the current directory. <br/>
   * 
   *   Note that if the thread is interrupted for whatever reason during a
   *   recursive search, the search will stop immediately. Consequently, the
   *   returned array may be incomplete. You can check the interrupted status
   *   with the Thread.isInterrupted() method. Alternatively, you can read and
   *   clear the interrupted status in a single operation using the
   *   Thread.interrupted() method.
   */    
    public Directory[] getSubDirectories(boolean RecursiveSearch){
        if (this.exists()){
        
            if (RecursiveSearch){
                
              //Get list of all the files and folders in the directory
                List items = getChildren(true, null, false);
                ArrayList<Directory> directories = new ArrayList<Directory>();
                try {
                    Object item;
                    while (true){
                        synchronized (items) {
                            while (items.isEmpty()) {
                              items.wait();
                            }
                            item = items.remove(0);
                            items.notifyAll();
                        }

                        if (item==null){
                            break;
                        }
                        else{
                            if (item instanceof Directory){
                                directories.add((Directory) item);
                            }
                        }
                    }
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return directories.toArray(new Directory[directories.size()]);
                
            }
            else{
                java.io.FileFilter fileFilter = new java.io.FileFilter() {
                    public boolean accept(java.io.File file) {
                        if (file.isDirectory()){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                };


                Object[] files = listFiles(new FileFilter(fileFilter));
                if (files==null) return new Directory[0];
                else{
                    Directory[] dirs = new Directory[files.length];
                    for (int i=0; i<files.length; i++){
                         dirs[i] = new Directory(files[i].toString());
                    }
                    return dirs;
                }
            }
        }
        else{
             return new Directory[0];
        }
    }
    
    
  //**************************************************************************
  //** getChildren
  //**************************************************************************
  /**  Used to retrieve an array of both files and folders found in this 
   *   directory. 
   */    
    public List getChildren(){
        return getChildren(false);
    }


  //**************************************************************************
  //** getChildren
  //**************************************************************************
  /**  Used to retrieve an array of both files and folders found in this
   *   directory. 
   */
    public List getChildren(boolean RecursiveSearch){
        return getChildren(RecursiveSearch, null, true);
    }


  //**************************************************************************
  //** getChildren
  //**************************************************************************
  /**  Used to retrieve an list of files and folders found in this directory.
   *
   *   @param RecursiveSearch If true, will include files found in the
   *   subdirectories.
   *
   *   @param filter A file filter. You can pass in a java.io.FileFilter, a
   *   String (e.g. "*.txt"), or an array of Strings (e.g. String[]{"*.txt", "*.doc"}).
   *   Wildcard filters are supported. Note that the filter is only applied to
   *   files, not directories.
   *
   */
    public List getChildren(boolean RecursiveSearch, Object filter){
        return getChildren(RecursiveSearch, filter, true);
    }

    
  //**************************************************************************
  //** getChildren
  //**************************************************************************
  /**  Used to retrieve an list of files and folders found in this directory.
   *
   *   @param RecursiveSearch If true, will perform a multi-threaded, recursive
   *   directory search to find all the files and folders found in the current
   *   directory, including any subdirectories. If false, the method will simply
   *   return items found in the current directory. <br/>
   *
   *   Note that if the thread is interrupted for whatever reason during a
   *   recursive search, the search will stop immediately. Consequently, the
   *   returned array may be incomplete. You can check the interrupted status
   *   with the Thread.isInterrupted() method. Alternatively, you can read and
   *   clear the interrupted status in a single operation using the
   *   Thread.interrupted() method.
   *
   *   @param filter A file filter. You can pass in a java.io.FileFilter, a
   *   String (e.g. "*.txt"), or an array of Strings (e.g. String[]{"*.txt", "*.doc"}).
   *   Wildcard filters are supported. Note that the filter is only applied to
   *   files, not directories.
   *
   *   @param wait Used to indicate whether to wait for the list to be fully
   *   populated before returning the response. When traversing very a large
   *   set of files, it maybe a good idea to process the files as they get added
   *   to the list. In this case you should set this parameter to false. If this
   *   parameter is set to false, a null entry will be added to the end of the
   *   list to indicate that the directory search is complete.
   *
   *   Example: <pre>
    boolean wait = false;
    java.util.List files = directory.getChildren(true, null, wait);
    if (wait){
        for (int i=0; i &lt; files.size(); i++){
            System.out.println(files.get(i));
        }
    }
    else{
        Object obj;
        while (true){
            synchronized (files) {
                while (files.isEmpty()) {
                  try {
                      files.wait();
                  }
                  catch (InterruptedException e) {
                      break;
                  }
                }
                obj = files.remove(0);
                files.notifyAll();
            }

            if (obj==null){
                break;
            }
            else{
                System.out.println(obj);
            }
        }
    }</pre>
   *
   */
    public List getChildren(boolean RecursiveSearch, Object filter, boolean wait){
        
        if (this.exists()){
            
            if (RecursiveSearch){

              //Create list to store items found in the directory
                List items = new LinkedList();


              //Create a file filter
                FileFilter fileFilter = new FileFilter(filter);


              //Spawn threads used to crawl through the file system
                long directoryID = Long.valueOf(java.util.Calendar.getInstance().getTimeInMillis() + "" + new Random().nextInt(100000)).longValue();
                int numThreads = 20; //<-- this should be set dynamically and self tuning
                DirectorySearch.deleteCache();
                DirectorySearch search = new DirectorySearch(fileFilter, items, directoryID, numThreads);
                for (int i=0; i<numThreads; i++) {
                     Thread t = new Thread(search);
                     t.setName("DirectorySearch_" + directoryID + "-" + i);
                     t.start();
                }


              //Initiate search
                DirectorySearch.updatePool(this);

                if (wait){

                    synchronized (items) {
                        while (!items.contains(null)) {
                          try {
                              items.wait();
                          }
                          catch (InterruptedException e) {
                              DirectorySearch.stop();
                              Thread.currentThread().interrupt();
                              return items;
                          }
                        }

                        items.remove(null);
                        items.notifyAll();
                    }

                    Collections.sort(items, new FileComparer(this));
                }

                
              //Return list
                return items;
        
        
            }//end recursive search
            else{
                List results = new LinkedList();
                Object[] files = listFiles(new FileFilter(filter));
                if (files==null) {
                    if (!wait) results.add(null);
                    return results;
                }
                else{
                    
                    for (int i=0; i<files.length; i++){

                        //System.out.println(files[i]);
                        boolean isDirectory = false;

                        java.io.File file = null;
                        if (files[i] instanceof java.io.File){
                            file = (java.io.File) files[i];
                            isDirectory = file.isDirectory();
                        }
                        else{
                            file = new java.io.File(files[i].toString());
                            isDirectory = files[i].toString().replace("\\", "/").endsWith("/"); //<-- Need this check for windows sym links...
                        }

                        if (isDirectory){
                            results.add(new Directory(file));
                        }
                        else{
                            results.add(new File(file));
                        }
                    }
                    Collections.sort(results, new FileComparer(this));
                    if (!wait) results.add(null);
                    return results;                                        
                }
            }
        }
        else{
          //Directory does not exist, return an empty list
            List list = new LinkedList();
            if (!wait) list.add(null);
            return list;
        }
    }

    
  //**************************************************************************
  //** getSharedDrives
  //**************************************************************************
  /**  Returns a list of shared directories found on a given server. */
    
    private java.io.File[] getSharedDrives(String serverName){
        if (isWindows){
            serverName = serverName.replace("/", "\\\\");
            if (serverName.startsWith("\\\\")) serverName = serverName.substring(2);
            if (serverName.contains("\\")) serverName = serverName.substring(0, serverName.indexOf("\\"));            
            boolean doNetView = false;
            
            if (File.loadDLL()){
                try{
                    String drives = File.GetSharedDrives(serverName);
                    if (drives!=null){
                        java.util.ArrayList<java.io.File> files = new java.util.ArrayList<java.io.File>();
                        for (String drive : drives.split("\n")){
                            drive = drive.trim();
                            if (drive.length()>0){
                                String[] arr = drive.split("\t");
                                files.add(new java.io.File("\\\\" + serverName + "\\" + arr[0]));
                            }
                        }
                        if (files.isEmpty()) return null;
                        else return files.toArray(new java.io.File[files.size()]);
                    }
                }
                catch(Exception e){
                    doNetView = true;
                }
            }   
            else{
                doNetView = true;
            }
            
          //If we're still here, something went wrong with the JNI. Try using 
          //net view instead. Note that unlike the JNI, net view won't return 
          //hidden network drives.
            if (doNetView){
            
                javaxt.io.Shell cmd = new javaxt.io.Shell("net view " + serverName);
                cmd.run();
                java.util.List errors = cmd.getErrors();
                errors.remove(null);
                if (errors.isEmpty()){

                    String path = "\\\\" + serverName + "\\";
                    java.util.List output = cmd.getOutput();


                  //Remove empty lines from the standard output stream
                    java.util.List<String> tmp = new java.util.Vector<String>();
                    java.util.Iterator<String> it = output.iterator();
                    while (it.hasNext()){
                        String row = it.next();
                        if (row==null || row.trim().length()==0){} else tmp.add(row);
                    }
                    output = tmp;
                    tmp = null;


                  //Parse the standard output stream and create a list of shared directories
                    java.util.ArrayList<java.io.File> files = new java.util.ArrayList<java.io.File>();
                    int x = 0;
                    int len = -1;
                    it = output.iterator();
                    while (it.hasNext()){
                        String row = it.next();
                        if (row.startsWith("---")){
                            String colHeader = (String) output.get(x-1);
                            if (colHeader.startsWith("Share name") && colHeader.contains("Type")){
                                len = colHeader.indexOf("Type");
                            }
                        }
                        else{

                            if (row.startsWith("The command completed successfully.")){
                                break;
                            }
                            
                            if (len>0 && row.length()>len){
                                String type = row.substring(len);
                                type = type.substring(0, type.indexOf(" "));
                                if (type.equalsIgnoreCase("Disk")){
                                    files.add(new java.io.File(path + row.substring(0, len).trim()));
                                }
                            }
                        }
                        x++;
                    }

                  //Convert the list of files into an array
                    if (files.isEmpty()) return null;
                    else return files.toArray(new java.io.File[files.size()]);

                }            
            }
        }
        return null;
    }
    

  //**************************************************************************
  //** listFiles
  //**************************************************************************
  /**  Used to return a list of files found in this directory. */

    protected Object[] listFiles(){
        return listFiles(null);
    }


  //**************************************************************************
  //** listFiles
  //**************************************************************************
  /**  Used to return a list of files found in this directory.
   *
   *   @param filter A file filter. You can pass in a java.io.FileFilter, a
   *   String (e.g. "*.txt"), or an array of Strings (e.g. String[]{"*.txt", "*.doc"}).
   *   Wildcard filters are supported. Note that the filter is only applied to
   *   files, not directories.
   *
   *   @return An array of java.io.File or an array of Strings representing
   *   paths to files. If the input FileFilter is generated using a 
   *   java.io.FileFilter, the method will return an array of java.io.File. 
   *   Otherwise, this method will return an array of Strings for most cases.
   *   Note that any subdirectories that are found in this directory are ALWAYS
   *   included in the result, regardless of file filter.
   */
    private Object[] listFiles(Object filter){

        FileFilter fileFilter;
        if (filter instanceof FileFilter) fileFilter = (FileFilter) filter;
        else fileFilter = new FileFilter(filter);


        String path = this.path + name;
        

      //Get a list of shared drives on a windows server (e.g. "\\192.168.0.80")
        if (isWindows && path.startsWith("\\\\")){
            
            if (path.endsWith(PathSeparator)) path = path.substring(0, path.length()-1);
            if (!path.substring(2).contains(PathSeparator)){
                java.io.File[] sharedDrives = getSharedDrives(path);
                if (sharedDrives!=null){
                    java.util.ArrayList<java.io.File> files = new java.util.ArrayList<java.io.File>();
                    for (java.io.File file : sharedDrives){
                        if (file.exists()){
                            if (fileFilter==null) {
                                files.add(file);
                            }
                            else{
                                if (fileFilter.accept(file)){
                                    files.add(file);
                                }
                            }
                        }     
                    }
                    return files.toArray(new java.io.File[files.size()]);
                }
                return null;
            }
        }



      //Generate a list of files in this directory that match the file filter
        java.util.List files = new java.util.ArrayList();
        if (isWindows){
            path = getPath();
            String[] list = dir();
            for (int i=0; i<list.length; i++){

                boolean isDirectory = list[i].endsWith("\\");

                if (fileFilter==null || isDirectory){
                    files.add(path + list[i]);
                }
                else{

                    if (fileFilter.usesIOFilter()){
                        java.io.File file = new java.io.File(path + list[i]);
                        if (fileFilter.accept(file)){
                            files.add(file);
                        }
                    }
                    else{
                        if (fileFilter.accept(list[i])){
                            files.add(path + list[i]);
                        }
                    }
                }
            }

        }
        else { //UNIX
            
            java.io.File[] fs = getFile().listFiles();
            if (fs!=null){
            
                for (int i=0; i<fs.length; i++){
                    java.io.File file = fs[i];
                    if (fileFilter==null){
                        files.add(file);
                    }
                    else{
                        if (fileFilter.accept(file)){
                            files.add(file);
                        }
                    }
                }
            }                                     

        }

        if (files.size()<1) return null;
        


      //Sort the list and return an array
        Collections.sort(files, new FileComparer(null));
        return files.toArray(new Object[files.size()]);
    }


  //**************************************************************************
  //** ls
  //**************************************************************************
  /** Used to list contents of a directory using a unix ls command.
   *
    private String[] ls(){

              
        java.util.List<String> files = new java.util.ArrayList<String>();
        try{
            String path = this.getPath();

            
          //Execute a ls command to get a directory listing
            String[] params = new String[]{"ls", "-ap", path};
            javaxt.io.Shell cmd = new javaxt.io.Shell(params);
            java.util.List<String> output = cmd.getOutput();
            cmd.run(true);



          //Parse the output
            String line;
            while (true){
                synchronized (output) {
                    while (output.isEmpty()) {
                        output.wait();
                    }
                    line = output.remove(0);
                }
                if (line!=null){
                    line = line.trim();
                    if (line.length()>0){
                        //System.out.println(line);
                        //boolean isDirectory = (line.endsWith("/"));

                        if (!line.equals("./") && !line.equals("../")){
                            files.add(line);
                        }
                    }
                }
                else{
                    break;
                }
            }
        }
      //Catch Exceptions thrown by cmd.run()
        catch(java.io.IOException e){
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }


      //Convert the Vector to an Array
        String[] arr = new String[files.size()];
        for (int i=0; i<arr.length; i++){
            arr[i] = files.get(i);
        }

        return arr;
    }
    */

  //**************************************************************************
  //** dir
  //**************************************************************************
  /** Listing files in using the listFiles() method is super slow on windows
   *  when listing contents from a network share. The alternative is to use
   *  list() method which is MUCH faster. Unfortunately, there's no way to
   *  distinguish between files and directories in the array. My only recourse
   *  is to either use a JNI or shell out a "dir" command and parse the output.
   *  @return An array of strings representing the names of files and 
   *  directories found in this directory. Note that directory names include
   *  a path separator which is used to distinguish files from directories.
   */
    private String[] dir(){

        java.util.ArrayList<String> files = new java.util.ArrayList<String>();
      
      //Try listing files using the JNI
        boolean doDir = false;
        if (File.loadDLL()){
            try{
                String list = File.GetFiles(getPath() + "*");
                if (list!=null){
                    for (String name : list.split("\n")){
                        name = name.trim();
                        if (name.length()>0 && !(name.equals(".\\") || name.equals("..\\")))
                        files.add(name);
                    }
                }
            }
            catch(Exception e){
                doDir = true;
            }
        }
        else{
            doDir = true;
        }
        
        
      //If we're still here, list files using a command prompt
        if (doDir)
        try{
            String path = this.getPath();
            if (path.contains(" ")) path = "\"" + path + "\"";

          //Execute a windows shell command to get a directory listing
            javaxt.io.Shell cmd = new javaxt.io.Shell("cmd.exe /c dir /OG " + path);
            java.util.List<String> output = cmd.getOutput();
            cmd.run(true);

          //Parse files returned from the directory listing
            boolean parseFiles = false;
            int colWidth = -1;
            String line;
            while (true){
                synchronized (output) {
                    while (output.isEmpty()) {
                        output.wait();
                    }
                    line = output.remove(0);
                }


                if (line!=null){
                    if (line.length()==0 || line.startsWith(" ")){
                        if (parseFiles==true) parseFiles = false;
                    }
                    else{
                        if (parseFiles==false) parseFiles = true;


                        if (parseFiles){


                            if (colWidth<0){
                                int offset = 20;
                                //String date = line.substring(0, 20);

                              //Get File Type
                                String type = line.substring(offset);
                                if (type.trim().startsWith("<")){
                                    offset += type.indexOf(">")+1;
                                    type = type.substring(type.indexOf("<"), type.indexOf(">")+1);
                                }
                                else{
                                    type = "";
                                }
                                boolean isDirectory = (type.contains("<DIR>"));
                                boolean isSymLink = (type.contains("<SYMLINK>"));
                                boolean isJunction = (type.contains("<JUNCTION>"));

                              //Get File Name
                                String name = line.substring(offset);
                                while (name.substring(0, 1).equals(" ")){
                                    name = name.substring(1);
                                    offset++;
                                }


                              //Set Column Width
                                if (isDirectory || isSymLink || isJunction){
                                    colWidth = offset;
                                }
                                else{
                                    if (name.contains(" ")){
                                        if (isNumeric(name.substring(0, name.indexOf(" ")))){
                                            colWidth = offset + name.indexOf(" ")+1;
                                        }
                                        else{
                                            colWidth = offset;
                                        }
                                    }
                                    else{
                                        colWidth = offset;
                                    }
                                }
                            }
                            if (colWidth>0){
                                //String date = line.substring(0,20);
                                String name = line.substring(colWidth);
                                String type = line.substring(20, colWidth);
                                boolean isDirectory = (type.contains("<DIR>"));
                                boolean isSymLink = (type.contains("<SYMLINK>"));
                                boolean isJunction = (type.contains("<JUNCTION>"));

                                if (isDirectory){
                                    if (!name.equals(".") && !name.equals(".."))
                                    files.add(name + this.PathSeparator);
                                }
                                else if (isSymLink || isJunction){
                                    java.io.File file = null; //<--Note that we're serializing to a file which is going to slow things down...
                                    if (name.contains("[") && name.contains("]")) {
                                        String link = name.substring(name.indexOf("[")+1, name.indexOf("]"));
                                        name = name.substring(0, name.indexOf("[")).trim();
                                        file = new java.io.File(link);
                                    }
                                    else {
                                        file = new java.io.File(path, name);
                                    }
                                    if (file.isDirectory()) name += this.PathSeparator;
                                    files.add(name);
                                }
                                else{
                                    files.add(name);
                                }
                            }

                        }

                    }

                }
                else{
                    break;
                }
            }

        }
      //Catch Exceptions thrown by cmd.run()
        catch(java.io.IOException e){
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        
      //Convert the list to an array
        return files.toArray(new String[files.size()]);
    }


  //**************************************************************************
  //** isNumeric
  //**************************************************************************
  /**  Used to determine whether a string is a number. This is used in the
   *   dir() method when parsing output from a Windows "dir" command. This
   *   method is called to see if a string represents a file size (e.g. 12,345).
   *   Since the number is meant to represent a file size, an attempt it made
   *   to convert the string to a long instead of an int or double.
   */
    private boolean isNumeric(String str){
        try{
            if (str.contains(",")) str = str.replace(",", "");
            Long.parseLong(str);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    

    @Override
  //**************************************************************************
  //** toString
  //**************************************************************************
  /**  Returns the full path to this directory, including the directory name.
   */ 
    public String toString(){
        return getPath();
    }


    @Override
    public int hashCode(){
        return getFile().hashCode();
    }

    //@Override
    public int compareTo(Object obj){
        if (obj==null) return -1;
        else return -obj.toString().compareTo(getPath());
    }


    @Override
  //**************************************************************************
  //** equals
  //**************************************************************************
  
    public boolean equals(Object obj){
        if (obj instanceof Directory){
            return getFile().equals(((Directory) obj).toFile());
        }
        else if (obj instanceof java.io.File){
            if (((java.io.File) obj).isDirectory()) 
                return getFile().equals(obj);
            else
                return false;
        }
        else{
            return false;
        }
    }


  //**************************************************************************
  //** clone
  //**************************************************************************
  /** Creates a copy of this object. */

    public Directory clone(){
        return new Directory(this.toString());
    }


  //**************************************************************************
  //** getEvents
  //**************************************************************************
  /** Used to start monitoring changes made to the directory. Changes include 
   *  creating, modifying or deleting files/folders found in this directory.
   *  Returns a list of Directory.Event(s). Clients can wait for new events
   *  using the wait() method. Recommend removing events from the list whenever
   *  !events.isEmpty().
   * 
   *  Example:<pre>
    java.util.List events = directory.getEvents();
    while (true){

        Object obj;
        synchronized (events) {
            while (events.isEmpty()) {
              try {
                  events.wait();
              }
              catch (InterruptedException e) {
              }
            }

            obj = events.remove(0);
        }
        if (obj!=null){

            javaxt.io.Directory.Event event = (javaxt.io.Directory.Event) obj;
            System.out.println(event.toString());

          //Compare files before/after the event
            if (event.getEventID()==event.RENAME){
                System.out.println(
                    event.getOriginalFile().getName() + " vs " +
                    event.getFile().getName()
                );
            }
        }

    }</pre>
   */
    public List getEvents() throws Exception {

        if (FileSystemWatcher==null){
            FileSystemWatcher = new FileSystemWatcher(this);
            new Thread(FileSystemWatcher).start();
        }
        return FileSystemWatcher.getEvents();
    }

  //**************************************************************************
  //** Stop
  //**************************************************************************
  /** Used to stop any worker threads that may be running (recursive search or
   *  event monitor).
   */
    public void stop(){
        if (FileSystemWatcher!=null) FileSystemWatcher.stop();

        try{
            //for (int i=0; i<20; i++)
            DirectorySearch.stop();
        }
        catch(Exception e){}
    }
    
    
  //**************************************************************************
  //** Finalize
  //**************************************************************************
  /** Method called by Java garbage collector to dispose operating system 
   *  resource.
   */
    protected void finalize() throws Throwable {
       stop();
       super.finalize();
    }
    
    
  //**************************************************************************
  //** Event Class
  //**************************************************************************
  /**  Used to encapsulate a single event on the file system. 
  */
    public static class Event {

        private String file;
        private String orgFile;
        private java.util.Date date;
        private String action;
        
        public static final int DELETE = 0;
        public static final int CREATE = 1;
        public static final int RENAME = 2;
        public static final int MODIFY = 3;

      //************************************************************************
      //** Constructor
      //************************************************************************
      /**  Creates a new instance of FileSystemEvent by parsing a string 
       *   returned from FileSystemWatcherNative.ReadDirectoryChangesW()
       */
        protected Event(String event) {
            if (event!=null){
                event = event.trim();
                if (event.length()==0) event = null;
            }
            if (event!=null){
                try{

                  //Parse event string
                    String date = event.substring(1,event.indexOf("]")).trim();
                    String text = event.substring(event.indexOf("]")+1).trim();
                    String path = text.substring(text.indexOf(" ")).trim();
                    String action = text.substring(0,text.indexOf(" ")).trim();
                    
                    

                  //Set local variables
                    this.date = parseDate(date);
                    this.file = path;
                    this.action = action;

                }
                catch(Exception ex){
                    //ex.printStackTrace();
                }
            }
        }

      //************************************************************************
      //** Constructor
      //************************************************************************
      /**  Creates a new instance of FileSystemEvent for EventMonitor 
       */
        protected Event(String action, String file){
            this.date = new java.util.Date();
            this.action = action;
            this.file = file;
        }

        
      //************************************************************************
      //** parseDate
      //************************************************************************
        private java.util.Date parseDate(String date){
            try{
                return new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy").parse(date);
            }
            catch(Exception e){
                return null;
            }        
        }


      //************************************************************************
      //** getAction
      //************************************************************************
      /**  Returns a decription of the event (created, modified, deleted, etc.) 
       */
        public String getAction(){
            return action;
        }

        protected void setAction(String action){
            this.action = action;
        }
        
      //************************************************************************
      //** getFile
      //************************************************************************
      /**  Returns the file or directory that was created, modified, or deleted. 
       */
        public String getFile(){
            return file;
        }
        
        
      //************************************************************************
      //** getOriginalFile
      //************************************************************************
        public String getOriginalFile(){
            return orgFile;
        }
        
        protected void setOrgFile(String orgFile){
            this.orgFile = orgFile;
        }
        
        
      //************************************************************************
      //** getEventID
      //************************************************************************
        public final int getEventID(){
            if (action.equalsIgnoreCase("create")) return this.CREATE;
            if (action.equalsIgnoreCase("delete")) return this.DELETE;
            if (action.equalsIgnoreCase("modify")) return this.MODIFY;
            if (action.equalsIgnoreCase("rename")) return this.RENAME;
            return -1;
        }

      //************************************************************************
      //** getDate
      //************************************************************************
      /**  Returns the date/time stamp when the event occured. */

        public java.util.Date getDate(){
            return date;
        }

      //************************************************************************
      //** toString
      //************************************************************************
      /**  Returns a string representation of this event. */

        public String toString(){
          if (action.equalsIgnoreCase("rename"))
            return "[" + date.toString() + "] " + action + " " + orgFile + " To " + file;
          else
            return "[" + date.toString() + "] " + action + " " + file;
        }

        public boolean equals(Object obj){
            //return obj.toString().equalsIgnoreCase(action);
            if (obj instanceof Event){
                Event event = (Event) obj;
                if (event.getFile().equals(this.file) && 
                    event.getDate().equals(this.date) && 
                    event.getAction().equals(this.action)){
                    return true;
                }
            }
            return false;
        }
        
    } //End Event Class

    
    
    
  //**************************************************************************
  //** FileComparer Class
  //**************************************************************************
  /**  Used to sort a list containing files/folders in alphabetical order.
   *   Note that directories are listed first. 
   */
    private class FileComparer implements Comparator {

        private int z;

        public FileComparer(Directory dir){
            if (dir==null) z = 0;            
            else z = dir.toString().replace("\\", "/").length();
        }

        public final int compare(Object a, Object b){

            String x = a.toString().toUpperCase();
            String y = b.toString().toUpperCase();
            x = x.replace("\\", "/").substring(z);
            y = y.replace("\\", "/").substring(z);


          //Check whether a or b is a file in the root directory.
            if (!x.contains("/") || !y.contains("/")){

              //If both a and b are files, compare file names. Use a multiplier
              //to ensure that the file falls toward the end of the list.
                if (!x.contains("/") && !y.contains("/")){
                    return (x.compareTo(y)) * 10000; 
                }
                else{
                    return 100000;
                }
            }
            else{
              //Niether a or b are in the root directory


              //Check whether a and b are in the same directory
                String dir1 = x.substring(0, x.lastIndexOf("/"));
                String dir2 = y.substring(0, y.lastIndexOf("/"));

                if (!dir1.equals(dir2)){
                    return dir1.compareTo(dir2);
                }
                else{

                  //a and b are in the same directory

                    return x.compareTo(y);
                }
            }
            

        }
        
    } //End FileComparer Class


} //End Directory Class




//******************************************************************************
//**  FileFilter
//******************************************************************************
/**
 *   Used to filter files and file names using regular expressions or java.io
 *   FileFilters. Note that directories are always returned.
 *
 ******************************************************************************/

class FileFilter {

    private java.io.FileFilter fileFilter = null;
    private List<java.util.regex.Pattern> regex = null;


    public FileFilter(Object filter){

        if (filter==null){
            filter = "*";
        }

        if (filter instanceof java.io.FileFilter){
            fileFilter = (java.io.FileFilter) filter;
        }

        if (filter instanceof String){
            filter = new String[]{(String) filter};
        }

        if (filter instanceof String[]){
            regex = new ArrayList<java.util.regex.Pattern>();
            String[] filters = (String[]) filter;

            for (int i=0; i<filters.length; i++){
                regex.add(
                        java.util.regex.Pattern.compile(
                            getRegEx(filters[i]),
                            java.util.regex.Pattern.CASE_INSENSITIVE
                        )
                );
            }
        }
    }


    public boolean accept(String file){
        if (fileFilter!=null){
            return accept(new java.io.File(file));
        }
        else{
            file = file.replace("\\", "/");
            if (file.endsWith("/")){ //then we're dealing with a directory
                return true;
            }
            else{
                file = file.substring(file.lastIndexOf("/")+1);
            }

            for (int i=0; i<regex.size(); i++){
                java.util.regex.Matcher matcher = regex.get(i).matcher(file);
                if (matcher.find()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean accept(java.io.File file){
        if (file.isDirectory()) {
            return true;
        }
        else{
            if (fileFilter!=null){
                return (fileFilter.accept(file));
            }
            else{
                return accept(file.toString());
            }
        }
    }

  //**************************************************************************
  //** usesIOFilter
  //**************************************************************************
  /**  Used to indicate whether this filter relies on a java.io.FileFilter
   */
    public boolean usesIOFilter(){
        return (fileFilter!=null);
    }


  //**************************************************************************
  //** getRegEx
  //**************************************************************************
  /**  Used to convert a wildcard (e.g. "*.txt") into a regular expression.
   */
    private String getRegEx(String wildcardSearch){
        String regex = wildcardSearch.trim();
        //if (!regex.startsWith("*")) regex = "^" + regex;
        if (!regex.endsWith("*")) regex += "$";

        if (regex.endsWith("*")){
            //regex = regex.substring(0, regex.length()-1) + "/";
            regex = regex.substring(0, regex.length()-1) + ")";
            if (regex.contains("*")){
                regex = regex.substring(0, regex.lastIndexOf("*")+1) + "(" + regex.substring(regex.lastIndexOf("*")+1);
            }
            else{
                regex = "(" + regex;
            }
        }

        regex = regex.replace(".", "\\.");
        regex = regex.replace("*", ".*");
        //System.out.println(regex);
        return regex;
    }

}//End FileFilter Class


//******************************************************************************
//**  Directory Search
//******************************************************************************
/**
 *   Thread used crawl through a file system and find files/folders. This is a
 *   recursive search and may take some time to complete.
 *
 ******************************************************************************/

class DirectorySearch implements Runnable {



    private static java.util.concurrent.ConcurrentHashMap map;
    private static java.util.concurrent.ConcurrentHashMap lut;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /**  Creates a new instance of DirectorySearch. Note that this thread needs
   *   to be named! Unfortunately, this has to happen outside of this class.
   *   Example:
   *
   <pre>
      Thread t = new Thread(new DirectorySearch(filter, files));
      t.setName("DirectorySearch-"+id);
      t.start();
   </pre>
   *
   *   @param filter A file filter.    *
   *   @param items A List (e.g. LinkedList) used to store items found in this
   *   directory.
   */
    public DirectorySearch(FileFilter filter, List items, long directoryID, int numThreads) {


        String parentThread = Thread.currentThread().getName();
        //System.out.println(parentThread);

        if (map==null) {
            map = new ConcurrentHashMap();
        }

        

        if (getVars()==null){
            createVars(filter, items, directoryID, numThreads);
        }


        if (lut==null) lut = new ConcurrentHashMap();
        synchronized(lut){
            if (lut.get(parentThread)==null)
                lut.put(parentThread, directoryID);
            else{

                //System.out.println(parentThread + " entering wait state...");
                while (!lut.isEmpty()) {
                    try {
                        lut.wait();
                    }
                    catch (InterruptedException e) {
                      //If interrupted, return immediately
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                //System.out.println(parentThread + " exited wait state!");

                createVars(filter, items, directoryID, numThreads);

                lut.put(parentThread, directoryID);
                lut.notifyAll();
            }
        }

        //System.out.println(parentThread + " " + lut.get(parentThread) + " vs " + directoryID);
    }


  //**************************************************************************
  //** createVars
  //**************************************************************************
  /** Used to instantiate local variables used by this thread and it's siblings.
   *  The variables are all stored in a hashmap and accessed via a directoryID.
   */
    private static void createVars(FileFilter filter, List items, long directoryID, int numThreads){
        synchronized(map){
            ConcurrentHashMap vars = new ConcurrentHashMap();
            vars.put("items", items);
            vars.put("filter", filter);
            vars.put("pool", new java.util.LinkedList());
            vars.put("path", new java.util.LinkedList());
            vars.put("status", new java.util.LinkedList());
            vars.put("threads", new LinkedList()); //<--list of active threads
            vars.put("activatedThreads", new ConcurrentHashMap());
            vars.put("numThreads", numThreads);
            map.put(directoryID, vars);
        }
    }



  //**************************************************************************
  //** getVars
  //**************************************************************************
  /** Used to retrieve variables associated with this thread.
   */
    private static ConcurrentHashMap getVars(){

        String currentThread = Thread.currentThread().getName();
        if (currentThread.startsWith("DirectorySearch_")){
            Long directoryID = getDirectoryID();
            return (ConcurrentHashMap) map.get(directoryID);
        }
        else{

            if (lut==null) return null;

            Long directoryID = null;
            synchronized(lut){
                directoryID = (Long) lut.get(currentThread);
            }

            //System.out.println(currentThread + " vs " + directoryID);

            if (directoryID==null) return null;
            else return (ConcurrentHashMap) map.get(directoryID);            
        }
    }


  //**************************************************************************
  //** deleteCache
  //**************************************************************************

    public static void deleteCache(){

        if (map!=null){
            ConcurrentHashMap vars = getVars();
            if (vars!=null){
                synchronized(vars){
                    List path = (List) vars.get("path");
                    List items = (List) vars.get("items");
                    List status = (List) vars.get("status");


                    synchronized(path){ path.clear(); }
                    synchronized(items){ items.clear(); }
                    synchronized(status){ status.clear(); }

                    vars.remove("startTime");
                    vars.remove("root");
                }
            }
        }
    }


    private static Long getDirectoryID(){
        try{
            String id = Thread.currentThread().getName();
            id = id.substring(id.indexOf("_")+1);
            id = id.substring(0,id.indexOf("-"));
            //System.out.println(id + " vs " + Thread.currentThread().getName());
            return Long.valueOf(id).longValue();
        }
        catch(Exception e){
            return 0L;
        }         
    }




  //**************************************************************************
  //** Stop
  //**************************************************************************
  /** Used to stop the current thread. */

    public static void stop(){
        updatePool(null);
    }


  //**************************************************************************
  //** updatePool
  //**************************************************************************
  /**  Used to add a new directory to the pool. */

    public static void updatePool(Directory directory) {

        ConcurrentHashMap vars = getVars();
        Long startTime = null;
        if (vars.get("startTime")!=null) startTime = (Long) vars.get("startTime");
        


      //if start time is null, then this is the first time the pool has been modified
        if (startTime==null){
            synchronized(vars){
                vars.put("startTime", getStartTime());
                vars.put("root", directory);
                vars.notifyAll();
            }            
        }


        boolean updatePool = true;
        if (directory == null){
            if (Thread.currentThread().getName().equalsIgnoreCase("Finalizer")){
                updatePool = false;
            }
        }

        if (updatePool){
            List pool = (List) vars.get("pool");
            synchronized (pool) {
                pool.add(directory);
                pool.notifyAll();
            }
        }


    }


  //**************************************************************************
  //** Add File
  //**************************************************************************
  /**  Used to add a file to the list of items found. */

    private static void addFile(File file) {

        ConcurrentHashMap vars = getVars();
        List items = (List) vars.get("items");

        synchronized (items) {
            items.add(file);
            items.notifyAll();
        }
    }

  //**************************************************************************
  //** Add Directory
  //**************************************************************************
  /**  Used to add a directory to the list of items found. */

    private static void addDirectory(Directory directory){

        ConcurrentHashMap vars = getVars();
        List items = (List) vars.get("items");
        Directory root = (Directory) vars.get("root");

        synchronized (items) {
            if (!directory.equals(root)){
                items.add(directory);
                items.notifyAll();
            }
        }
    }




    private static void addPath(Directory directory) {
        List path = (List) getVars().get("path");
        synchronized (path) {
            path.add(directory.toString());
        }
    }

    private static void removePath(Directory directory) {
        List path = (List) getVars().get("path");
        synchronized (path) {
            path.remove(directory.toString());
        }
    }


  //**************************************************************************
  //** Get Status
  //**************************************************************************
  /**  Used to retrieve the status object. The status object is a List that
   *   will remain empty until all of the threads have completed searching
   *   the file system. While status.isEmpty() call status.wait();
   */
    public static List getStatus(){

        ConcurrentHashMap vars = getVars();
        List status = (List) vars.get("status");

        return status;
    }


  //**************************************************************************
  //** Update Status
  //**************************************************************************
  /**  Used to insert an entry into the status object. This is used to notify
   *   the client that all threads have completed searching the file system.
   */
    private static void updateStatus(){

        ConcurrentHashMap vars = getVars();
        List status = (List) vars.get("status");
        List items = (List) vars.get("items"); 
        Long startTime = (Long) vars.get("startTime");

        synchronized (status) {
         status.add("ellapsedTime = " + getEllapsedTime(startTime) + " ms");
         status.notifyAll();
         //System.out.println(Thread.currentThread().getName() + " Updated Status!");
        }
        synchronized (items) {
          items.add(null);
          items.notifyAll();
        }


        ((List) vars.get("pool")).clear();
        ((List) vars.get("path")).clear();
        ((List) vars.get("threads")).clear();
        ((ConcurrentHashMap) vars.get("activatedThreads")).clear();



      //Update the lut
        synchronized(lut){
            long directoryID = getDirectoryID();
            java.util.Iterator it = lut.keySet().iterator();
            while (it.hasNext()){
                String parentThread = (String) it.next();
                if ((Long) lut.get(parentThread) == directoryID){
                    lut.remove(parentThread);
                    lut.notifyAll();
                    break;
                }
            }
        }

    }


  //**************************************************************************
  //** Get Items
  //**************************************************************************
  /**  Used to retrieve a list of files and folders found in this directory.
   */
    public static List getItems(){
        return (List) getVars().get("items");
    }






  //**************************************************************************
  //** Run
  //**************************************************************************
  /** Processes entries in the pool. Waits and add children to the array. */

    public void run() {

      //Get shared variables
        ConcurrentHashMap vars = getVars();
        if (vars==null) return; //<--This should never happen!
        
        List threads = (List) vars.get("threads");
        ConcurrentHashMap activatedThreads = (ConcurrentHashMap) vars.get("activatedThreads");
        List path = (List) vars.get("path");
        List pool = (List) vars.get("pool");
        FileFilter filter = (FileFilter) vars.get("filter");
        int numThreads = (Integer) vars.get("numThreads");
        String threadID = Thread.currentThread().getName();



        while (true) {
            Directory dir;

          //Wait for new directories to be added to the pool
            synchronized (pool) {
                while (pool.isEmpty()) {
                    try {
                        pool.wait();
                    }
                    catch (InterruptedException e) {
                      //If interrupted, return immediately
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

              //Update the list of active threads
                synchronized (threads){threads.add(threadID);}
                synchronized (activatedThreads){
                    if (activatedThreads.get(threadID)==null){
                        activatedThreads.put(threadID, true);
                    }
                }



                Object obj = pool.get(0);
                if (obj==null){
                    //System.out.println("Terminating Thread: " + threadID);
                    synchronized (threads){threads.remove(threadID);}
                    synchronized(activatedThreads){
                        activatedThreads.replace(threadID, false);                        
                        if (activatedThreads.size()==numThreads){
                            if (!activatedThreads.containsValue(true)){
                                updateStatus();
                            }
                        }
                    }
                    return;
                }
                else{
                    dir = (Directory) obj;
                    pool.remove(obj);
                    pool.notifyAll();
                }
            }


          //Process directory
            if (dir!=null) {
                
              //Notify Path/Status Object of new task
                addPath(dir);



              //Add subdirectories to the processing pool and insert files into file array
                Object[] items = dir.listFiles();
                if (items!=null){
                    for (int i=0; i<items.length; i++){
                        Object obj = items[i];
                        boolean isDirectory = false;
                        boolean accept = false;
                        if (obj instanceof String){
                            String s = (String)obj;
                            accept = filter.accept(s);
                            isDirectory = s.replace("\\", "/").endsWith("/");
                        }
                        else if (obj instanceof java.io.File){
                            java.io.File f = (java.io.File)obj;
                            accept = filter.accept(f);
                            isDirectory = f.isDirectory();
                        }
                        
                        if (accept){
                            
                            if (isDirectory){

                              //Add directory to the array and update the pool
                                Directory d = null;
                                if (obj instanceof String){
                                    d = (new Directory((String)obj));
                                }
                                else if (obj instanceof java.io.File){
                                    d = (new Directory((java.io.File)obj));
                                }
                                if (d!=null){
                                    addDirectory(d);
                                    updatePool(d);
                                }
                            }
                            else{

                             //Add File to the array
                                if (obj instanceof String){
                                    addFile(new File((String)obj));
                                }
                                else if (obj instanceof java.io.File){
                                    addFile(new File((java.io.File)obj));
                                }

                            }
                        }
                    }
                }



              //Notify Path/Status Object of task completion
                removePath(dir);


                synchronized (threads){
                  threads.remove(threadID);
                }


              //Check whether we're done processing all the directories and notify the client
                synchronized (path) {
                  if (path.isEmpty()){
                      synchronized (pool) {
                          if (pool.isEmpty()){
                              synchronized (threads){
                                if (threads.isEmpty()){
                                    //System.out.println("Terminating Thread: " + threadID + "*");
                                    synchronized(activatedThreads){activatedThreads.replace(threadID, false);}
                                    updatePool(null);
                                    return;
                                }
                              }
                          }
                      }
                  }
                }



            }
            /*
            else{
              //Found null entry. This is our que to stop waiting for new directories...
                //System.out.println("Terminating Thread: " + Thread.currentThread().getName());


                

                synchronized (threads){
                    String threadID = Thread.currentThread().getName();
                    threads.remove(threadID);
                }

                
                synchronized (activatedThreads){
                    activatedThreads.replace(Thread.currentThread().getName(), false);
                    if (activatedThreads.containsValue(true)){
                        updatePool(null);
                    }
                    else{
                        
                        if (activatedThreads.size()<numThreads){
                            updatePool(null);
                        }
                        else{
                            //System.out.println(this.getDirectoryID() + " activated " + activatedThreads.size() + " threads");
                            updateStatus();
                        }
                    }
                }

                return;
            }
            */


        }//end while


    } // end run



  //**************************************************************************
  //** Get Start Time
  //**************************************************************************
  /**  Used to set the start time. Returns the current time in milliseconds. */

    private static long getStartTime(){
        return java.util.Calendar.getInstance().getTimeInMillis();
    }


  //**************************************************************************
  //** Get Ellapsed Time
  //**************************************************************************
  /**  Used to compute the ellapsed time in milliseconds. */

    private static long getEllapsedTime(long StartTime){
        try{
            long endTime = java.util.Calendar.getInstance().getTimeInMillis();
            return endTime-StartTime;
        }
        catch(Exception e){
            return 0;
        }
    }

}//End Directory Search







//******************************************************************************
//**  FileSystemWatcher Class - By Peter Borissow
//******************************************************************************
/**
 *   Used to watch changes made to files and folders (subdirectories) found in 
 *   a directory. Provides an option to monitor NTFS events on Windows-based 
 *   machines. This is an extremely efficient way to monitor a file system. 
 *   Unlike more traditional methods, this approach minimizes the amount of  
 *   disk i/o required and significantly reduces memory consumption. Note that
 *   this approach only works on Windows 2000, XP, or later and you need a
 *   dynamic link library called javaxt-core.dll. Again, this is just an option.
 *   For non-Windows operation systems, the FileWatcher class will periodically
 *   scan the file system for updates. 
 *
 ******************************************************************************/

class FileSystemWatcher implements Runnable {
    
    private Directory directory;
    private Timer timer;
    private boolean includeSubdirectories = true;
    private boolean terminationRequested = false;
    private Long osHandle = null;
    
    private List events = new LinkedList();
    private Directory.Event LastEvent = null;

  //**************************************************************************
  //** Constructors
  //**************************************************************************   
  /** Creates a new instance of FileSystemWatcher using a directory and a dll.
   */ 
    public FileSystemWatcher(Directory directory) throws java.io.IOException {
        if (!directory.exists()) throw new java.io.IOException("Directory not found.");
        this.directory = directory;
    }
    
    
  //**************************************************************************
  //** Run
  //**************************************************************************
    
    public final void run(){ 

        if (!File.loadDLL()){
            this.timer = new Timer();
            timer.schedule( new EventMonitor(), new java.util.Date(), 1000 );
        }
        else{
            try {
                long osWaitHandle = FileSystemWatcherNative.FindFirstChangeNotification(directory.getPath(), includeSubdirectories, -1);
                this.osHandle = new Long(osWaitHandle);

                FileSystemWatcherNative.FindNextChangeNotification(osWaitHandle);

              //Process events. Note that the ReadDirectoryChangesW method will
              //block until the next event comes in.
                String event = null;
                while(!terminationRequested && ( event = FileSystemWatcherNative.ReadDirectoryChangesW()) != null){
                    for (String e : event.split("\n")) addEvent(e.trim());
                }

              //I have no idea what this block of code does. Probably safe to delete.
                if (FileSystemWatcherNative.WaitForSingleObject(osWaitHandle,
                    FileSystemWatcherNative.INFINITE) !=  FileSystemWatcherNative.WAIT_OBJECT_0) {
                    throw new Exception("Wait failed while waiting for OS to signal file system event.");
                }
            }
            catch (Exception ex) {
              //nothing can be done here except logging the error.
              //Logger.getLogger("FileSystemWatcher").log(Level.WARNING, "Exception encountered.", ex);
            }
            finally {
                if (this.osHandle != null) {
                    System.out.println("Shutting down...");
                    try {
                      FileSystemWatcherNative.FindCloseChangeNotification(this.osHandle.longValue());
                    }
                    catch (Exception ex2) {
                      //nothing can be done here except logging the error.
                      //Logger.getLogger("FileSystemWatcher").log(Level.WARNING,
                      //"Unable to close file system watch handle.", ex2);
                    }
                    this.osHandle = null;
                }
            }
        }
    }
    
    
  //**************************************************************************
  //** addEvent
  //**************************************************************************
    
    private void addEvent(String str){

        Directory.Event event = new Directory.Event(str);
        String action = event.getAction();
        String path = event.getFile();
        java.util.Date date = event.getDate();
        
        
        boolean exists = true;
        boolean isDirectory = false;
        try{
            isDirectory = new File.FileAttributes(path).isDirectory();
        }
        catch(Exception e){
            exists = false;
        }        
        
        boolean updateEvents = true;


      //Determine whether to update the events list when a file or folder is modified
        if (action.equalsIgnoreCase("modify")){
            if (isDirectory || !exists){
                updateEvents = false;
            }
            else{
                if (LastEvent!=null) { 
                    if (LastEvent.getFile().equals(path)){
                        if (LastEvent.getDate().equals(date)){
                            updateEvents = false;
                        }
                    }
                    LastEvent = null;
                }
                else{
                    LastEvent = event;
                }
            }
        }


      //Special Case: If a rename event is encountered, wait for the "renam2"
      //event before updating the events list. 
        if (action.equalsIgnoreCase("rename")){
            updateEvents = false;
            LastEvent = event;
        }
        else if(action.equalsIgnoreCase("renam2")){
            if (LastEvent!=null) {                            
                event.setOrgFile(LastEvent.getFile());
                event.setAction("Rename");
                LastEvent = null;
            }
        }


      //Update events list, as needed
        if (updateEvents){
            synchronized(events){
                events.add(event);
                events.notifyAll();
                //System.out.println(events.size());
            }
        }
    }
    

  //**************************************************************************
  //** getEvents
  //**************************************************************************
    
    public List getEvents() {
        return events;
    }
    
    
  //**************************************************************************
  //** Stop
  //**************************************************************************
    
    public void stop(){
        
        terminationRequested = true;

        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    
  //**************************************************************************
  //** EventMonitor
  //**************************************************************************
  /** Used to periodically check for changes made to the file system. This 
   *  class is only used on non-windows machines.
   */
    private class EventMonitor extends TimerTask {
        
        private List index = null;
        private long lastUpdate = 0;
        private long interval = 0;
    
        public EventMonitor(){
            this.index = createIndex();
        }
        
        public final void run() {
            //System.out.println((lastEvent-lastUpdate) + " vs " + interval);
            long startTime = java.util.Calendar.getInstance().getTimeInMillis();
            if (interval==0) interval = 100;
            if ((startTime-lastUpdate)>(interval*2)){
                //System.out.println((startTime-lastUpdate) + " vs " + (interval));
                
                List orgIndex = index;
                List newIndex = createIndex();

                for (int i=0; i<newIndex.size(); i++){
                    Item item = (Item) newIndex.get(i);
                    if (orgIndex.contains(item)){
                        int x = orgIndex.indexOf(item);
                        Item orgItem = (Item) orgIndex.get(x); 
                        orgIndex.remove(x);
                        if (item.isDirectory()==false){
                            if (item.getSize()!=orgItem.getSize() || 
                                item.getDate()!=orgItem.getDate())
                            {
                                addEvent("Modify",item.getPath());
                            }
                        }
                    }
                    else{
                        addEvent("Create",item.getPath());
                    }
                }
                if (orgIndex.size()>0){ 
                    for (int i=0; i<orgIndex.size(); i++){
                        Item item = (Item) orgIndex.get(i);
                        addEvent("Delete",item.getPath());
                    }
                    orgIndex.clear();
                }
                
                index = newIndex;
                long endTime = java.util.Calendar.getInstance().getTimeInMillis();
                interval = endTime-startTime;
                //System.out.println(interval);
                lastUpdate = endTime;

                
            }// end if
        }// end run
        
        
        private void addEvent(String action, String file){
            Directory.Event event = new Directory.Event(action,file);
            synchronized (events) {
                events.add(event);
                events.notifyAll();
            }
        }
        

      //************************************************************************
      //** createIndex
      //************************************************************************
      /** Used to create an array of files and folders found in a directory. 
       *   Also used to update the interval variable used by the EventMonitor.
       */
        private List createIndex(){
            List index = new LinkedList();
            java.util.List files = directory.getChildren(true, null, false);
            Object obj;
            while (true){
                synchronized (files) {
                    while (files.isEmpty()) {
                      try {
                          files.wait();
                      }
                      catch (InterruptedException e) {
                          break;
                      }
                    }
                    obj = files.remove(0);
                    files.notifyAll();
                }

                if (obj==null){
                    break;
                }
                else{
                    index.add( new Item(obj) );
                }
            }
            return index;
        }
        
        
        
      //************************************************************************
      //** Item Class
      //************************************************************************
      /**  Used to represent a single file or folder. Records the size and date 
       *   of the file/folder at the time this object is instantiated. This 
       *   information is used to determine which items have changed state 
       *   within the EventMonitor class.
       */
        private class Item {

            private String path;
            private long size;
            private long date;
            private boolean isDirectory;

            public Item(Object obj){

                java.io.File file = null;

                if (obj instanceof File) file=((File) obj).toFile();
                else if (obj instanceof Directory) file=((Directory) obj).toFile();
                else if (obj instanceof java.io.File) file=(java.io.File) obj;

                if (file!=null){
                    this.path = file.getPath();
                    this.size = file.length();
                    this.date = file.lastModified();
                    this.isDirectory = file.isDirectory();
                }
            }

            public String getPath() { return path; }
            public long getSize() {  return size; }
            public long getDate() { return date; }
            public boolean isDirectory() { return isDirectory; }
            public String toString(){ return path; }

            public boolean equals(Object obj){

                if (obj instanceof Item){
                    Item item = (Item) obj;
                    return item.getPath().equals(this.getPath());
                }
                else{
                    return false;
                }
            }

        } // End Item
        
    } //End EventMonitor Class
    
    
  //**************************************************************************
  //** Finalize
  //**************************************************************************
  /** Method called by Java garbage collector to dispose operating system 
   *  resource.
   */
    protected void finalize() throws Throwable {
      if (this.osHandle != null) {
          FileSystemWatcherNative.FindCloseChangeNotification(this.osHandle.longValue());
      }
    }

} //End FileSystemWatcher Class



//******************************************************************************
//**  FileSystemWatcherNative Class
//******************************************************************************
/**
 *   Java wrapper of Windows native APIs for file system monitoring. The name of 
 *   the methods in this class corresponds to the name of Windows native APIs. 
 *   You might want to check Windows Platform SDK for detailed technical 
 *   information. <p/>
 *
 *   Also note that if you change the package or method name of this class, you 
 *   need to use javah to regenerate a C header file and recompile the dll file. 
 *   As far as I know, there is no way around it because of the way JVM binds to
 *   native dll.
 *
 ******************************************************************************/

final class FileSystemWatcherNative {
    
    static {}
    
    public static native long FindFirstChangeNotification(String lpPathName, 
            boolean bWatchSubtree, int dwNotifyFilter) throws Exception;
    
    public static native void FindNextChangeNotification(long hChangeHandle) 
    throws Exception;

    public static native void FindCloseChangeNotification(long hChangeHandle) 
    throws Exception; 

    public static native int WaitForSingleObject(long hHandle, int dwTimeoutMilliseconds); 
   
    public static native String ReadDirectoryChangesW();
    
  //**************************************************************************
  //** Static Local Variables
  //**************************************************************************
    
  /** A constant value representing infinite. */
    public static final int INFINITE = 0xFFFFFFFF;  //(WinBase.h)

  /** A wait return value indicating a failed wait. */
    public static final int WAIT_FAILED = 0xFFFFFFFF;
  
  /** A wait return value indicating that the specified object is a mutex object that was not released by the thread.  */
    public static final int WAIT_ABANDONED = 0x00000080;
  
  /** A wait return value indicating The state of the specified object is signaled. */
    public static final int WAIT_OBJECT_0 = 0x00000000;
  
  /** The time-out interval elapsed, and the object's state is nonsignaled. */
    public static final int WAIT_TIMEOUT = 0x00000102;
    
    
} // End FileSystemWatcherNative