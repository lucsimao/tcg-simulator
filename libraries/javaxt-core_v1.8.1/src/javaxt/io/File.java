package javaxt.io;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.io.*;
import java.util.Locale;

//******************************************************************************
//**  File Class
//******************************************************************************
/**
 *   Used to represent a single file on a file system. In many ways, this class 
 *   is an extension of the java.io.File class. However, unlike the java.io.File 
 *   class, this object provides functions that are relevant and specific to
 *   files (not directories). 
 *
 ******************************************************************************/

public class File implements Comparable {
    
    private java.io.File file; //<--DO NOT USE DIRECTLY! Use getFile() instead.
    private String name = "";
    private String path = "";
    private FileAttributes attr;
    private long lastAttrUpdate = 0;

    public final String PathSeparator = System.getProperty("file.separator");
    public final String LineSeperator = System.getProperty("line.separator");
    private static final boolean isWindows = Directory.isWindows;
    private int bufferSize = 1024*1024; //1MB
    
    
  //**************************************************************************
  //** Constructor
  //************************************************************************** 
  /** Creates a new File instance by converting the given pathname string into 
   *  an abstract pathname. 
   */    
    public File(String Path) {
	if (Path == null) {
	    throw new NullPointerException();
	}

        if (Path.startsWith("\"") && Path.endsWith("\"")){
            Path = Path.substring(1,Path.length()-1).trim();
        }
        if (Path.endsWith("\\") || Path.endsWith("/")){
            throw new IllegalArgumentException("Invalid Path: " + Path);
        }

        init(Path);

        //int idx = Path.replace("\\", "/").lastIndexOf("/")+1;
        //path = Path.substring(0, idx);
        //name = Path.substring(idx);
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Instantiates this class using a java.io.File. Please use the Directory
   *  class for directories. Example:
   * <pre>if (file.isDirectory()) new Directory(file);</pre>
   */        
    public File(java.io.File File) {
	if (File==null) {
	    throw new NullPointerException();
	}

        if (File.isDirectory()){
            throw new IllegalArgumentException("Invalid Path.");
        }

        this.file = File;
        init(file.getAbsolutePath());
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new File instance from a parent abstract pathname and a child 
   *  pathname string. 
   */
    public File(java.io.File Parent, String Child){
        this(Parent.getPath(), Child);
    }
    
    public File(Directory Parent, String Child){
        this(Parent.getPath(), Child);
    }
        
    public File(String Parent, String Child){
	if (Parent == null || Child == null) {
	    throw new NullPointerException();
	}

      //Normalize the strings
        Parent = Parent.replace("\\", "/");
        Child = Child.replace("\\", "/");

      //Combine the Parent and Child into a single path. This is done in 
      //case the Child variable contains additional path information.
        if (!Parent.endsWith("/")) Parent += "/";
        if (Child.startsWith("/")) Child = Child.substring(1);
        if (Child.endsWith("/")) Child = Child.substring(0, Child.length()-1);

        init(Parent + Child);
    }


  /** Used to parse the absolute path to the file. For performance reasons,
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
  //** Get File Name
  //************************************************************************** 
  /**  Returns the name of the file, excluding the path. */
    
    public String getName(){
        return name;
    }


  //**************************************************************************
  //** Get File Name
  //************************************************************************** 
  /**  Returns the name of the file, excluding the path. 
   *
   *   @param IncludeFileExtension If true, includes the file extension. 
   *   Otherwise, will return the file name without the extension.
   */
    public String getName(boolean IncludeFileExtension){
        if (!IncludeFileExtension){
            int idx = name.lastIndexOf(".");
            if (idx > -1) return name.substring(0, idx);
        }
        return name;
    }


  //**************************************************************************
  //** Get File Path
  //**************************************************************************  
  /**  Used to retrieve the path to the file, excluding the file name. Appends 
   *   a file separator to the end of the string. 
   */
    public String getPath(){
        return path;
    }


  //**************************************************************************
  //** Get Directory
  //************************************************************************** 
  /**  Returns the file's parent directory. Same as getParentDirectory() */

    public Directory getDirectory(){
        return getParentDirectory();
    }


  //**************************************************************************
  //** Get Parent Directory
  //**************************************************************************
  /** Returns the file's parent directory. Returns null if the parent
   *  directory is unknown.
   */
    public Directory getParentDirectory(){
        if (path.length()>0) return new Directory(path);
        else return null;
    }


  //**************************************************************************
  //** toFile
  //**************************************************************************
  /**  Returns the java.io.File representation of this object. */

    public java.io.File toFile(){
        return getFile();
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
  //** Get File Extension
  //**************************************************************************  
  /**  Returns the file's extension, excluding the last dot/period 
   *   (e.g. "C:\image.jpg" will return "jpg"). Returns a zero-length string
   *   if there is no extension.
   */
    public String getExtension(){
        int idx = name.lastIndexOf(".");
        if (idx > -1) return name.substring(idx+1);
        else return "";
    }


  //**************************************************************************
  //** getSize
  //**************************************************************************
  /** Returns the size of the file, in bytes. Returns 0L if the file is does
   *  not exist or if the object is a directory.
   */
    public long getSize(){
        if (file!=null) return getFileSize();

        try{
            FileAttributes attr = getFileAttributes();
            if (attr!=null) return attr.getSize();
            else{
                getFile();
                return getFileSize();
            }
        }
        catch(java.io.FileNotFoundException e){
            return 0L;
        }
    }

    private long getFileSize(){
        long size = file.length();
        if (size>0L && file.isFile()) return size;
        else return 0L;
    }


  //**************************************************************************
  //** getDate
  //**************************************************************************
  /** Returns a timestamp of when the file was last modified. Returns null if
   *  the file does not exist or if the object is a directory.
   */
    public java.util.Date getDate(){
        if (file!=null) return getFileDate();

        try{
            FileAttributes attr = getFileAttributes();
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
        if (file.exists() && file.isFile()) return new java.util.Date(file.lastModified());
        else return null;
    }


  //**************************************************************************
  //** setDate
  //**************************************************************************
  /**  Used to set/update the last modified date.
   */
    public boolean setDate(java.util.Date lastModified){
        if (lastModified!=null){
            java.io.File file = getFile();
            if (file.exists() && file.isFile()){
                long t = lastModified.getTime();
                if (getDate().getTime()!=t){
                    attr = null;
                    return getFile().setLastModified(t);
                }
            }
        }
        return false;
    }


  //**************************************************************************
  //** Exists
  //**************************************************************************
  /**  Used to determine whether a file exists. Returns false if the file
   *   system can't find the file or if the object is a directory.
   */
    public boolean exists(){
        if (file!=null){
            return (file.isFile() && file.exists());
        }

        try{
            FileAttributes attr = getFileAttributes();
            if (attr!=null) return true;
            else{
                getFile();
                return (file.isFile() && file.exists());
            }
        }
        catch(java.io.FileNotFoundException e){
            return false;
        }
    }


  //**************************************************************************
  //** isHidden
  //**************************************************************************
  /** Used to check whether the file is hidden. Returns true if the file
   *  exists and is hidden according to the conventions of the underlying
   *  file system.
   */
    public boolean isHidden(){
        if (file!=null){
            return (file.isFile() && file.isHidden());
        }

        try{
            FileAttributes attr = getFileAttributes();
            if (attr!=null) return attr.isHidden();
            else{
                getFile();
                return (file.isFile() && file.isHidden());
            }
        }
        catch(java.io.FileNotFoundException e){
            return false;
        }
    }


  //**************************************************************************
  //** isReadOnly
  //**************************************************************************
  /** Used to check whether the file has write permissions. Returns true if
   *  the file exists and the application is not allowed to write to the file.
   */
    public boolean isReadOnly(){
        if (file!=null){
            return (file.isFile() && !file.canWrite());
        }

        try{
            FileAttributes attr = getFileAttributes();
            if (attr!=null) return attr.isReadOnly();
            else{
                getFile();
                return (file.isFile() && !file.canWrite());
            }
        }
        catch(java.io.FileNotFoundException e){
            return false;
        }
    }


  //**************************************************************************
  //** isExecutable
  //**************************************************************************
  /** Used to check whether the file has execute permissions. Note that this
   *  method is not supported by JDK 1.5 or lower. Instead, the method will
   *  return false.
   */
    public boolean isExecutable(){
        java.io.File File = getFile();
        if (File!=null){

            //return File.canExecute(); //<--incompatable with JDK 1.5

            String[] arr = System.getProperty("java.version").split("\\.");
            if (Integer.valueOf(arr[0]).intValue()==1 && Integer.valueOf(arr[1]).intValue()<6) return false;
            else{
                try{
                    return (Boolean) File.getClass().getMethod("canExecute").invoke(File, null);
                }
                catch(Exception e){
                    return false;
                }
            }
        }
        else return false;
    }


  //**************************************************************************
  //** isLink
  //**************************************************************************
  /**  Used to determine whether the file is actually a link to another file.
   *   Returns true for symbolic links, Windows junctions, and Windows
   *   shortcuts.
   */
    public boolean isLink(){
        return getLink()!=null;
    }


  //**************************************************************************
  //** getLink
  //**************************************************************************
  /**  Returns the target of a symbolic link, Windows junction, or Windows
   *   shortcut.
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
  //** Delete File
  //**************************************************************************
  /**  Used to delete the file. Warning: this operation is irrecoverable. */
    
    public boolean delete(){
        java.io.File file = getFile();
        if (file!=null){            
            if (file.delete()){
                attr=null;
                return true;
            }
        }
        return false;
    }


  //**************************************************************************
  //** setBufferSize
  //**************************************************************************
  /** Used to set the size of the buffer used to read/write bytes. The default
   *  is 1MB (1,048,576 bytes)
   */
    public void setBufferSize(int numBytes){
        bufferSize = numBytes;
    }
    
    
  //**************************************************************************
  //** Move File
  //**************************************************************************
  /** Used to move the file to a different directory. If the operation is
   *  successful, returns a handle to the new file. Otherwise, the original
   *  file is returned.
   */
    public javaxt.io.File moveTo(Directory Destination){
        return moveTo(new javaxt.io.File(Destination, getName()), true);
    }

  //**************************************************************************
  //** Move File
  //**************************************************************************
  /** Used to move the file to a different location. If the operation is
   *  successful, returns a handle to the new file. Otherwise, the original
   *  file is returned.
   */
    public javaxt.io.File moveTo(javaxt.io.File Destination, boolean Overwrite){
        if (Destination.exists()){
            if (Overwrite){
                Destination.delete();
            }
            else{
                return this;
            }
        }

        java.io.File oldFile = getFile();
        java.io.File newFile = Destination.toFile();
        newFile.getParentFile().mkdirs();
        if (oldFile.renameTo(newFile)){
            attr = null;
            file = newFile;
            init(file.getAbsolutePath());
        }
        return this;
    }


  //**************************************************************************
  //** Copy File
  //**************************************************************************
  /** Used to create a copy of this file. Preserves the last modified date
   *  associated with the source file. Returns true if the file was copied
   *  successfully.
   */
    public boolean copyTo(Directory Destination, boolean Overwrite){
        return copyTo(new File(Destination, getName()), Overwrite);
    }


  //**************************************************************************
  //** Copy File
  //**************************************************************************
  /** Used to create a copy of this file. Preserves the last modified date
   *  associated with the source file. Returns true if the file was copied
   *  successfully.
   */
    public boolean copyTo(javaxt.io.File Destination, boolean Overwrite){
        
      //Validate Input/Output
        java.io.File File = getFile();
        if (File.exists()==false) return false;
        if (Destination.exists() && Overwrite==false) return false;
        if (File.equals(Destination.toFile())) return false;
        
      //Create New Path
        Destination.getParentDirectory().create();
        
      //Copy File
        ReadableByteChannel inputChannel = null;
        WritableByteChannel outputChannel = null;
        try{
            FileInputStream input = new FileInputStream(File);
            FileOutputStream output = new FileOutputStream(Destination.toFile());
            inputChannel = Channels.newChannel(input);
            outputChannel = Channels.newChannel(output);
            final java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(bufferSize);

            while (inputChannel.read(buffer) != -1) {
                buffer.flip();
                outputChannel.write(buffer);
                buffer.compact();

            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                outputChannel.write(buffer);
            }

            inputChannel.close();
            outputChannel.close();

            Destination.toFile().setLastModified(File.lastModified());
            return true;
        }
        catch(Exception e){
            try{inputChannel.close();}catch(Exception ex){}
            try{outputChannel.close();}catch(Exception ex){}
            return false;
        }
    }

    
  //**************************************************************************
  //** Rename File
  //**************************************************************************
  /** Used to rename the file. The existing file name is replaced with a new
   *  name. Only the file name is affected. The file path remains unchanged.
   *  This method is therefore different from the java.io.File "renameTo"
   *  method. If the operation is successful, returns a handle to the new file.
   *  Otherwise, the original file is returned.
   *  @param FileName The new file name (including the file extension).
   */
    public javaxt.io.File rename(String FileName){
        if (FileName!=null){
            FileName = FileName.trim();
            if (FileName.length()>0){
                java.io.File File = getFile();
                if (File!=null) {
                    java.io.File newFile = new java.io.File(getPath() + FileName);
                    if (File.renameTo(newFile)){
                        attr = null;
                        file = newFile;
                        init(file.getAbsolutePath());
                    }
                }
            }
        }
        return this;
    }


  //**************************************************************************
  //** getBufferedWriter
  //**************************************************************************
  /**  Used to instantiate a BufferedWriter for this file.
   */
    public BufferedWriter getBufferedWriter(String charsetName){

        try{
            java.io.File File = getFile();
            attr = null;
            File.getParentFile().mkdirs();
            if (charsetName==null){
                return new BufferedWriter( new FileWriter(File) );
            }
            else{
                return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(File), charsetName));
            }
        }
        catch (Exception e){
            return null;
        }        
    }


  //**************************************************************************
  //** getBufferedReader
  //**************************************************************************
  /**  Used to extract the contents of the file into a BufferedReader.
   *   <pre>
   *   BufferedReader br = file.getBufferedReader("UTF-8");
   *   String strLine;
   *   while ((strLine = br.readLine()) != null){
   *      System.out.println(strLine);
   *   }
   *   </pre>
   *   @return BufferedReader or null
   */
    public BufferedReader getBufferedReader(){
        java.io.File File = getFile();
        if (File.exists()){
            try{
              //NOTE: FileReader always assumes default encoding is OK!
                return new BufferedReader( new FileReader(File) );
            }
            catch (Exception e){
            }
        }
        return null;
    }


  //**************************************************************************
  //** getBufferedReader
  //**************************************************************************
  /**  Used to extract the contents of the file into a BufferedReader.
   *   <pre>
   *   BufferedReader br = file.getBufferedReader("UTF-8");
   *   String strLine;
   *   while ((strLine = br.readLine()) != null){
   *      System.out.println (strLine);
   *   }
   *   </pre>
   *   WARNING: This method will never throw an error.
   *
   *   @param charsetName Name of the character encoding used to read the file.
   *   Examples include UTF-8 and ISO-8859-1
   */
    public BufferedReader getBufferedReader(String charsetName){
        java.io.File File = getFile();
        if (File.exists()){
            try{
                return new java.io.BufferedReader(new java.io.InputStreamReader(this.getInputStream(),charsetName));
            }
            catch (Exception e){
            }
        }
        return null;
    }
    
    
  //**************************************************************************
  //** getBufferedImage
  //**************************************************************************
    
    public java.awt.image.BufferedImage getBufferedImage(){
        Image img = getImage();
        if (img!=null) return img.getBufferedImage();
        return null;
    }
    
    
  //**************************************************************************
  //** getImage
  //**************************************************************************
  /** Used to open the file and read the contents into an image.
   */
    public Image getImage(){
        java.io.File File = getFile();
        if (File.exists()) return new Image(File);
        else return null;
    }

    
  //**************************************************************************
  //** getText
  //**************************************************************************
  /**  Used to open the file and read the contents into a string.
   */
    public String getText(){
        try{
            return getText("UTF-8");
        }
        catch(Exception e){}
        try{
            return getBytes().toString();
        }
        catch(Exception e){}
        return "";
    }


  //**************************************************************************
  //** getText
  //**************************************************************************
  /** Used to extract the contents of the file as a String. Returns an empty
   *  String if the file is empty or the contents cannot be converted to a
   *  String.
   *  @param charsetName Name of the character encoding used to read the file.
   *  Examples include UTF-8 and ISO-8859-1
   */
    public String getText(String charsetName){
        try{
           return getBytes().toString(charsetName);
        }
        catch(Exception e){}
        return "";
    }


  //**************************************************************************
  //** getXML
  //**************************************************************************
  /** Returns an XML DOM Document (org.w3c.dom.Document) represented by this
   *  file. Returns a null if the file contents cannot be converted into a
   *  DOM Document.
   */
    public org.w3c.dom.Document getXML(){
        FileInputStream is = null;
        try{
            is = getInputStream();
            org.w3c.dom.Document xml = javaxt.xml.DOM.createDocument(is);
            is.close();
            return xml;
        }
        catch(Exception e){
        }
        finally{
            if (is!=null){
                try{ is.close(); }catch(Exception e){}
            }
        }
        return null;
    }


  //**************************************************************************
  //** getBytes
  //**************************************************************************
  /** Returns a ByteArrayOutputStream for the file. Returns a null if a
   *  ByteArrayOutputStream cannot be created (e.g. file does not exist).
   */
    public ByteArrayOutputStream getBytes(){
        return getBytes(getFile(), bufferSize);
    }

    private static ByteArrayOutputStream getBytes(java.io.File File, int bufferSize){
        if (File.exists()){
            FileInputStream is = null;
            try{
                is = new FileInputStream(File);
                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                byte[] b = new byte[bufferSize];
                int x=0;
                while((x=is.read(b,0,bufferSize))>-1) {
                    bas.write(b,0,x);
                }
                bas.close();
                is.close();
                return bas;
            }
            catch(Exception e){
            }
            finally{
                if (is!=null){
                    try{ is.close(); }catch(Exception e){}
                }
            }
        }
        return null;
    }


  //**************************************************************************
  //** checksum
  //**************************************************************************
  /** Returns a long value representing a cyclic redundancy check
   * (CRC-32 checksum) of the file, or -1 if not known.
   */
    public long checksum(){
        if (!exists()) return -1;
        try{
            java.util.zip.CRC32 crc = new java.util.zip.CRC32();
            crc.update(getBytes().toByteArray());
            return crc.getValue();
        }
        catch(Exception e){
            return -1;
        }
    }



    public void write(ByteArrayOutputStream bas){
        write(bas.toByteArray());
    }


    public void write(byte[] bytes){
        java.io.File File = getFile();
        attr = null;
        if (File!=null){                
            FileOutputStream output = null;
            try {
                File.getParentFile().mkdirs();
                output = new FileOutputStream(File);
                output.write(bytes);
            }
            catch (Exception e){}
            finally {
                try { if (output != null) output.close(); }
                catch (Exception e){}
            }
        }
    }
    
    
    public void write(InputStream input){
        java.io.File File = getFile();
        attr = null;
        if (File!=null){                
            FileOutputStream output = null;
            try {
                File.getParentFile().mkdirs();
                output = new FileOutputStream(File);
                byte[] buf = new byte[bufferSize];
                int i = 0;
                while((i=input.read(buf))!=-1) {
                  output.write(buf, 0, i);
                }              
                
            }
            catch (Exception e){}
            finally {
                try { if (output != null) output.close(); }
                catch (Exception e){}
            }
        }
    }
    

  //**************************************************************************
  //** Write Text
  //**************************************************************************
  /** Used to write text to a file. Uses UTF-8 character encoding. Use the
   *  other write method to specify a different character encoding (e.g.
   *  ISO-8859-1).
   */
    public void write(String Text){
        write(Text, "UTF-8");
    }

    
  //**************************************************************************
  //** Write Text
  //**************************************************************************
  /** Used to write text to a file. Allows users to specify character encoding.
   *
   *  @param charsetName Name of the character encoding used to read the file.
   *  Examples include UTF-8 and ISO-8859-1. If null, the writer will use the
   *  default character encoding defined on the host machine.
   */
    public void write(String Text, String charsetName){
        java.io.File File = getFile();
        attr = null;
        if (File!=null){                
            Writer output = null;
            try {
                File.getParentFile().mkdirs();
                
                if (charsetName==null){
                    output = new BufferedWriter( new FileWriter(File) );
                }
                else{
                    output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(File), charsetName));
                }
                
                output.write( Text );
            }
            catch (Exception e){}
            finally {
                try { if (output != null) output.close(); }
                catch (Exception e){}
            }
        }
    }


