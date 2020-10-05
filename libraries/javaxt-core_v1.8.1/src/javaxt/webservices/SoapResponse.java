package javaxt.webservices;
import org.w3c.dom.*;
import javaxt.xml.DOM;

//******************************************************************************
//**  SoapResponse Class
//******************************************************************************
/**
 *   Used to encapsulate an XML/SOAP message returned from a WebService.
 *
 ******************************************************************************/

public class SoapResponse {

    private java.net.URL url;
    private java.util.Map<String, java.util.List<String>> headers;
    private String body;
    private String message;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Instantiates this class using an HTTP Response object. Note that the
   *  entire response is parsed and stored as a class variable. This can be
   *  problematic when dealing with very large SOAP messages.
   */
    protected SoapResponse(javaxt.http.Response response, String resultsNode) throws SoapException {

        
        int status = response.getStatus();
        if (status==200 || status==202 || status==203){
            body = response.getText();
            if (body!=null){

                url = response.getURL();
                headers = response.getHeaders();


              //Parse Response
                Document xml = DOM.createDocument(body);
                if (xml!=null){
                    try{
                        
                        NodeList Response = xml.getElementsByTagName(resultsNode);
                        if (Response!=null){

                          //Special Case: Probably Missing Namespace in Soap.resultsNode
                            if (Response.getLength()==0) {
                                resultsNode = getResultsNode(body, resultsNode);
                                Response = xml.getElementsByTagName(resultsNode);
                            }


                          //Get the content of the results node
                            message = DOM.getNodeValue(Response.item(0));

                            
                          //Hack to deal with unexpected outputs from DOM.getNodeValue().
                          //If the response node has children, the method will return an
                          //xml fragment which will include the resultsNode as the outer node.
                          //We obvously don't want that so we need to strip out the outer node.
                            if (DOM.hasChildren(Response.item(0))){
                                message = message.substring(message.indexOf(">")+1);
                                message = message.substring(0, message.lastIndexOf("</"));
                            }

                        }
                        else{
                            throw new SoapException(
                                    "Failed to parse SOAP Response. " +
                                    "Could not find the " + resultsNode + " node, " +
                                    "possibly due to a service exception.", body);
                        }
                    }
                    catch(Exception e){
                        throw new SoapException("Failed to parse SOAP Response. " + e.getLocalizedMessage(), body);
                    }
                }
                else{
                    throw new SoapException("Invalid SOAP Response. Response does not appear to be xml.", body);
                }
            }
            else{
                throw new SoapException("Invalid SOAP Response.", body);
            }
        }
        else{
            throw new SoapException(response.getMessage() + " (" + status + ")", response.getText());
        }
    }



    private String getResultsNode(String ServiceResponse, String resultsNode){
        resultsNode = ServiceResponse.substring(0,
                      ServiceResponse.toLowerCase().indexOf(resultsNode.toLowerCase()) + resultsNode.length());

        resultsNode = resultsNode.substring(resultsNode.lastIndexOf("<")+1);
        return resultsNode;
    }


  //**************************************************************************
  //** getHeaders
  //**************************************************************************
  /** Returns key/value map representing all the HTTP headers returned from
   *  the server.
   */
    public java.util.Map<String, java.util.List<String>> getHeaders(){
        return headers;
    }


  //**************************************************************************
  //** getURL
  //**************************************************************************
  /** Returns the url used to connect to the server. Note that this URL may
   *  differ from the one used to instantiate the Request object. 
   */
    public java.net.URL getURL(){
        return url;
    }


  //**************************************************************************
  //** getRawResponse
  //**************************************************************************
  /** Returns the body of the HTTP response returned from the server. The body
   *  contains the raw XML/SOAP document.
   */
    public String getBody(){
        return body;
    }


  //**************************************************************************
  //** toXML
  //**************************************************************************
  /** Converts the raw response found in the body of the SOAP message into an
   *  xml document.
   */
    public Document toXML(){
        return DOM.createDocument(message);
    }


  //**************************************************************************
  //** toByteArray
  //**************************************************************************
  /** Converts the raw response found in the body of the SOAP message into a
   *  byte array. Assumes that the response is Base64 encoded.
   */
    public byte[] toByteArray(){
        return javaxt.utils.Base64.decode(message.trim());
    }


  //**************************************************************************
  //** toImage
  //**************************************************************************
  /** Converts the raw response found in the body of the SOAP message into an
   *  image. Typically images are encoded in Base64.
   */
    public javaxt.io.Image toImage(){
        return new javaxt.io.Image(toByteArray());
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /** Returns the raw response found in the body of the SOAP message.
   */
    public String toString(){
        return message;
    }

}