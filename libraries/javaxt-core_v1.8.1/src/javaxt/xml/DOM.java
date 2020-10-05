package javaxt.xml;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.w3c.dom.*;
import javax.xml.parsers.*;

//imports used to transform xml
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//******************************************************************************
//**  DOM Utilities - By Peter Borissow
//******************************************************************************
/**
 *   Provides basic utilities to simplify loading and parsing xml
 *
 ******************************************************************************/

public class DOM {

  //**************************************************************************
  //** Private Constructor
  //**************************************************************************
  /**  Defeats Instantiation */
    
    private DOM() {
    }
    
    
  //**************************************************************************
  //** createDocument
  //**************************************************************************
  /**  Used to create a DOM document from a URL. */ 
    
    public static Document createDocument(java.net.URL url){
        return new javaxt.http.Request(url).getResponse().getXML();
    }
    
    
  //**************************************************************************
  //** createDocument
  //**************************************************************************
  /** Used to create a DOM document from a java.io.File. Returns null if the
   *  file cannot be serialized to XML.
   */
    public static Document createDocument(java.io.File file){
        if (file.exists()==false) return null;
        try{
            return createDocument(new java.io.FileInputStream(file));
        }
        catch(Exception e){
            //e.printStackTrace();
            return null;
        }
    }
    
    
  //**************************************************************************
  //** createDocument
  //**************************************************************************
  /** Used to create a DOM document from a String. Returns null if the string
   *  is invalid.
   */
    public static Document createDocument(String xml){
        if (xml==null) return null;

        xml = xml.trim();
        String encoding = "UTF-8";
        
        try{
            String xmlHeader = xml.substring(xml.indexOf("<?"), xml.indexOf("?>"));
            if (xmlHeader.contains(" encoding")){
                encoding = xmlHeader.substring(xmlHeader.indexOf(" encoding")+" encoding".length());
                encoding = encoding.substring(encoding.indexOf("=")+1);
                while(encoding.substring(0, 1).equals(" ")){
                    encoding = encoding.substring(1);
                }

                String firstChar = encoding.substring(0, 1);
                if (firstChar.equals("\"") || firstChar.equals("'")){
                    encoding = encoding.substring(1);
                    encoding = encoding.substring(0, encoding.indexOf(firstChar)).trim();
                }
                else{
                    encoding = encoding.substring(0, encoding.indexOf(" ")).trim();
                }

            }
        }
        catch(Exception e){}
        
        return createDocument(xml, encoding);
    }


    
    public static Document createDocument(String xml, String charsetName){
        xml = xml.trim();
        try{
            return createDocument(new ByteArrayInputStream(xml.getBytes(charsetName)));
        }
        catch(Exception e){
            //e.printStackTrace();
            return createDocument(new ByteArrayInputStream(xml.getBytes()));
        }
    }

    
  //**************************************************************************
  //** createDocument
  //**************************************************************************
  /**  Used to create a DOM document from an InputStream. */ 
    
    public static Document createDocument(InputStream is){
        try{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(is);
        }
        catch(Exception e){
            //e.printStackTrace();
            return null;
        }
    }
    
    
    
  //**************************************************************************
  //** getOuterNode
  //**************************************************************************
  /** Returns the outer node for a given xml document.
   *  @param xml A org.w3c.dom.Document
   */
    public static Node getOuterNode(Document xml){
        if (xml==null) return null;
        NodeList OuterNodes = xml.getChildNodes();
        for (int i=0; i<OuterNodes.getLength(); i++ ) {
             if (OuterNodes.item(i).getNodeType() == 1){
                 return OuterNodes.item(i);
             }
        }
        return null;
    }


  //**************************************************************************
  //** equals
  //**************************************************************************
  /** Used to compare two XML documents by performing a raw string comparison.
   */
    public static boolean equals(Document xml, Document xml2){
        if (xml==null && xml2==null) return true;
        if (xml!=null && xml2==null) return false;
        if (xml==null && xml2!=null) return false;
        return getText(xml).equals(getText(xml2));
    }