  //**************************************************************************
  //** Write XML
  //**************************************************************************
  /**  Used to write an XML DOM Document to a file. 
   */
    public void write(org.w3c.dom.Document xml){
        write(javaxt.xml.DOM.getText(xml), xml.getXmlEncoding());
    }


  //**************************************************************************
  //** Write Text
  //**************************************************************************
    
    public void write(String[] Content){
        java.io.File File = getFile();
        attr = null;
        if (File!=null){
        
            Writer output = null;
            try {
                File.getParentFile().mkdirs();
                output = new BufferedWriter( new FileWriter(File) );
                for (int i=0; i<Content.length-1; i++){
                     output.write( Content[i] + LineSeperator);
                }
                output.write( Content[Content.length-1]);
            }
            catch (Exception e){}
            finally {
                try { if (output != null) output.close(); }
                catch (Exception e){}
            }

        }
    }
    
    
  //**************************************************************************
  //** Write Image
  //**************************************************************************
  /** Used to write an image to a file. */
    
    public void write(java.awt.image.BufferedImage Image){
        java.io.File File = getFile();
        attr = null;
        if (File!=null){      
            try{
                
                File.getParentFile().mkdirs();
                java.awt.image.RenderedImage rendImage = Image;
                javax.imageio.ImageIO.write(rendImage,getExtension(),File);
            }
            catch (Exception e){
                //System.out.println(e.toString());
            }
        }
    }

    
    
    
  //**************************************************************************
  //** MapPath
  //**************************************************************************
    
