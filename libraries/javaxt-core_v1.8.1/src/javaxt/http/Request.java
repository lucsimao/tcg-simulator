package javaxt.http;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import javax.net.ssl.*;

//******************************************************************************
//**  Http Request
//******************************************************************************
/**
 *   Used to set up a connection to an http server. This class is used in
 *   conjunction with the HTTP Response class. Example:
 <pre>
    javaxt.http.Response response = new javaxt.http.Request(url).getResponse();
 </pre>
 *
 *   A slightly more complex example might look like this:
 <pre>
    javaxt.http.Request request = new javaxt.http.Request(url);
    request.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.10)");
    request.setHeader("Accept-Encoding", "deflate"); //no gzip encoding
    java.io.InputStream inputStream = request.getResponse().getInputStream();
    new javaxt.io.File("/temp/image.jpg").write(inputStream);
    inputStream.close();
 </pre>
 *
 ******************************************************************************/

public class Request {

    private URLConnection conn = null;
    private Proxy HttpProxy;
    private java.net.URL url;
    private java.net.URL orgURL;
    private int connectionTimeout = 0;
    protected int readTimeout = 0;
    private boolean useCache = false;
    private int maxRedirects = 5;
    private String username;
    private String password;
    private String method;

    private java.util.Map<String, List<String>> requestHeaders = null;
    private HashMap<String, List<String>> RequestProperties = new HashMap<String, List<String>>();

  //Http response properties
    private java.util.Map<String, List<String>> headers = null;
    private String protocol;
    private String version;
    private int responseCode;
    private String message;


