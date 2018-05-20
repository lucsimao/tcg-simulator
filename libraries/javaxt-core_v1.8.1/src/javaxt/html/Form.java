package javaxt.html;

//******************************************************************************
//**  HTML Form
//******************************************************************************
/**
 *   Used to post html form data to an http server. Supports both HTTP GET and
 *   POST methods.
 *
 ******************************************************************************/

public class Form {

    private String name;
    private String method = "post";
    private String action = "";
    private java.util.ArrayList<Input> inputs = new java.util.ArrayList<Input>();


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class.
   *  @param method HTTP request method (e.g. "GET" or "POST")
   *  @param action URL to send the form data.
   */
    public Form(String method, String action){
        this.method = method;
        this.action = action;
    }

    public void setName(String name){
        this.name = name;
    }


    public void addInput(String name, String value){
        inputs.add(new Input(name,value));
    }
        
    public void addInput(Input input){
        inputs.add(input);
    }
    
    
  //**************************************************************************
  //** setMethod
  //**************************************************************************
  /** @param method HTTP request method (e.g. "GET" or "POST")
   */    
    public void setMethod(String method){
        this.method = method;
    }


  //**************************************************************************
  //** setAction
  //**************************************************************************
  /** @param action URL to send the form data.
   */
    public void setAction(String action){
        this.action = action;
    }


  //**************************************************************************
  //** submit
  //**************************************************************************
  /** Used to submit the form data. Returns the http response from the server.
   */
    public javaxt.http.Response submit(){
        
      //Convert the form inputs into a request
        StringBuffer payload = new StringBuffer();
        for (int i=0; i<inputs.size(); i++){
            if (i>0) payload.append("&");
            payload.append(inputs.get(i).toString());
        }


        javaxt.http.Request request = null;
        if (method.equalsIgnoreCase("post")){

          //Instantiate HTTP request
            request = new javaxt.http.Request(action);
            request.addHeader("Content-Type", "application/x-www-form-urlencoded");

          //Send the http request
            request.write(payload.toString());
        }
        else{
            javaxt.utils.URL url = new javaxt.utils.URL(action);
            payload.append("&" + url.getQueryString());
            url.setQueryString(url.getQueryString() + "&" + payload.toString());
            request = new javaxt.http.Request(url.toString());
        }


      //Return the response
        return request.getResponse();
        
    }
}