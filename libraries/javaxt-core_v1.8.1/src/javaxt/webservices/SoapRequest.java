package javaxt.webservices;
import java.net.Proxy;

//******************************************************************************
//**  SoapRequest Class
//******************************************************************************
/**
 *   Used to execute a web service request using XML/SOAP over HTTP.
 *
 ******************************************************************************/

public class SoapRequest {

    private javaxt.http.Request request;
    private String resultsNode;
    private String body;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of SoapRequest. */

    public SoapRequest(Service service, Method method, Object parameters) {
        
      //Set the results node
        resultsNode = method.getResultsNodeName();


      //Create new http request
        request = new javaxt.http.Request(service.getURL());

        request.setHeader("Content-Type", "text/xml; charset=utf-8");
        request.setHeader("Accept", "text/html, text/xml, text/plain");

      //Add SoapAction to the header
        String action = method.getSoapAction();
        if (action!=null) request.addHeader("SOAPAction", action);


      //Create body
        String ns = "ns2";
        StringBuffer body = new StringBuffer();
        body.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
              "<soap:Envelope " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
              "<soap:Body>");

      //Add method tag
        //body.append("<" + method.getName() + " xmlns=\"" + service.getNameSpace() + "\">");
        body.append("<" + ns + ":" + method.getName() + " xmlns:" + ns + "=\"" + service.getNameSpace() + "\">");


      //Insert parameters (inside the method node)
        if (parameters!=null){
            if (method.getParameters().getArray()!=null){
                if (parameters instanceof Parameters){
                    body.append(((Parameters) parameters).toString(ns));
                }
                if (parameters instanceof Parameter){
                    Parameters params = method.getParameters();
                    params.setValue((Parameter)parameters);
                    body.append(params.toString(ns));
                }
                else if (parameters instanceof String){
                    body.append(parameters); //assumes parameters is a correctly formatted xml fragment
                }
                else if (parameters instanceof String[]){
                    String[] values = (String[]) parameters;
                    Parameter[] params = method.getParameters().getArray();
                    if (params!=null){
                        for (int i=0; i<params.length; i++ ) {
                             String parameterName = params[i].getName();
                             String parameterValue = values[i];
                             //body.append("<" + parameterName + "><![CDATA[" + parameterValue + "]]></" + parameterName + ">");
                             body.append("<" + ns + ":" + parameterName + ">");
                             body.append(javaxt.xml.DOM.escapeXml(parameterValue));
                             body.append("</" + ns + ":" + parameterName + ">");
                        }
                    }
                }
            }
        }

      //Close tags
        //body.append("</" + method.getName() + ">");
        body.append("</" + ns + ":" + method.getName() + ">");
        body.append("</soap:Body></soap:Envelope>");

        this.body = body.toString();
        request.setHeader("Content-Length", body.length()+"");
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
        request.setUseCache(useCache);
    }


  //**************************************************************************
  //** validateSSLCertificates
  //**************************************************************************
  /** Used to enable/disable certificate validation for HTTPS Connections.
   *  Note that this is set to false by default.
   */
    public void validateSSLCertificates(boolean validateCertificates){
        request.validateSSLCertificates(validateCertificates);
    }


  //**************************************************************************
  //** setMaxRedirects
  //**************************************************************************
  /** Sets the maximum number of redirects to follow. By default, this number
   *  is set to 5.
   */
    public void setNumRedirects(int maxRedirects){
        request.setNumRedirects(maxRedirects);
    }

    public void setCredentials(String username, String password){
        request.setCredentials(username, password);
    }

    public void setUserName(String username){
        request.setUserName(username);
    }

    public void setPassword(String password){
        request.setPassword(password);
    }


  //**************************************************************************
  //** setProxy
  //**************************************************************************
  /** Used to set the http proxy.
   */
    public Proxy setProxy(String proxyHost, int proxyPort){
        return request.setProxy(proxyHost, proxyPort);
    }


  //**************************************************************************
  //** setProxy
  //**************************************************************************
  /** Used to set the http proxy.
   */
    public Proxy setProxy(String httpProxy){
        return request.setProxy(httpProxy);
    }

  //**************************************************************************
  //** setProxy
  //**************************************************************************
  /** Used to set the http proxy as needed.
   */
    public void setProxy(Proxy httpProxy){
        request.setProxy(httpProxy);
    }


  //**************************************************************************
  //** setHeader
  //**************************************************************************
  /**  Used to set a Request Property in the HTTP header (e.g. "User-Agent").
   */
    public void setHeader(String key, String value){
        request.setHeader(key, value);
    }

    
  //**************************************************************************
  //** addHeader
  //**************************************************************************
  /**  Used to add a Request Property to the HTTP header (e.g. "User-Agent").
   */
    public void addHeader(String key, String value){
        request.addHeader(key, value);
    }


  //**************************************************************************
  //** getBody
  //**************************************************************************
  /** Returns the raw XML sent to the web service.
   */
    public String getBody(){
        return body;
    }


  //**************************************************************************
  //** getResponse
  //**************************************************************************
  /** Used to execute the web service method specified in the constructor and
   *  returns a response from the server.
   */
    public SoapResponse getResponse() throws SoapException {
        request.write(body);
        return new SoapResponse(request.getResponse(), resultsNode);
    }

}