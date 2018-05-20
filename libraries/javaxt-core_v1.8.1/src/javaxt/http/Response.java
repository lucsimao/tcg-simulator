package javaxt.http;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.io.*;

//******************************************************************************
//**  Http Response
//******************************************************************************
/**
 *   Used to process the response from an HTTP server.
 *
 ******************************************************************************/

public class Response {
    
    private URLConnection conn;
    private Request request;


    protected Response(Request request, URLConnection conn){
        this.request = request;
        this.conn = conn;
    }


//    public void close(){
//        ((HttpURLConnection) conn).disconnect();
//    }

  //**************************************************************************
  //** getURL
  //**************************************************************************
  /** Returns the url used to connect to the server. Note that this URL may
   *  differ from the one used to instantiate the Request object. This only
   *  occurs when the server returns a redirect code and the maximum number of
   *  redirects is greater than 0. See Request.setNumRedirects().
   */
    public java.net.URL getURL(){
        return request.getURL();
    }


  //**************************************************************************
  //** getHeaders
  //**************************************************************************
  /** Returns key/value map representing all the HTTP headers returned from
   *  the server.
   */
    public java.util.Map<String, java.util.List<String>> getHeaders(){
        return request.getResponseHeaders();
    }


  //**************************************************************************
  //** getHeaders
  //**************************************************************************
  /** Returns an array of values associated with a given key found in the HTTP
   *  headers returned from the server.
   */
    public String[] getHeaders(String headerName){
        return request.getResponseHeaders(headerName);
    }


  //**************************************************************************
  //** getHeader
  //**************************************************************************
  /** Returns the value of a given key in the HTTP header. Returns null if the
   *  header is not found in the response.
   *  @param headerName A String specifying the header name (e.g. "Content-Encoding")
   */
    public String getHeader(String headerName){
        return request.getResponseHeader(headerName);
    }


  //**************************************************************************
  //** getStatus
  //**************************************************************************
  /** Returns the HTTP status code extracted from the first line in the
   *  response header. If the client fails to connect to the server, a value
   *  of -1 is returned.
   */
    public int getStatus(){
        return request.getResponseCode();
    }


  //**************************************************************************
  //** getMessage
  //**************************************************************************
  /** Returns the status message found in the first line in the response
   *  header.
   */
    public String getMessage(){
        return request.getResponseMessage();
    }


  //**************************************************************************
  //** getCharacterEncoding
  //**************************************************************************
  /** Returns the name of the character encoding used in the body of this
   *  response as specified in the "Content-Type" header. For example, the 
   *  following "Content-Type" header specifies "UTF-8" character encoding: 
   *  <pre>Content-Type: text/html; charset=utf-8</pre>
   *  This method returns a null if the response does not specify a character 
   *  encoding.
   */
    public String getCharacterEncoding(){
        String contentType = getHeader("Content-Type");
        if (contentType!=null){
            for (String str : contentType.split(";")){
                str = str.trim();
                if (str.startsWith("charset=")){
                    return str.substring(8).trim();
                }
            }
        }
        return null;
    }


  //**************************************************************************
  //** getInputStream
  //**************************************************************************
  /** Returns the body of the http response as an input stream. No distinction
   *  is made between "normal" responses (e.g. status code 200) and error
   *  responses (e.g. 404). <p/>
   *  Sample Usage:
   <pre>
        java.io.InputStream inputStream = response.getInputStream();
        byte[] b = new byte[1024];
        int x=0;
        while ( (x = inputStream.read(b)) != -1) {
            //Do something! Example: outputStream.write(b,0,x);
        }
        inputStream.close();
   </pre>
   */
    public InputStream getInputStream(){

        InputStream errorStream = ((HttpURLConnection)conn).getErrorStream();
        if (errorStream!=null) return errorStream;

        try{
            return conn.getInputStream();
        }
        catch(Exception e){
            return null;
        }
    }

    
  //**************************************************************************
  //** getText
  //**************************************************************************
  /** Used read through the entire response stream and cast it to a string.
   *  The string is encoded using the character set specified in the
   *  "Content-Type" header as returned by the getCharacterEncoding() method.
   *  Defaults to "UTF-8" if no character set is defined.
   */
    public String getText(){
        String charset = getCharacterEncoding();
        if (charset==null) charset = "UTF-8";
        return getText(charset);
    }


