package javaxt.webservices;
import org.w3c.dom.*;
import javaxt.xml.DOM;
import javaxt.utils.Value;

//******************************************************************************
//**  Parameter Class
//******************************************************************************
/**
 *   Used to represent a parameter associated with a web method.
 *
 ******************************************************************************/

public class Parameter {

    private String name;
    private String type;
    private Object value;
    private int minOccurs = 0;
    private int maxOccurs = 1;
    protected boolean IsNillable;
    protected boolean IsAttribute;
    
    private Parameter[] children = null;
    private Option[] options = null;
    private Node parentNode;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Instantiates this class using a "Parameter" node from an SSD.
   */
    protected Parameter(Node ParameterNode) throws InstantiationException {

        if (!ParameterNode.getNodeName().equalsIgnoreCase("parameter")){
            throw new InstantiationException(DOM.getText(ParameterNode));
        }

        this.parentNode = ParameterNode.getParentNode();

        NamedNodeMap attr = ParameterNode.getAttributes();
        name = DOM.getAttributeValue(attr, "name");
        type = DOM.getAttributeValue(attr, "type");
        IsAttribute = bool(DOM.getAttributeValue(attr, "isattribute"));
        IsNillable = bool(DOM.getAttributeValue(attr, "isnillable"));

        if (name.length()==0){
            throw new InstantiationException(DOM.getText(ParameterNode));
        }


      //Get min occurances
        try{
            minOccurs = cint(DOM.getAttributeValue(attr, "minOccurs").trim());
            if (minOccurs<0) minOccurs = 0;
        }
        catch (Exception e){}


      //Get max occurances
        String maxOccurs = DOM.getAttributeValue(attr, "maxOccurs").trim();
        if (maxOccurs.equalsIgnoreCase("unbounded")){
            this.maxOccurs = 32767;
        }
        else if(maxOccurs.equals("")){
            this.maxOccurs = 1;
        }
        else{
            try{
                this.maxOccurs = cint(maxOccurs);
                if (this.maxOccurs<0) this.maxOccurs = 0;
            }
            catch (Exception e){}
        }


      //Get children
        java.util.ArrayList<Parameter> params = new java.util.ArrayList<Parameter>();
        for (Node node : DOM.getNodes(ParameterNode.getChildNodes())){

            if (node.getNodeName().equalsIgnoreCase("parameter")){
                try{
                    params.add(new Parameter(node));
                }
                catch(InstantiationException e){
                    e.printStackTrace();
                }
            }

            //params.add(new Parameter(node));
        }
        if (!params.isEmpty()){
            children = params.toArray(new Parameter[params.size()]);
        }


      //Get list of possible values (i.e. options)
        java.util.ArrayList<Option> options = new java.util.ArrayList<Option>();
        for (Node node : DOM.getNodes(ParameterNode.getChildNodes())){
            if (node.getNodeName().equalsIgnoreCase("options")){
                for (Node optionNode : DOM.getNodes(node.getChildNodes())){
                    String value = DOM.getAttributeValue(optionNode.getAttributes(), "value");
                    String name = value;
                    options.add(new Option(name, value));
                }
            }
        }
        if (!options.isEmpty()){
            this.options = options.toArray(new Option[options.size()]);
        }

    }


  //**************************************************************************
  //** getName
  //**************************************************************************
  /** Returns the parameter name.
   */
    public String getName(){return name;}


  //**************************************************************************
  //** getType
  //**************************************************************************
  /** Returns the parameter type (e.g. string, int, boolean, base64Binary,
   *  dateTime, etc).
   */
    public String getType(){return type;}


  //**************************************************************************
  //** getValue
  //**************************************************************************
  /** Returns the value set for this parameter. Note that the value returned
   *  from this method may vary from what was passed to setValue. Refer to the
   *  setValue method for more information.
   */
    public Object getValue(){
        return value;
    }


  //**************************************************************************
  //** setValue
  //**************************************************************************
  /** Used to set the value for this parameter. If there is a type mismatch
   *  between the value and the parameter data type, this method will attempt
   *  to convert the value to the correct datatype. For example, if a byte[]
   *  array is given for a base64Binary parameter, the array is converted to a
   *  base64 encoded string. <p/>
   *  This method is intended for parameters with simple types. For complex
   *  types, use the getChildren() method and set values for the composite
   *  parameters individually. Alternatively, you can pass a properly formed
   *  xml fragment (string) which will be used, without modification, in the
   *  SOAP/XML request.
   *  @param val Accepts either an array or a single instance of byte, string,
   *  boolean, double, float, int, short, long, decimal, java.util.Date,
   *  javaxt.utils.Date, java.io.File, and javaxt.io.File.
   */
    public void setValue(Object val){

        if (val==null){
            value = null;
            return;
        }
        
        if (isComplex()){
            if (val instanceof String){
                String str = (String) val;
                if (str.trim().startsWith("<")){
                    value = str;
                }
            }
        }
        else{

            if (val.getClass().isArray()){

                if (getMaxOccurs()>1){

                  //Copy all the objects in the array. Covert types as needed.
                    Object[] a = (Object[]) val;
                    Object[] b = new Object[a.length];
                    for (int i=0; i<a.length; i++){
                        b[i] = getValue(a[i]);
                    }
                    value = b;
                }
                else{

                    if (val instanceof byte[]){
                        value = getValue(val);
                    }
                    else{
                        //throw new Exception("This parameter does not support arrays.");
                    }

                }
            }
            else{
                value = getValue(val);
            }
        }
    }