  //**************************************************************************
  //** getText
  //**************************************************************************
  /** Converts a DOM Document to a String
   *  @param xml A org.w3c.dom.Document
   */
    public static String getText(Document xml){
        
        try{
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            DOMSource source = new DOMSource(xml);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();            
            StreamResult result = new StreamResult(bas);
            transformer.transform(source, result);
            String encoding = xml.getXmlEncoding();
            if (encoding!=null) return bas.toString(encoding);
            else return bas.toString();
        }
        catch (Exception e){
            //System.out.println(e.toString());
            return "";            
        }
    }
    
    
  //**************************************************************************
  //** getText
  //**************************************************************************
  /** Converts a NodeList to a String */

    public static String getText(NodeList nodeList){
        StringBuffer ret = new StringBuffer();
        for (int i=0; i<nodeList.getLength(); i++ ) {
            ret.append(getText(nodeList.item(i)));
        }
        return ret.toString();
    }


  //**************************************************************************
  //** getText
  //**************************************************************************
  /** Converts a Node to a String */

    public static String getText(Node node){
         StringBuffer ret = new StringBuffer();
         if (node.getNodeType()==1){

             if (hasChildren(node)){
                 ret.append(getNodeValue(node));
             }
             else{
                 ret.append("<" + node.getNodeName() + getAttributes(node) + ">");
                 ret.append(getNodeValue(node));
                 ret.append("</" + node.getNodeName() + ">");
             }
         }
         return ret.toString();
    }


  //**************************************************************************
  //** getAttributes
  //**************************************************************************
  /** Used to retrieve all of the attributes for a given node.   */
    