    public String MapPath(String RelPath){
        
      //Update currDir
        String currDir = getPath();
        currDir = currDir.replace("\\","/");
        if (!currDir.endsWith("/")){
            currDir+="/";
        }


        RelPath = RelPath.replace("\\","/");
        
        
        if (RelPath.startsWith("/")){
            return (currDir + RelPath.substring(1)).replace("/", PathSeparator);
        }

        
        String[] arrRelPath = RelPath.split("/");
        String[] arrAbsPath = currDir.split("/");
        
        
        
        int x = -1;
        RelPath = "";
        String Dir = "";
        for (int i=0; i<arrRelPath.length; i++) {
            Dir = arrRelPath[i];
            if (Dir.equals("..")){
                x = x + 1;               
            }
            else if (Dir.equals(".")){
                //do nothing?
            }
            else{
                RelPath = RelPath + "\\" + arrRelPath[i];
            }                      
            
        }
                
        
        //x = x + 1 'because currDir has a "\" at the end of it
        Dir = "";
        int ubound = 0;
        for (int i=0; i<arrAbsPath.length-(x+1); i++){ //because currDir has a "\" at the end of it
            Dir = Dir + arrAbsPath[i] + "\\";
        }

        
        //trim off last "\"
        //Dir = left(Dir, len(Dir) - 1);
        Dir = Dir.substring(0,Dir.length()-1);
        
        //replace any leftover "/" characters
        Dir = Dir + RelPath.replace("/", "\\");
        
        
        Dir = Dir.replace((CharSequence) "\\", (CharSequence) PathSeparator);
        
        return Dir;
    }
    
    
    