    private Object getValue(Object val){
        if (type.equalsIgnoreCase("base64Binary")){
            if (val instanceof byte[]){
                return val;
            }
            else if (val instanceof String) {
                return javaxt.utils.Base64.decode((String) val);
            }
            else if (val instanceof java.io.File){
                return new javaxt.io.File((java.io.File) val).getBytes().toByteArray();
            }
            else if (val instanceof javaxt.io.File){
                return ((javaxt.io.File) val).getBytes().toByteArray();
            }
        }
        else if(type.equalsIgnoreCase("boolean")){
            return new Value(val).toBoolean();
        }
        else if (type.equalsIgnoreCase("byte")){

        }
        else if (type.equalsIgnoreCase("double")){
            return new Value(val).toDouble();
        }
        else if (type.equalsIgnoreCase("dateTime")){
            if (val instanceof java.util.Date){
                return (java.util.Date) val;
            }
            else if (val instanceof javaxt.utils.Date){
                return ((javaxt.utils.Date) val).getDate();
            }
            else if (val instanceof String){
                try{ return new javaxt.utils.Date((String) val).getDate(); }
                catch(Exception e){}
            }
        }
        else if (type.equalsIgnoreCase("decimal")){
            return new Value(val).toBigDecimal();
        }
        else if (type.equalsIgnoreCase("float")){
            return new Value(val).toFloat();
        }
        else if (type.equalsIgnoreCase("int")){
            return new Value(val).toInteger();
        }
        else if (type.equalsIgnoreCase("long")){
            return new Value(val).toLong();
        }
        else if (type.equalsIgnoreCase("short")){
            return new Value(val).toShort();
        }
        else if (type.equalsIgnoreCase("string")){
            return new Value(val).toString();
        }

        return null;
    }


  //**************************************************************************
  //** toXML
  //**************************************************************************
  /** Returns the parameter name and value as an XML fragment suitable for a
   *  SOAP request.
   */
    protected String toXML(String attributes, String ns){
        
        if (ns!=null) ns = ns.trim();
        else ns = "";

        String name = this.name;
        if (ns.length()>0) name = ns + ":" + name;


        StringBuffer xml = new StringBuffer();

        if (value!=null && value.getClass().isArray()){

            if (value instanceof byte[]){
                xml.append("<" + name + attributes + ">");
                xml.append(javaxt.utils.Base64.encodeBytes((byte[]) value));
                xml.append("</" + name + ">");
            }
            else if (value instanceof Object[]){
                boolean escapeXML = type.equalsIgnoreCase("string");
                for (Object obj : (Object[]) value){
                    String val = obj + "";
                    if (escapeXML) val = javaxt.xml.DOM.escapeXml(val);

                    xml.append("<" + name + attributes + ">");
                    xml.append(val);
                    xml.append("</" + name + ">");
                }

                /*
                String arr = "<name " +
                 "xmlns:ns2=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
                 "xsi:type=\"ns2:Array\" " +
                 "ns2:arrayType=\"xsd:string[3]\"> " +
                    "<item xsi:type=\"xsd:string\">MINDSTRM</item> " +
                    "<item xsi:type=\"xsd:string\">MSFT</item> " +
                    "<item xsi:type=\"xsd:string\">SUN</item> " +
               "</name>";
               */

            }
        }
        else{
            xml.append("<" + name + attributes + ">");

            if (value!=null){
                if (value instanceof String){
                    if (!isComplex()){
                        //xml.append("<![CDATA[" + Value + "]]>");
                        xml.append(javaxt.xml.DOM.escapeXml((String) value));
                    }
                    else{ //xml fragment?
                        xml.append((String) value);
                    }
                }
                else if (value instanceof java.util.Date){
                    java.util.Date date = (java.util.Date) value;

                  //2003-11-24T00:00:00.0000000-05:00
                    java.text.SimpleDateFormat formatter =
                         new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSZ");

                    String d = formatter.format(date).replace(" ", "T");

                    String d1 = d.substring(0,d.length()-2);
                    String d2 = d.substring(d.length()-2);
                    xml.append(d1 + ":" + d2);
                }
                else {
                    xml.append(value);
                }
            }
            xml.append("</" + name + ">");
        }

        return xml.toString();
    }


    public int getMaxOccurs(){
        return maxOccurs;
    }

    public int getMinOccurs(){
        return minOccurs;
    }

    public boolean isRequired(){
        if (getMinOccurs()==0) return false;
        else return true;
    }

    public boolean isComplex(){

        if (this.getChildren()!=null){
            if (this.getChildren().length>0) return true;
        }

        if (options!=null){
            return false;
        }

        return false;
    }

    public Parameter[] getChildren(){
        return children;
    }

    protected Node getParentNode(){
        return parentNode;
    }

    public Option[] getOptions(){
        return options;
    }

    public String toString(){
        if (isComplex()){
            return name;
        }
        else{
           return name + "=" + value;
        }
    }


//    public void reset(){
//        reset(this);
//    }
//
//    private void reset(Parameter param){
//        param.Value = null;
//        if (Children!=null){
//            for (Parameter p : Children){
//                reset(p);
//            }
//        }
//    }


    private int cint(String str){
        return javaxt.utils.string.cint(str);
    }

    private boolean bool(String str){
        if (str.equalsIgnoreCase("true")) return true;
        else return false;
    }
}