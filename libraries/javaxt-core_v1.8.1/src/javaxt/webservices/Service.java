package javaxt.webservices;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import javaxt.xml.DOM;

//******************************************************************************
//**  Service Class
//******************************************************************************
/**
 *   Used to represent a web service.
 *
 ******************************************************************************/

public class Service {

    private String Name = "";
    private String Description = "";
    private String URL = "";
    private String NameSpace;
    private Method[] Methods = null;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Instantiates this class using a "Service" node from an SSD.
   */
    protected Service(Node ServiceNode){

        NamedNodeMap attr = ServiceNode.getAttributes();
        Name = DOM.getAttributeValue(attr, "name");
        NameSpace = DOM.getAttributeValue(attr, "namespace");
        URL = DOM.getAttributeValue(attr, "url");

        NodeList ChildNodes = ServiceNode.getChildNodes();
        for (int j=0; j<ChildNodes.getLength(); j++ ) {
            if (ChildNodes.item(j).getNodeType()==1){
                String NodeName = ChildNodes.item(j).getNodeName();
                String NodeValue = ChildNodes.item(j).getTextContent();
                if (NodeName.toLowerCase().equals("description")){
                    Description = NodeValue;
                }
                if (NodeName.toLowerCase().equals("methods")){

                    NodeList methodNodes = ChildNodes.item(j).getChildNodes();
                    java.util.ArrayList<Method> methods = new java.util.ArrayList<Method>();
                    for (Node methodNode : DOM.getNodes(methodNodes)){
                        methods.add(new Method(methodNode));
                    }
                    if (!methods.isEmpty()){
                        Methods = methods.toArray(new Method[methods.size()]);
                    }
                }
            }
        }
    }


    public String getName(){return Name;}
    public String getDescription(){return Description;}
    public String getURL(){return URL;}
    public String getNameSpace(){return NameSpace;}
    public Method[] getMethods(){return Methods;}

  //Equals
    public boolean equals(String ServiceName){
        return Name.equalsIgnoreCase(ServiceName);
    }

  //Get Method
    public Method getMethod(String MethodName){

        if (MethodName==null) MethodName = "";
        else MethodName = MethodName.trim();

        if (MethodName.equals("")){
            return getMethod(0);
        }
        else{
            for (int i=0; i<Methods.length; i++){
                 if (Methods[i].equals(MethodName)){
                     return Methods[i];
                 }
            }
        }
        return null;
    }

  //Get Method
    public Method getMethod(int i){
        if (Methods==null){
            return getMethod(0);
        }
        else{
            if (i<Methods.length){
                return Methods[i];
            }
        }
        return null;
    }

    public void setURL(String URL){
        this.URL = URL;
    }

}