  //**************************************************************************
  //** getInputStream
  //**************************************************************************
  /**  Returns a new FileInputStream Object */
    
    public FileInputStream getInputStream() throws IOException{
        return new FileInputStream(getFile());
    }
    
  //**************************************************************************
  //** getInputStream
  //**************************************************************************
  /**  Returns a new FileOutputStream Object */
    
    public FileOutputStream getOutputStream() throws IOException{
        attr = null;
        return new FileOutputStream(getFile());
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /**  Returns the full file path (including the file name) */
    
    public String toString(){
        //if (file!=null) return file.toString();
        return path + name;
    }
 

    @Override
    public int hashCode(){
        if (isWindows) return toString().toLowerCase(Locale.ENGLISH).hashCode() ^ 1234321; //java.io.Win32FileSystem.java
        else return toString().hashCode() ^ 1234321; //java.io.UnixFileSystem.java
    }


    //@Override
    public int compareTo(Object obj){
        if (obj==null) return -1;
        else return -obj.toString().compareTo(getPath());
    }


  //**************************************************************************
  //** equals
  //**************************************************************************
  
    public boolean equals(Object obj){
        if (obj instanceof javaxt.io.File || obj instanceof java.io.File){
            return obj.hashCode()==this.hashCode();
        }
        return false;
    }


  //**************************************************************************
  //** clone
  //**************************************************************************
  /** Creates a copy of this object. */

    public File clone(){
        return new File(this.toString());
    }


  //**************************************************************************
  //** IsValidPath -- NOT USED!
  //**************************************************************************
  /**  Checks whether PathToFile is a valid */
    
    private boolean isValidPath(String PathToFile){
        
        if (PathToFile==null) return false;
        if (PathToFile.length()<1) return false;
  
        //if (File.isDirectory()) return false;
        
        String FileName = getName();
        if (FileName.length()<1) return false;
        if (FileName.length()>260) return false;
        
        PathToFile = toString();
        PathToFile = PathToFile.replace((CharSequence) "\\", "/");
        String[] Path = PathToFile.split("/");                
        String[] arr = new String[]{ "/", "?", "<", ">", "\\", ":", "*", "|", "\"" };
        
        for (int i=0; i<Path.length; i++){
             for (int j=0; j<arr.length; j++){
                  if (arr[j].equals(":") && i==0 & Path[i].length()==2){ //&& File.pathSeparator.equals(":")
                    //skip check b/c we've got something like "C:\" in the path                      
                  }
                  else{
                      if (Path[i].contains((CharSequence) arr[j])) return false;
                  }
             }
        }
        

        return true;

    }


  //**************************************************************************
  //** getContentType
  //**************************************************************************
  /** Returns the mime type associated with the file extension found in a
   *  given file name. This method only covers the most common/popular mime
   *  types. The returned mime type is NOT authoritative.
   *  @param file File name (e.g. hello.txt)
   *  @return Content type (e.g. text/plain)
   */
    public static String getContentType(String file){

      //Get file extension
        FileExtension ext = null;
        if (file!=null){
            file = file.trim();
            int x = file.lastIndexOf(".");
            if (x!=-1) ext = new FileExtension(file.substring(x+1));
        }
        if (ext==null) return "application/octet-stream";


      //TEXT
        if (ext.equals("css")) return "text/css";
        if (ext.equals("dtd")) return "text/plain";
        if (ext.equals("htm,html")) return "text/html";
        if (ext.equals("java")) return "text/plain";
        if (ext.equals("js")) return "text/javascript";
        if (ext.equals("txt")) return "text/plain";
        if (ext.equals("csv")) return "text/csv";
        
      //JSON
        if (ext.equals("json")) return "application/json";

      //IMAGE
        if (ext.equals("bmp")) return "image/bmp";
        if (ext.equals("gif")) return "image/gif";
        if (ext.equals("jp2,j2c,j2k,jpx")) return "image/jp2";
        if (ext.equals("jpg,jpe,jpeg,jfif,pjpeg,pjp")) return "image/jpeg";
        if (ext.equals("png")) return "image/png";
        if (ext.equals("psd")) return "image/x-photoshop";
        if (ext.equals("rgb")) return "image/x-rgb";
        if (ext.equals("tif,tiff")) return "image/tiff";
        if (ext.equals("xbm")) return "image/x-xbitmap";
        if (ext.equals("xpm")) return "image/x-xpixmap";
        if (ext.equals("ico")) return "image/vnd.microsoft.icon";

      //MICROSOFT OFFICE APPLICATIONS
        if (ext.equals("doc,dot")) return "application/msword";
        if (ext.equals("xls,xlw,xla,xlc,xlm,xlt,xll")) return "application/vnd.ms-excel";
        if (ext.equals("ppt,pps,pot")) return "application/vnd.ms-powerpoint";
        if (ext.equals("mdb")) return "application/x-msaccess";
        if (ext.equals("mpp")) return "application/vnd.ms-project";
        if (ext.equals("pub")) return "application/x-mspublisher";
        if (ext.equals("wmz")) return "application/x-ms-wmz";
        if (ext.equals("wmd")) return "application/x-ms-wmd";
        if (ext.equals("one,onetoc2,onetmp,onepkg")) return "application/msonenote";
        if (ext.equals("docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (ext.equals("dotx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
        if (ext.equals("xlsx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (ext.equals("xltx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
        if (ext.equals("pptx")) return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        if (ext.equals("ppsx")) return "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
        if (ext.equals("potx")) return "application/vnd.openxmlformats-officedocument.presentationml.template";
        if (ext.equals("sldx")) return "application/vnd.openxmlformats-officedocument.presentationml.slide";

      //OTHER APPLICATIONS
        if (ext.equals("ai,eps,ps")) return "application/postscript";
        if (ext.equals("gz")) return "application/x-gzip";
        if (ext.equals("pdf")) return "application/pdf";
        if (ext.equals("xml")) return "application/xml"; //return "text/xml";
        if (ext.equals("z")) return "application/x-compress";
        if (ext.equals("zip")) return "application/zip";

      //AUDIO
        if (ext.equals("mid,midi")) return "audio/x-midi";
        if (ext.equals("mp1,mp2,mp3,mpa,mpega")) return "audio/x-mpeg";
        if (ext.equals("ra,ram")) return "audio/x-pn-realaudio";
        if (ext.equals("wav")) return "audio/x-wav";
        if (ext.equals("wma")) return "audio/x-ms-wma";
        if (ext.equals("wax")) return "audio/x-ms-wax";
        if (ext.equals("wmv")) return "audio/x-ms-wmv";

      //VIDEO
        if (ext.equals("asf,asx")) return "video/x-ms-asf";
        if (ext.equals("avi")) return "video/msvideo";
        if (ext.equals("mov")) return "video/quicktime";
        if (ext.equals("mpe,mpeg,mpg")) return "video/mpeg";
        if (ext.equals("mpv2")) return "video/mpeg2";
        if (ext.equals("qt,mov,moov")) return "video/quicktime";

        if (ext.equals("wvx")) return "video/x-ms-wvx";
        if (ext.equals("wm")) return "video/x-ms-wm";
        if (ext.equals("wmx")) return "video/x-ms-wmx";

        return "application/octet-stream";
    }

    private static class FileExtension {
        private String ext;
        public FileExtension(String ext){
            this.ext = ext.toLowerCase();
        }

      /**
       *  @param obj Comma Separated List Of File Extensions
       */
        public boolean equals(Object obj){
            if (obj instanceof String){

                String[] arr = ((String) obj).split(",");
                for (int i=0; i<arr.length; i++){
                     String str = arr[i].trim().toLowerCase();
                     if (str.equals(ext)) return true;
                }

            }
            return false;
        }

        public int hashCode(){
            return ext.hashCode();
        }
    }


  //**************************************************************************
  //** getContentType
  //**************************************************************************
  /** Returns the mime type associated with the file extension. This method 
   *  only covers the most common/popular mime types. The returned mime type
   *  is NOT authoritative.
   */
    public String getContentType(){
        return getContentType(this.getName());
    }


  //**************************************************************************
  //** setLastModifiedTime
  //**************************************************************************
  /** Used to update the timestamp of when the file was last modified.
   */
    public void setLastModifiedTime(java.util.Date date){
        setDate(date);
    }


  //**************************************************************************
  //** getLastModifiedTime
  //**************************************************************************
  /** Returns a timestamp of when the file was last modified. This is
   *  identical to the getDate() method.
   */
    public java.util.Date getLastModifiedTime(){
        return getDate();
    }


  //**************************************************************************
  //** getCreationTime
  //**************************************************************************
  /** Returns a timestamp of when the file was first created. Returns a null
   *  if the timestamp is not available. 
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
  /** Returns a timestamp of when the file was last accessed. Returns a null
   *  if the timestamp is not available. 
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
  /** Returns keywords representing file attributes (e.g. "READONLY", "HIDDEN",
   *  etc).
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
    private FileAttributes getFileAttributes() throws java.io.FileNotFoundException {
        if (attr==null) lastAttrUpdate = 0;

        if (attr==null || (new java.util.Date().getTime()-lastAttrUpdate)>1000){
            try{
              //Get file attributes
                String pathToFile = toString();
                attr = new FileAttributes(pathToFile);
                if (attr.isDirectory()) throw new java.io.FileNotFoundException(pathToFile);

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
  //** loadDLL
  //**************************************************************************
  /** Used to load the javaxt-core.dll. Returns a boolean to indicate load
   *  status. Note that the dll is only loaded once per JVM so it should be
   *  safe to call this method multiple times.
   */
    protected static synchronized boolean loadDLL(){

      //Try to load the dll as needed. Update the
        if (isWindows){

            if (dllLoaded==null){ //haven't tried to load the dll yet...
                String jvmPlatform = System.getProperty("os.arch");
                String dllName = null;
                if (jvmPlatform.equalsIgnoreCase("x86")){
                    dllName = "javaxt-core.dll";
                }
                else if(jvmPlatform.equalsIgnoreCase("amd64")){
                    dllName = "javaxt-core64.dll";
                }
                else{
                    dllLoaded = false;
                    return dllLoaded;
                }


              //Find dll entry in the zip/jar file
                Jar jar = new Jar(Jar.class);
                Jar.Entry entry = jar.getEntry(null, dllName);
                long checksum = entry.checksum();
                
                
              //Construct list of possible file locations for the dll
                java.util.ArrayList<java.io.File> files = new java.util.ArrayList<java.io.File>();
                files.add(new java.io.File(jar.getFile().getParentFile(), dllName));
                javaxt.io.Directory dir = new javaxt.io.Directory(System.getProperty("user.home"));
                for (String appDir : new String[]{"AppData\\Local", "Application Data"}){
                    javaxt.io.Directory d = new javaxt.io.Directory(dir + appDir);
                    java.io.File f = d.toFile(); //flip to file to prevent infinite recursion
                    if (f.exists() && f.canWrite()){
                        dir = d;
                        break;
                    }
                }
                files.add(new java.io.File(dir + "JavaXT\\" + dllName));


              //Try to load dll
                for (java.io.File dll : files){
                    
                    if (dll.exists()){

                      //Check whether the dll equals the jar entry. Extract as needed.
                        byte[] b = new byte[(int)dll.length()];
                        java.io.DataInputStream is = null;
                        try{
                            is = new java.io.DataInputStream(new FileInputStream(dll));
                            is.readFully(b, 0, b.length);
                            is.close();
                            java.util.zip.CRC32 crc = new java.util.zip.CRC32();
                            crc.update(b);
                            if (checksum!=crc.getValue()){
                                dll.delete();
                                entry.extractFile(dll);
                            }
                        }
                        catch(Exception e){
                            try{is.close();}catch(Exception ex){}
                        }
                    }
                    else{

                      //File does not exist so extract the dll
                        entry.extractFile(dll);
                    }


                  //Load the dll
                    if (dll.exists()){
                        try{
                            System.load(dll.toString());
                            dllLoaded = true;
                            break;
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }


              //Don't update the static variable to give users a chance to fix
              //the load error (e.g. manually extract the dll and copy it into
              //one of the directories).
              //dllLoaded = false;
            }
            
            return dllLoaded;
        }
        else{//not windows...
            return false;
        }
    }

    
    /** Used to determine whether the JVM is running on Mac OS X. */
    private static final boolean isOSX = 
            System.getProperty("os.name").toLowerCase().contains("os x");
    
    private static final boolean isSolaris = 
            System.getProperty("os.name").toLowerCase().contains("sunos");

  /** Used to track load status. Null = no load attempted, True = successfully
   *  loaded the dll, False = failed to load dll (don't try again). Do not try
   *  to modify the value directly. Use the loadDLL() method instead.
   */
    private static Boolean dllLoaded;


    /** JNI entry point to retrieve file attributes. */
    private static native long[] GetFileAttributesEx(String lpPathName) throws Exception;

    /** JNI entry point to retrieve link target. */
    private static native String GetTarget(String lpPathName) throws Exception;

     /** JNI entry point to retrieve a list of shared drives on a server. */
    protected static native String GetSharedDrives(String serverName) throws Exception;

     /** JNI entry point to retrieve a list of network drives mounted to the host. */
    protected static native String GetNetworkDrives() throws Exception;
    
     /** JNI entry point to retrieve a list of files and directories. */
    protected static native String GetFiles(String lpPathName) throws Exception;
    


//******************************************************************************
//**  FileAttributes Class
//******************************************************************************
/**
 *  Used to encapsulate extended file attributes. On unix and linux machines, 
 *  this class is used to parse the output from ls. On windows, this class 
 *  uses a JNI to return WIN32_FILE_ATTRIBUTE_DATA:
 *
 <pre>
    typedef struct _WIN32_FILE_ATTRIBUTE_DATA {
      DWORD dwFileAttributes;
      FILETIME ftCreationTime;
      FILETIME ftLastAccessTime;
      FILETIME ftLastWriteTime;
      DWORD nFileSizeHigh;
      DWORD nFileSizeLow;
    } WIN32_FILE_ATTRIBUTE_DATA;
 </pre>
 *
 ******************************************************************************/

public static class FileAttributes {

    private java.util.Date ftCreationTime;
    private java.util.Date ftLastAccessTime;
    private java.util.Date ftLastWriteTime;
    private long size;
    private java.util.HashSet<String> flags = new java.util.HashSet<String>();
    private java.io.File link;

    public FileAttributes(String path) throws java.io.FileNotFoundException, Exception {

        if (isWindows){

            if (loadDLL()){

              //Get attributes
                long[] attributes = null;
                try{
                    attributes = GetFileAttributesEx(path);
                }
                catch(Exception e){
                    if (!new java.io.File(path).exists()){
                        throw new java.io.FileNotFoundException(path);
                    }
                    else throw e;
                }

                
              //Parse dates
                java.text.SimpleDateFormat ftFormatter =
                new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS");
                ftCreationTime = ftFormatter.parse(attributes[1]+"");
                ftLastAccessTime = ftFormatter.parse(attributes[2]+"");
                ftLastWriteTime = ftFormatter.parse(attributes[3]+"");


              //Compute file size using the nFileSizeHigh and nFileSizeLow attributes
              //Note that nFileSizeHigh will be zero unless the file size is greater 
              //than MAXDWORD (4.2 Gig)
                long MAXDWORD = 4294967296L;
                long nFileSizeHigh = attributes[4];
                long nFileSizeLow = attributes[5];
                size = (nFileSizeHigh * MAXDWORD) + nFileSizeLow;


              //Parse misc file attributes
                long dwFileAttributes = attributes[0];
                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_READONLY) == FILE_ATTRIBUTE_READONLY)
                flags.add("READONLY");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_HIDDEN) == FILE_ATTRIBUTE_HIDDEN)
                flags.add("HIDDEN");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_SYSTEM) == FILE_ATTRIBUTE_SYSTEM)
                flags.add("SYSTEM");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_DIRECTORY) == FILE_ATTRIBUTE_DIRECTORY)
                flags.add("DIRECTORY");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_ARCHIVE) == FILE_ATTRIBUTE_ARCHIVE)
                flags.add("ARCHIVE");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_DEVICE) == FILE_ATTRIBUTE_DEVICE)
                flags.add("DEVICE");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_NORMAL) == FILE_ATTRIBUTE_NORMAL)
                flags.add("NORMAL");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_TEMPORARY) == FILE_ATTRIBUTE_TEMPORARY)
                flags.add("TEMPORARY");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_SPARSE_FILE) == FILE_ATTRIBUTE_SPARSE_FILE)
                flags.add("SPARSE_FILE");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_REPARSE_POINT) == FILE_ATTRIBUTE_REPARSE_POINT)
                flags.add("REPARSE_POINT");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_COMPRESSED) == FILE_ATTRIBUTE_COMPRESSED)
                flags.add("COMPRESSED");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_OFFLINE) == FILE_ATTRIBUTE_OFFLINE)
                flags.add("OFFLINE");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_NOT_CONTENT_INDEXED) == FILE_ATTRIBUTE_NOT_CONTENT_INDEXED)
                flags.add("NOT_CONTENT_INDEXED");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_ENCRYPTED) == FILE_ATTRIBUTE_ENCRYPTED)
                flags.add("ENCRYPTED");

                if (bitand(dwFileAttributes, FILE_ATTRIBUTE_VIRTUAL) == FILE_ATTRIBUTE_VIRTUAL)
                flags.add("VIRTUAL");


              //Parse symlink
                if (flags.contains("REPARSE_POINT")){
                    link = new java.io.File(GetTarget(path));
                }

            }
            else{
                
              //Failed to load the javaxt-core.dll. Fall back to the java.io.File object.
                java.io.File f = new java.io.File(path);
                if (!f.exists()) throw new java.io.FileNotFoundException(path);
                size = f.length();
                ftLastWriteTime = new java.util.Date(f.lastModified());
                if (!f.canWrite()) flags.add("READONLY");
                if (f.isHidden()) flags.add("HIDDEN");
                if (f.isDirectory()) flags.add("DIRECTORY");
                
            }
        }
        else{//UNIX or LINIX Operating System


            java.io.File f = new java.io.File(path);
            if (!f.exists()) throw new java.io.FileNotFoundException(path);


          //Execute ls command to get last access time and creation time
            if (isOSX){
                String[] params = new String[]{"ls", "-lauT", path};
                javaxt.io.Shell cmd = new javaxt.io.Shell(params);            
                cmd.run();                        
                java.util.Iterator<String> it = cmd.getOutput().iterator();
                ftLastAccessTime = parseOSXDate(it);
                
                params = new String[]{"ls", "-laUT", path};
                cmd = new javaxt.io.Shell(params);
                cmd.run();
                it = cmd.getOutput().iterator();
                ftCreationTime = parseOSXDate(it);
            }
            else if (isSolaris){
                String[] params = new String[]{"ls", "-lauE", path};
                javaxt.io.Shell cmd = new javaxt.io.Shell(params);            
                cmd.run();                        
                java.util.Iterator<String> it = cmd.getOutput().iterator();
                ftLastAccessTime = parseFullDate(it);
            }
            else{//Linux (e.g. Ubuntu)
                String[] params = new String[]{"ls", "-lau", "--full-time", path};
                javaxt.io.Shell cmd = new javaxt.io.Shell(params);
                cmd.run();                        
                java.util.Iterator<String> it = cmd.getOutput().iterator();
                ftLastAccessTime = parseFullDate(it);

                params = new String[]{"ls", "-laU", "--full-time", path};
                cmd = new javaxt.io.Shell(params);
                cmd.run();
                it = cmd.getOutput().iterator();
                ftCreationTime = parseFullDate(it);
            }

      
          //Set other attributes including last modified date
            size = f.length();
            ftLastWriteTime = new java.util.Date(f.lastModified());
            if (!f.canWrite()) flags.add("READONLY");
            if (f.isHidden()) flags.add("HIDDEN");
            if (f.isDirectory()) flags.add("DIRECTORY");
            try{
                if (!f.getCanonicalFile().equals(f.getAbsoluteFile())){
                    flags.add("REPARSE_POINT");
                    link = f.getCanonicalFile(); 
                }
            }
            catch(Exception e){
            }
        }

        
      //Check whether the file is a Windows shortcut (.lnk file) and parse as needed
        if (!isDirectory() && link==null){
            if (path.toLowerCase().endsWith(".lnk")){
                try{
                    link = new LnkParser(path).getFile();
                }
                catch(Exception e){
                }
            }
        }
        
    }
    
    /** Used to extract a date from a ls output using the "--full-time" option. */
    private java.util.Date parseFullDate(java.util.Iterator<String> it){
        while (it.hasNext()){
            String line = it.next();
            if (line!=null){
                while (line.contains("  ")) line = line.replace("  ", " ");
                String[] arr = line.split(" ");
                if (arr.length>7){
                    try{
                        if (arr[6].length()>8) arr[6] = arr[6].substring(0,8);
                        String date = arr[5] + " " + arr[6] + " " + arr[7];
                        return new javaxt.utils.Date(date, "yyyy-MM-dd HH:mm:ss z").getDate();
                    }
                    catch(Exception e){
                    }
                }
            }
        }
        return null;
    }

    /** Used to extract a date from a ls output using the "T" option on OSX. */
    private java.util.Date parseOSXDate(java.util.Iterator<String> it){
        while (it.hasNext()){
            String line = it.next();
            if (line!=null){
                while (line.contains("  ")) line = line.replace("  ", " ");
                String[] arr = line.split(" ");
                if (arr.length>10){
                    try{
                        String date = arr[5] + " " + arr[6] + " " + arr[7]+ " " + arr[8];
                        return new javaxt.utils.Date(date, "MMM dd hh:mm:ss yyyy").getDate();
                    }
                    catch(Exception e){
                    }
                }
            }
        }
        return null;
    }

    public long getSize(){
        return size;
    }
    public java.util.Date getCreationTime(){
        return ftCreationTime;
    }
    public java.util.Date getLastAccessTime(){
        return ftLastAccessTime;
    }
    public java.util.Date getLastWriteTime(){
        return ftLastWriteTime;
    }
    public boolean isDirectory(){
        return flags.contains("DIRECTORY");
    }
    public boolean isHidden(){
        return flags.contains("HIDDEN");
    }
    public boolean isReadOnly(){
        return flags.contains("READONLY");
    }
    public java.util.HashSet<String> getFlags(){
        return flags;
    }
    public java.io.File getLink(){
        return link;
    }

  /** A file that is read-only. Applications can read the file, but cannot
   *  write to it or delete it. This attribute is not honored on directories.
   *  For more information, see You cannot view or change the Read-only or the
   *  System attributes of folders in Windows Server 2003, in Windows XP, in
   *  Windows Vista or in Windows 7.
   */
    private static final int FILE_ATTRIBUTE_READONLY  = 1;


  /** The file or directory is hidden. It is not included in an ordinary
   *  directory listing.
   */
    private static final int FILE_ATTRIBUTE_HIDDEN    = 2;

  /** A file or directory that the operating system uses a part of, or uses
   * exclusively.
   */
    private static final int FILE_ATTRIBUTE_SYSTEM    = 4;

  /** The handle that identifies a directory. */
    private static final int FILE_ATTRIBUTE_DIRECTORY = 16;

  /** A file or directory that is an archive file or directory. Applications
   *  typically use this attribute to mark files for backup or removal.
   */
    private static final int FILE_ATTRIBUTE_ARCHIVE   = 32;

  /** This value is reserved for system use. */
    private static final int FILE_ATTRIBUTE_DEVICE    = 64;

  /** A file that does not have other attributes set. This attribute is valid
   *  only when used alone.
   */
    private static final int FILE_ATTRIBUTE_NORMAL    = 128;

  /** A file that is being used for temporary storage. File systems avoid
   *  writing data back to mass storage if sufficient cache memory is available,
   *  because typically, an application deletes a temporary file after the
   *  handle is closed. In that scenario, the system can entirely avoid writing
   *  the data. Otherwise, the data is written after the handle is closed.
   */
    private static final int FILE_ATTRIBUTE_TEMPORARY = 256;

  /** A file that is a sparse file. */
    private static final int FILE_ATTRIBUTE_SPARSE_FILE  = 512;

  /** A file or directory that has an associated reparse point, or a file that
   *  is a symbolic link.
   */
    private static final int FILE_ATTRIBUTE_REPARSE_POINT = 1024; 

  /** A file or directory that is compressed. For a file, all of the data in
   *  the file is compressed. For a directory, compression is the default for
   *  newly created files and subdirectories.
   */
    private static final int FILE_ATTRIBUTE_COMPRESSED = 2048; 

  /** The data of a file is not available immediately. This attribute indicates
   *  that the file data is physically moved to offline storage. This attribute
   *  is used by Remote Storage, which is the hierarchical storage management
   *  software. Applications should not arbitrarily change this attribute.
   */
    private static final int FILE_ATTRIBUTE_OFFLINE  = 4096;

  /** The file or directory is not to be indexed by the content indexing
   *  service.
   */
    private static final int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 8192;

   /** A file or directory that is encrypted. For a file, all data streams in
    *  the file are encrypted. For a directory, encryption is the default for
    *  newly created files and subdirectories.
    */
    private static final int FILE_ATTRIBUTE_ENCRYPTED = 16384;

    private static final int FILE_ATTRIBUTE_VIRTUAL = 65536;  //This value is reserved for system use.



    private long bitand(long Number1, long Number2){
        try {
            return Number1 & Number2;
        }
        catch (Exception e) {
            return -1;
        }
    }


}// End FileAttributes Class