    private static TrustManager[] trustAllCerts = new TrustManager[]{
    new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
        }
    }
    };

    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private boolean validateCertificates = true;


    public Request clone(){
        Request request = new Request(orgURL);
        request.HttpProxy = HttpProxy;
        request.connectionTimeout = connectionTimeout;
        request.readTimeout = readTimeout;
        request.useCache = useCache;
        request.maxRedirects = maxRedirects;
        request.username = username;
        request.password = password;
        request.requestHeaders = requestHeaders;
        request.RequestProperties = RequestProperties;
        request.method = method;
        return request;
    }

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** @param url URL endpoint
   */
    public Request(String url){
        this(url, null);
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** @param url URL endpoint
   *  @param httpProxy Proxy server
   */
    public Request(String url, String httpProxy) {
        this(getURL(url), httpProxy);
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** @param url URL endpoint
   */
    public Request(java.net.URL url){
        this(url, null);
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** @param url URL endpoint
   *  @param httpProxy Proxy server
   */
    public Request(java.net.URL url, String httpProxy){
        this.url = orgURL = url;
        setProxy(httpProxy);
        initHeaders();
    }


    private static java.net.URL getURL(String url){
        try{
            return new java.net.URL(url);
        }
        catch (Exception e) {
            return null;
        }
    }

  //**************************************************************************
  //** initHeaders
  //**************************************************************************
    
    private void initHeaders(){
        this.setUseCache(false);
        this.setHeader("Accept", "*/*");
        this.setHeader("Accept-Encoding", "gzip,deflate");
        this.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
      //this.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.10)"); //windows xp
        this.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0"); //windows 7
      //this.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0"); //windows 8
    }


    public void setURL(java.net.URL url){
        this.url = url;
        this.orgURL = url;
    }

  //**************************************************************************
  //** getURL
  //**************************************************************************
  /** Returns the URL used to make the request. Note that the URL may be
   *  different from the URL used to invoke this class if the request
   *  encounters a redirect. See setNumRedirects() for more information.
   *  Use the getInitialURL() to get the original URL used to instantiate this
   *  class.
   */
    public java.net.URL getURL(){
        return url;
    }


  //**************************************************************************
  //** getInitialURL
  //**************************************************************************
  /** Returns the URL used to instantiate this class. This URL may be 
   *  different from the URL used to execute the request. See getURL() for  
   *  more information.
   */
    public java.net.URL getInitialURL(){
        return orgURL;
    }


  //**************************************************************************
  //** setRequestMethod
  //**************************************************************************
  /** Used to specify the request method ("GET", "POST", "PUT", "DELETE", etc).
   *  By default, requests are made using "GET" when fetching data and "POST" 
   *  when writing data to the server. This method is used to override these 
   *  defaults (e.g. "PUT" and "DELETE" for REST services). 
   */
    public void setRequestMethod(String method){
        if (method!=null){
            method = method.trim();
            if (method.equalsIgnoreCase("DELETE")) method = "DELETE";
            else if (method.equalsIgnoreCase("PUT")) method = "PUT";
            this.method = method;
        }
    }


  //**************************************************************************
  //** getResponse
  //**************************************************************************
  /**  Used to return the response from the server.
   */
    public Response getResponse(){
        if (conn==null) conn = getConnection(false);
        return new Response(this, conn);
    }


  //**************************************************************************
  //** setUseCache
  //**************************************************************************
  /**  Sets the header associated with cache-control. If true, the protocol is
   *   allowed to use caching whenever it can. If false, the protocol must
   *   always try to get a fresh copy of the object. By default, the useCache
   *   variable is set to false.
   */
    public void setUseCache(boolean useCache){
        this.useCache = useCache;
    }


  //**************************************************************************
  //** validateSSLCertificates
  //**************************************************************************
  /** Used to enable/disable certificate validation for HTTPS Connections.
   *  By default, certificates are validated via a Certificate Authority (CA).
   *  However, there are times where you may not want to validate a site's
   *  certificate (e.g. connecting to a intranet site or a development server 
   *  with self-signed certificate). In these cases, you can bypass the 
   *  validation process by setting this method to false. 
   */
    public void validateSSLCertificates(boolean validateCertificates){
        this.validateCertificates = validateCertificates;
    }


  //**************************************************************************
  //** setMaxRedirects
  //**************************************************************************
  /** Sets the maximum number of redirects to follow. By default, this number
   *  is set to 5.
   */
    public void setNumRedirects(int maxRedirects){
        this.maxRedirects = maxRedirects;
    }


  //**************************************************************************
  //** setCredentials
  //**************************************************************************
  /** Used to set the username and password used to authenticate the client.
   *  The credentials are used in the "Authorization" request header and 
   *  encoded using "Basic" authentication. Note that credentials encoded 
   *  using "Basic" authentication can be easily decoded. As a general rule, 
   *  do not pass credentials to sites that are not secured using SSL.
   */
    public void setCredentials(String username, String password){
        this.username = username;
        this.password = password;
    }


  //**************************************************************************
  //** setUserName
  //**************************************************************************
  /** Used to set the username used to authenticate the client. See 
   *  setCredentials() for more information.
   */    
    public void setUserName(String username){
        this.username = username;
    }


  //**************************************************************************
  //** setPassword
  //**************************************************************************
  /** Used to set the password used to authenticate the client. See 
   *  setCredentials() for more information.
   */ 
    public void setPassword(String password){
        this.password = password;
    }


  //**************************************************************************
  //** getCredentials
  //**************************************************************************
  /** Used to get a Base64 encoded string representing the username and 
   *  password. The string is used for "Basic" authentication.
   */ 
    private String getCredentials() throws Exception {
        if (username==null || password==null) return null;
        else
            return javaxt.utils.Base64.encodeBytes(
                (username + ":" + password).getBytes("UTF-8"));
    }


  //**************************************************************************
  //** setConnectTimeout
  //**************************************************************************
  /** Connection timeout in milliseconds
   */
    public void setConnectTimeout(int timeout){
        if (timeout>0) connectionTimeout = timeout;
    }


  //**************************************************************************
  //** setReadTimeout
  //**************************************************************************
  /** Read timeout in milliseconds.
   */
    public void setReadTimeout(int timeout){
        if (readTimeout>0) readTimeout = timeout;
    }


  //**************************************************************************
  //** ConnectTimeout
  //**************************************************************************
  /** Thread used to enforce the connectionTimeout property.
   */
    private class ConnectTimeout implements Runnable {

        HttpURLConnection con;
        public ConnectTimeout(URLConnection con) {
            this.con = (HttpURLConnection) con;
        }

        public void run() {
            try {
                Thread.sleep(connectionTimeout);
            } catch (InterruptedException e) {

            }

            if (responseCode==-1){
                con.disconnect();
                //System.out.println("** Timer thread forcing to quit connection");
            }
        }
    }


  //**************************************************************************
  //** write
  //**************************************************************************
  /**  Used to open an HTTP connection to the URL and POST data to the server.
   *   @param payload InputStream containing the body of the HTTP request.
   */
    public void write(InputStream payload) {

        if (conn==null) conn = getConnection(true);

        OutputStream output = null;
        try{
            output = conn.getOutputStream();
            byte[] buf = new byte[8192]; //8KB
            int i = 0;
            while((i=payload.read(buf))!=-1) {
              output.write(buf, 0, i);
            }

        }
        catch (Exception e){}
        finally {
            try { if (output != null) output.close(); }
            catch (Exception e){}
        }

        parseResponse(conn);

    }


  //**************************************************************************
  //** write
  //**************************************************************************
  /**  Used to open an HTTP connection to the URL and POST data to the server.
   *   @param payload String containing the body of the HTTP request.
   */
    public void write(String payload) {
        write(payload.getBytes());
    }


  //**************************************************************************
  //** write
  //**************************************************************************
  /**  Used to open an HTTP connection to the URL and POST data to the server.
   *   @param payload Byte array containing the body of the HTTP request.
   */
    public void write(byte[] payload) {
        setHeader("Content-Length", payload.length + "");
        if (conn==null) conn = getConnection(true);

        try{
            conn.getOutputStream().write(payload);
        }
        catch(Exception e){
            //e.printStackTrace();
        }

        parseResponse(conn);
    }


  //**************************************************************************
  //** write
  //**************************************************************************
  /** Used to post an array of form inputs to a server. Form inputs can 
   *  include text or binary data, including files. Payload is normally
   *  "multipart/form-data" encoded.
   */
    public void write(javaxt.html.Input[] inputs){        
        
      //Generate boundary
        String boundary = "---------------------------";
        for (int i=0; i<14; i++) boundary += new java.util.Random().nextInt(10);
        int boundarySize = boundary.length();
        
        try{
            
          //Compute payload size and generate content metadata for each input
            long size = 0;
            java.util.ArrayList<byte[]> metadata = new java.util.ArrayList<byte[]>();
            for (int i=0; i<inputs.length; i++){
                
                javaxt.html.Input input = inputs[i];
                String contentType = "";
                String fileName = "";
                long inputLength = 0;
                
                if (input.isFile()){
                    javaxt.io.File file = (javaxt.io.File) input.getValue();
                    fileName = "; filename=\"" + file.getName() + "\"";
                    contentType = "Content-Type: " + file.getContentType() + "\r\n";
                    inputLength = file.getSize();
                }
                else{
                    inputLength = input.getSize();
                }
                
                String contentDisposition = "Content-Disposition: form-data; name=\"" + input.getName() + "\"" + fileName;
                byte[] md = (contentDisposition + "\r\n" + contentType + "\r\n").getBytes("UTF-8");
                metadata.add(md);
                size += ((i>0?2:0) + 2 + boundarySize + 2); //CRLF + 2 hyphens + boundary + CRLF
                size += md.length + inputLength;
            }
            size+=(boundarySize+8); //CRLF + 2 hyphens + boundary + 2 hyphens + CRLF

            
          //Set request headers
            setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
            setHeader("Content-Length", size+"");


          //Open socket
            if (conn==null) conn = getConnection(true);

            
          //Write content
            java.io.OutputStream outputStream = null;
            try{
                outputStream = conn.getOutputStream();
                for (int i=0; i<inputs.length; i++){                    

                  //Write boundary and input metadata
                    byte[] bd = ((i>0?"\r\n":"") + "--" + boundary + "\r\n").getBytes("UTF-8");
                    byte[] md = metadata.get(i);
                    outputStream.write(bd);
                    outputStream.write(md);
                    
                  //Write input value
                    if (inputs[i].isFile()){
                        javaxt.io.File file = (javaxt.io.File) inputs[i].getValue();
                        java.io.InputStream inputStream = file.getInputStream();
                        byte[] b = new byte[1024];
                        int x=0;
                        while ( (x = inputStream.read(b)) != -1) {
                            outputStream.write(b,0,x);
                        }            
                        inputStream.close();
                    }
                    else{
                        outputStream.write(inputs[i].toByteArray());
                    }
                }
                
              //Write footer and close outputstream
                byte[] bd = ("\r\n--" + boundary + "--\r\n").getBytes("UTF-8");
                outputStream.write(bd);
                outputStream.close();                
            }
            catch(IOException e){
                if (outputStream!=null){
                    try{outputStream.close();} 
                    catch(Exception ex){}
                }
            }
        }
        catch(java.io.UnsupportedEncodingException e){} //should never happen!
        parseResponse(conn);
    }


    public List<String> getHeader(String key){
        java.util.Iterator<String> it = RequestProperties.keySet().iterator();
        while (it.hasNext()){
            String currKey = it.next();
            if (key.equalsIgnoreCase(currKey)){
                return RequestProperties.get(currKey);
            }
        }
        return null;
    }

  //**************************************************************************
  //** setHeader
  //**************************************************************************
  /**  Used to set a Request Property in the HTTP header (e.g. "User-Agent").
   */
    public void setHeader(String key, String value){

        boolean foundProperty = false;
        java.util.Iterator<String> it = RequestProperties.keySet().iterator();
        while (it.hasNext()){
            String currKey = it.next();
            if (key.equalsIgnoreCase(currKey)){
                foundProperty = true;
                List<String> values = new ArrayList<String>();
                values.add(value);
                RequestProperties.put(currKey, values);
                break;
            }
        }

        if (!foundProperty){
            List<String> values = new ArrayList<String>();
            values.add(value);
            RequestProperties.put(key, values);
        }
    }


  //**************************************************************************
  //** addHeader
  //**************************************************************************
  /**  Used to add a Request Property to the HTTP header (e.g. "User-Agent").
   */
    public void addHeader(String key, String value){
        if (key.equalsIgnoreCase("If-None-Match") || key.equalsIgnoreCase("If-Modified-Since") && value!=null){
            useCache = true;
        }

        boolean foundProperty = false;
        java.util.Iterator<String> it = RequestProperties.keySet().iterator();
        while (it.hasNext()){
            String currKey = it.next();
            if (key.equalsIgnoreCase(currKey)){
                foundProperty = true;
                List<String> values = RequestProperties.get(currKey);
                if (values==null) values = new ArrayList<String>();
                values.add(value);
                RequestProperties.put(currKey, values);
                break;
            }
        }

        if (!foundProperty){
            List<String> values = new ArrayList<String>();
            values.add(value);
            RequestProperties.put(key, values);
        }
    }


  //**************************************************************************
  //** connect
  //**************************************************************************
  /**  Used to create a URLConnection.
   */
    private URLConnection connect(boolean doOutput){
        try {
            
          //Set flag used to indicate whether this is an SSL request
            boolean ssl = url.getProtocol().equalsIgnoreCase("https");


          //By default, Java 1.6 and earlier use the SSLv2 protocol to initiate
          //an SSL handshake. For security reasons, most modern webservers will
          //actively refuse to accept SSLv2 messages. As a workaround, we'll
          //try to use to use SSLv3 or TLSv1.
            if (ssl){
                String[] arr = System.getProperty("java.version").split("\\.");
                if (Double.valueOf(arr[0] + "." + arr[1])<1.7){
                    String sslProtocols = System.getProperty("https.protocols");
                    if (sslProtocols==null){
                        System.setProperty("https.protocols", "TLSv1,SSLv3");
                    }
                }
            }


          //Disable SSL certificate validation as needed (Part 1)
            if (ssl && validateCertificates==false){
                try {
                    //SSLContext sc = SSLContext.getInstance("SSL");
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                } catch (Exception e) {
                }
            }


          //Encode whitespaces and other illegal chars using the javaxt URL class
            url = new javaxt.utils.URL(url).toURL();
            

          //Open connection
            URLConnection conn;
            if (HttpProxy==null || isLocalHost(url.getHost())){
                conn = url.openConnection();
            }
            else{
                conn = url.openConnection(HttpProxy);
            }

            
          //Set request method as needed
            if (method!=null){
                HttpsURLConnection con = (HttpsURLConnection)conn;
                con.setRequestMethod(method);
                System.out.println(method);
            }
            

          //Set timeouts
            if (connectionTimeout>0){
                new Thread(new ConnectTimeout(conn)).start();
                conn.setConnectTimeout(connectionTimeout);
            }

            if (readTimeout>0){
                //new Thread(new ReadTimeout(conn)).start();
                conn.setReadTimeout(readTimeout);
            }



          //Disable HTTP redirects
            if (ssl){
                HttpsURLConnection con = (HttpsURLConnection)conn;
                con.setInstanceFollowRedirects(false);
            }
            else{
                HttpURLConnection con = (HttpURLConnection)conn;
                con.setInstanceFollowRedirects(false);
            }


          //Disable SSL certificate validation as needed (Part 2)
            if (ssl && validateCertificates==false){
            	HttpsURLConnection con = (HttpsURLConnection)conn;
        	con.setHostnameVerifier(DO_NOT_VERIFY);
            }


          //Set request headers
            conn.setUseCaches(useCache);
            if (doOutput) conn.setDoOutput(true);

            String credentials = getCredentials();
            if (credentials!=null) conn.setRequestProperty ("Authorization", "Basic " + credentials);

            java.util.Iterator<String> it = RequestProperties.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                List<String> values = RequestProperties.get(key);
                if (values!=null){
                    if (values.size()==1){
                        conn.setRequestProperty(key, values.iterator().next());
                    }
                    else{
                        java.util.Iterator<String> value = values.iterator();
                        while (value.hasNext()){
                            conn.addRequestProperty(key, value.next());
                        }
                    }
                }
            }


            return conn;

        }
        catch (Exception e) {
            return null;
        }
    }


  //**************************************************************************
  //** getConnection
  //**************************************************************************
  /**  Used to open a connection to a url/host.
   */
    private URLConnection getConnection(boolean doOutput){

        
      //If we're writing data to the socket (e.g. "POST") and maxRedirects<0, 
      //simply open a connection and let the caller figure out what to do with 
      //the response.
        if (doOutput && maxRedirects<1){
            URLConnection conn = this.connect(true);
            requestHeaders = conn.getRequestProperties();
            return conn;
        }
        

      //If we're still here, perform a "GET" request and look for 3XX series
      //response codes. Follow redirects as needed. Note that once the
      //parseResponse() method is called, clients can no longer write to the
      //socket. Therefore, for "POST" requests, we must open a writable socket
      //after parsing the response from the original "GET" request. The net
      //effect is that when posting data and maxRedirects>0, the HTTP client
      //will make 2 requests to the server: 1 to test the URL, the second to
      //post data.
        conn = null;
        URLConnection conn = this.connect(false);
        if (conn!=null){
	    requestHeaders = conn.getRequestProperties();
            parseResponse(conn);
            if ((responseCode>=300 && responseCode<400) && maxRedirects>0){
                int numRedirects = 0;
                while (responseCode>=300 && responseCode<400){

                    if (useCache && responseCode==304) break;


                  //Parse location response header
                    String location = getResponseHeader("Location");
                    javaxt.utils.URL newUrl = new javaxt.utils.URL(location);
                    if (newUrl.getProtocol()==null){
                        javaxt.utils.URL url = new javaxt.utils.URL(this.url);
                        url.setPath(location);
                        this.url = url.toURL();
                    }
                    else{
                        this.url = newUrl.toURL();
                    }


                  //Connect to the new url
                    try{
                        conn = this.connect(false);
                        parseResponse(conn);
                        numRedirects++;
                        if (numRedirects>maxRedirects) break;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }


      //Open a writable socket as needed.
        if (doOutput){
            conn = connect(true);
            requestHeaders = conn.getRequestProperties();
        }

        return conn;
    }


  //**************************************************************************
  //** parseResponse
  //**************************************************************************
  /** Used to parse the first line from the http response. Note that once,
   *  this method is called, clients can no longer write to the socket!
   */
    private void parseResponse(URLConnection conn){


        protocol = "";
        version = "";
        responseCode = -1;
        message = "";


        //requestHeaders = conn.getRequestProperties();


        headers = conn.getHeaderFields(); //<-- Once this is called, clients can no longer write to the socket!
        if (!headers.isEmpty()){

            List status = (List)headers.get(null);
            if (status!=null){

                java.util.StringTokenizer st = new java.util.StringTokenizer( (String)(status).get(0) );
                if (st.hasMoreTokens()) protocol = st.nextToken().trim().toUpperCase();
                if (protocol.contains("/")) {
                    String temp = protocol;
                    protocol = temp.substring(0,temp.indexOf("/"));
                    version = temp.substring(temp.indexOf("/")+1);
                }
                else{
                    protocol = "HTTP";
                    version = "1.1";
                }

                if (st.hasMoreTokens()) responseCode = javaxt.utils.string.toInt(st.nextToken().trim());
                if (st.hasMoreTokens()){
                    message = "";
                    while (st.hasMoreTokens()){
                        message += st.nextToken() + " ";
                    }
                    message = message.trim();
                }
            }
        }

    }




  //**************************************************************************
  //** getExpiration
  //**************************************************************************
  /** Returns the time when the document should be considered expired.
   *  The time will be zero if the document always needs to be revalidated.
   *  It will be <code>null</code> if no expiration time is specified.
   */
    private Long getExpiration(URLConnection connection, long baseTime) {

        DateFormat PATTERN_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); //, Locale.US

        String cacheControl = connection.getHeaderField("Cache-Control");
        if (cacheControl != null) {
            java.util.StringTokenizer tok = new java.util.StringTokenizer(cacheControl, ",");
            while(tok.hasMoreTokens()) {
                String token = tok.nextToken().trim().toLowerCase();
                if ("must-revalidate".equals(token)) {
                    return new Long(0);
                }
                else if (token.startsWith("max-age")) {
                    int eqIdx = token.indexOf('=');
                    if (eqIdx != -1) {
                        String value = token.substring(eqIdx+1).trim();
                        int seconds;
                        try {
                            seconds = Integer.parseInt(value);
                            return new Long(baseTime + seconds * 1000);
                        }
                        catch(NumberFormatException nfe) {
                            System.err.println("getExpiration(): Bad Cache-Control max-age value: " + value);
                            // ignore
                        }
                    }
                }
            }
        }

        String expires = connection.getHeaderField("Expires");
        if (expires != null) {
            try {
                synchronized(PATTERN_RFC1123) {
                    java.util.Date expDate = PATTERN_RFC1123.parse(expires);
                    return new Long(expDate.getTime());
                }
            }
            catch(java.text.ParseException pe) {
                int seconds;
                try {
                    seconds = Integer.parseInt(expires);
                    return new Long(baseTime + seconds * 1000);
                }
                catch(NumberFormatException nfe) {
                    System.err.println("getExpiration(): Bad Expires header value: " + expires);
                }
            }
        }
        return null;
    }


  //**************************************************************************
  //** getResponseCode
  //**************************************************************************
  /** Returns the HTTP status code extracted from the first line in the
   *  response.
   */
    protected int getResponseCode(){
        return responseCode;
    }


  //**************************************************************************
  //** getResponseMessage
  //**************************************************************************
  /** Returns the message extracted from the first line in the response.
   */
    protected String getResponseMessage(){
        return message;
    }


    protected java.util.Map<String, List<String>> getResponseHeaders(){
        return headers;
    }

    public java.util.Map<String, List<String>> getRequestHeaders(){
        if (requestHeaders!=null) return requestHeaders;
        else{
            return RequestProperties;
        }
    }


    protected String[] getResponseHeaders(String headerName){

        if (headers==null) return new String[0];

      //Iterate through the headers and find the matching header
        java.util.ArrayList<String> values = new java.util.ArrayList<String>();
        java.util.Iterator<String> it = headers.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            if (key!=null){
                if (key.equalsIgnoreCase(headerName)){

                    java.util.List<String> list = headers.get(key);
                    java.util.Iterator<String> val = list.iterator();
                    while (val.hasNext()){
                        values.add(val.next());
                    }

                }
            }
        }

      //Convert the list into a string array
        return values.toArray(new String[values.size()]);
    }
    

    protected String getResponseHeader(String headerName){
        String[] arr = getResponseHeaders(headerName);
        if (arr.length>0) return getResponseHeaders(headerName)[0];
        return null;
    }


  //**************************************************************************
  //** getProxy
  //**************************************************************************
  /** Returns the http proxy address.
   */
    public String getProxy(){
        if (HttpProxy==null) return null;
        InetSocketAddress sa = (InetSocketAddress) HttpProxy.address();
        String proxy = sa.toString();
        if (proxy.startsWith("/") && proxy.length()>1){
            proxy = proxy.substring(1);
        }
        return proxy;
    }


  //**************************************************************************
  //** setProxy
  //**************************************************************************
  /** Used to set the http proxy.
   */
    public Proxy setProxy(String proxyHost, int proxyPort){
        SocketAddress proxyAddr = InetSocketAddress.createUnresolved(proxyHost, proxyPort); //new InetSocketAddress(proxyHost, proxyPort);
        HttpProxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
        return HttpProxy;
    }


  //**************************************************************************
  //** setProxy
  //**************************************************************************
  /** Used to set the http proxy.
   */
    public Proxy setProxy(String httpProxy){
        if (httpProxy!=null)
        if (httpProxy.length()>0){
            String[] arr = httpProxy.split(":");
            String httpHost = arr[0];
            int httpPort = 80;
            if (arr.length>0){
                httpPort = Integer.valueOf(arr[1]).intValue();
            }
            return setProxy(httpHost,httpPort);
        }
         return null;
    }


  //**************************************************************************
  //** setProxy
  //**************************************************************************
  /** Used to set the http proxy as needed.
   */
    public void setProxy(Proxy httpProxy){
        HttpProxy = httpProxy;
    }


  //**************************************************************************
  //** isProxyAvailable
  //**************************************************************************
  /** Used to check whether a proxy server is online/accessible.
   */
    public boolean isProxyAvailable(String proxyHost, int proxyPort){

        try {
           InetAddress address = InetAddress.getByName(proxyHost);
           System.out.println("Name: " + address.getHostName());
           System.out.println("Addr: " + address.getHostAddress());
           System.out.println("Reach: " + address.isReachable(3000));
           return true;
        }
        catch (UnknownHostException e) {}
        catch (IOException e) {}
        return false;
    }


  //**************************************************************************
  //** isLocalHost
  //**************************************************************************
  /** Used to determine whether to use the proxy server. doesn't account for
   *  the local machine name.
   */
    private boolean isLocalHost(String host){
        host = host.toLowerCase();
        if (host.equals("localhost") || host.equals("127.0.0.1")){
            return true;
        }
        else{
            return false;
        }
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /**  Returns the request headers sent to the server.
   */
    public String toString(){


        StringBuffer out = new StringBuffer();
        //System.out.println("Request Header");
        //System.out.println("------------------------------------------------");
out.append(url + "\r\n");
        java.util.Map<String,List<String>> requestHeaders = getRequestHeaders();
        if (requestHeaders!=null){
            java.util.Iterator it = requestHeaders.keySet().iterator();
            while(it.hasNext()){
                String key = (String) it.next();
                if (key!=null){
                    java.util.List list = (java.util.List) requestHeaders.get(key);
                    for (int i=0; i<list.size(); i++){
                        String value = list.get(i).toString();
                        out.append(key + ": " + value + "\r\n");
                    }
                }
                else{
                    out.append(requestHeaders.get(key) + "\r\n");
                }
            }
        }

        out.append("\r\n");
        return out.toString();
    }

}