  //**************************************************************************
  //** getText
  //**************************************************************************
  /**  Used read through the entire response stream and cast it to a string.
   *   WARNING: This method will never throw an error.
   *
   *   @param charsetName Name of the character encoding used to read the file.
   *   Examples include UTF-8 and ISO-8859-1
   */    
    public String getText(String charsetName){
        try{
            return getBytes(true).toString(charsetName);
        }
        catch(Exception e){
            //e.printStackTrace();
        }
        return null;
    }


  //**************************************************************************
  //** getXML
  //**************************************************************************
  /**  Used read through the entire response stream and converts it to an xml
   *   DOM document.
   */
    public org.w3c.dom.Document getXML(){
        return javaxt.xml.DOM.createDocument(new ByteArrayInputStream(getBytes(true).toByteArray()));
    }


  //**************************************************************************
  //** getImage
  //**************************************************************************
  /**  Used read through the entire response stream and returns an Image.
   */
    public javaxt.io.Image getImage(){
        return new javaxt.io.Image(getBytes(true).toByteArray());
    }
    

  //**************************************************************************
  //** getBytes
  //**************************************************************************
  /** Used read through the entire response stream and returns a raw byte
   *  array (ByteArrayOutputStream). Note that this method does not
   *  automatically decompress the response if the data is compressed. Use
   *  the Response.getBytes(true) method to automatically decompress the
   *  response.
   */
    public ByteArrayOutputStream getBytes(){                
        return getBytes(false);
    }


  //**************************************************************************
  //** getBytes
  //**************************************************************************
  /** Used read through the entire response stream and returns a byte array
   *  ByteArrayOutputStream.
   *  @param deflate Option to decompress a gzip encoded response.
   */
    public ByteArrayOutputStream getBytes(boolean deflate){

        if (request.readTimeout>0) new Thread(new ReadTimeout(conn)).start();

        InputStream inputStream = this.getInputStream();
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        String encoding = this.getHeader("Content-Encoding");
        if (deflate && encoding!=null){
            if (encoding.equalsIgnoreCase("gzip")){

                GZIPInputStream gzipInputStream = null;
                byte[] buf = new byte[1024];
                int len;

                try{
                    gzipInputStream = new GZIPInputStream(inputStream);
                    while ((len = gzipInputStream.read(buf)) > 0) {
                        bas.write(buf, 0, len);
                    }
                }
                catch(Exception e){
                    //e.printStackTrace();
                }

                try { gzipInputStream.close(); } catch (Exception e){}
                try { bas.close(); } catch (Exception e){}
done = true;

                return bas;

            }
            else{
                System.err.println("Unsupported encoding:  " + encoding);
            }
        }
        else{

            //InputStream inputStream = null;
            byte[] buf = new byte[1024];
            int len=0;

            try{
                //inputStream = conn.getInputStream();
                while((len=inputStream.read(buf,0,1024))>-1) {
                    bas.write(buf,0,len);
                }
            }
            catch(Exception e){
                //e.printStackTrace();
            }


            try { inputStream.close(); } catch (Exception e){}
            try { bas.close(); } catch (Exception e){}
done = true;
            return bas;


        }
        return null;
    }


private boolean done = false;

    private class ReadTimeout implements Runnable {

        HttpURLConnection con;
        public ReadTimeout(URLConnection con) {
            this.con = (HttpURLConnection) con;
        }

        public void run() {
            try {
                Thread.sleep(request.readTimeout);
            } catch (InterruptedException e) {

            }

            if (done==false){
                con.disconnect();
                //System.out.println("** Timer thread forcing to quit connection");
            }
        }
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /** Returns the raw response headers returned from the server. Use the
   *  getText() method to get response body as a String.
   */
    public String toString(){

        java.util.Map headers = request.getResponseHeaders();

        StringBuffer out = new StringBuffer();
        java.util.Iterator it = headers.keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();
            if (key!=null){
                java.util.List<String> list = (java.util.List<String>) headers.get(key);
                for (int i=0; i<list.size(); i++){
                    out.append(key + ": " + list.get(i) + "\r\n");
                }
            }
            else{
                out.append(headers.get(key) + "\r\n");
            }
        }
        
        out.append("\r\n");
        return out.toString();
    }
}