//******************************************************************************
//**  LnkParser Class
//******************************************************************************
/**
 *   Class used to parse a windows lnk files (aka shortcuts). Credit:
 *   http://stackoverflow.com/a/672775/
 *
 ******************************************************************************/

public static class LnkParser {

    private boolean isDirectory;
    private boolean isLocal;
    private String real_file;

    public LnkParser(String lnk) {
        this(new java.io.File(lnk));
    }

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Instantiates the class by parsing the Windows shortcut (lnk) file.
   *  @param lnk File representing the full path to the shortcut file.
   */
    public LnkParser(java.io.File lnk) {
        byte[] link = getBytes(lnk, 1024).toByteArray();

        // get the flags byte
        byte flags = link[0x14];

        // get the file attributes byte
        final int file_atts_offset = 0x18;
        byte file_atts = link[file_atts_offset];
        byte is_dir_mask = (byte)0x10;
        if ((file_atts & is_dir_mask) > 0) {
            isDirectory = true;
        } else {
            isDirectory = false;
        }

        // if the shell settings are present, skip them
        final int shell_offset = 0x4c;
        final byte has_shell_mask = (byte)0x01;
        int shell_len = 0;
        if ((flags & has_shell_mask) > 0) {
            // the plus 2 accounts for the length marker itself
            shell_len = bytes2short(link, shell_offset) + 2;
        }

        // get to the file settings
        int file_start = 0x4c + shell_len;

        final int file_location_info_flag_offset_offset = 0x08;
        int file_location_info_flag = link[file_start + file_location_info_flag_offset_offset];
        isLocal = (file_location_info_flag & 2) == 0;
        // get the local volume and local system values
        //final int localVolumeTable_offset_offset = 0x0C;
        final int basename_offset_offset = 0x10;
        final int networkVolumeTable_offset_offset = 0x14;
        final int finalname_offset_offset = 0x18;
        int finalname_offset = link[file_start + finalname_offset_offset] + file_start;
        String finalname = getNullDelimitedString(link, finalname_offset);
        if (isLocal) {
            int basename_offset = link[file_start + basename_offset_offset] + file_start;
            String basename = getNullDelimitedString(link, basename_offset);
            real_file = basename + finalname;
        } else {
            int networkVolumeTable_offset = link[file_start + networkVolumeTable_offset_offset] + file_start;
            int shareName_offset_offset = 0x08;
            int shareName_offset = link[networkVolumeTable_offset + shareName_offset_offset]
                    + networkVolumeTable_offset;
            String shareName = getNullDelimitedString(link, shareName_offset);
            real_file = shareName + "\\" + finalname;
        }
    }

    private static String getNullDelimitedString(byte[] bytes, int off) {
        int len = 0;
        // count bytes until the null character (0)
        while (true) {
            if (bytes[off + len] == 0) {
                break;
            }
            len++;
        }
        return new String(bytes, off, len);
    }

    /*
     * convert two bytes into a short note, this is little endian because it's
     * for an Intel only OS.
     */
    private static int bytes2short(byte[] bytes, int off) {
        return ((bytes[off + 1] & 0xff) << 8) | (bytes[off] & 0xff);
    }

    public java.io.File getFile(){
        return new java.io.File(real_file);
    }

}//End LnkParser Inner Class
}//End File Class
