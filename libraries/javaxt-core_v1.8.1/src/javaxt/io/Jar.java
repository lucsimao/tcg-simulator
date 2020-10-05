package javaxt.io;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

//Imports for dealing with ZIP files
import java.net.URI;
import java.util.zip.*;

//******************************************************************************
//**  Jar Class - By Peter Borissow
//******************************************************************************
/**
 *   Used to find entries in a jar file associated with a given class or 
 *   package. 
 *
 *   The original motivation behind this class was to support a requirement to 
 *   extract and update config files stored in Java packages. For console apps, 
 *   the config file is stored in the jar (zip) file. For web apps, chances are 
 *   that the package has been un-zipped and the config file is laying around
 *   on disk. This class was designed to support both use cases.
 *
 ******************************************************************************/

public class Jar {
    
    private java.io.File file;
    private java.lang.Package Package;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class using an instance of another class.
   */
    public Jar(java.lang.Object object){
        this(object.getClass());
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class using a Class.
   */
    public Jar(java.lang.Class Class) {
        this.Package = Class.getPackage();
        String path = Class.getName();
        if (path!=null) path = path.replace((CharSequence)".",(CharSequence)"/") + ".class";
        java.util.ArrayList<java.net.URL> urls = getResource(path, Class.getClassLoader());
        if (urls.size()==1){
            file = getFile(urls.get(0));
        }
        /*
        else if (urls.size()>1){ //Should never happen!

            for (java.net.URL url : urls){

                java.net.JarURLConnection urlcon = (java.net.JarURLConnection) (url.openConnection());
                java.util.jar.JarFile jar = urlcon.getJarFile();
                java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    String entry = entries.nextElement().getName();
                    //System.out.println(entry);
                }
            }
        }
        */
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class using a Package. This method is
   *  somewhat unreliable if there are multiple jar files that contain the
   *  same package name. Suggest using the getJars() method instead.
   */
    public Jar(java.lang.Package Package) {
        this.Package = Package;
        String path = Package.getName().replace((CharSequence)".",(CharSequence)"/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (java.net.URL url : getResource(path, classLoader)){
            file = getFile(url);
            if (file!=null) break;
        }
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class using a path to a jar file or
   *  directory.
   */
    public Jar(java.io.File file){
        this.file = file;
    }


  //**************************************************************************
  //** getJars
  //**************************************************************************
  /** Returns an array of files or directories associated with a given Package.
   *  This method should be used instead of new Jar(java.lang.Package).
   */
    public static Jar[] getJars(java.lang.Package Package){
        return getJars(Package.getName());
    }

    public static Jar[] getJars(String packageName){
        java.util.ArrayList<Jar> jars = new java.util.ArrayList<Jar>();
        String path = packageName.replace((CharSequence)".",(CharSequence)"/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (java.net.URL url : getResource(path, classLoader)){
            java.io.File file = getFile(url);
            if (file!=null) jars.add(new Jar(file));
        }
        return jars.toArray(new Jar[jars.size()]);
    }

  //**************************************************************************
  //** getFile
  //**************************************************************************
  /** Returns a java.io.File representation of the jar file or directory where 
   *  the jar file has been extracted.
   */
    public java.io.File getFile(){
        return file;
    }


  //**************************************************************************
  //** getManifest
  //**************************************************************************
  /**  Returns the Manifest file found in the "META-INF" directory. The
   *   Manifest file contains metadata for the jar file including version
   *   numbers, vendor name, etc. You can loop through properties in the
   *   Manifest like this:
   <pre>
    java.io.File file = new java.io.File("/Drivers/h2/h2-1.3.162.jar");
    java.util.jar.JarFile jar = new javaxt.io.Jar(file);
    java.util.jar.Manifest manifest = jar.getManifest();

    System.out.println("\r\nMain Attributes:\r\n--------------------------");
    printAttributes(manifest.getMainAttributes());


    System.out.println("\r\nOther Attributes:\r\n--------------------------");
    java.util.Map&lt;String, java.util.jar.Attributes&gt; entries = manifest.getEntries();
    java.util.Iterator&lt;String&gt; it = entries.keySet().iterator();
    while (it.hasNext()){
        String key = it.next();
        printAttributes(entries.get(key));
        System.out.println();
    }

    jar.close();

    private static void printAttributes(java.util.jar.Attributes attributes){
        java.util.Iterator it = attributes.keySet().iterator();
        while (it.hasNext()){
            java.util.jar.Attributes.Name key = (java.util.jar.Attributes.Name) it.next();
            Object value = attributes.get(key);
            System.out.println(key + ":  " + value);
        }
    }
   </pre>
   */
    public java.util.jar.Manifest getManifest(){
        try{
            Entry entry = this.getEntry("META-INF", "MANIFEST.MF");
            if (entry!=null) {
                ByteArrayInputStream is = new ByteArrayInputStream(entry.getBytes());
                java.util.jar.Manifest manifest = new java.util.jar.Manifest(is);
                is.close();
                return manifest;
            }
        }
        catch(Exception e){
        }
        return null;
    }


  //**************************************************************************
  //** getVersion
  //**************************************************************************
  /** Returns the version number of the jar file, if available. Two different
   *  strategies are used to find the version number. First strategy is to
   *  parse the jar file manifest and return the value of the
   *  "Implementation-Version" or "Bundle-Version", whichever is found first.
   *  If no version information is found in the manifest, an attempt is made
   *  to parse the file name. Returns a null is no version information is
   *  available.
   */
    public String getVersion(){

        java.util.jar.Attributes attributes = getManifest().getMainAttributes();
        if (attributes!=null){
            java.util.Iterator it = attributes.keySet().iterator();
            while (it.hasNext()){
                java.util.jar.Attributes.Name key = (java.util.jar.Attributes.Name) it.next();
                String keyword = key.toString();
                if (keyword.equals("Implementation-Version") || keyword.equals("Bundle-Version")){
                    return (String) attributes.get(key);
                }
            }
        }

        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        if (fileName.contains(".")){
            String majorVersion = fileName.substring(0, fileName.indexOf("."));
            int delimiter = majorVersion.lastIndexOf("-");
            if (majorVersion.indexOf("_")>delimiter) delimiter = majorVersion.indexOf("_");
            majorVersion = majorVersion.substring(delimiter+1, fileName.indexOf("."));
            String minorVersion = fileName.substring(fileName.indexOf("."));
            return majorVersion + minorVersion;
        }

        return null;
    }


  //**************************************************************************
  //** getEntries
  //**************************************************************************
  /**  Used to return a list of all the entries found in the jar file.
   */    
    public Entry[] getEntries(){
        java.util.ArrayList<Entry> entries = new java.util.ArrayList<Entry>();
        try{
            
            if (file.isDirectory()){
                Directory dir = new Directory(file);
                java.util.List items = dir.getChildren(true);
                for (int i=0; i<items.size(); i++){
                     Object item = items.get(i);
                     if (item instanceof File){
                         entries.add(new Entry(((File) item).toFile()));
                     }
                }
            }
            else{
                ZipInputStream in = new ZipInputStream(new FileInputStream(file));
                ZipEntry zipEntry = null;
                while((zipEntry = in.getNextEntry())!=null){
                    entries.add(new Entry(zipEntry));
                }
                in.close();
            }
        }
        catch(Exception e){
        }
        return entries.toArray(new Entry[entries.size()]);
    }


  //**************************************************************************
  //** getEntry
  //**************************************************************************
  /**  Used to retrieve a single entry from the jar file. */
    
    public Entry getEntry(String Entry){
        return getEntry(Package.getName(),Entry);
    }


  //**************************************************************************
  //** getEntry
  //**************************************************************************
  /**  Used to retrieve a single entry from the jar file. 
   *  @param Package Name of the package or directory in the jar file 
   *  (e.g. "javaxt.io"). Null values and zero length strings default to the
   *  the root directory. 
   *  @param Entry Name of the class/file found in the given package  
   *  (e.g. "Jar.class").
   */
    public Entry getEntry(String Package, String Entry){

        ZipInputStream in = null;
        try{
            
            if (file.isDirectory()){
                return new Entry(new java.io.File(file, Entry));
            }
            else{
            
              //Update package name and entry
                if (Package!=null){
                    Package = Package.trim();
                    if (Package.length()==0) Package = null;
                }
                if (Package!=null){
                    if (Package.contains(".")) Package = Package.replace(".","/");
                    Entry = Package + "/" + Entry;
                }


              //Find entry in the jar file
                in = new ZipInputStream(new FileInputStream(file));
                ZipEntry zipEntry = null;
                while((zipEntry = in.getNextEntry())!=null){
                    if (zipEntry.getName().equalsIgnoreCase(Entry)){
                        //System.out.println(zipEntry.getName() + " <--");
                        Entry entry = new Entry(zipEntry);
                        in.close();
                        return entry;
                    }
                }
                in.close();
            }
        }
        catch(Exception e){
            if (in!=null){
                try{ in.close(); }
                catch(Exception ex){}
            }
            //e.printStackTrace();
        }
        
        return null;
    }


  //**************************************************************************
  //** getEntries
  //**************************************************************************
  /**  Used to retrieve a single entry from the jar file. */
    
    public Entry getEntry(java.lang.Class Class) {
        String ClassName = Class.getName();
        String PackageName = Class.getPackage().getName();
        ClassName = ClassName.substring(PackageName.length()+1);
        return getEntry(PackageName,ClassName+".class");
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /**  Returns the path to the jar file. */
    
    public String toString(){
        return file.toString();
    }


  //**************************************************************************
  //** getResource
  //**************************************************************************
  /** Returns the URL associated with a given path in the jar file.
   *  @param cl ClassLoader used to find the given path/resource. If the 
   *  ClassLoader is null or fails to find the requested path, an alternate
   *  ClassLoader is used (e.g. ClassLoader's Parent, ContextClassLoader,
   *  SystemClassLoader).
   */
    private static java.util.ArrayList<java.net.URL> getResource(String path, ClassLoader cl){

        java.util.ArrayList<java.net.URL> arr = new java.util.ArrayList<java.net.URL>();
        try{

            int x = 0;
            while (true){

                if (cl==null){
                    cl = Thread.currentThread().getContextClassLoader();
                    if (cl==null){
                        cl = java.lang.ClassLoader.getSystemClassLoader();
                        if (cl==null){
                            break; //Throw Exception?
                        }
                    }
                }


                java.util.Enumeration<java.net.URL> en = cl.getResources(path);
                if (en.hasMoreElements()){
                    while (en.hasMoreElements()){
                        java.net.URL url = en.nextElement();
                        arr.add(url);
                    }

                    break;
                }
                else{
                    if (cl.getParent()!=null && cl.getParent().equals(cl)){
                        break;
                    }
                    cl = cl.getParent();
                }



                x++;
                if (x==50) break;
            }

            /*
            String debug = "";
            debug += "Path: " + path + "\r\n";
            debug += "ClassLoader: " + cl + "\r\n";
            for (java.net.URL url : arr){
                debug += "Resource: " + url + "\r\n";
            }
            System.out.println(debug);
            */

        }
        catch(Exception e){
            //e.printStackTrace();
        }

        return arr;
    }


    private static java.io.File getFile(java.net.URL url){
        if (url!=null)
        try{
            java.net.URI uri = new java.net.URI(url.toString().replace((CharSequence)" ",(CharSequence)"%20"));
            if (uri.getPath()==null){
                String path = uri.toString();
                if (path.startsWith("jar:file:")){

                  //Update Path and Define Zipped File
                    path = path.substring(path.indexOf("file:/"));
                    path = path.substring(0,path.toLowerCase().indexOf(".jar")+4);

                    if (path.startsWith("file://")){ //UNC Path
                        path = "C:/" + path.substring(path.indexOf("file:/")+7);
                        path = "/" + new URI(path).getPath();
                    }
                    else{
                        path = new URI(path).getPath();
                    }
                    return new java.io.File(path);
                }
                else if (path.startsWith("jar:http")){
                    path = path.substring(path.indexOf("http"));
                    path = path.substring(0,path.toLowerCase().indexOf(".jar")+4);
                    
                }
            }
            else{
                return new java.io.File(uri);
            }
        }
        catch(Exception e){
            //e.printStackTrace();
        }

        return null;
    }


  //**************************************************************************
  //** JAR Entry Class
  //**************************************************************************
  /** Used to represent an entry in a jar/war file. The jar file might be
   *  zipped or unpacked by a web server.
   */
    public class Entry{
        private ZipEntry zipEntry = null;
        private java.io.File fileEntry = null;
        
      /** Constructor for zipped jar files. */
        private Entry(ZipEntry zipEntry){
            this.zipEntry = zipEntry;
        }
        
      /** Constructor for unzipped jar files. */  
        private Entry(java.io.File fileEntry){
            this.fileEntry = fileEntry;
        }

        public String getName(){
            if (fileEntry==null) return zipEntry.getName();
            else return fileEntry.getName();
        }

        public java.util.Date getDate(){
            if (fileEntry==null) return new java.util.Date(zipEntry.getTime());
            else return new java.util.Date(fileEntry.lastModified());
        }


      /** Returns a long value representing a cyclic redundancy check
       * (CRC-32 checksum) of the uncompressed entry data, or -1 if not known.
       */
        public long checksum(){
            if (fileEntry==null) return zipEntry.getCrc();
            else return new javaxt.io.File(fileEntry).checksum();
        }


        public java.io.File getFile(){
            return fileEntry;
        }

        public long getSize(){
            if (fileEntry==null){
                return zipEntry.getSize();
            }
            else{
                return fileEntry.length();
            }
        }
        

        public byte[] getBytes(){
            ZipFile zip = null;
            try{
                
                if (fileEntry==null){
                    zip = new ZipFile(file);
                    java.io.DataInputStream is = new java.io.DataInputStream(zip.getInputStream(zipEntry));

                    int bufferSize = 1024;
                    ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    byte[] b = new byte[bufferSize];
                    int x=0;
                    while((x=is.read(b,0,bufferSize))>-1) {
                        bas.write(b,0,x);
                    }
                    bas.close();

                    zip.close();
                    return bas.toByteArray();
                }
                else{
                    return new javaxt.io.File(fileEntry).getBytes().toByteArray();
                }
            }
            catch(Exception e){
                if (zip!=null){
                    try{ zip.close(); }
                    catch(Exception ex){}
                }
                return null;
            }
        }


      /** Used to extract the zip entry to a file. */
        public void extractFile(java.io.File destination){
            try{
                if (fileEntry==null){
                    destination.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(destination);
                    ZipInputStream in = new ZipInputStream(new FileInputStream(file));
                    ZipEntry zipEntry = null;
                    while((zipEntry = in.getNextEntry())!=null){
                        if (zipEntry.getName().equals(this.zipEntry.getName())){

                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            break;
                        }
                    }
                    in.close();
                    out.close();
                }
                else{
                    
                  //Simply copy the file to the destination
                    if (destination.isFile()){
                        new File(fileEntry).copyTo(new File(destination),false);
                    }
                    else{
                        new File(fileEntry).copyTo(new Directory(destination),false);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
        public void setText(String text){
            try{
                if (fileEntry==null){
                    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                    ByteArrayInputStream byteInput = new ByteArrayInputStream(text.getBytes());
                    ZipOutputStream zipOutput = new ZipOutputStream(byteOutput);
                    ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
                    
                    
                    ZipEntry zipEntry = null;
                    while((zipEntry = zipInput.getNextEntry())!=null){
                        
                        if (zipEntry.getName().equals(this.zipEntry.getName())){
                            
                          //Write Updated Config File
                            zipOutput.putNextEntry(new ZipEntry(this.zipEntry.getName()));
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = byteInput.read(buf)) > 0) {
                                zipOutput.write(buf, 0, len);
                            }
                            byteInput.close();
                        }
                        else{
                            zipOutput.putNextEntry(zipEntry);
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = zipInput.read(buf)) > 0) {
                                zipOutput.write(buf, 0, len);
                            }
                        }
                        
                        zipInput.closeEntry();
                        zipOutput.closeEntry();
                    }
                    
                    zipInput.close();
                    zipOutput.close();


                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(byteOutput.toByteArray());
                    fos.close();
                    
                    byteOutput.close();
                    
                }
                else{
                    new File(fileEntry).write(text);
                }
            }
            catch(Exception e){
            }
        }

      /** Used to extract the contents to a string. */
        public String getText(){
            return getText("UTF-8");
        }

      /** Used to extract the contents to a string. Returns null if the 
       *  extraction failed.
       *  @param charsetName Name of the character encoding used to read the
       *  file. Examples include UTF-8 and ISO-8859-1
       */
        public String getText(String charsetName){
            try{
                if (fileEntry==null){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ZipInputStream in = new ZipInputStream(new FileInputStream(file));
                    ZipEntry zipEntry = null;
                    while((zipEntry = in.getNextEntry())!=null){
                        if (zipEntry.getName().equals(this.zipEntry.getName())){

                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            break;
                        }
                    }
                    in.close();
                    return out.toString(charsetName);
                }
                else{
                    return new File(fileEntry).getText(charsetName);
                }
            }
            catch(Exception e){
                return null;
            }

        }
        
        public String toString(){
            return getName();
        }
    }
}