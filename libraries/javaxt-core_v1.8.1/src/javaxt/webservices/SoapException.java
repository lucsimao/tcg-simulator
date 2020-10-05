package javaxt.webservices;

//******************************************************************************
//**  SoapException Class
//******************************************************************************
/**
 *   Custom error thrown when parsing a SOAP response.
 *
 ******************************************************************************/

public class SoapException extends Exception {

    private String serverResponse;

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of SoapException. */

    public SoapException(String message, String serverResponse) {
        super(getErrorMessage(message, serverResponse));
        this.serverResponse = serverResponse;
    }

    public String getServerResponse(){
        return serverResponse;
    }

    private static String getErrorMessage(String message, String serverResponse){
        org.w3c.dom.Document xml = javaxt.xml.DOM.createDocument(serverResponse);
        if (xml!=null){
            org.w3c.dom.Node[] faultstrings = javaxt.xml.DOM.getElementsByTagName("faultstring", xml);
            if (faultstrings.length>0){
                return faultstrings[0].getTextContent();
            }
        }
        return message;
    }
}