    private static String getAttributes(Node node){
        if (node==null) return "";
        NamedNodeMap attr = node.getAttributes();
        String Attributes = "";
        if (attr!=null){
            for (int j=0; j<attr.getLength(); j++){
                 String name = attr.item(j).getNodeName();
                 String value = attr.item(j).getTextContent();
                 if (value==null) value = attr.item(j).getNodeValue();
                 if (value==null) value = "";
                 //System.out.println(name + "=" + attr.item(j).getNodeValue());
                 Attributes += " " + name + "=\"" + value + "\"";
            }
        }
        return Attributes;
    }
    
    
  //**************************************************************************
  //** hasChildren
  //**************************************************************************
  /** Used to determine whether a given node has any children. Differs from the 
   *  native DOM implementation in that this function only considers child
   *  nodes that have a node type value equal to 1.
   */    
    public static boolean hasChildren(Node node){
        
        NodeList nodeList = node.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++ ) {
            if (nodeList.item(i).getNodeType()==1){
                return true;
            }            
        }
        return false;
    }

    
  //**************************************************************************
  //** getAttributeValue
  //**************************************************************************
  /** Used to return the value of a given node attribute. The search is case
   *  insensitive. If no match is found, returns an empty string.
   */
    public static String getAttributeValue(NamedNodeMap attrCollection, String attrName){
        
        if (attrCollection!=null){
            for (int i=0; i < attrCollection.getLength(); i++ ) {
                Node node = attrCollection.item(i);
                if (node.getNodeName().equalsIgnoreCase(attrName)) {
                    return node.getNodeValue();
                }
            }
        }
        return "";               
    }


  //**************************************************************************
  //** getAttributeValue
  //**************************************************************************
  /** Used to return the value of a given node attribute. The search is case
   *  insensitive. If no match is found, returns an empty string.
   */
    public static String getAttributeValue(Node node, String attrName){
        return getAttributeValue(node.getAttributes(), attrName);
    }

    
  //**************************************************************************
  //** setAttributeValue
  //**************************************************************************
  /** Used to create or update an attribute. 
   */
    public static void setAttributeValue(Node node, String attrName, String attrValue){
        
        if (node==null) return;
        
        try{
            ((Element)node).setAttribute(attrName, attrValue);
        }
        catch(Exception e){
            
          //Apparently some nodes can't be cast to an Element so we can try
          //to update the node's attribute collection.
            try{
                
                NamedNodeMap attrCollection = node.getAttributes();
                for (int i=0; i<attrCollection.getLength(); i++ ) {
                    org.w3c.dom.Node attribute = attrCollection.item(i);
                    if (attribute.getNodeName().equals(attrName)) {
                        attribute.setNodeValue(attrValue);
                        return;
                    }
                }
                
                //TODO: If we're still here, create new attribute
            }
            catch(Exception ex){
            }
        }
    }

    
    
  //**************************************************************************
  //** getNodeValue
  //**************************************************************************    
  /** Returns the value of a given node as text. If the node has children, the
   *  method will return an xml fragment which will include the input node as
   *  the outer node. This is a legacy feature which should be deprecated over
   *  time.
   */
    public static String getNodeValue(Node node){
        
        String nodeValue = "";
        
        if (hasChildren(node)) {

            StringBuffer xmlTree = new StringBuffer();
            traverse(node, xmlTree);
            nodeValue = xmlTree.toString();

        }
        else{
            nodeValue = node.getTextContent();
        }
        
        if (nodeValue == null){
            return "";
        }
        else{
            return nodeValue;
        }
    }
    
    private static void traverse(Node tree, StringBuffer xmlTree){
        if (tree.getNodeType()==1){
            String Attributes = getAttributes(tree);
            xmlTree.append("<" + tree.getNodeName() + Attributes + ">");
            if (hasChildren(tree)) {
                
                NodeList xmlNodeList = tree.getChildNodes();
                for (int i=0; i<xmlNodeList.getLength(); i++){
                    traverse(xmlNodeList.item(i), xmlTree);
                }

            }
            else{

                String nodeValue = tree.getTextContent();
                if (nodeValue == null){
                    nodeValue = "";
                }
                
                xmlTree.append(nodeValue);
            }

            xmlTree.append("</" + tree.getNodeName() + ">");  
        }
    }
    
    
  //**************************************************************************
  //** getDocumentAttributes
  //**************************************************************************
  /**
   *  @param xml A org.w3c.dom.Document
   */
    public static NamedNodeMap getDocumentAttributes(Document xml){
        NodeList Definitions = xml.getChildNodes();
        for (int i=0; i<Definitions.getLength(); i++ ) {
             if (Definitions.item(i).getNodeType() == 1){
                 return Definitions.item(i).getAttributes();
             }
        }
        return null;        
    }

    
  //**************************************************************************
  //** getTargetNameSpace
  //**************************************************************************
  /** Returns the "targetNamespace" for a given xml document.
   *  @param xml A org.w3c.dom.Document
   */
    public static String getTargetNameSpace(Document xml){
        NamedNodeMap attr = getDocumentAttributes(xml);
        return getAttributeValue(attr,"targetNamespace");
    }
    
    
  //**************************************************************************
  //** getNameSpaces
  //**************************************************************************
  /** Returns a hashmap with all the namespaces found in a given xml document.
   *  The hashmap key is the namespace prefix and the corresponding value is
   *  the namespace url.
   *
   *  @param xml A org.w3c.dom.Document
   */
    public static java.util.HashMap<String, String> getNameSpaces(Document xml){
        java.util.HashMap<String, String> namespaces = new java.util.HashMap<String, String>();
        getNameSpaces(getOuterNode(xml), namespaces);
        return namespaces;
    }

    private static void getNameSpaces(Node node, java.util.HashMap<String, String> namespaces){
        if (node.getNodeType()==1){

            NamedNodeMap attr = node.getAttributes();
            if (attr!=null){
                for (int j=0; j<attr.getLength(); j++){
                     String name = attr.item(j).getNodeName();
                     String value = attr.item(j).getTextContent();
                     if (name.startsWith("xmlns:")){
                         name = name.substring(6);
                         namespaces.put(name, value);
                     }
                }
            }


            if (hasChildren(node)) {
                NodeList childNodes = node.getChildNodes();
                for (int i=0; i<childNodes.getLength(); i++){
                    getNameSpaces(childNodes.item(i), namespaces);
                }
            }

        }
    }



  //**************************************************************************
  //** getElementsByTagName
  //**************************************************************************
  /** Returns an array of nodes that match a given tagName (node name). The
   *  results will include all nodes that match, regardless of namespace. To
   *  narrow the results to a specific namespace, simply include the namespace
   *  prefix in the tag name (e.g. "t:Contact").
   */
    public static org.w3c.dom.Node[] getElementsByTagName(String tagName, Document xml){
        return getElementsByTagName(tagName, getOuterNode(xml));
    }

  //**************************************************************************
  //** getElementsByTagName
  //**************************************************************************
  /** Returns an array of nodes that match a given tagName (node name). The
   *  results will include all nodes that match, regardless of namespace. To
   *  narrow the results to a specific namespace, simply include the namespace
   *  prefix in the tag name (e.g. "t:Contact"). Returns an empty array if
   *  no nodes are found.
   */
    public static org.w3c.dom.Node[] getElementsByTagName(String tagName, Node node){
        java.util.ArrayList<Node> nodes = new java.util.ArrayList<Node>();
        getElementsByTagName(tagName, node, nodes);
        return nodes.toArray(new org.w3c.dom.Node[nodes.size()]);
    }

    private static void getElementsByTagName(String tagName, Node node, java.util.ArrayList<Node> nodes){
        if (node!=null && node.getNodeType()==1){

            String nodeName = node.getNodeName().trim();
            if (nodeName.contains(":") && !tagName.contains(":")){
                nodeName = nodeName.substring(nodeName.indexOf(":")+1);
            }

            if (nodeName.equalsIgnoreCase(tagName)){
                nodes.add(node);
            }

            if (hasChildren(node)) {
                NodeList childNodes = node.getChildNodes();
                for (int i=0; i<childNodes.getLength(); i++){
                    getElementsByTagName(tagName, childNodes.item(i), nodes);
                }
            }

        }
    }

    
  /** Converts a NodeList into an array to simplify nested loops. */
    public static Node[] getNodes(NodeList nodeList){
        java.util.ArrayList<Node> nodes = new java.util.ArrayList<Node>();
        for (int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeType()==1) nodes.add(node);
        }
        return nodes.toArray(new Node[nodes.size()]);
    }



    public final static String ESCAPE_CHARS = "<>&\"\'";
    public final static java.util.List<String> ESCAPE_STRINGS =
    java.util.Collections.unmodifiableList(java.util.Arrays.asList(new String[] {
      "&lt;"
    , "&gt;"
    , "&amp;"
    , "&quot;"
    , "&apos;"
    }));

    private static String UNICODE_LOW =  "" + ((char)0x20); //space
    private static String UNICODE_HIGH = "" + ((char)0x7f);


  //**************************************************************************
  //** escapeXml
  //**************************************************************************
  /** Used to encode text data for use in an XML tag or attribute.
   */
    public static String escapeXml(String content) {
        String result = content;

        if ((content != null) && (content.length() > 0)) {
          boolean modified = false;
          StringBuilder stringBuilder = new StringBuilder(content.length());
          for (int i = 0, count = content.length(); i < count; ++i) {
            String character = content.substring(i, i + 1);
            int pos = ESCAPE_CHARS.indexOf(character);
            if (pos > -1) {
              stringBuilder.append(ESCAPE_STRINGS.get(pos));
              modified = true;
            }
            else {
              if (    (character.compareTo(UNICODE_LOW) > -1)
                   && (character.compareTo(UNICODE_HIGH) < 1)
                 ) {
                stringBuilder.append(character);
              }
              else {
                stringBuilder.append("&#" + ((int)character.charAt(0)) + ";");
                modified = true;
              }
            }
          }
          if (modified) {
            result = stringBuilder.toString();
          }
        }

        return result;
    }

}